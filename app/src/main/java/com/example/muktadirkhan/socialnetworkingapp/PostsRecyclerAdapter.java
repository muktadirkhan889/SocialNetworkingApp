package com.example.muktadirkhan.socialnetworkingapp;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.CountDownTimer;
import android.provider.ContactsContract;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.sql.Time;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Handler;

public class PostsRecyclerAdapter extends RecyclerView.Adapter<PostsRecyclerAdapter.MyHoder> implements TextToSpeech.OnInitListener {

    List<Post> list;
    Context context;
    TextToSpeech tts;

    public PostsRecyclerAdapter(List<Post> list, Context context) {
        this.list = list;
        this.context = context;

    }

    @Override
    public MyHoder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.text_card,parent,false);
        MyHoder myHoder = new MyHoder(view);

        tts = new TextToSpeech(parent.getContext(),this);
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


        holder.speech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.speech.setImageResource(R.drawable.megaphone_active);
                int speechStatus = tts.speak(mylist.getContent(),TextToSpeech.QUEUE_FLUSH,null);
                CountDownTimer timer = new CountDownTimer(1400, 1400)
                {
                    public void onTick(long millisUntilFinished)
                    {
                    }

                    public void onFinish()
                    {
                        holder.speech.setImageResource(R.drawable.megaphone);
                    }
                };
                timer.start();

            }
        });




        holder.author.setText(mylist.getAuthor());
        holder.content.setText(mylist.getContent());
        holder.time.setText(TimeAgo.getTimeAgo(Long.parseLong(mylist.getTime())));
        Random rnd = new Random();
        int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        holder.profile_pic.setBackgroundColor(color);
       /* try {
            byte[] bytes = Base64.decode(mylist.getProfilePic,Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            holder.profile_pic.setImageBitmap(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }*/

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

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int ttsLang = tts.setLanguage(Locale.getDefault());

            if (ttsLang == TextToSpeech.LANG_MISSING_DATA
                    || ttsLang == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "The Language is not supported!");
            } else {
                Log.i("TTS", "Language Supported.");
            }
            Log.i("TTS", "Initialization success.");
        } else {
//            Toast.makeText((, "TTS Initialization failed!", Toast.LENGTH_SHORT).show();
        }

        tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
            @Override
            public void onStart(String s) {

            }

            @Override
            public void onDone(String s) {

            }

            @Override
            public void onError(String s) {

            }
        });

    }

    int done(int i) {
        if(i==1) {
            return 1;
        }
        else {
            return 0;
        }
    }

    class MyHoder extends RecyclerView.ViewHolder{
        TextView author,content,time;
        ImageView speech;
        ImageView profile_pic;
        View view;

        public MyHoder(View itemView) {
            super(itemView);
            author = (TextView) itemView.findViewById(R.id.author_posts);
            content= (TextView) itemView.findViewById(R.id.content_posts);
            time = (TextView) itemView.findViewById(R.id.author_posts_time);
            speech = (ImageView) itemView.findViewById(R.id.speak_icon);
            profile_pic = (ImageView) itemView.findViewById(R.id.profile_image);


            Typeface typefaceRegular = Typeface.createFromAsset(context.getAssets(), "fonts/robotoregular.ttf");
            Typeface typefaceBold = Typeface.createFromAsset(context.getAssets(), "fonts/robotobold.ttf");

            author.setTypeface(typefaceBold);
            content.setTypeface(typefaceRegular);
            time.setTypeface(typefaceRegular);

            this.view = itemView;
        }
    }



}
