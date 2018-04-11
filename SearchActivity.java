//package edu.fsu.cs.mobile.cop4656p2;
package com.susa.ajayioluwatobi.susa;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class SearchActivity extends AppCompatActivity {
    private RecyclerView mPostList;
    private DatabaseReference mDatabase;
    private FirebaseAuth firebaseAuth;
    private FloatingActionButton fab;
    private Spinner mSpinner;
    private Spinner nSpinner;
    private ArrayAdapter<String> mAdapter;
    private ArrayAdapter<String> nAdapter;
    private ArrayAdapter<String> sAdapter;
    private Spinner sSpinner;
    private Button mButton;


    private String semesterEntries[] = {"", "Summer A", "Summer B"};
    private String locationEntries[] = {"Tallahassee"};
    private String priceEntries[] = {"", "< 300", "300 - 600", "600 +"};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mButton = (Button)
                findViewById(R.id.button_search);
        mSpinner = (Spinner)
                findViewById(R.id.spinner);
        nSpinner = (Spinner)
                findViewById(R.id.spinner2);
        sSpinner = (Spinner)
                findViewById(R.id.spinner3);


        mAdapter = new ArrayAdapter<String>(SearchActivity.this, android.R.layout.simple_spinner_item, semesterEntries);
        nAdapter = new ArrayAdapter<String>(SearchActivity.this, android.R.layout.simple_spinner_item, locationEntries);
        sAdapter = new ArrayAdapter<String>(SearchActivity.this, android.R.layout.simple_spinner_item, priceEntries);

        mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(mAdapter);

        nAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        nSpinner.setAdapter(nAdapter);

        sAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sSpinner.setAdapter(sAdapter);

        mDatabase= FirebaseDatabase.getInstance().getReference();

        FirebaseUser user = firebaseAuth.getCurrentUser();

        mDatabase.child("susa-67f96");

        mDatabase.keepSynced(true);

        mPostList= (RecyclerView) findViewById(R.id.my_recyclerView);
        mPostList.setHasFixedSize(true);
        mPostList.setLayoutManager(new LinearLayoutManager(this));

        mButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                String semester = mSpinner.getSelectedItem().toString();
                String location = nSpinner.getSelectedItem().toString();
                String price = nSpinner.getSelectedItem().toString();

                if(location.equals("Tallahassee")){
                    if(semester.equals("Summer A")){
                        if(price.equals("")){
                            mDatabase.child("Tallahassee").child("SummerA").child("allprice");
                        }else if(price.equals("< 300")){
                            mDatabase.child("Tallahassee").child("SummerA").child("lessthan");
                        }else if(price.equals("300 - 600")){
                            mDatabase.child("Tallahassee").child("SummerA").child("between");
                        }else if(price.equals("600 +")){
                            mDatabase.child("Tallahassee").child("SummerA").child("greaterthan");
                        }
                    }else if(semester.equals("Summer B")){
                        if(price.equals("")){
                            mDatabase.child("Tallahassee").child("SummerB").child("allprice");
                        }else if(price.equals("< 300")){
                            mDatabase.child("Tallahassee").child("SummerB").child("lessthan");
                        }else if(price.equals("300 - 600")){
                            mDatabase.child("Tallahassee").child("SummerB").child("between");
                        }else if(price.equals("600 +")){
                            mDatabase.child("Tallahassee").child("SummerB").child("greaterthan");
                        }
                    }else if(semester.equals("")){
                        if(price.equals("")){
                            mDatabase.child("Tallahassee").child("allsemester").child("allprice");
                        }else if(price.equals("< 300")){
                            mDatabase.child("Tallahassee").child("allsemester").child("lessthan");
                        }else if(price.equals("300 - 600")){
                            mDatabase.child("Tallahassee").child("allsemester").child("between");
                        }else if(price.equals("600 +")){
                            mDatabase.child("Tallahassee").child("allsemester").child("greaterthan");
                        }
                    }
                }

                mDatabase.keepSynced(true);

                mPostList= (RecyclerView) findViewById(R.id.my_recyclerView);
                mPostList.setHasFixedSize(true);
                mPostList.setLayoutManager(new LinearLayoutManager(SearchActivity.this));
//Not sure if this portion is correct. I think a listener is needed for the recycler holder to change it
                //if not this is right then based off the way the database schema is set up it should display
                //new posts based off of the searched items.
                FirebaseRecyclerAdapter<UserPost,UserPostHolder> firebaseRecyclerAdapter= new FirebaseRecyclerAdapter<UserPost, UserPostHolder>
                        (UserPost.class,R.layout.post_row,UserPostHolder.class,mDatabase) {
                    @Override
                    protected void populateViewHolder(UserPostHolder viewHolder, UserPost model, int position) {

                        viewHolder.setAddress(model.getAddress());
                        viewHolder.setPrice(model.getPrice());
                        viewHolder.setImage(getApplicationContext() ,model.getAddress());
                        viewHolder.setLocation(model.getLocation());

                    }
                };

                mPostList.setAdapter(firebaseRecyclerAdapter);
            }

    });


    }


    protected  void fab(View view)
    {
        Intent intent = new Intent(SearchActivity.this,PostActivity.class);
        startActivity(intent);
    }
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<UserPost,UserPostHolder> firebaseRecyclerAdapter= new FirebaseRecyclerAdapter<UserPost, UserPostHolder>
                (UserPost.class,R.layout.post_row,UserPostHolder.class,mDatabase) {
            @Override
            protected void populateViewHolder(UserPostHolder viewHolder, UserPost model, int position) {

                viewHolder.setAddress(model.getAddress());
                viewHolder.setPrice(model.getPrice());
                viewHolder.setImage(getApplicationContext() ,model.getAddress());
                viewHolder.setLocation(model.getLocation());

            }
        };

        mPostList.setAdapter(firebaseRecyclerAdapter);
    }

    public static class UserPostHolder extends RecyclerView.ViewHolder
    {
        View mView;
        public UserPostHolder(View itemView)
        {
            super(itemView);
            mView= itemView;
        }

        public void setAddress(String addy){
            TextView post_addy= (TextView)mView. findViewById(R.id.post_address);
            post_addy.setText(addy);
        }

        public void setPrice(int price){
            TextView post_addy= (TextView)mView. findViewById(R.id.post_id);
            post_addy.setText(Integer.toString(price));
        }

        public void setImage(Context ctx, String image){
            ImageView post_image= (ImageView) mView. findViewById(R.id.post_image);
            Picasso.with(ctx).load(image).into(post_image);
        }

        public void setLocation(String city){
            TextView post_city= (TextView)mView. findViewById(R.id.post_location);
            post_city.setText(city);
        }
    }
}