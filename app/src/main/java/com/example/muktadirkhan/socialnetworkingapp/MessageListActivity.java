package com.example.muktadirkhan.socialnetworkingapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MessageListActivity extends AppCompatActivity {

    TextView sender_textview;
    EditText chatbox_edittext;
    Button send_button;
    DatabaseReference mDatabase;
    DatabaseReference getChatsDatabase;
    String name;
    String restored_email;
    String receiver_email;
    ArrayList<Message> messageList;
    LinearLayout linearLayout;
    ScrollView scrollView;
    Toolbar toolbar;
    TextView toolbar_title;

    RecyclerView mMessageRecycler;
    MessageListAdapter mMessageAdapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_list);
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        chatbox_edittext = findViewById(R.id.edittext_chatbox);
        send_button = findViewById(R.id.button_chatbox_send);
        linearLayout=(LinearLayout)findViewById(R.id.llMessages);
        scrollView=(ScrollView)findViewById(R.id.sv);
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);

        messageList = new ArrayList<>();

        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        Log.i("name_log",name);
        receiver_email = intent.getStringExtra("email");
        toolbar_title.setText(name);







        SharedPreferences pref = getSharedPreferences("USER_PREF", Context.MODE_PRIVATE);
        restored_email = pref.getString("email","email");


        chatbox_edittext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scrollView.post(new Runnable() {
                    @Override
                    public void run() {
                        scrollView.scrollTo(0,linearLayout.getBottom());
                    }
                });
            }
        });

        mDatabase = FirebaseDatabase.getInstance().getReference().child("chats/");
        send_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(chatbox_edittext.getText()!=null) {

                    Message message = new Message(chatbox_edittext.getText().toString(),restored_email.split("@")[0],name,Long.toString(System.currentTimeMillis()),receiver_email.split("@")[0]);
                    mDatabase.child(Long.toString(System.currentTimeMillis())).setValue(message);
                    chatbox_edittext.setText("");
                }
            }
        });

        getChatsDatabase = FirebaseDatabase.getInstance().getReference().child("chats/");
        getChatsDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                linearLayout.removeAllViews();
                for(DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    Message retrievedMessage = snapshot.getValue(Message.class);
                   Log.i("retr_msg",retrievedMessage.getMessage().toString() + " " + retrievedMessage.getSender() + " " + retrievedMessage.getReceiver() + " " + retrievedMessage.getTime());
                   Log.i("retr_msg",receiver_email + " " + restored_email);

                    if((retrievedMessage.getReceiver().equals(receiver_email.split("@")[0]) &&
                            retrievedMessage.getSender().equals(restored_email.split("@")[0])) ||
                            (retrievedMessage.getSender().equals(receiver_email.split("@")[0])&&
                            retrievedMessage.getReceiver().equals(restored_email.split("@")[0]))) {
                       // messageList.add(retrievedMessage);

                        setMessages(retrievedMessage);
                      /*
                        mMessageRecycler = (RecyclerView) findViewById(R.id.reyclerview_message_list);
                        mMessageAdapter = new MessageListAdapter(getApplicationContext(),messageList);
                        mMessageRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));*/
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private void setMessages(Message messages){
        if(messages.getSender().equals(receiver_email.split("@")[0])&& messages.getReceiver().equals(restored_email.split("@")[0])) {


            View view=View.inflate(this,R.layout.item_message_received,null);
            TextView tvName=(TextView)view.findViewById(R.id.text_message_name);
            TextView tvTime=(TextView)view.findViewById(R.id.text_message_time);
            TextView tvMessage=(TextView)view.findViewById(R.id.text_message_body);

            tvName.setText(name);
            tvTime.setText(TimeAgo.getTimeAgo(Long.parseLong(messages.getTime())));
            tvMessage.setText(messages.getMessage());

            linearLayout.addView(view);

        }
        else  if(messages.getReceiver().equals(receiver_email.split("@")[0]) && messages.getSender().equals(restored_email.split("@")[0])){


            View view=View.inflate(this,R.layout.item_message_sent,null);
            TextView tvTime=(TextView)view.findViewById(R.id.text_message_time);
            TextView tvMessage=(TextView)view.findViewById(R.id.text_message_body);

            tvTime.setText(TimeAgo.getTimeAgo(Long.parseLong(messages.getTime())));
            tvMessage.setText(messages.getMessage());

            linearLayout.addView(view);




        }
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.scrollTo(0,linearLayout.getBottom());
            }
        });
    }

}
