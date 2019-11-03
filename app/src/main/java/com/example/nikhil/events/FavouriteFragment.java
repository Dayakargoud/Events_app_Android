package com.example.nikhil.events;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nikhil.events.Adapter.AlleventsRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */

public class FavouriteFragment extends Fragment  {
    private DatabaseReference mDatabaseref;
    private RecyclerView mRecyclerView;
    private List<Event> listEvent;
    private LinearLayoutManager linearLayoutManager;
    private TextView fav_item_info;
    private ProgressBar mProgressBar;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private  AlleventsRecyclerAdapter adapter;
    private Button login;






    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_favourite, container, false);
        mAuth=FirebaseAuth.getInstance();
        currentUser=mAuth.getCurrentUser();
        listEvent=new ArrayList<>();

        mProgressBar=(ProgressBar)v.findViewById(R.id.fav_loading_progressbar);
        fav_item_info=(TextView)v.findViewById(R.id.fav_info_TextView);
        mRecyclerView = (RecyclerView)v.findViewById(R.id.fav_RecyclerView);
        linearLayoutManager=new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        login=v.findViewById(R.id.fav_login);
        if(currentUser==null){
            login.setVisibility(View.VISIBLE);
            fav_item_info.setText("Login to view your favourites");
            fav_item_info.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.INVISIBLE);
        }
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_contaner, new LoginFragment())
                        .commit();

            }
        });

        if(currentUser!=null){
        mDatabaseref = FirebaseDatabase.getInstance().getReference().child("favourites").child(currentUser.getUid());

       load_fav_events();


        }
        return v;
    }
    private void load_fav_events() {

        mDatabaseref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    Event firebaseEvent = postSnapshot.getValue(Event.class);
                    listEvent.add(firebaseEvent);

                }

                adapter = new AlleventsRecyclerAdapter(getContext(), listEvent);
                mRecyclerView.setHasFixedSize(true);
                mRecyclerView.setAdapter(adapter);
                if (adapter.getItemCount() == 0) {

                    fav_item_info.setText("You have not added any favourites yet...");
                    fav_item_info.setVisibility(View.VISIBLE);}


                mProgressBar.setVisibility(View.INVISIBLE);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onPause() {
        super.onPause();
        listEvent.clear();
    }

    @Override
    public void onResume() {
        super.onResume();


    }


}
