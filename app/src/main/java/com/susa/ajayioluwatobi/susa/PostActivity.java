package com.susa.ajayioluwatobi.susa;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import android.content.res.Configuration;//These are added to implement the Navigation Drawer - WJL
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;


import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

public class PostActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    DrawerLayout mDrawer; 					//implemented for the drawer item - WJL
    ActionBarDrawerToggle mDrawerToggle; 	//implemented to add Action to the drawer - WJL
    private ListView mDrawerList; 			//implement to store Tab Names - WJL
    private ArrayList<String> list; 		//implement to store Tab Names - WJL

    private DatabaseReference databaseReference,db2;
    private  Uri mdownloadUri;
    private EditText address, price,desc;
    private Button post;
    private Button feed;
    private FirebaseAuth firebaseAuth;
    private Spinner location;
    private Button mSelectImage;
    private boolean location_assert= false;
    private String city;
    private ImageView image_1;
    private ImageView image_2;
    private ImageView image_3;
    private StorageReference mStorage;
    private static final int GALLERY_INTENT= 2;

    // Source for firebase storage image upload https://www.youtube.com/watch?v=mSi7bNk4ySM

    Uri post_image;
    Uri post_image2;
    Uri post_image3;




    boolean image1= false;
    boolean image2= false;
    boolean image3= false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        address = (EditText) findViewById(R.id.address_Text);
        price= (EditText) findViewById(R.id.edit_Price);
        post= (Button) findViewById(R.id.Post_Button);
        feed= (Button) findViewById(R.id.feed_button);
        desc = (EditText) findViewById(R.id.editText);
        firebaseAuth= FirebaseAuth.getInstance();
        location = (Spinner) findViewById(R.id.location_spinner);
        location.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);
        mSelectImage= (Button) findViewById(R.id.upload_image) ;
        mdownloadUri= Uri.parse("android.resource://"+getApplicationContext().getPackageName()+"/drawable/no_image_available");
         post_image= Uri.parse("android.resource://"+getApplicationContext().getPackageName()+"/drawable/no_image_available");
         post_image2 = Uri.parse("android.resource://"+getApplicationContext().getPackageName()+"/drawable/no_image_available");
         post_image3 = Uri.parse("android.resource://"+getApplicationContext().getPackageName()+"/drawable/no_image_available");
        image_1= (ImageView) findViewById(R.id.image_1);
        image_2=  (ImageView)   findViewById(R.id.image_2);
        image_3=    (ImageView) findViewById(R.id.image_3);

        Toolbar myToolbar=(Toolbar) findViewById(R.id.my_toolbar) ;
       // myToolbar.inflateMenu(R.menu.bar_meu);

        setSupportActionBar(myToolbar);



        String userid = "User Name";		//FirebaseAuth.getInstance().getCurrentUser().getUid(); //
        String Uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String useremail = firebaseAuth.getInstance().getCurrentUser().getEmail();  //to get useremail to display in drawer
        list = new ArrayList<String>();  	//Store in array List - WJL
        list.add(userid);
        list.add(useremail);

        //Fill the drawers. Code came from https://www.androidcode.ninja/android-navigation-drawer-example/

        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        ObjectDrawerItem[] drawerItem = new ObjectDrawerItem[5];

        drawerItem[0] = new ObjectDrawerItem(R.drawable.ic_account_circle_black_24dp, userid);      //These create the tabs to be clicked - WJL
        drawerItem[1] = new ObjectDrawerItem(R.drawable.ic_email_black_24dp, useremail);            //
        drawerItem[2] = new ObjectDrawerItem(R.drawable.ic_home_black_24dp, "Home");
        drawerItem[3] = new ObjectDrawerItem(R.drawable.ic_stars_black_24dp, "Favorites");
        drawerItem[4] = new ObjectDrawerItem(R.drawable.ic_power_settings_new_black_24dp, "Sign out");  //

        DrawerItemCustomAdapter madapter = new DrawerItemCustomAdapter(this, R.layout.listview_item_row, drawerItem); //Adapter to create the listview - WJL

        mDrawerList.setAdapter(madapter);//

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


        mStorage= FirebaseStorage.getInstance().getReference();

        mSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,GALLERY_INTENT);
            }
        });

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.location, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        location.setAdapter(adapter);

        databaseReference= FirebaseDatabase.getInstance().getReference();

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(location_assert && FormComplete() ) {
                    postLease();
                    Intent intent = new Intent(PostActivity.this, FeedActivity.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(PostActivity.this ,"Please Fill in fields" ,Toast.LENGTH_LONG).show();
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


    //This function helps us deal with the images.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==GALLERY_INTENT && resultCode == RESULT_OK){

            //mProgressDialog.setMessage("Uploading ...");
            Uri uri= data.getData();
            FirebaseUser user = firebaseAuth.getCurrentUser();

            StorageReference filepath= mStorage.child(user.getUid()).child(uri.getLastPathSegment());

            filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(PostActivity.this, "Upload Done", Toast.LENGTH_LONG).show();

                    mdownloadUri= Uri.parse("android.resource://"+getApplicationContext().getPackageName()+"/drawable/no_image_available");

                    mdownloadUri = taskSnapshot.getDownloadUrl();
                    if(!image1) {
                        Picasso.with(PostActivity.this).load(mdownloadUri).fit().centerCrop().into(image_1);
                        post_image= mdownloadUri;
                        post_image2= mdownloadUri;
                        post_image3= mdownloadUri;



                        image1= true;
                    }
                    else if (!image2){
                        Picasso.with(PostActivity.this).load(mdownloadUri).fit().centerCrop().into(image_2);
                        post_image2= mdownloadUri;
                        image2= true;
                    }
                    else if (!image3){
                        Picasso.with(PostActivity.this).load(mdownloadUri).fit().centerCrop().into(image_3);
                        post_image3= mdownloadUri;
                        image3=true;
                    }
                    else
                    {
                        Toast.makeText(PostActivity.this, "Upload Limit", Toast.LENGTH_LONG).show();
                    }
                }
            });

        }
    }

    //Creates the main post.
    private void postLease(){
        String timeOf;
        String Uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String addy= address.getText().toString().trim();
        String des = desc.getText().toString().trim();
        int value= Integer.parseInt(price.getText().toString().trim());

        String temp_image= post_image.toString();
        String temp_image2= post_image2.toString();
        String temp_image3= post_image3.toString();

        Date date = new Date();
        timeOf = date.toString();


            UserPost userPost = new UserPost(value, addy, city, des, Uid, timeOf, temp_image, temp_image2, temp_image3);


            FirebaseUser user = firebaseAuth.getCurrentUser();
            databaseReference.child("posts").child(timeOf).setValue(userPost);

            Toast.makeText(this, "Posted", Toast.LENGTH_LONG).show();






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
                break;
            case 2:
                Intent nIntent = new Intent(PostActivity.this, FeedActivity.class);
                startActivity(nIntent);
                flag = true;
                break;
            case 3:
                Intent mIntent = new Intent(PostActivity.this, FavoritesActivity.class);
                startActivity(mIntent);
                flag = true;
                break;
            case 4:
                firebaseAuth.signOut();
                Intent oIntent = new Intent(PostActivity.this, MainActivity.class);
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

    private boolean FormComplete(){
    String Address= address.getText().toString().trim();

        String Price = price.getText().toString().trim();
        String Description= desc.getText().toString().trim();

        if(TextUtils.isEmpty(Address)|| TextUtils.isEmpty(Description)|| TextUtils.isEmpty(Price)){
            return false;
        }
        return true;
    }
}
