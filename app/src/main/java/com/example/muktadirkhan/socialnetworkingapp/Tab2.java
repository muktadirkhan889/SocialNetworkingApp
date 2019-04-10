package com.example.muktadirkhan.socialnetworkingapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Tab2 extends Fragment {

    DatabaseReference mDatabase;
    ArrayList<User> list;
    RecyclerView recyclerView;

    FriendsRecyclerAdapter recyclerAdapter;
    RecyclerView.LayoutManager recyce;
    //Overriden method onCreateView
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View RootView = inflater.inflate(R.layout.tab2,container,false);
        final FragmentActivity c = getActivity();
        list = new ArrayList<>();
        SharedPreferences pref = getActivity().getSharedPreferences("USER_PREF", Context.MODE_PRIVATE);
        String restored_email = pref.getString("email","email");

        recyclerView = (RecyclerView) RootView.findViewById(R.id.recycle);
        mDatabase = FirebaseDatabase.getInstance().getReference("friends/"+restored_email.split("@")[0]);
        MainActivity activity = new MainActivity();
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot childDataSnapshot: dataSnapshot.getChildren()) {

                    list.add(childDataSnapshot.getValue(User.class));

                    recyclerAdapter = new FriendsRecyclerAdapter(list,getActivity(),restored_email);
                    recyce = new GridLayoutManager(getActivity(),1);
                    /// RecyclerView.LayoutManager recyce = new LinearLayoutManager(MainActivity.this);
                    // recycle.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
                    recyclerView.setLayoutManager(recyce);
                    Log.i("data",list.size()+"");
                    recyclerView.setAdapter(recyclerAdapter);
                    recyclerView.setItemAnimator( new DefaultItemAnimator());

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });

        setRetainInstance(true);
        return RootView;
    }

}
