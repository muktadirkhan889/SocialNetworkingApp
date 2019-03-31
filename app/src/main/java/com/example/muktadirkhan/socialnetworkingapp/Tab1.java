package com.example.muktadirkhan.socialnetworkingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class Tab1 extends Fragment {

    DatabaseReference mDatabase;
    ArrayList<Post> list;
    RecyclerView recyclerView;
    FloatingActionButton fab;

    PostsRecyclerAdapter recyclerAdapter;
    RecyclerView.LayoutManager recyce;
    //Overriden method onCreateView
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View RootView = inflater.inflate(R.layout.tab1, container, false);
        final FragmentActivity c = getActivity();
        list=new ArrayList<>();
        recyclerView = (RecyclerView) RootView.findViewById(R.id.recycle);
        mDatabase = FirebaseDatabase.getInstance().getReference("posts");
        fab = RootView.findViewById(R.id.fab);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),NewPostActivity.class);
                MainActivity activity = (MainActivity) getActivity();
                String name = "";
                name = activity.getName();
                intent.putExtra("name",name);
                startActivity(intent);
            }
        });

        fab.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                recyclerAdapter = new PostsRecyclerAdapter(list,getActivity());
                recyce = new GridLayoutManager(getActivity(),1);
                /// RecyclerView.LayoutManager recyce = new LinearLayoutManager(MainActivity.this);
                // recycle.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
                recyclerView.setLayoutManager(recyce);
                Log.i("data",list.size()+"");
                recyclerView.setAdapter(recyclerAdapter);
                recyclerView.setItemAnimator( new DefaultItemAnimator());
                Toast.makeText(c, "Refreshed", Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                    Log.v("maybe",""+ childDataSnapshot.getKey()); //displays the key for the node
                    mDatabase.child(childDataSnapshot.getKey().toString()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            try {
                                if(dataSnapshot.getValue()!=null) {
                                    try {
                                        Post retrieve_post = dataSnapshot.getValue(Post.class);
                                        Post current_post = new Post();
                                        String author = retrieve_post.getAuthor();
                                        String content = retrieve_post.getContent();
                                        current_post.setAuthor(author);
                                        current_post.setContent(content);
                                        list.add(current_post);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                            Log.w("Hello", "Failed to read value.", databaseError.toException());

                        }
                    });
//                    Log.v("scnd",""+ mDatabase.child(childDataSnapshot.getKey().toString()));   //gives the value for given keyname
                }
                recyclerAdapter = new PostsRecyclerAdapter(list,getActivity());
                recyce = new GridLayoutManager(getActivity(),1);
                /// RecyclerView.LayoutManager recyce = new LinearLayoutManager(MainActivity.this);
                // recycle.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
                recyclerView.setLayoutManager(recyce);
                Log.i("data",list.size()+"");
                recyclerView.setAdapter(recyclerAdapter);
                recyclerView.setItemAnimator( new DefaultItemAnimator());


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        




        //Returning the layout file after inflating
        //Change R.layout.tab1 in you classes
        setRetainInstance(true);
        return RootView;
    }


}
