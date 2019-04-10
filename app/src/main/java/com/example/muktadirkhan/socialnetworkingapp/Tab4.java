package com.example.muktadirkhan.socialnetworkingapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.module.AppGlideModule;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

public class Tab4 extends Fragment {

    TextView name_textview;

    String email;
    String name;
    String password;
    String dob;
    String gender;
    String profile_pic;

    Uri filePath;
    CircleImageView imageView;
    FirebaseStorage storage;
    StorageReference storageReference;
    StorageReference storageDownloadReference;
    private final int PICK_IMAGE_REQUEST = 71;
    Bitmap bitmap;

    User retrievedUser;

    //Overriden method onCreateView
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Returning the layout file after inflating
        //Change R.layout.tab1 in you classes
        View RootView = inflater.inflate(R.layout.tab4, container, false);

        name_textview = (TextView) RootView.findViewById(R.id.name_tab4);
        imageView = (CircleImageView) RootView.findViewById(R.id.profile_image);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        Typeface typefaceRegular = Typeface.createFromAsset(getContext().getAssets(), "fonts/robotoregular.ttf");
        Typeface typefaceBold = Typeface.createFromAsset(getContext().getAssets(), "fonts/robotobold.ttf");

        name_textview.setTypeface(typefaceBold);



        MainActivity activity = (MainActivity) getActivity();
        name = activity.getName();
        email = activity.getEmail();
        password = activity.getPassword();
        dob = activity.getDob();
        gender = activity.getGender();

        name_textview.setText(name);

        DatabaseReference setImagetoImageViewRef = FirebaseDatabase.getInstance().getReference().child("users");

        setImagetoImageViewRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot messageSnapshot: dataSnapshot.getChildren()) {
                    if(messageSnapshot.getKey().equals(email.split("@")[0])) {
                        retrievedUser = messageSnapshot.getValue(User.class);
                        if(retrievedUser.getProfile_pic()!=null) {
                            byte[] decodedString = Base64.decode(retrievedUser.getProfile_pic(), Base64.DEFAULT);
                            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                            imageView.setImageBitmap(decodedByte);
                        }
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
            }
        });
        return RootView;
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), filePath);
                Glide.with(getContext())
                        .asBitmap()
                        .load(bitmap);
               uploadImage();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
    
    // upload image to firebase storage
    private void uploadImage() {

        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
      //  Bitmap bitmap= BitmapFactory.decodeResource(getActivity(),R.drawable.fpp);
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        byte [] bytes= byteArrayOutputStream.toByteArray();
        String pp= Base64.encodeToString(bytes,0);
        profile_pic = pp;

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("users");

        final User user = new User(name,email,password,dob,gender,profile_pic);
        mDatabase.child(email.split("@")[0]).setValue(user);
        Glide.with(getContext())
                .asBitmap()
                .load(bitmap);
        Toast.makeText(getActivity(), "Uploaded Profile Picture", Toast.LENGTH_SHORT).show();
    }

}
