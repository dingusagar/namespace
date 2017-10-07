package com.example.dingu.ctjourn;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.FileDescriptorBitmapDecoder;
import com.bumptech.glide.load.resource.bitmap.VideoBitmapDecoder;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.IOException;


public class PostActivity extends AppCompatActivity {

    private static final int GALLERY_REQUEST =1;
    private ImageView postVideoButton ;
    private Glide glide;
    private EditText editTextTitle;
    private EditText editTextDesc;
    private Button butttonPost;
    private  Uri videoUri = null;
    private ProgressDialog progressDialog;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private StorageReference myStorageRef;

    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;
    private static final int REQUEST_TAKE_GALLERY_VIDEO = 1;
    private DatabaseReference mDatabaseUser,dbLastPostIDRef;

    private float lati,longi;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        mAuth = FirebaseAuth.getInstance();
        mCurrentUser= mAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("Post");
        dbLastPostIDRef = firebaseDatabase.getReference().child("lastPostID");
        mDatabaseUser = FirebaseDatabase.getInstance().getReference().child("Users").child(mCurrentUser.getUid());
        myStorageRef = FirebaseStorage.getInstance().getReference();

        progressDialog = new ProgressDialog(this);

        editTextTitle = (EditText) findViewById(R.id.titleText);
        editTextDesc = (EditText)findViewById(R.id.descText);
        butttonPost = (Button)findViewById(R.id.postButton);
        butttonPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startPosting();
            }
        });


        postVideoButton = (ImageView) findViewById(R.id.addVideo);
        postVideoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                galleryIntent.setType("video/*");
                startActivityForResult(galleryIntent,REQUEST_TAKE_GALLERY_VIDEO);

            }
        });

        SingleShotLocationProvider.requestSingleUpdate(this,new SingleShotLocationProvider.LocationCallback() {
            @Override
            public void onNewLocationAvailable(SingleShotLocationProvider.GPSCoordinates location) {
                lati  = location.latitude;
                longi = location.longitude;
                Log.d("Location", "my location is " + location.toString());
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_TAKE_GALLERY_VIDEO) {
                videoUri= data.getData();
                Glide.with(this).load(videoUri).into(postVideoButton);

            }
        }

    }

    private void startPosting()
    {
        progressDialog.setMessage("Posting");
        final String title = editTextTitle.getText().toString().trim();
        final String desc = editTextDesc.getText().toString().trim();

        if(!TextUtils.isEmpty(title) && !TextUtils.isEmpty(desc) && videoUri!=null)
        {
            progressDialog.show();
            StorageReference filepath = myStorageRef.child("Project_Images").child(videoUri.getLastPathSegment());
            filepath.putFile(videoUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    final Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    final DatabaseReference newPost = databaseReference.push();


                    mDatabaseUser.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            final Post post = new Post(title,desc,downloadUrl.toString(),dataSnapshot.child("name").getValue().toString());
                            post.setLatitude(lati);
                            post.setLongitude(longi);

                            dbLastPostIDRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    Integer lastPostID = dataSnapshot.getValue(Integer.class);
                                    post.setPostID(lastPostID + 1);
                                    databaseReference.child(""+post.getPostID()).setValue(post);
                                    dbLastPostIDRef.setValue(post.getPostID());

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    progressDialog.dismiss();
                    startActivity(new Intent(PostActivity.this,MainActivity.class));
                }
            });
        }

    }


}

