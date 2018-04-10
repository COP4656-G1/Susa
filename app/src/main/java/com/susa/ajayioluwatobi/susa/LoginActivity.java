package com.susa.ajayioluwatobi.susa;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private Button LoginButton;

    private TextView ForgotText;
    private EditText Email;
    private EditText PasswordText;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Intent intent= getIntent();

        progressDialog= new ProgressDialog(this);
        LoginButton= (Button) findViewById(R.id.button_login);
        Email= (EditText) findViewById(R.id.login_email);
        PasswordText = (EditText) findViewById(R.id.login_Password);
        ForgotText= (TextView) findViewById(R.id.forgot_password);
        mAuth = FirebaseAuth.getInstance();


        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

            ForgotText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                    startActivity(intent);
                }
            });



    }


    private void loginUser(){
        String email= Email.getText().toString().trim();
        String password= PasswordText.getText().toString().trim();


        if(TextUtils.isEmpty(email)){

            // email empty
            Toast.makeText(this, "pleas enter email", Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(password)){
            // password empty
            Toast.makeText(this, "pleas enter password", Toast.LENGTH_LONG).show();
            return;
        }

        progressDialog.setMessage("logining User...");
        progressDialog.show();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            // Sign in success, update UI with the signed-in user's information
                            //  Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(LoginActivity.this, "login Successful.",
                                    Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            Intent intent = new Intent(LoginActivity.this,FeedActivity.class);
                            startActivity(intent);
                            //
                            // updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            //     Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "login failed.",
                                    Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            // updateUI(null);
                        }

                        // ...
                    }
                });



    }


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);
    }
}
