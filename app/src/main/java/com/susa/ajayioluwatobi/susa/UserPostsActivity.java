package com.susa.ajayioluwatobi.susa;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
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
import android.util.Log;

import android.content.res.Configuration;//These are added to implement the Navigation Drawer - WJL
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.Transaction;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class UserPostsActivity extends AppCompatActivity {
    private static final String TAG= "MapViewActivity";
    private static final int ERROR_DIALOG_REQUEST = 9001;

    public static String useremail="";
    private static Context mContext;

    private RecyclerView mPostList;
    // private DatabaseReference mDatabase,db;
    private DatabaseReference nameDB, DBname, nameDB1, nameDB2, nameDB3;
    private FirebaseAuth firebaseAuth;
    private static FloatingActionButton fab;
    private LinearLayout bs;
    private String userid;
    public static Button occupiedButton, vacantButton;
    public static ImageView likeButton,msgButton, deleteButton;
    public static TextView tv1,tv2,remove_from_favorites;
    public static ImageView bottom_image1, bottom_image2, bottom_image3;
    public static BottomSheetBehavior bottomSheetBehavior;
    private Query first;
    private FirebaseFirestore fDatabase;
    FirestoreRecyclerAdapter<UserPost, UserPostsActivity.UserPostHolder> firestoreRecyclerAdapter;
    DrawerLayout mDrawer; 			//implemented for the drawer item - WJL
    ActionBarDrawerToggle mDrawerToggle;//implemented to add Action to the drawer - WJL
    private ListView mDrawerList; 	//implement to store Tab Names - WJL
    private ArrayList<String> list; //implement to store Tab Names - WJL
    public ObjectDrawerItem[] drawerItem;
    private FirebaseFirestore get;
    public static class UserPostHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        View mView;
        Context mContext;
        String loc,desc,addr,uid,img,time, img2, img3,email;
        int pri, likes_digit;

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



            occupiedButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    vacantButton.setVisibility(View.VISIBLE);
                    occupiedButton.setVisibility(View.INVISIBLE);
                    Date date = new Date();
                    String id = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    UserPost e = new UserPost(pri, addr,loc, desc, uid, time,img,img2, img3,email);
                    FirebaseFirestore dt = FirebaseFirestore.getInstance();
                    FirebaseFirestore dl = FirebaseFirestore.getInstance();
                    //DatabaseReference dt = FirebaseDatabase.getInstance().getReference();
                    //dt.child("users").child("user-likes").child(id).child(date.toString()).setValue(e);

                    final DocumentReference docRef = dl.collection("posts").document(time);
                    docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            UserPost user = documentSnapshot.toObject(UserPost.class);

                            if(user.status.equals("VACANT")){
                                docRef.update("status", "OCCUPY");
                            }
                        }
                    });


                }
            });



            vacantButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    occupiedButton.setVisibility(View.VISIBLE);
                    vacantButton.setVisibility(View.INVISIBLE);
                    FirebaseFirestore dl = FirebaseFirestore.getInstance();

                    final DocumentReference docRef = dl.collection("posts").document(time);
                    docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            UserPost user = documentSnapshot.toObject(UserPost.class);

                            if(user.status.equals("OCCUPY")){
                                docRef.update("status", "VACANT");
                            }
                        }
                    });


                }
            });

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String id = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    FirebaseFirestore db = FirebaseFirestore.getInstance();

                    db.collection("posts").document(time)
                            .delete()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    refreshActivity(mContext);
                                    Snackbar
                                            .make(mView, "Post Deleted",Snackbar.LENGTH_SHORT)
                                            .show();

                                    //Log.d(TAG, "DocumentSnapshot successfully deleted!");


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

        public static void refreshActivity (Context mContext){

            Intent mIntent = new Intent(mContext , UserPostsActivity.class);
            mContext.startActivity(mIntent);
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
        public void setLikes(int likes){
            this.likes_digit= likes;
            TextView like_num = (TextView)mView.findViewById(R.id.likes_num);
            like_num.setText(Integer.toString(likes));
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
        likeButton = (ImageView) findViewById(R.id.like_post);
        deleteButton = (ImageView) findViewById(R.id.delete_post);
        msgButton = (ImageView) findViewById(R.id.message_post);
        remove_from_favorites = (TextView) findViewById(R.id.remove_post);


        vacantButton= (Button) findViewById(R.id.vacant_post);
        occupiedButton= (Button) findViewById(R.id.occupied_post);
        vacantButton.setVisibility(View.INVISIBLE);
        msgButton.setVisibility(View.INVISIBLE);
        remove_from_favorites.setVisibility(View.INVISIBLE);
        mContext=this;





        get = FirebaseFirestore.getInstance();





        mPostList = (RecyclerView) findViewById(R.id.my_recyclerView);
        mPostList.setHasFixedSize(true);
        mPostList.setLayoutManager(new LinearLayoutManager(UserPostsActivity.this));
        Intent intent= getIntent();
        if(IsServicesOk()){
            init();
        }

        //Map<String,String> map = new HashMap<>();
        //map.put("userid","Williams");
        FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();
        //mFirestore.collection("users").add(map);
        //mFirestore.collection("users").document("X").set("Williams");
        //initializing toolbar
        Toolbar myToolbar=(Toolbar) findViewById(R.id.my_toolbar) ;
        myToolbar.inflateMenu(R.menu.bar_meu);
        setSupportActionBar(myToolbar);

        //Getting DB references

        fDatabase = FirebaseFirestore.getInstance();

        //mDatabase= FirebaseDatabase.getInstance().getReference();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        String ID = FirebaseAuth.getInstance().getCurrentUser().getUid();
     /*   DBname= FirebaseDatabase.getInstance().getReference();
        nameDB = DBname.child("users");
        nameDB1 = nameDB.child("user-info");
        nameDB2 = nameDB1.child(ID);
        nameDB3 = nameDB2.child("name");
*/


        String userid = "User Name";		 //
        useremail = firebaseAuth.getInstance().getCurrentUser().getEmail();  //to get useremail to display in drawer
        list = new ArrayList<String>();  	//Store in array List - WJL
        list.add(userid);
        list.add(useremail);



        //This code adds the drawer and populates it.
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        drawerItem = new ObjectDrawerItem[4]; //

        drawerItem[0] = new ObjectDrawerItem(R.drawable.ic_account_circle_black_24dp, useremail);

        //These create the tabs to be clicked - WJL

        drawerItem[1] = new ObjectDrawerItem(R.drawable.ic_home_black_24dp, "Home");
        drawerItem[2] = new ObjectDrawerItem(R.drawable.ic_public_black_24dp, "My Posts");
        drawerItem[3] = new ObjectDrawerItem(R.drawable.ic_power_settings_new_black_24dp, "Sign out");


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

        // db = mDatabase.child("posts");

        //db.keepSynced(true);

        String id;
        id = FirebaseAuth.getInstance().getUid();

        first = fDatabase.collection("posts")
                .whereEqualTo("userID", id)
                .orderBy("timeOf", Query.Direction.DESCENDING);

        fetchData();


    }

    private void init(){

    }


    public boolean IsServicesOk() {

        Log.d(TAG, "IsServicesOk:checking google services version ");

        int available= GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(UserPostsActivity.this);

        if(available== ConnectionResult.SUCCESS) {
            //everything is fine and the user can make map request
            Log.d(TAG, "isServicesOK: Google Play is working");
            return true;
        }
        else if(GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            //if error occurred but can be fixed
            Log.d(TAG, "isServicesOK: Error occured but can be fixed");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(UserPostsActivity.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();


        }
        else
        {
            Toast.makeText(this, "You can't make map requests", Toast.LENGTH_LONG).show();
        }
        return false;
    }

    //The onStart contains code that will populate the views.
 /*   @Override
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
   */
    protected  void fab(View view)
    {
        Intent intent = new Intent(UserPostsActivity.this,PostActivity.class);
        startActivity(intent);
    }

    public void fetchData(){
        FirestoreRecyclerOptions<UserPost> options = new FirestoreRecyclerOptions.Builder<UserPost>()
                .setQuery(first, UserPost.class)
                .build();

        firestoreRecyclerAdapter = new FirestoreRecyclerAdapter<UserPost, UserPostHolder>(options) {
            @Override
            protected void onBindViewHolder(UserPostHolder viewHolder, int position, UserPost model) {
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
            public UserPostHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new UserPostsActivity.UserPostHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.post_row, parent, false));
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
    //Item click listener for the Drawer items - WJL
    private class DrawerItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }

    }
    @Override
    public void onStart(){
        super.onStart();
        firestoreRecyclerAdapter.notifyDataSetChanged();
    }
    //Takes position of the clicked item and starts Activity.
    private void selectItem(int position) {
        boolean flag = false;

        switch (position) {
            case 0:
                break;
            case 1:
                Intent nIntent = new Intent(UserPostsActivity.this, FeedActivity.class);
                startActivity(nIntent);
                flag = true;
                break;
            case 2:
                Intent mIntent = new Intent(UserPostsActivity.this, UserPostsActivity.class);
                startActivity(mIntent);
                flag = true;
                break;
            case 3:
                firebaseAuth.signOut();
                Intent oIntent = new Intent(UserPostsActivity.this, MainActivity.class);
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

    //to inflate tool bar

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bar_meu, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        /*if(id== R.id.action_map){
            Intent intent = new Intent(UserPostsActivity.this, MapViewActivity.class);
            startActivity(intent);
            return true;
        }
        */
        if(id == android.R.id.home){
            mDrawer.openDrawer(GravityCompat.START);
            return true;
        }

        if(id == R.id.action_favorites){
            Intent intent = new Intent(UserPostsActivity.this, FavoritesActivity.class);
            startActivity(intent);
            return true;
        }

        if(id == R.id.action_search){
            Intent intent = new Intent(UserPostsActivity.this, SearchActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
