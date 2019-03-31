package com.example.muktadirkhan.socialnetworkingapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Locale;

public class NewPostActivity extends AppCompatActivity {

    DatabaseReference mDatabase;
    EditText new_post_content;
    Button post_button;
    ImageButton speak_button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);

        new_post_content = findViewById(R.id.content_new_post);
        post_button = findViewById(R.id.post_it);
        speak_button = findViewById(R.id.microphone);
        final SpeechRecognizer mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);


        post_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getIntent();

                String name = intent.getStringExtra("name");
                String post_content = new_post_content.getText().toString();


                Post new_post = new Post(name,post_content,Long.toString(System.currentTimeMillis()));
                mDatabase.child(Long.toString(Long.MAX_VALUE - System.currentTimeMillis())).setValue(new_post);

                Toast.makeText(NewPostActivity.this, "Posted", Toast.LENGTH_SHORT).show();
                Intent back_intent = new Intent(NewPostActivity.this,MainActivity.class);
                back_intent.putExtra("author",name);
                back_intent.putExtra("source","fromNewPostActivity");
                startActivity(back_intent);

            }
        });

        final Intent mSpeechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,
                Locale.getDefault());

        mSpeechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {

            }

            @Override
            public void onBeginningOfSpeech() {

            }

            @Override
            public void onRmsChanged(float v) {

            }

            @Override
            public void onBufferReceived(byte[] bytes) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int i) {

            }

            @Override
            public void onResults(Bundle bundle) {
                //getting all the matches
                ArrayList<String> matches = bundle
                        .getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                Log.i("matches",matches.toString());

                //displaying the first match
                if (matches != null)
                    new_post_content.setText(matches.get(0));
            }

            @Override
            public void onPartialResults(Bundle bundle) {

            }

            @Override
            public void onEvent(int i, Bundle bundle) {

            }
        });
        speak_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        speak_button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_UP:
                        mSpeechRecognizer.stopListening();
                        new_post_content.setHint("You will see input here");
                        break;

                    case MotionEvent.ACTION_DOWN:
                        checkPermission();
                        mSpeechRecognizer.startListening(mSpeechRecognizerIntent);
                        new_post_content.setText("");
                        new_post_content.setHint("Listening...");
                        break;
                }
                return false;
            }

        });


        mDatabase = FirebaseDatabase.getInstance().getReference("posts");

    }
    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!(ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED)) {
                Toast.makeText(this, "Please grant the permission to use the microphone", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.parse("package:" + getPackageName()));
                startActivity(intent);
                finish();
            }
        }
    }
}
