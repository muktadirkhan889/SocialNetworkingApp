package com.example.muktadirkhan.socialnetworkingapp;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private static FragmentManager fragmentManager;
    User usermain;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Adding toolbar to the activity
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        User user = new User();

        // intent from login activity
        if(intent.getStringExtra("source").equals("fromLoginActivity")) {
            user = (User) intent.getSerializableExtra("object");
            Log.i("user_object",user.getEmail().toString());
            email = user.getEmail().toString();
        }

        // intent from elsewhere
        else {
            user.setName(intent.getStringExtra("author").toString());
            user.setDob("");
            user.setEmail("");
            user.setGender("");
            user.setPassword("");
        }
//        Toast.makeText(this, user.getEmail().toString(), Toast.LENGTH_SHORT).show();
        usermain = user;

        //Initializing the tablayout
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);

        //Adding the tabs using addTab() method

        tabLayout.addTab(tabLayout.newTab().setText("Feed"));
        tabLayout.addTab(tabLayout.newTab().setText("Friends"));
        tabLayout.addTab(tabLayout.newTab().setText("Chats"));
        tabLayout.addTab(tabLayout.newTab().setText("Me"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        //Initializing viewPager
        viewPager = (ViewPager) findViewById(R.id.pager);
//        tabLayout.setupWithViewPager(viewPager);
        //to retain fragment state
        viewPager.setOffscreenPageLimit(4);
        //Creating our pager adapter
        Pager adapter = new Pager(getSupportFragmentManager(), tabLayout.getTabCount());

        //Adding adapter to pager
        viewPager.setAdapter(adapter);


        //Adding onTabSelectedListener to swipe views
        tabLayout.setOnTabSelectedListener(this);






    }



    public String getName() {
        try {
            return usermain.getName();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public String getEmail() {
        try {
            return  usermain.getEmail();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public String getPassword() {
        try {
            return  usermain.getPassword();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public String getDob() {
        try {
            return  usermain.getDob();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public String getGender() {
        try {
            return  usermain.getGender();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {

        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_favorite) {
            Intent intent = new Intent(MainActivity.this,AllUsersActivity.class);
            intent.putExtra("email",usermain.getEmail());
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);


    }

}
