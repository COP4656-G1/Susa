/*
* Author: Christopher Holder
*
* */

package com.susa.ajayioluwatobi.susa;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class ContactActivity extends AppCompatActivity {
    FirebaseFirestore mFirestore;
    RecyclerView recyclerView;
    static String user,user2;
    String TAG = "ContactActivity";
    public static class EmailHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        View mView;
        Context mContext;
        String userx;
        public EmailHolder(View itemView){
            super(itemView);
            mView = itemView;
            mContext = itemView.getContext();
            itemView.setOnClickListener(this);
        }

        public void setUser(String user){
            this.userx = user;
            TextView tv2 = (TextView) mView.findViewById(R.id.textView);
            tv2.setText(user);
        }
        @Override
        public void onClick(View view) {
            Log.i("XXXXX","XXXXXX");
            Intent intent = new Intent(mContext.getApplicationContext(),MessageActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("user",user);
            bundle.putString("user2",this.userx);
            intent.putExtras(bundle);
            mContext.startActivity(intent);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        mFirestore = FirebaseFirestore.getInstance();
        recyclerView =  (RecyclerView) findViewById(R.id.rec);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        user = bundle.getString("user");
        user2 = bundle.getString("user2");

        Query query = mFirestore.collection("users").document(user2)
                                .collection("messages").document(user2)
                                .collection("emails");
        FirestoreRecyclerOptions<Email> response = new FirestoreRecyclerOptions
                .Builder<Email>()
                .setQuery(query,Email.class)
                .build();
        final FirestoreRecyclerAdapter<Email,EmailHolder> adapter = new FirestoreRecyclerAdapter<Email, EmailHolder>(response) {
            @Override
            protected void onBindViewHolder(EmailHolder holder, int position, Email model) {
                holder.setUser(model.getEmail());
            }
            @NonNull
            @Override
            public EmailHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.element,parent,false);
                return new EmailHolder(view);
            }
        };
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }
    @Override
    protected void onStop(){
        super.onStop();

    }
    @Override
    protected void onStart(){
        super.onStart();
    }
}
