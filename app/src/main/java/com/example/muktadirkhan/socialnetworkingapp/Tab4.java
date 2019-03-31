package com.example.muktadirkhan.socialnetworkingapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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



        MainActivity activity = (MainActivity) getActivity();
        name = activity.getName();
        email = activity.getEmail();
        password = activity.getPassword();
        dob = activity.getDob();
        gender = activity.getGender();

        name_textview.setText(name);



//        storageDownloadReference = storage.getReference().child("images");

        // download the image from firebase storage and set it to the imageview
      /*  storageDownloadReference.child(email.split("@")[0]).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Log.i("storagetag",uri.toString());
                Toast.makeText(getContext(), uri.toString(), Toast.LENGTH_SHORT).show();
                Glide.with(getContext())
                        .load(uri)
                        .into(imageView);
            }
        });*/


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
                imageView.setImageBitmap(bitmap);
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

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                    mDatabase.child(email.split("@")[0]).setValue(user);

//                    Toast.makeText(getActivity(), "Uploaded Profile Picture", Toast.LENGTH_SHORT).show();



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




//        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//
//                    mDatabase.setValue(pp);
//                    Log.d("pppp",pp);
//                    Toast.makeText(getContext(), "Successfully signed up", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
        /*if(filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(getActivity());
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            StorageReference ref = storageReference.child("images/"+ email.split("@")[0]);
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), "Uploaded", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });
        }*/
    }

}
