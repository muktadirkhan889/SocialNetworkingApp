package com.example.muktadirkhan.socialnetworkingapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AllUsersRecyclerAdapter extends RecyclerView.Adapter<AllUsersRecyclerAdapter.MyHolder> {
    List<User> list;
    Context context;
    String email;
    int pos;

    public AllUsersRecyclerAdapter(List<User> list, Context context, String email) {
        this.list = list;
        this.context = context;
        this.email = email;
    }

    @NonNull
    @Override
    public AllUsersRecyclerAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.user_card,parent,false);
        MyHolder myHolder = new MyHolder(view);

        return myHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AllUsersRecyclerAdapter.MyHolder holder, int position) {
        pos=position;
        User mylist = list.get(position);
        Log.i("Name",mylist.getName());
        holder.username.setText(mylist.getName());
        DatabaseReference check_friends_ref = FirebaseDatabase.getInstance().getReference().child("friends/"+email.split("@")[0]);
        check_friends_ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(mylist.getEmail().split("@")[0])) {
                    holder.friendship_status.setImageResource(R.drawable.tick);
                }
                else {
                    holder.friendship_status.setImageResource(R.drawable.plus);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        holder.friendship_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.friendship_status.setImageResource(R.drawable.tick);

                Log.i("email_adapter",email);
                DatabaseReference friendsRef = FirebaseDatabase.getInstance().getReference().child("friends/"+email.split("@")[0]);
                friendsRef.child(list.get(position).getEmail().split("@")[0]).setValue(mylist);
            }
        });

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

    class MyHolder extends RecyclerView.ViewHolder {

        TextView username;
        ImageView friendship_status;

        public MyHolder(View itemView) {
            super(itemView);

            username = (TextView) itemView.findViewById(R.id.username_all_users);
            friendship_status = (ImageView) itemView.findViewById(R.id.friendship_status);
        }
    }
}
