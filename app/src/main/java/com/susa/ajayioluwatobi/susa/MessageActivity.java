/*
* Author: Christopher Holder
*
* */
package com.susa.ajayioluwatobi.susa;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class MessageActivity extends AppCompatActivity {

    FirebaseFirestore mFirestore;
    String user,user2;
    String a;
    RecyclerView feed;
    LinearLayout bs;
    BottomSheetBehavior bottomSheetBehavior;
    public String doc = "";
    EditText editText;
    Button button;
    Boolean s = false;
    String TAG = "MessageActivity";
    public static class MessageHolder extends RecyclerView.ViewHolder{
        View mView;
        Context mContext;
        String user,text,date;
        public MessageHolder(View itemView){
            super(itemView);
            mView = itemView;
            mContext = itemView.getContext();
        }
        public void setText(String text){
            this.text = text;
            TextView tv = (TextView) mView.findViewById(R.id.textView2);
            tv.setText(text);
        }
        public void setUser(String user){
            this.user = user;
            TextView tv2 = (TextView) mView.findViewById(R.id.textView);
            tv2.setText(user);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout2);
        mFirestore = FirebaseFirestore.getInstance();
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        button = (Button) findViewById(R.id.button);
        user = bundle.getString("user");
        user2 = bundle.getString("user2");
        feed = (RecyclerView) findViewById(R.id.rec);
        editText = (EditText) findViewById(R.id.editText3);
        bs = (LinearLayout) findViewById(R.id.bottom_sheet);
        bottomSheetBehavior = BottomSheetBehavior.from(bs);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);


        feed.setHasFixedSize(true);
        feed.setLayoutManager(new LinearLayoutManager(this));


        Query query = mFirestore.collection("users").document(user)
                .collection("messages").document(user2)
                .collection("chats").orderBy("date", Query.Direction.ASCENDING);


        FirestoreRecyclerOptions<Chat> response = new FirestoreRecyclerOptions
                .Builder<Chat>()
                .setQuery(query,Chat.class)
                .build();
        final FirestoreRecyclerAdapter<Chat,MessageHolder> adapter = new FirestoreRecyclerAdapter<Chat, MessageHolder>(response) {
            @Override
            protected void onBindViewHolder(MessageHolder holder, int position, Chat model) {
                holder.setText(model.getText());
                holder.setUser(model.getUser());
            }

            @NonNull
            @Override
            public MessageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout3,parent,false);
                return new MessageHolder(view);

            }
        };
        adapter.notifyDataSetChanged();
        feed.setAdapter(adapter);
        adapter.startListening();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(editText.getText().toString().equals(""))){

                    Chat chat = new Chat(editText.getText().toString(),user);
                    Email email = new Email();
                    email.setEmail(user);

                    //Adds the messages to the user personal collection.
                    mFirestore.collection("users").document(user)
                            .collection("messages").document(user2)
                            .collection("chats").add(chat);


                    // Replicates them the other way around
                    mFirestore.collection("users").document(user2)
                            .collection("messages").document(user)
                            .collection("chats").add(chat);

                    editText.setText("");
                    //Populates the contact page with emails.
                    //e.g. if you press the msg button of your own post, you will see a list
                    // of the people who have contacted you.
                    mFirestore.collection("users").document(user2)
                            .collection("messages").document(user2)
                            .collection("emails").add(email);

                    adapter.notifyDataSetChanged();
                }
            }
        });

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
