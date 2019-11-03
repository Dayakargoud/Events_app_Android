package com.example.nikhil.events;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.UUID;

public class Event_Details_Activity extends AppCompatActivity {
    private Toolbar mToolbar;
    private ImageView image;
    private TextView title,description,contact_Info,reg_fee,venue,branch;
    private DatabaseReference  mDatabaseReference;;
    private StorageReference mRef;
    private  String mPost_key=null;
    private String imagevalue,descValue,titleValue,contactValue,branchValue,reg_fee_Value,venueValue,postId;
    private boolean is_favorite=false;
    private int adapter_postion;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);
       user= FirebaseAuth.getInstance().getCurrentUser();
        setUpUIviews();

        Intent intent = getIntent();
        if(intent!=null){
        getValuesFromIntent(intent);
            search_favorites(postId);

        }

    }


    private void setUpUIviews(){
        mToolbar=(Toolbar)findViewById(R.id.singlepostToolbar);
        image=(ImageView)findViewById(R.id.singlepostImage);
        title=(TextView)findViewById(R.id.singleposttitle);
        description=(TextView)findViewById(R.id.singlepostDescription);
        contact_Info=(TextView)findViewById(R.id.singlepostcontact);
        reg_fee=(TextView)findViewById(R.id.single_reg_fee_value);
        venue=(TextView)findViewById(R.id.single_venue_value);
        branch=(TextView)findViewById(R.id.single_branch_value);
        setSupportActionBar(mToolbar);
        CollapsingToolbarLayout collapsingToolbarLayout=(CollapsingToolbarLayout)findViewById(R.id.collapsingToolarView);
        getSupportActionBar().setTitle("Event");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    private void getValuesFromIntent(Intent intent){
        imagevalue = intent.getStringExtra("image");
        descValue=intent.getStringExtra("desc");
        titleValue=intent.getStringExtra("title");
        contactValue=intent.getStringExtra("contact");
        branchValue=intent.getStringExtra("branch");
        reg_fee_Value=intent.getStringExtra("reg_fee");
        venueValue=intent.getStringExtra("venue");
        postId=intent.getStringExtra("postId");
        adapter_postion= (int) intent.getIntExtra("position",0);


        Glide.with(this).load(imagevalue).into(image);
        title.setText(titleValue);
        description.setText(descValue);
        contact_Info.setText(contactValue);
        reg_fee.setText(reg_fee_Value);
        branch.setText(branchValue);
        venue.setText(venueValue);
        if(postId==null){
            postId=UUID.randomUUID().toString();
            Toast.makeText(this, "generated id "+postId, Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.event_details_toolbar_menu,menu);
        if(is_favorite){
         MenuItem item=menu.findItem(R.id.favourite_Event);
         item.setIcon(R.drawable.ic_favorite_black_24dp);}
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()) {
            case android.R.id.home: {
                onBackPressed();
                break;
            }
            case R.id.share_single_Event:{
                Toast.makeText(this, "Sharing Event", Toast.LENGTH_SHORT).show();
               break;
            }
            case R.id.favourite_Event:{
                if(is_favorite) {
                    remove_favourite(postId);
                    item.setIcon(R.drawable.ic_favorite_border_black_24dp);

                }else {
                    add_to_favourites(postId);
                    item.setIcon(R.drawable.ic_favorite_black_24dp);
                }
            }

                break;
            case R.id.chat:{

                Toast.makeText(this, "Feature will added soon..", Toast.LENGTH_SHORT).show();
            }

        }
        return super.onOptionsItemSelected(item);


    }

    private void search_favorites(String postId){
         if(user!=null){
             String uid=user.getUid();
             DatabaseReference mRef=FirebaseDatabase.getInstance().getReference().child("favourites").child(uid).child(postId);
             mRef.addListenerForSingleValueEvent(new ValueEventListener() {
                 @Override
                 public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                     if(dataSnapshot.exists()) {
                         is_favorite=true;

                     }
                 }

                 @Override
                 public void onCancelled(@NonNull DatabaseError databaseError) {

                 }
             });

         }




    }
    private void add_to_favourites(String postid) {
        if (user != null) {
            String uid = user.getUid();

            mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("favourites").child(uid);

            Event favourite = new Event(titleValue, descValue, imagevalue, contactValue, reg_fee_Value, venueValue, branchValue, postId);
            mDatabaseReference.child(postid).setValue(favourite);
            Toast.makeText(Event_Details_Activity.this, "Favourited", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(this, "Failed to add", Toast.LENGTH_SHORT).show();
        }
    }
    private void remove_favourite(String postId){
        if(user!=null){
            String uid=user.getUid();
            DatabaseReference mRef=FirebaseDatabase.getInstance().getReference().child("favourites").child(uid).child(postId);
            mRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()) {
                        dataSnapshot.getRef().removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                is_favorite=false;
                            }
                        });
                        Toast.makeText(Event_Details_Activity.this, "Removed from favourites", Toast.LENGTH_SHORT).show();

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }

    }

}
