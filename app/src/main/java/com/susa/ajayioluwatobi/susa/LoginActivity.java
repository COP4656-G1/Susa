package com.susa.ajayioluwatobi.susa;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.app.AlertDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.Login;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity implements ForgotPasswordDialog.NoticeDialogListener {
    private Button LoginButton;

    private TextView ForgotText;
    private EditText memail;
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
         memail= (EditText)findViewById(R.id.forgot_email);
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
                   showDialog();
                }
            });




    }

    void showDialog(){
        DialogFragment newFragment;
        newFragment = new ForgotPasswordDialog();
        newFragment.show(getFragmentManager(),"dialog");

    }



    @Override
    protected void onStart() {

        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser!= null) {
            Intent intent = new Intent(LoginActivity.this, FeedActivity.class);
            startActivity(intent);
        }

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

        progressDialog.setMessage("Signing in....");
        progressDialog.show();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            progressDialog.dismiss();
                            Intent intent = new Intent(LoginActivity.this,FeedActivity.class);
                            startActivity(intent);

                        } else {

                            Toast.makeText(LoginActivity.this, "login failed.",
                                    Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            
                        }

                        // ...
                    }
                });



    }


    @Override
    public void onDialogPositiveClick(DialogFragment dialog, String email) {
        FirebaseAuth auth = FirebaseAuth.getInstance();


        auth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            DialogFragment dialog = new MessageSentFragment();
                            dialog.show(getFragmentManager(), "MessageSentFragment");
                        }
                    }
                });
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        dialog.dismiss();
    }

    public static class MessageSentFragment extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Message Sent");
            builder.setMessage("Follow the link provided in your email");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                }
            });

            // Create the AlertDialog object and return it
            return builder.create();
        }
    }

}


