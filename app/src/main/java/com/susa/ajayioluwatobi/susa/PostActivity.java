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

import android.content.res.Configuration;					//These are added to implement the Navigation Drawer - WJL
import android.support.v4.widget.DrawerLayout;				
import android.support.v7.app.ActionBarDrawerToggle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PostActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
	DrawerLayout mDrawer; 					//implemented for the drawer item - WJL
    ActionBarDrawerToggle mDrawerToggle; 	//implemented to add Action to the drawer - WJL 
	private ListView mDrawerList; 			//implement to store Tab Names - WJL
    private ArrayList<String> list; 		//implement to store Tab Names - WJL
	

    private DatabaseReference databaseReference;
	private DatabaseReference mDataBase;	//to reference to get post number - WJL

	
    private EditText address, price,desc;
    private Button post;
    private Button feed;
    private FirebaseAuth firebaseAuth;
    private Spinner location;
    private boolean location_assert= false;
    private String city;
	private int posts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
	//	postID = 0;
		
		//mDataBase = FirebaseDatabase.getInstance().getReference().child("/user-posts");
		
		firebaseAuth= FirebaseAuth.getInstance();		//
		
		String userid = "User Name";		//FirebaseAuth.getInstance().getCurrentUser().getUid(); //
		String useremail = firebaseAuth.getInstance().getCurrentUser().getEmail();  //to get useremail to display in drawer
		list = new ArrayList<String>();  	//Store in array List - WJL
        list.add(userid); 					//
        list.add(useremail); 				//
		
		
		
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout); //
        mDrawerList = (ListView) findViewById(R.id.left_drawer); //

        ObjectDrawerItem[] drawerItem = new ObjectDrawerItem[4]; //

		drawerItem[0] = new ObjectDrawerItem(R.drawable.ic_account_circle_black_24dp, userid);      //These create the tabs to be clicked - WJL
        drawerItem[1] = new ObjectDrawerItem(R.drawable.ic_email_black_24dp, useremail);            //
        drawerItem[2] = new ObjectDrawerItem(R.drawable.ic_email_black_24dp, "Home");
        drawerItem[3] = new ObjectDrawerItem(R.drawable.ic_stars_black_24dp, "Favorites");
        drawerItem[4] = new ObjectDrawerItem(R.drawable.ic_email_black_24dp, "Notifications");   //
        drawerItem[5] = new ObjectDrawerItem(R.drawable.ic_power_settings_new_black_24dp, "Sign out");  //
		
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
		
		//END OF DRAWER CODE IN ONCREATE - WJL
	
        address = (EditText) findViewById(R.id.address_Text);
        price= (EditText) findViewById(R.id.edit_Price);
        post= (Button) findViewById(R.id.Post_Button);
        feed= (Button) findViewById(R.id.feed_button);
        desc = (EditText) findViewById(R.id.editText);
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
		String timeOf;
		String Uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String addy= address.getText().toString().trim();
        String des = desc.getText().toString().trim();
        int value= Integer.parseInt(price.getText().toString().trim());
        String image= "https://images.pexels.com/photos/880481/pexels-photo-880481.jpeg?cs=srgb&dl=arches-architecture-brickwall-880481.jpg&fm=jpg";

		Date date = new Date();
        timeOf = date.toString();

		
        UserPost userPost= new UserPost(value, addy, image, city, des, Uid, timeOf);

        FirebaseUser user = firebaseAuth.getCurrentUser();

        databaseReference.child("posts").child(timeOf).setValue(userPost);

        Toast.makeText(this, "Posted", Toast.LENGTH_LONG).show();
        return;

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
			    Intent mIntent = new Intent(PostActivity.this, FeedActivity.class);
                startActivity(mIntent);
                flag = true;
                break;
            case 3:
              //  Intent mIntent = new Intent(PostActivity.this, FavoritesActivity.class);
              //  startActivity(mIntent);
              //  flag = true;
                break;
			case 4:
               // Intent nIntent = new Intent(PostActivity.this, NotificationsActivity.class);
               // startActivity(nIntent);
               // flag = true;
                break;
			case 5:
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
}
