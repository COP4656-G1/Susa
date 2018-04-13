package com.susa.ajayioluwatobi.susa;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import android.content.res.Configuration;					//These are added to implement the Navigation Drawer - WJL
import android.support.v4.widget.DrawerLayout;				
import android.support.v7.app.ActionBarDrawerToggle;			

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class NotificationsActivity extends AppCompatActivity {
	DrawerLayout mDrawer; 					//implemented for the drawer item - WJL
    ActionBarDrawerToggle mDrawerToggle; 	//implemented to add Action to the drawer - WJL 
	private ListView mDrawerList; 			//implement to store Tab Names - WJL
    private ArrayList<String> list; 		//implement to store Tab Names - WJL
	
    private RecyclerView mPostList;
    private DatabaseReference mDatabase, db, db2, db3,;
	private DatabaseReference notifyDB, notifyDB1, notifyDB2, notifyDB3, notifyDB4;
	
    private FirebaseAuth firebaseAuth;		//
	
    private FloatingActionButton fab;
    private LinearLayout bs;
    public static TextView tv1,tv2;
    public static BottomSheetBehavior bottomSheetBehavior;
	
    public static class NotificationHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        View mView;
        Context mContext;
        String username;
        String email;

        public NotificationHolder(View itemView)
        {
            super(itemView);
            mView= itemView;
            mContext = itemView.getContext();
            itemView.setOnClickListener(this);

        }
		
        @Override
        public void onClick(View view) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            tv1.setText(email);
            
        }

        public void setAction(String action){
            TextView post_action= (TextView)mView.findViewById(R.id.action_taken);
            post_action.setText(action);
			
			if(action.equals("liked"){
				TextView your_post = (TextView)mView.findViewById(R.id.your_post);
				your_post.setVisibility(your_post.VISIBLE);
			}
			else if(action.equals("contact"){
				TextView wants_to = (TextView)mView.findViewById(R.id.wants_to);
				wants_to.setVisibility(wants_to.VISIBLE);
			}
			
        }


        public void setNotify(String notifyID){}
		
		notifyDB = FirebaseDatabase.getInstance().getReference();

        FirebaseUser user = firebaseAuth.getCurrentUser();

        notifyDB1 = notifyDB.child("users");				// to reach the posts of users - WJL
		notifyDB1 = notifyDB.child("user-info");
		notifyDB2 = notifyDB1.child(notifyID);
		notifyDB3 = notifyDB2.child("name");
		notifyDB4 = notifyDB2.child("email");
		
		notifyDB3.addValueEventListener(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot dataSnapshot) {
				TextView user_name= (TextView)mView.findViewById(R.id.user_name);
				username = dataSnapshot.getValue(String.class);
				user_name.setText(username);
			}

			@Override
			public void onCancelled(DatabaseError databaseError) {
				Log.w(TAG, "onCancelled", databaseError.toException());
			}
		});
		
		notifyDB4.addValueEventListener(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot dataSnapshot) {
				email = dataSnapshot.getValue(String.class);
			}

			@Override
			public void onCancelled(DatabaseError databaseError) {
				Log.w(TAG, "onCancelled", databaseError.toException();
			}
		});
		//Need to get name and email of user
         //   TextView post_city= (TextView)mView.findViewById(R.id.post_location);
          //  loc = city;
          //  post_city.setText(city);
        }


    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
		
		
        firebaseAuth= FirebaseAuth.getInstance();		//
		
		String userid = "User Name";		//FirebaseAuth.getInstance().getCurrentUser().getUid(); //
		String Uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
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
		
		
		
        fab = (FloatingActionButton) findViewById(R.id.fab);
        //Bottom sheet implementation from
        // https://medium.com/android-bits/android-bottom-sheet-30284293f066
        tv1 = (TextView) findViewById(R.id.tv1);
        tv2 = (TextView) findViewById(R.id.tv2);
        bs = (LinearLayout) findViewById(R.id.bottom_sheet);
        bottomSheetBehavior = BottomSheetBehavior.from(bs);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        Intent intent= getIntent();

        mDatabase= FirebaseDatabase.getInstance().getReference();

        FirebaseUser user = firebaseAuth.getCurrentUser();

        db = mDatabase.child("user-notifications");				// to reach the posts of users - WJL
		db2 = db.child(Uid);
		db3 = db.child("notifications")
		
		
        mDatabase.keepSynced(true);

        mPostList= (RecyclerView) findViewById(R.id.my_recyclerView);
        mPostList.setHasFixedSize(true);
        mPostList.setLayoutManager(new LinearLayoutManager(this));
		
		


    }


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<Notification, NotificationHolder> firebaseRecyclerAdapter= new FirebaseRecyclerAdapter<Notification, NotificationHolder>
                (Notification.class,R.layout.notify_post_row, NotificationHolder.class, db3) {
            @Override
            protected void populateViewHolder(NotificationHolder viewHolder, Notification model, int position) {

                viewHolder.setAction(model.getAction());
                viewHolder.setNotify(model.getNotifyID());
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
				Intent mIntent = new Intent(NotificationsActivity.this, FeedActivity.class);
                startActivity(mIntent);
                flag = true;
                break;
            case 3:
				Intent nIntent = new Intent(NotificationsActivity.this, FavoritesActivity.class);
                startActivity(nIntent);
                flag = true;
                break;
			case 4:
                break;
			case 5:
				firebaseAuth.signOut();
                Intent oIntent = new Intent(NotificationsActivity.this, MainActivity.class);
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