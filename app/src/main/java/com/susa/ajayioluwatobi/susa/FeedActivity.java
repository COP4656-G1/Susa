package com.susa.ajayioluwatobi.susa;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class FeedActivity extends AppCompatActivity {
    private RecyclerView mPostList;
    private DatabaseReference mDatabase;
    private FirebaseAuth firebaseAuth;
    private FloatingActionButton fab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        firebaseAuth= FirebaseAuth.getInstance();
        fab = (FloatingActionButton) findViewById(R.id.fab);

        Intent intent= getIntent();

        mDatabase= FirebaseDatabase.getInstance().getReference();

        FirebaseUser user = firebaseAuth.getCurrentUser();

        mDatabase.child("susa-67f96");

        mDatabase.keepSynced(true);

        mPostList= (RecyclerView) findViewById(R.id.my_recyclerView);
        mPostList.setHasFixedSize(true);
        mPostList.setLayoutManager(new LinearLayoutManager(this));


    }


    protected  void fab(View view)
    {
        Intent intent = new Intent(FeedActivity.this,PostActivity.class);
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

        public void setImage(Context ctx,String image){
            ImageView post_image= (ImageView) mView. findViewById(R.id.post_image);
            Picasso.with(ctx).load(image).into(post_image);
        }

        public void setLocation(String city){
            TextView post_city= (TextView)mView. findViewById(R.id.post_location);
            post_city.setText(city);
        }
    }
}

