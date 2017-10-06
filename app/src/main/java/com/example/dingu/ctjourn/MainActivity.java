package com.example.dingu.ctjourn;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private RecyclerView postList;
    private DatabaseReference myDBRef;
    private DatabaseReference mDatabaseUsers;
    private DatabaseReference mDatabaseLike;
    private FirebaseDatabase firebaseDatabase;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private boolean mProcessLike = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
       mAuthListener = new FirebaseAuth.AuthStateListener() {
           @Override
           public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
               if(firebaseAuth.getCurrentUser()==null)
               {
                   Intent loginIntent = new Intent(MainActivity.this,LoginActivity.class);
                   loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                   startActivity(loginIntent);
               }
           }
       };

        firebaseDatabase = FirebaseDatabase.getInstance();
        myDBRef = firebaseDatabase.getReference().child("Post");
        myDBRef.keepSynced(true);

        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users");
        mDatabaseUsers.keepSynced(true);

        mDatabaseLike = FirebaseDatabase.getInstance().getReference().child("Likes");

        postList = (RecyclerView)findViewById(R.id.post_list);
        postList.setHasFixedSize(true);
        postList.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    protected void onStart() {
        super.onStart();

        mAuth.addAuthStateListener(mAuthListener);

        FirebaseRecyclerAdapter<Post,PostViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Post, PostViewHolder>(
                Post.class,
                R.layout.post_row,
                PostViewHolder.class,
                myDBRef

        ) {
            @Override
            protected void populateViewHolder(final PostViewHolder viewHolder, Post model, int position) {
                final String postKey = getRef(position).getKey();

                viewHolder.setTitle(model.getTitle());
                viewHolder.setDesc(model.getDesc());
                viewHolder.setImage(getApplicationContext(),model.getVideo());
                viewHolder.setUserName(model.getUserName());
                viewHolder.setLikeButton(postKey);


                View.OnClickListener likeButtonListener =  new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mProcessLike = true;
                        mDatabaseLike.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(mProcessLike)
                                {
                                    if(dataSnapshot.child(postKey).hasChild((mAuth.getCurrentUser().getUid())))
                                    {
                                        mDatabaseLike.child(postKey).child(mAuth.getCurrentUser().getUid()).removeValue();
                                        mProcessLike = false;

                                    }
                                    else
                                    {
                                        mDatabaseLike.child(postKey).child(mAuth.getCurrentUser().getUid()).setValue("LIKED");
                                        mProcessLike = false;

                                    }
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                };
                viewHolder.likeButton.setOnClickListener(likeButtonListener);
            }
        };
        postList.setAdapter(firebaseRecyclerAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
       if(item.getItemId() == R.id.action_add)
            startActivity(new Intent(MainActivity.this,PostActivity.class));
        else if(item.getItemId() == R.id.action_log_out)
       {
           mAuth.signOut();
           startActivity(new Intent(MainActivity.this, LoginActivity.class)); //Go back to home page
           finish();
       }
        return super.onOptionsItemSelected(item);
    }

    private Boolean exit = false;
    @Override
    public void onBackPressed() {
        if (exit) {
            finish(); // finish activity
        } else {
            Toast.makeText(this, "Press Back again to Exit.",
                    Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 3 * 1000);

        }

    }

    public static class PostViewHolder extends RecyclerView.ViewHolder{

        View mView;
        ImageButton likeButton;
        DatabaseReference mDatabaseRef;
        FirebaseAuth newAuth;
        public PostViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            likeButton =(ImageButton) mView.findViewById(R.id.like_button);
            mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Likes");
            newAuth = FirebaseAuth.getInstance();
            mDatabaseRef.keepSynced(true);
        }

        public void setTitle(String title)
        {
            TextView post_title = (TextView) mView.findViewById(R.id.post_title);
            post_title.setText(title);
        }

        public void setDesc(String desc)
        {
            TextView post_desc = (TextView)mView.findViewById(R.id.post_desc);
            post_desc.setText(desc);
        }

        public void setImage(Context context, String image)
        {
            ImageView postImage = (ImageView)mView.findViewById(R.id.post_image);
//            Picasso.with(context).load(image).into(postImage);
        }

        public void setUserName(String userName)
        {
            TextView post_username = (TextView)mView.findViewById(R.id.post_username);
            post_username.setText("posted by "+userName);
        }

        public void setLikeButton(final String postKey)
        {
            mDatabaseRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    boolean userLoggedIn ;
                    if (newAuth.getCurrentUser() != null) {
                        if(dataSnapshot.child(postKey).hasChild(newAuth.getCurrentUser().getUid()))
                        {
                            likeButton.setImageResource(R.mipmap.blue_like);
                        }
                        else
                        {
                            likeButton.setImageResource(R.mipmap.grey_like);
                        }
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

}
