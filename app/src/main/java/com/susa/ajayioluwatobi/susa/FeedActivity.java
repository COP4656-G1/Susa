package com.susa.ajayioluwatobi.susa;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Date;

public class FeedActivity extends AppCompatActivity {
    private RecyclerView mPostList;
    private DatabaseReference mDatabase,db;
    private DatabaseReference nameDB, DBname, nameDB1, nameDB2, nameDB3;
    private FirebaseAuth firebaseAuth;
    private static FloatingActionButton fab;
    private LinearLayout bs;
    private String userid;
    public static Button likeButton;
    public static TextView tv1,tv2;
    public static ImageView bottom_image1, bottom_image2, bottom_image3;
    public static BottomSheetBehavior bottomSheetBehavior;
    DrawerLayout mDrawer; 			//implemented for the drawer item - WJL
    ActionBarDrawerToggle mDrawerToggle;//implemented to add Action to the drawer - WJL
    private ListView mDrawerList; 	//implement to store Tab Names - WJL
    private ArrayList<String> list; //implement to store Tab Names - WJL
    public ObjectDrawerItem[] drawerItem;
    public static class UserPostHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        View mView;
        Context mContext;
        String loc,desc,addr,uid,img,time, img2, img3;
        int pri;

        public UserPostHolder(View itemView)
        {
            super(itemView);
            mView= itemView;
            mContext = itemView.getContext();
            itemView.setOnClickListener(this);

        }
        @Override
        public void onClick(View view) {
            bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
                @Override
                public void onStateChanged(@NonNull View bottomSheet, int newState) {
                    if(BottomSheetBehavior.STATE_COLLAPSED== newState)
                    {
                        fab.setVisibility(View.VISIBLE);
                    }
                    else if(BottomSheetBehavior.STATE_EXPANDED==newState){
                            fab.setVisibility(View.GONE);

                    }
                    else if(BottomSheetBehavior.STATE_HIDDEN==newState){
                        fab.setVisibility(View.VISIBLE);

                    }

                }

                @Override
                public void onSlide(@NonNull View bottomSheet, float slideOffset) {

                }
            });

            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            tv1.setText(loc);
            tv2.setText(desc);
            Picasso.with(mContext).load(img).into(bottom_image1);
            Picasso.with(mContext).load(img2).into(bottom_image2);
            Picasso.with(mContext).load(img3).into(bottom_image3);



            likeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Date date = new Date();
                    String id = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    UserPost e = new UserPost(pri, addr,loc, desc, uid, time,img,img2, img3);
                    DatabaseReference dt = FirebaseDatabase.getInstance().getReference();
                    dt.child("users").child("user-likes").child(id).child(date.toString()).setValue(e);
                }
            });
        }
        public void setUid(String id){
            this.uid =id ;
        }
        public void setAddress(String addy){
            addr = addy;
            TextView post_addy= (TextView)mView.findViewById(R.id.post_address);
            post_addy.setText(addy);
        }

        public void setPrice(int price){
            pri = price;
            TextView post_addy= (TextView)mView.findViewById(R.id.post_id);
            post_addy.setText(Integer.toString(price));
        }

        public void setImage(Context ctx,String image){
            this.img = image;
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
        public void setTime(String s){
            this.time = s;
        }



    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        firebaseAuth= FirebaseAuth.getInstance();
        fab = (FloatingActionButton) findViewById(R.id.fab);
        //Bottom sheet implementation from
        // https://medium.com/android-bits/android-bottom-sheet-30284293f066
        tv1 = (TextView) findViewById(R.id.tv1);
        tv2 = (TextView) findViewById(R.id.tv2);
        bottom_image1= (ImageView) findViewById(R.id.bottom_image1);
        bottom_image2= (ImageView) findViewById(R.id.bottom_image2);
        bottom_image3= (ImageView) findViewById(R.id.bottom_image3);
        bs = (LinearLayout) findViewById(R.id.bottom_sheet);
        bottomSheetBehavior = BottomSheetBehavior.from(bs);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        likeButton = (Button) findViewById(R.id.button2);
        Intent intent= getIntent();

        //Getting DB references

        mDatabase= FirebaseDatabase.getInstance().getReference();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        String ID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DBname= FirebaseDatabase.getInstance().getReference();
        nameDB = DBname.child("users");
        nameDB1 = nameDB.child("user-info");
        nameDB2 = nameDB1.child(ID);
        nameDB3 = nameDB2.child("name");



        String userid = "User Name";		 //
        String useremail = firebaseAuth.getInstance().getCurrentUser().getEmail();  //to get useremail to display in drawer
        list = new ArrayList<String>();  	//Store in array List - WJL
        list.add(userid);
        list.add(useremail);



        //This code adds the drawer and populates it.
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        drawerItem = new ObjectDrawerItem[5]; //
        drawerItem[0] = new ObjectDrawerItem(R.drawable.ic_account_circle_black_24dp, userid);
        drawerItem[0] = new ObjectDrawerItem(R.drawable.ic_account_circle_black_24dp, userid);      //These create the tabs to be clicked - WJL
        drawerItem[1] = new ObjectDrawerItem(R.drawable.ic_email_black_24dp, useremail);
        drawerItem[2] = new ObjectDrawerItem(R.drawable.ic_home_black_24dp, "Home");
        drawerItem[3] = new ObjectDrawerItem(R.drawable.ic_stars_black_24dp, "Favorites");
        drawerItem[4] = new ObjectDrawerItem(R.drawable.ic_power_settings_new_black_24dp, "Sign out");

        DrawerItemCustomAdapter adapter = new DrawerItemCustomAdapter(this, R.layout.listview_item_row, drawerItem); //Adapter to create the listview - WJL

        mDrawerList.setAdapter(adapter);

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

        db = mDatabase.child("posts");

        db.keepSynced(true);

        mPostList= (RecyclerView) findViewById(R.id.my_recyclerView);
        mPostList.setHasFixedSize(true);
        mPostList.setLayoutManager(new LinearLayoutManager(this));


    }

    //The onStart contains code that will populate the views.
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<UserPost,UserPostHolder> firebaseRecyclerAdapter= new FirebaseRecyclerAdapter<UserPost, UserPostHolder>
                (UserPost.class,R.layout.post_row,UserPostHolder.class,db) {
            @Override
            protected void populateViewHolder(UserPostHolder viewHolder, UserPost model, int position) {

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
    protected  void fab(View view)
    {
        Intent intent = new Intent(FeedActivity.this,PostActivity.class);
        startActivity(intent);
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
    //Item click listener for the Drawer items - WJL
    private class DrawerItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }

    }
    //Takes position of the clicked item and starts Activity.
    private void selectItem(int position) {
        boolean flag = false;

        switch (position) {
            case 0:
                break;
            case 1:
                break;
            case 2:
                Intent nIntent = new Intent(FeedActivity.this, FeedActivity.class);
                startActivity(nIntent);
                flag = true;
                break;
            case 3:
                Intent mIntent = new Intent(FeedActivity.this, FavoritesActivity.class);
                startActivity(mIntent);
                flag = true;
                break;
            case 4:
                firebaseAuth.signOut();
                Intent oIntent = new Intent(FeedActivity.this, MainActivity.class);
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


}

