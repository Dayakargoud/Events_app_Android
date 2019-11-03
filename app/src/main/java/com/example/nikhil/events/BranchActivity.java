package com.example.nikhil.events;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nikhil.events.Adapter.AlleventsRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class BranchActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private RecyclerView mRecyclerView;
    private List<Event> listEvent;
    private Toolbar mToolbar;
    private ProgressBar mProgressBar;
    private String toolbarTitle;
    private TextView adapterItems;
    private SwipeRefreshLayout refreshLayout;
    private String branchValue;
    private LinearLayoutManager linearLayoutManager;
    private TextView branch_intro,event_Name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_branch);
        Intent intent=getIntent();
        setUpUIviews();

        toolbarTitle =getToolbarTitle(intent);
        getSupportActionBar().setTitle(toolbarTitle);

        branchValue=getBranchValue(intent);

        branch_intro.setText(getResources().getIdentifier(getBranchValue(intent),"string",getPackageName()));
        event_Name.setText(toolbarTitle);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("All_Events").child(branchValue);
          load_branch_events();

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });


    }
    void setUpUIviews(){
        mToolbar = (Toolbar) findViewById(R.id.branchtoolbar);
        adapterItems=(TextView)findViewById(R.id.itemcount);
        branch_intro=(TextView)findViewById(R.id.event_intro);
        event_Name=(TextView)findViewById(R.id.event_name);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mRecyclerView = (RecyclerView)findViewById(R.id.branchRecyclerView);
        linearLayoutManager=new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        mProgressBar=(ProgressBar)findViewById(R.id.branch_event_loading_progressbar);
        //refresh layout


    }
    private String getToolbarTitle(Intent intent){

        return intent.getStringExtra("activity_title");
    }
    private String getBranchValue(Intent intent){
        String s=intent.getStringExtra("branch");
        if(s.isEmpty()){
            return " ";
        }else return s;

    }


    private void load_branch_events() {
        listEvent=new ArrayList<>();
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Event firebaseEvent = postSnapshot.getValue(Event.class);
                    listEvent.add(firebaseEvent);

                }

                AlleventsRecyclerAdapter adapter = new AlleventsRecyclerAdapter(BranchActivity.this, listEvent);
                mRecyclerView.setHasFixedSize(true);
                mRecyclerView.setAdapter(adapter);
                if (adapter.getItemCount() == 0) {
                    adapterItems.setText("No Events Found...");
                    adapterItems.setVisibility(View.VISIBLE);

                }


                mProgressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                mProgressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(BranchActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()) {
            case android.R.id.home: {
                onBackPressed();
            }

        }
        return super.onOptionsItemSelected(item);


    }
}
