package com.example.dingu.ctjourn;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private EditText emailField;
    private EditText passwordField;
    private Button loginButton;
    private Button signUpButton;

    private FirebaseAuth mAuth;
    private DatabaseReference mdatabaseUsers;

    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login);
        progress = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();
        mdatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users");
        mdatabaseUsers.keepSynced(true);

        emailField = (EditText)findViewById(R.id.email);
        passwordField = (EditText)findViewById(R.id.password);
        loginButton = (Button)findViewById(R.id.login);
        signUpButton = (Button)findViewById(R.id.signUp);

        ButtonAnimator.buttonEffect(loginButton);
        ButtonAnimator.buttonEffect(signUpButton);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkLogin();
            }
        });


    }

    private void checkLogin() {
        String email = emailField.getText().toString().trim();
        String password = passwordField.getText().toString().trim();
        progress.setMessage("Logging in ..");

        if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password))
        {
            progress.show();
            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                     if(task.isSuccessful())
                     {
                        checkUserExists();



                     }else
                     {
                         Toast.makeText(LoginActivity.this,"Error Login...", Toast.LENGTH_SHORT).show();
                     }
                }
            });
            progress.dismiss();

        }
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

    private void checkUserExists() {
        final String userID = mAuth.getCurrentUser().getUid();
       mdatabaseUsers.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(DataSnapshot dataSnapshot) {

               if(dataSnapshot.hasChild(userID))
               {
                   Intent mainIntent = new Intent(LoginActivity.this,MainActivity.class);
                   mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                   startActivity(mainIntent);
               }
               else
               {
                   Toast.makeText(LoginActivity.this,"You need to setup account", Toast.LENGTH_SHORT).show();
               }
           }

           @Override
           public void onCancelled(DatabaseError databaseError) {

           }
       });

    }


}
