package com.susa.ajayioluwatobi.susa;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import android.support.annotation.NonNull;


import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;


import com.google.firebase.firestore.FirebaseFirestore;

import com.squareup.picasso.Picasso;

import com.google.firebase.firestore.Query;

import android.util.Log;


public class SearchFragment extends Fragment {

    public static final String TAG = SearchFragment.class.getCanonicalName();
    private static final int ERROR_DIALOG_REQUEST = 9001;
    Log log;

    public SearchFragment(){

    }

    private FirebaseFirestore db;
    private RecyclerView mPostList;
    private LinearLayout bs;
    public Button backButton;
    public static TextView tv1, tv2;
    private FirestoreRecyclerAdapter<UserPost, UserPostHolder> firebaseRecyclerAdapter;
    public static ImageView bottom_image1, bottom_image2, bottom_image3;

    private Query first;

    public static class UserPostHolder extends RecyclerView.ViewHolder {
        View mView;
        Context mContext;
        String loc, desc, addr, uid, img, time, img2, img3;
        int pri,likes_digit;

        public UserPostHolder(View itemView) {
            super(itemView);
            mView = itemView;
            mContext = itemView.getContext();


        }

        public void setUid(String id) {
            this.uid = id;
        }

        public void setAddress(String addy) {
            addr = addy;
            TextView post_addy = (TextView) mView.findViewById(R.id.post_address);
            post_addy.setText(addy);
        }

        public void setPrice(int price) {
            pri = price;
            TextView post_addy = (TextView) mView.findViewById(R.id.post_id);
            post_addy.setText(Integer.toString(price));
        }

        public void setImage(Context ctx, String image) {
            this.img = image;
            ImageView post_image = (ImageView) mView.findViewById(R.id.post_image);
            Picasso.with(ctx).load(image).into(post_image);
        }

        public void setImage2(Context ctx, String image) {
            this.img2 = image;
            ImageView post_image = (ImageView) mView.findViewById(R.id.post_image2);
            Picasso.with(ctx).load(image).into(post_image);
        }

        public void setImage3(Context ctx, String image) {
            this.img3 = image;
            ImageView post_image = (ImageView) mView.findViewById(R.id.post_image3);
            Picasso.with(ctx).load(image).into(post_image);
        }

        public void setLocation(String city) {
            TextView post_city = (TextView) mView.findViewById(R.id.post_location);
            loc = city;
            post_city.setText(city);
        }

        public void setLikes(int likes){
            this.likes_digit= likes;
            TextView like_num = (TextView)mView.findViewById(R.id.likes_num);
            like_num.setText(Integer.toString(likes));
        }
        public void setDesc(String s) {
            desc = s;
        }

        public void setTime(String s) {
            this.time = s;
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle state) {
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);
        Bundle args = getArguments();



        String loc = args.get("location").toString();
        String p = args.get("price").toString();
        int price = 0;

        db = FirebaseFirestore.getInstance();
        if(loc.isEmpty() && p.isEmpty() ){
            first = db.collection("posts")
                    .whereEqualTo("status", "VACANT");

        }else if(loc.isEmpty() && !p.isEmpty()){
            price = Integer.parseInt(p);

            first = db.collection("posts")
                    .whereLessThan("price", price)
                    .whereEqualTo("status", "VACANT");

        }else if(!loc.isEmpty() && p.isEmpty()){
            first = db.collection("posts")
                    .whereEqualTo("location", loc)
                    .whereEqualTo("status", "VACANT");
        }else {
            price = Integer.parseInt(p);


            first = db.collection("posts")
                    .whereEqualTo("location", loc)
                    .whereLessThan("price", price+1)
                    .whereEqualTo("status", "VACANT");
        }

        mPostList = (RecyclerView) rootView.findViewById(R.id.my_recyclerView);
        mPostList.setHasFixedSize(true);
        mPostList.setLayoutManager(new LinearLayoutManager(getContext()));




        fetchData();

        return rootView;
    }


    public void fetchData( ){

        FirestoreRecyclerOptions<UserPost> options = new FirestoreRecyclerOptions.Builder<UserPost>()
                .setQuery(first, UserPost.class)
                .build();

        FirestoreRecyclerAdapter<UserPost,UserPostHolder> firebaseRecyclerAdapter= new FirestoreRecyclerAdapter<UserPost, UserPostHolder>
                (options) {
            @NonNull
            @Override
            public UserPostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                //return ;
                return new UserPostHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.post_row, parent, false));
            }

            @Override
            protected void onBindViewHolder(@NonNull UserPostHolder viewHolder, int position, @NonNull UserPost model) {

                viewHolder.setAddress(model.getAddress());
                viewHolder.setPrice(model.getPrice());
                viewHolder.setImage(getContext() ,model.getPost_image());
                viewHolder.setImage2(getContext() ,model.getPost_image2());
                viewHolder.setImage3(getContext() ,model.getPost_image3());
                viewHolder.setLocation(model.getLocation());
                viewHolder.setDesc(model.getDesc());
                viewHolder.setUid(model.getUserID());
                viewHolder.setTime(model.getTimeOf());
                viewHolder.setLikes(model.getLikes());


            }

        };


        mPostList.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();
        firebaseRecyclerAdapter.notifyDataSetChanged();
    }


/*
    @Override
    public void onStop() {
        super.onStop();
        firebaseRecyclerAdapter.stopListening();
    }
*/

}
