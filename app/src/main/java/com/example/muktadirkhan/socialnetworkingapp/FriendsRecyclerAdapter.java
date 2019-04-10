package com.example.muktadirkhan.socialnetworkingapp;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class FriendsRecyclerAdapter extends RecyclerView.Adapter<FriendsRecyclerAdapter.MyHolder> {

    List<User> list;
    Context context;
    String email;

    public FriendsRecyclerAdapter(List<User> list, Context context, String email) {
        this.list = list;
        this.context = context;
        this.email = email;
    }

    @NonNull
    @Override
    public FriendsRecyclerAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.friends_card,parent, false);
        MyHolder myHolder = new MyHolder(view);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FriendsRecyclerAdapter.MyHolder holder, int position) {
        User mylist = list.get(position);
        holder.username.setText(mylist.getName());
    }

    @Override
    public int getItemCount() {
        int arr = 0;
        try{
            if(list.size()==0){
                arr = 0;
            }
            else{
                arr=list.size();
            }
        }catch (Exception e){
        }
        return arr;
    }

    class MyHolder extends  RecyclerView.ViewHolder {

        TextView username;

        public MyHolder(View itemView) {
            super(itemView);
            username = (TextView) itemView.findViewById(R.id.username_friends);

            Typeface typefaceRegular = Typeface.createFromAsset(context.getAssets(), "fonts/robotoregular.ttf");
            Typeface typefaceBold = Typeface.createFromAsset(context.getAssets(), "fonts/robotobold.ttf");

            username.setTypeface(typefaceBold);
        }
    }
}
