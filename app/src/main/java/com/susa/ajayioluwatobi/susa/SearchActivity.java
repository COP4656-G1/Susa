package com.susa.ajayioluwatobi.susa;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SearchActivity extends AppCompatActivity implements QueryFragment.OnButtonClickListener  {
    private static final String TAG= "MapViewActivity";
    private static final int ERROR_DIALOG_REQUEST = 9001;

    public static String useremail="";

    private QueryFragment mQuery;

    private RecyclerView mPostList;
    // private DatabaseReference mDatabase,db;
    private DatabaseReference nameDB, DBname, nameDB1, nameDB2, nameDB3;
    private FirebaseAuth firebaseAuth;
    private static FloatingActionButton fab;
    private LinearLayout bs;
    private String userid;
    public static Button likeButton,msgButton;
    public static TextView tv1,tv2;

    private Query first;
    private FirebaseFirestore fDatabase;
    FirestoreRecyclerAdapter<UserPost, FeedActivity.UserPostHolder> firestoreRecyclerAdapter;
    DrawerLayout mDrawer; 			//implemented for the drawer item - WJL
    ActionBarDrawerToggle mDrawerToggle;//implemented to add Action to the drawer - WJL
    private ListView mDrawerList; 	//implement to store Tab Names - WJL
    private ArrayList<String> list; //implement to store Tab Names - WJL
    public ObjectDrawerItem[] drawerItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        firebaseAuth= FirebaseAuth.getInstance();
        fab = (FloatingActionButton) findViewById(R.id.fab);

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
       // Toolbar myToolbar=(Toolbar) findViewById(R.id.my_toolbar) ;
       // myToolbar.inflateMenu(R.menu.bar_meu);
        //setSupportActionBar(myToolbar);

        //Getting DB references

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
//        getSupportActionBar().setHomeButtonEnabled(true);//
  //      getSupportActionBar().setDisplayHomeAsUpEnabled(true);// at top

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


        mQuery = new QueryFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_container, mQuery)
                .addToBackStack(QueryFragment.TAG)
                .commit();


    }

    private void init(){

    }

    @Override
    public void onButtonClick(Bundle bundle) {
        SearchFragment mSearch = new SearchFragment();
        mSearch.setArguments(bundle);

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction trans = fm.beginTransaction();
        trans.hide(mQuery);
        trans.add(R.id.fragment_container, mSearch);
        trans.addToBackStack(SearchFragment.TAG);
        trans.commit();
    }

    public boolean IsServicesOk() {

        Log.d(TAG, "IsServicesOk:checking google services version ");

        int available= GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(SearchActivity.this);

        if(available== ConnectionResult.SUCCESS) {
            //everything is fine and the user can make map request
            Log.d(TAG, "isServicesOK: Google Play is working");
            return true;
        }
        else if(GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            //if error occurred but can be fixed
            Log.d(TAG, "isServicesOK: Error occured but can be fixed");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(SearchActivity.this, available, ERROR_DIALOG_REQUEST);
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
        Intent intent = new Intent(SearchActivity.this,PostActivity.class);
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
                Intent nIntent = new Intent(SearchActivity.this, FeedActivity.class);
                startActivity(nIntent);
                flag = true;
                break;
            case 2:
                Intent mIntent = new Intent(SearchActivity.this, FavoritesActivity.class);
                startActivity(mIntent);
                flag = true;
                break;
            case 3:
                firebaseAuth.signOut();
                Intent oIntent = new Intent(SearchActivity.this, MainActivity.class);
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
            Intent intent = new Intent(SearchActivity.this, MapViewActivity.class);
            startActivity(intent);
            return true;
        }

*/
        if(id == R.id.action_favorites){
            Intent intent = new Intent(SearchActivity.this, FavoritesActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

