package com.example.muktadirkhan.socialnetworkingapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
    List<Message> messageList;

    RecyclerView mMessageRecycler;
    MessageListAdapter mMessageAdapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_list);

        chatbox_edittext = findViewById(R.id.edittext_chatbox);
        send_button = findViewById(R.id.button_chatbox_send);

        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        receiver_email = intent.getStringExtra("email");




        SharedPreferences pref = getSharedPreferences("USER_PREF", Context.MODE_PRIVATE);
        restored_email = pref.getString("email","email");



        mDatabase = FirebaseDatabase.getInstance().getReference().child("chats/");
        send_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(chatbox_edittext.getText()!=null) {
                    Message message = new Message(chatbox_edittext.getText().toString(),restored_email.split("@")[0],name,Long.toString(System.currentTimeMillis()),receiver_email.split("@")[0]);
                    mDatabase.child(Long.toString(System.currentTimeMillis())).setValue(message);
                }
            }
        });

        getChatsDatabase = FirebaseDatabase.getInstance().getReference().child("chats/");
        getChatsDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    Message retrievedMessage = snapshot.getValue(Message.class);
                   Log.i("retr_msg",retrievedMessage.getMessage().toString() + " " + retrievedMessage.getSender() + " " + retrievedMessage.getReceiver() + " " + retrievedMessage.getTime());
                    if(retrievedMessage.getReceiver().equals(receiver_email) && retrievedMessage.getSender().equals(restored_email)) {
                        messageList.add(retrievedMessage);
                        mMessageRecycler = (RecyclerView) findViewById(R.id.reyclerview_message_list);
                        mMessageAdapter = new MessageListAdapter(getApplicationContext(),messageList);
                        mMessageRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
}
