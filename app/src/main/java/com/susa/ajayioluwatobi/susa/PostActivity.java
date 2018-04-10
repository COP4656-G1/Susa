package com.susa.ajayioluwatobi.susa;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PostActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {


    private DatabaseReference databaseReference;

    private EditText address, price;
    private Button post;
    private Button feed;
    private FirebaseAuth firebaseAuth;
    private Spinner location;
    private boolean location_assert= false;
    private String city;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        address = (EditText) findViewById(R.id.address_Text);
        price= (EditText) findViewById(R.id.edit_Price);
        post= (Button) findViewById(R.id.Post_Button);
        feed= (Button) findViewById(R.id.feed_button);
        firebaseAuth= FirebaseAuth.getInstance();
        location = (Spinner) findViewById(R.id.location_spinner);
        location.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.location, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        location.setAdapter(adapter);

        databaseReference= FirebaseDatabase.getInstance().getReference();

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postLease();
                if(location_assert) {
                    Intent intent = new Intent(PostActivity.this, FeedActivity.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(PostActivity.this ,"Please select location" ,Toast.LENGTH_LONG).show();
                }
            }
        });


        feed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PostActivity.this,FeedActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        city = parent.getItemAtPosition(position).toString();
        location_assert= true;

        // Showing selected spinner item
        //Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

    }


    private void postLease(){

        String addy= address.getText().toString().trim();
        int value= Integer.parseInt(price.getText().toString().trim());
        String image= "https://images.pexels.com/photos/880481/pexels-photo-880481.jpeg?cs=srgb&dl=arches-architecture-brickwall-880481.jpg&fm=jpg";

        UserPost userPost= new UserPost(value,addy,image, city);

        FirebaseUser user = firebaseAuth.getCurrentUser();

        databaseReference.child(user.getUid()).setValue(userPost);

        Toast.makeText(this, "Posted", Toast.LENGTH_LONG).show();
        return;

    }
}
