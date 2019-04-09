package com.example.muktadirkhan.socialnetworkingapp;

import android.content.Context;
import android.content.DialogInterface;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Time;
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
//                builder.setPositiveButton("Yeah", (dialog, which) ->
//
//                        Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show()
//
//                );
                builder.setPositiveButton("Yeah", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {


                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("posts");
                        reference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                DataSnapshot snap = null;
                                for(DataSnapshot snapshot: dataSnapshot.getChildren()) {
                                    Post post = snapshot.getValue(Post.class);
                                    if(list.get(position).getTime().equals(post.getTime())) {

                                            removeItem(position);
                                            snap = snapshot;
                                            break;
                                    }
                                }
                                if(snap!=null) {
                                    reference.child(snap.getKey()).removeValue();
                                    reference.removeEventListener(this);
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                        Log.i("deleted","deleted");
                    }
                });

                builder.setNegativeButton("Nah", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.show();
                return false;
            }
        });
        holder.author.setText(mylist.getAuthor());
        holder.content.setText(mylist.getContent());
        holder.time.setText(TimeAgo.getTimeAgo(Long.parseLong(mylist.getTime())));
        Log.i("time_time",mylist.getTime().toString());
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

    public void removeItem(int position) {
        list.remove(position);
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }

    class MyHoder extends RecyclerView.ViewHolder{
        TextView author,content,time;
        View view;

        public MyHoder(View itemView) {
            super(itemView);
            author = (TextView) itemView.findViewById(R.id.author_posts);
            content= (TextView) itemView.findViewById(R.id.content_posts);
            time = (TextView) itemView.findViewById(R.id.author_posts_time);
            this.view = itemView;
        }
    }



}
