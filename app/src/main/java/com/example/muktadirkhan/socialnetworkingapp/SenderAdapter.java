package com.example.muktadirkhan.socialnetworkingapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class SenderAdapter extends RecyclerView.Adapter<SenderAdapter.MyHolder> {

    private ArrayList<String> messages;
    private Context context;

    public SenderAdapter(Context context, ArrayList<String> messages) {
        this.context=context;
        this.messages=messages;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_message_sent,parent);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class  MyHolder extends RecyclerView.ViewHolder{

        public MyHolder(View itemView) {
            super(itemView);
        }
    }
}
