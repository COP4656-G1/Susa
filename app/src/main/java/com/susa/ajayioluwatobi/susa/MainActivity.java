package com.susa.ajayioluwatobi.susa;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private TextView info;

	private DatabaseReference mDatabase;	//to access FirebaseDatabase - WJL
	
    private Button RegisterButton;
    private TextView LoginText;
    private EditText Email;
    private EditText PasswordText;
    private EditText mUsername;
    
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private static final String TAG = "EmailPassword";



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.setApplicationId(getString(R.string.facebook_app_id));
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.activity_main);
        loginButton = (LoginButton) findViewById(R.id.login_button);
        RegisterButton= (Button) findViewById(R.id.button_login);
        LoginText= (TextView) findViewById(R.id.login_text);
        Email= (EditText) findViewById(R.id.email);
        PasswordText = (EditText) findViewById(R.id.login_Password);
        progressDialog= new ProgressDialog(this);
		
		//mUsername = (EditText) findViewById(R.id.user_name);
		
		
        firebaseAuth= FirebaseAuth.getInstance();
		mDatabase = FirebaseDatabase.getInstance().getReference();

        info= findViewById(R.id.info);
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                info.setText(
                        "User ID: "
                                + loginResult.getAccessToken().getUserId()
                                + "\n" +
                                "Auth Token: "
                                + loginResult.getAccessToken().getToken()
                );
            }

            @Override
            public void onCancel() { // App code
                info.setText("Login attempt canceled.");
            }

            @Override
            public void onError(FacebookException exception) {
                info.setText("Login attempt failed.");

            }
        });


        RegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                registerUser();

            }
        });

        LoginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private void registerUser(){
        String email= Email.getText().toString().trim();
        String password= PasswordText.getText().toString().trim();

		
		String check = mEmailField.getText().toString();

        if(TextUtils.isEmpty(email)){

            // email empty
            Toast.makeText(this, "Please enter email", Toast.LENGTH_LONG).show();
            return;
        }

		  if(check.contains("@")){	//checks to see if there is an email entered - WJL
            check = check.split("@")[1];

            if(check.equalsIgnoreCase("my.fsu.edu") || check.equalsIgnoreCase("tcc.fl.edu")){		//makes sure user registering has a College Email - WJL

            }else{
                Toast.makeText(SignInActivity.this, "College Email Required",
                        Toast.LENGTH_LONG).show();
				return;            
			}
        }//end of large if - WJL
        else if{//made in case of false positive - WJL
            Toast.makeText(SignInActivity.this, "Email Required",
                    Toast.LENGTH_LONG).show();

            return;

        }
	
		
        if(TextUtils.isEmpty(password)){
            // password empty
            Toast.makeText(this, "Please enter password", Toast.LENGTH_LONG).show();
            return;
        }

        progressDialog.setMessage("Registering User...");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            // Sign in success, update UI with the signed-in user's information
                          //  Log.d(TAG, "createUserWithEmail:success");
						  
						  
						    OnSuccess(task.getResult().getUser());	//function that stores user information into database - WJL
							
							
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            Toast.makeText(MainActivity.this, "Authentication Successful.",
                                    Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                            startActivity(intent);
                            //
                           // updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                       //     Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                           // updateUI(null);
                        }

                        // ...
                    }
                });

        progressDialog.hide();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
	
	 private void OnSuccess(FirebaseUser user){
		String username = mUsername.getText().toString();
        newUser(user.getUid(), username, user.getEmail());
		
        finish();
    }
	
	 private void newUser(String userID, String name, String email) {
        User user = new User(name, email);

        mDatabase.child("users").child(userID).setValue(user);
    }

}