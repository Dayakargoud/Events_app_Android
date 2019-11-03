package com.example.nikhil.events;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.ShareCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import technolifestyle.com.imageslider.FlipperLayout;

public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        LoginFragment.OnLoginListener,SignUpFragment.SignUpListener {
    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private NavigationView mNavigationView;
    private BottomNavigationView mBottomNav_View;
    private FlipperLayout mFlipper;
    private RecyclerView mRecyclerView;
    private DatabaseReference mDatabase;
    private StorageReference mRef;
    private ImageView flipperImage;
    private List<Event> listEvent;
    private String TAG="MainActivity";
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private TextView txtProfileName;
    SignUpFragment.SignUpListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getTokenFromFireBse();
        mAuth=FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        registeringForFirebaseTopic();

        if(getIntent().getExtras()!=null){
            for(String key:getIntent().getExtras().keySet()){

                if(key.equals("title")){
                    System.out.println(getIntent().getExtras().getString(key));
                }
                if(key.equals("content")){
                    System.out.println(getIntent().getExtras().getString(key));
                }
                if(key.equals("url")){
                    System.out.println(getIntent().getExtras().getString(key));
                }
            }



        }

        setUpUIviews();
        listEvent=new ArrayList<>();
        loadFragment(new HomeFragment());


        if (user != null) {
            // Name, email address, and profile photo Url
            String name = user.getDisplayName();
            txtProfileName.setText(name);
            String email = user.getEmail();

            // Check if user's email is verified
            boolean emailVerified = user.isEmailVerified();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
            String uid = user.getUid();

        }


    }

    private void registeringForFirebaseTopic(){
        FirebaseMessaging.getInstance().subscribeToTopic("updates")
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.i(TAG,"Topic Registation successful");
                    }
                });
    }
    private void setUpUIviews(){
         mToolbar = (Toolbar) findViewById(R.id.maintoolbar);
         mDrawerLayout = (DrawerLayout) findViewById(R.id.mainDrawerLayout);
         mNavigationView = (NavigationView) findViewById(R.id.mainNavView);
         mNavigationView.bringToFront();
         mNavigationView.setNavigationItemSelectedListener(this);
        txtProfileName = (TextView) mNavigationView.getHeaderView(0).findViewById(R.id.nav_username);

         setSupportActionBar(mToolbar);
         getSupportActionBar().setTitle("Events");
         BottomNavigationView navigation = findViewById(R.id.mainBottomnav);
         navigation.setOnNavigationItemSelectedListener(bottomNavListener);

         mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.open, R.string.close);
         mDrawerLayout.addDrawerListener(mToggle);
         mToggle.syncState();



     }
    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_contaner, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    private  void getTokenFromFireBse(){

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();

                        // Log and toast
                        String msg = token.toString();
                        Log.d(TAG, msg);
                      //  Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addOptionoption: {
                Toast.makeText(MainActivity.this, item.getTitle(), Toast.LENGTH_SHORT).show();
                item.setChecked(true);
                mDrawerLayout.closeDrawer(GravityCompat.START);
                Fragment fragment = null;
                fragment = new AddEventFragment();
                loadFragment(fragment);
                break;

            }
            case R.id.share: {
                item.setChecked(true);
                shareApp();
                mDrawerLayout.closeDrawer(GravityCompat.START);

                return true;
            }
            case R.id.about_app: {
                Toast.makeText(MainActivity.this, "Nirvana v0.08-alpha", Toast.LENGTH_SHORT).show();
                item.setChecked(true);
                mDrawerLayout.closeDrawer(GravityCompat.START);

                return true;
            }
            case R.id.feedbackoption: {
                item.setChecked(true);
                sendFeedback();
                mDrawerLayout.closeDrawer(GravityCompat.START);

                return true;
            }


        }
        return false;
    }

    private BottomNavigationView.OnNavigationItemSelectedListener bottomNavListener=new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

            Fragment fragment=null;
            switch (menuItem.getItemId()) {
                case R.id.Home: {
                    fragment = new HomeFragment();
                    menuItem.setChecked(true);
                    getSupportActionBar().setTitle(menuItem.getTitle());
                    break;
                }
                case R.id.events:{
                    fragment=new AllEventsFragment();
                    menuItem.setChecked(true);
                    getSupportActionBar().setTitle(menuItem.getTitle());
                    break;
                }
                case R.id.notifications:{
                    fragment=new NotificationFragment();
                    menuItem.setChecked(true);
                    getSupportActionBar().setTitle(menuItem.getTitle());
                    break;}
                case R.id.favourite: {
                        fragment = new FavouriteFragment();
                        menuItem.setChecked(true);
                        getSupportActionBar().setTitle(menuItem.getTitle());

                } break;
            }
        loadFragment(fragment);
            return true;
        }
    };
    @Override
    public void onBackPressed() {
            super.onBackPressed();


    }
    private void shareApp(){

        String body="To get latest Events from our college download Nirvana app at Free of cost \n"+
                "https://drive.google.com/folderview?id=1XNLmD49jVCpE7sq7InXkLEemnsuZMUmy";

        String mimeType="text/plain";
        ShareCompat.IntentBuilder
                .from(this)
                .setType(mimeType)
                .setChooserTitle("Share App with: ")
                .setText(body)
                .startChooser();
    }
    private void sendFeedback() {

        String mailto = "mailto:timetableapp214@gmail.com" +
                "?cc=" + "dayakargoudbandari@gmail.com" +
                "&subject=" + Uri.encode("Feedback about Niravana Application") +
                "&body=" + Uri.encode("write your valuable feedback ");

        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse(mailto));

        try {
            startActivity(emailIntent);
        } catch (ActivityNotFoundException e) {
            e.getMessage();
            Toast.makeText(this, "No apps found to send email", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onLogin(FirebaseUser user) {

        if(user!=null){
            String username=user.getDisplayName();
            txtProfileName.setText(username);

        }

    }

    @Override
    public void newUsersignUpListener(FirebaseUser user) {
        if(user!=null){
            String username=user.getDisplayName();
            txtProfileName.setText(username);
            Toast.makeText(this, username, Toast.LENGTH_SHORT).show();

        }

    }
}
