package com.example.muktadirkhan.socialnetworkingapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Type;

public class LoginActivity extends AppCompatActivity {

    Button signupbtn, loginbtn;
    EditText email_edit_text;
    EditText password_edit_text;

    private DatabaseReference mDatabase;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.activity_login);

        signupbtn = findViewById(R.id.signupbtn);
        loginbtn = findViewById(R.id.loginbtn);

        email_edit_text = findViewById(R.id.login_input_email);
        password_edit_text = findViewById(R.id.login_input_password);

        Typeface typefaceRegular = Typeface.createFromAsset(getAssets(), "fonts/robotoregular.ttf");
        email_edit_text.setTypeface(typefaceRegular);
        password_edit_text.setTypeface(typefaceRegular);

        Typeface typefaceBold = Typeface.createFromAsset(getAssets(),"fonts/robotobold.ttf");
        signupbtn.setTypeface(typefaceBold);
        loginbtn.setTypeface(typefaceRegular);

        mDatabase = FirebaseDatabase.getInstance().getReference("users");

        signupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(LoginActivity.this,SignupActivity.class);
                startActivity(intent);

            }
        });

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = email_edit_text.getText().toString();
                String password = password_edit_text.getText().toString();
                final boolean account_exists = false;
                Toast.makeText(LoginActivity.this, "Please wait while we check your details", Toast.LENGTH_SHORT).show();
                mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.hasChild(email.split("@")[0])) {
                            mDatabase.child(email.split("@")[0]).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    User user = dataSnapshot.getValue(User.class);
                                    if(password_edit_text.getText().toString().equals(user.getPassword()) && email_edit_text.getText().toString().equals(user.getEmail())) {
                                        Toast.makeText(LoginActivity.this, "Logged in successfully", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                                        intent.putExtra("object",user);
                                        intent.putExtra("source","fromLoginActivity");
                                        intent.putExtra("email",email_edit_text.getText().toString());
                                        SharedPreferences.Editor editor = getSharedPreferences("USER_PREF",MODE_PRIVATE).edit();
                                        editor.putString("email",user.getEmail());
                                        editor.apply();
                                        startActivity(intent);
                                    } else {
                                        Toast.makeText(LoginActivity.this, "Please check your credentials and try again", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

    }

}
