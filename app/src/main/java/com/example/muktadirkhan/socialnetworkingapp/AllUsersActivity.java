package com.example.muktadirkhan.socialnetworkingapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AllUsersActivity extends AppCompatActivity {

    ArrayList<User> list;
    RecyclerView recyclerView;
    DatabaseReference mDatabase;
    AllUsersRecyclerAdapter recyclerAdapter;
    RecyclerView.LayoutManager recyce;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_users);
        list = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.recycle);
        mDatabase = FirebaseDatabase.getInstance().getReference("users");

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot childDataSnapshot: dataSnapshot.getChildren()) {

                    list.add(childDataSnapshot.getValue(User.class));
                }
                recyclerAdapter = new AllUsersRecyclerAdapter(list,AllUsersActivity.this);
                recyce = new GridLayoutManager(AllUsersActivity.this,1);
                recyclerView.setLayoutManager(recyce);
                Log.i("data",list.size()+"");
                recyclerView.setAdapter(recyclerAdapter);
                recyclerView.setItemAnimator( new DefaultItemAnimator());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });

    }

}
