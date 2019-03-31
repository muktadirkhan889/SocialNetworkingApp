package com.example.muktadirkhan.socialnetworkingapp;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class PostsRecyclerAdapter extends RecyclerView.Adapter<PostsRecyclerAdapter.MyHoder> {

    List<Post> list;
    Context context;

    public PostsRecyclerAdapter(List<Post> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public MyHoder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.text_card,parent,false);
        MyHoder myHoder = new MyHoder(view);


        return myHoder;
    }

    @Override
    public void onBindViewHolder(MyHoder holder, int position) {
        Post mylist = list.get(position);
        holder.view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Delete?");
                builder.setMessage("Do you want to delete the post?");
                builder.setPositiveButton("Yeah", (dialog, which) ->

                        Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show()
                );
                builder.setNegativeButton("Nah", (dialog, which) -> dialog.cancel());
                builder.show();
                return false;
            }
        });
        holder.author.setText(mylist.getAuthor());
        holder.content.setText(mylist.getContent());
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

    class MyHoder extends RecyclerView.ViewHolder{
        TextView author,content;
        View view;


        public MyHoder(View itemView) {
            super(itemView);
            author = (TextView) itemView.findViewById(R.id.author_posts);
            content= (TextView) itemView.findViewById(R.id.content_posts);
            this.view = itemView;

        }
    }



}
