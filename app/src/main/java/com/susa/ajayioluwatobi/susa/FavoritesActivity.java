/*
    This activity is a mirror of the FeedActivity but that works
    inside with the Favorite platform.
*/

package com.susa.ajayioluwatobi.susa;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import android.content.res.Configuration;//These are added to implement the Navigation Drawer - WJL
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class FavoritesActivity extends AppCompatActivity {
    DrawerLayout mDrawer; 					//implemented for the drawer item - WJL
    ActionBarDrawerToggle mDrawerToggle; 	//implemented to add Action to the drawer - WJL
    private ListView mDrawerList; 			//implement to store Tab Names - WJL
    private ArrayList<String> list; 		//implement to store Tab Names - WJL

    private RecyclerView mPostList;
    private DatabaseReference mDatabase, db, db1, db2;
    public static Button occupiedButton, vacantButton;


    private FirebaseAuth firebaseAuth;		//
    public static ImageView likeButton,msgButton, deleteButton;

    private FloatingActionButton fab;
    private LinearLayout bs;
    public static TextView tv1,tv2,remove_from_favorites;
    public static BottomSheetBehavior bottomSheetBehavior;
    private Query first;
    private FirebaseFirestore fDatabase;
    FirestoreRecyclerAdapter<UserPost, FavoritesActivity.UserPostHolder> firestoreRecyclerAdapter;
    public static class UserPostHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        View mView;
        Context mContext;
        String loc;
        String desc,addr,uid,img,time, img2, img3,email;
        int likes_digit;

        public UserPostHolder(View itemView)
        {
            super(itemView);
            mView= itemView;
            mContext = itemView.getContext();
            itemView.setOnClickListener(this);

        }



        public void setEmail(String email){
            this.email = email;
        }
        public void setUid(String id){
            this.uid =id ;
        }
        @Override
        public void onClick(View view) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            tv1.setText(loc);
            tv2.setText(desc);
            remove_from_favorites.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String id = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    FirebaseFirestore db = FirebaseFirestore.getInstance();

                    db.collection("user-likes").document(id).collection("like").document("time")
                            .delete()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {


                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Log.w(TAG, "Error deleting document", e);
                                }
                            });
                }
            });

        }

        public void setAddress(String addy){
            TextView post_addy= (TextView)mView.findViewById(R.id.post_address);
            post_addy.setText(addy);
        }

        public void setPrice(int price){
            TextView post_addy= (TextView)mView.findViewById(R.id.post_id);
            post_addy.setText(Integer.toString(price));
        }

        public void setImage(Context ctx,String image){
            ImageView post_image= (ImageView) mView.findViewById(R.id.post_image);
            Picasso.with(ctx).load(image).into(post_image);
        }
        public void setImage2(Context ctx,String image){
            this.img2 = image;
            ImageView post_image= (ImageView) mView.findViewById(R.id.post_image2);
            Picasso.with(ctx).load(image).into(post_image);
        }
        public void setImage3(Context ctx,String image){
            this.img3 = image;
            ImageView post_image= (ImageView) mView.findViewById(R.id.post_image3);
            Picasso.with(ctx).load(image).into(post_image);
        }

        public void setLocation(String city){
            TextView post_city= (TextView)mView.findViewById(R.id.post_location);
            loc = city;
            post_city.setText(city);
        }
        public void setDesc(String s){
            desc = s;

        }
        public void setLikes(int likes){
            this.likes_digit= likes;
            TextView like_num = (TextView)mView.findViewById(R.id.likes_num);
            like_num.setText(Integer.toString(likes));
        }

        public void setTime(String s){
            this.time = s;
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        msgButton = (ImageView) findViewById(R.id.message_post);

        deleteButton = (ImageView) findViewById(R.id.delete_post);
        msgButton.setVisibility(View.INVISIBLE);
        deleteButton.setVisibility(View.INVISIBLE);
        remove_from_favorites= (TextView)findViewById(R.id.remove_post);

        vacantButton= (Button) findViewById(R.id.vacant_post);
        occupiedButton= (Button) findViewById(R.id.occupied_post);
        vacantButton.setVisibility(View.INVISIBLE);
        occupiedButton.setVisibility(View.INVISIBLE);



        firebaseAuth= FirebaseAuth.getInstance();		//

        String userid = "User Name";		//FirebaseAuth.getInstance().getCurrentUser().getUid(); //
        String Uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String useremail = firebaseAuth.getInstance().getCurrentUser().getEmail();  //to get useremail to display in drawer
        list = new ArrayList<String>();  	//Store in array List - WJL
        list.add(userid); 					//
        list.add(useremail); 				//


        //initializing toolbar
        Toolbar myToolbar=(Toolbar) findViewById(R.id.my_toolbar) ;
        myToolbar.inflateMenu(R.menu.bar_meu);
        setSupportActionBar(myToolbar);


        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout); //
        mDrawerList = (ListView) findViewById(R.id.left_drawer); //

        ObjectDrawerItem[] drawerItem = new ObjectDrawerItem[4]; //

        drawerItem[0] = new ObjectDrawerItem(R.drawable.ic_account_circle_black_24dp, useremail);

        //These create the tabs to be clicked - WJL

        drawerItem[1] = new ObjectDrawerItem(R.drawable.ic_home_black_24dp, "Home");
        drawerItem[2] = new ObjectDrawerItem(R.drawable.ic_public_black_24dp, "My Posts");
        drawerItem[3] = new ObjectDrawerItem(R.drawable.ic_power_settings_new_black_24dp, "Sign out");

        DrawerItemCustomAdapter adapter = new DrawerItemCustomAdapter(this, R.layout.listview_item_row, drawerItem); //Adapter to create the listview - WJL

        mDrawerList.setAdapter(adapter);//

        // These lines are needed to display the top-left hamburger button -WJL
        getSupportActionBar().setHomeButtonEnabled(true);//
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);// at top

        // Make the hamburger button work
        mDrawerToggle = new ActionBarDrawerToggle(this,mDrawer,R.string.app_name,R.string.app_name){
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        mDrawer.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();



        fab = (FloatingActionButton) findViewById(R.id.fab);
        //Bottom sheet implementation from
        // https://medium.com/android-bits/android-bottom-sheet-30284293f066
        tv1 = (TextView) findViewById(R.id.tv1);
        tv2 = (TextView) findViewById(R.id.tv2);
        bs = (LinearLayout) findViewById(R.id.bottom_sheet);
        bottomSheetBehavior = BottomSheetBehavior.from(bs);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        Intent intent= getIntent();

       // mDatabase= FirebaseDatabase.getInstance().getReference();
        fDatabase = FirebaseFirestore.getInstance();


        FirebaseUser user = firebaseAuth.getCurrentUser();

      //  db = mDatabase.child("users");				// to reach the posts of users - WJL
       // db1 = db.child("user-likes");
       // db2 = db1.child(Uid);

       // mDatabase.keepSynced(true);

        mPostList= (RecyclerView) findViewById(R.id.my_recyclerView);
        mPostList.setHasFixedSize(true);
        mPostList.setLayoutManager(new LinearLayoutManager(this));



        first = fDatabase.collection("user-likes").document(user.getUid()).collection("like")
                .whereEqualTo("status", "VACANT")
                .orderBy("timeOf", Query.Direction.DESCENDING);

        fetchData();


    }
/*
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<UserPost,FeedActivity.UserPostHolder> firebaseRecyclerAdapter= new FirebaseRecyclerAdapter<UserPost, FeedActivity.UserPostHolder>
                (UserPost.class,R.layout.post_row,FeedActivity.UserPostHolder.class,db2) {
            @Override
            protected void populateViewHolder(FeedActivity.UserPostHolder viewHolder, UserPost model, int position) {

                viewHolder.setAddress(model.getAddress());
                viewHolder.setPrice(model.getPrice());
                viewHolder.setImage(getApplicationContext() ,model.getPost_image());
                viewHolder.setImage2(getApplicationContext() ,model.getPost_image2());
                viewHolder.setImage3(getApplicationContext() ,model.getPost_image3());
                viewHolder.setLocation(model.getLocation());
                viewHolder.setDesc(model.getDesc());
                viewHolder.setUid(model.getUserID());
                viewHolder.setTime(model.getTimeOf());

            }
        };

        mPostList.setAdapter(firebaseRecyclerAdapter);

    }

    */
    protected  void fab(View view)
    {
        Intent intent = new Intent(FavoritesActivity.this,PostActivity.class);
        startActivity(intent);
    }

    public void fetchData(){
        FirestoreRecyclerOptions<UserPost> options = new FirestoreRecyclerOptions.Builder<UserPost>()
                .setQuery(first, UserPost.class)
                .build();

        firestoreRecyclerAdapter = new FirestoreRecyclerAdapter<UserPost, FavoritesActivity.UserPostHolder>(options) {
            @Override
            protected void onBindViewHolder(FavoritesActivity.UserPostHolder viewHolder, int position, UserPost model) {
                viewHolder.setAddress(model.getAddress());
                viewHolder.setPrice(model.getPrice());
                viewHolder.setImage(getApplicationContext() ,model.getPost_image());
                viewHolder.setImage2(getApplicationContext() ,model.getPost_image2());
                viewHolder.setImage3(getApplicationContext() ,model.getPost_image3());
                viewHolder.setLocation(model.getLocation());
                viewHolder.setDesc(model.getDesc());
                viewHolder.setUid(model.getUserID());
                viewHolder.setTime(model.getTimeOf());
                viewHolder.setEmail(model.getEmail());
                viewHolder.setLikes(model.getLikes());

            }

            @Override
            public FavoritesActivity.UserPostHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new FavoritesActivity.UserPostHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.post_row, parent, false));
            }
        };


        mPostList.setAdapter(firestoreRecyclerAdapter);
        firestoreRecyclerAdapter.startListening();
        firestoreRecyclerAdapter.notifyDataSetChanged();

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onStart(){
        super.onStart();
        firestoreRecyclerAdapter.notifyDataSetChanged();
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {	//item click listener for the Drawer items - WJL

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }

    }

    private void selectItem(int position) {
        boolean flag = false;

        switch (position) {
            case 0:
                break;
            case 1:
                 Intent nIntent = new Intent(FavoritesActivity.this, FeedActivity.class);
                 startActivity(nIntent);
                 flag = true;
                break;
            case 2:
                Intent mIntent = new Intent(FavoritesActivity.this, UserPostsActivity.class);
                startActivity(mIntent);
                flag = true;
                break;
            case 3:
                firebaseAuth.signOut();
                Intent oIntent = new Intent(FavoritesActivity.this, MainActivity.class);
                startActivity(oIntent);
                flag = true;
                break;
            default:
                break;
        }

        if(flag == true) {
            mDrawerList.setItemChecked(position, true);
            mDrawerList.setSelection(position);
            mDrawer.closeDrawer(mDrawerList);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bar_meu, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            mDrawer.openDrawer(GravityCompat.START);
            return true;
        }
/*
        if(id== R.id.action_map){
            Intent intent = new Intent(FavoritesActivity.this, MapViewActivity.class);
            startActivity(intent);
            return true;
        }
*/

        return super.onOptionsItemSelected(item);
    }


}