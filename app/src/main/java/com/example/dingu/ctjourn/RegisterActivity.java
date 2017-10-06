package com.example.dingu.ctjourn;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private ProgressDialog progress;
    private DatabaseReference mdatabaseRefUsers;

    private EditText nameField;
    private EditText emailField;
    private EditText passwordField;
    Button signUp;



    public enum UserMode{
        STUDENT,FACULTY,INDUSTRIALIST
    }

    UserMode userMode ;
    boolean radioButtonChecked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        mdatabaseRefUsers = FirebaseDatabase.getInstance().getReference().child("Users");
        progress = new ProgressDialog(this);

        nameField = (EditText)findViewById(R.id.name);
        emailField = (EditText)findViewById(R.id.email);
        passwordField = (EditText)findViewById(R.id.password);
        signUp = (Button)findViewById(R.id.signUp);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startRegister();
            }
        });
    }

    private void startRegister() {

        final String name = nameField.getText().toString().trim();
        String email = emailField.getText().toString().trim();
        String password = passwordField.getText().toString().trim();

        if(!TextUtils.isEmpty(name) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) && radioButtonChecked)
        {
            progress.setMessage("Signing up..");
            progress.show();
            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {
                        String userID = mAuth.getCurrentUser().getUid();
                        DatabaseReference currentUserDB = mdatabaseRefUsers.child(userID);
                        currentUserDB.child("name").setValue(name);
                        currentUserDB.child("userMode").setValue(userMode);
                        currentUserDB.child("image").setValue("default");
                        progress.dismiss();

                        Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                }
            });
        }
    }

    public void onProfileRadioButtonClicked (View view) {
        // Is the button now checked?
        radioButtonChecked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio_student:
                if (radioButtonChecked)
                   userMode = UserMode.STUDENT;
                    break;
            case R.id.radio_faculty:
                if (radioButtonChecked)
                   userMode = UserMode.FACULTY;
                    break;
            case R.id.radio_industralist:
                if (radioButtonChecked)
                   userMode = UserMode.INDUSTRIALIST;
                    break;
        }
    }

}
