package com.example.nikhil.events;


import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.nikhil.events.Adapter.AlleventsRecyclerAdapter;
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
public class AllEventsFragment extends Fragment {
    private DatabaseReference mDatabase;
    private RecyclerView mRecyclerView;
    private List<Event> listEvent;
    private ProgressBar mProgressBar;
    private SwipeRefreshLayout refreshLayout;
    LinearLayoutManager linearLayoutManager;
    public AllEventsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_all_events, container, false);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.contentRecyclerView);
        refreshLayout=(SwipeRefreshLayout)v.findViewById(R.id.all_events_refresh_layout);
        refreshLayout.setColorSchemeResources(R.color.colorAccent);
        mProgressBar = (ProgressBar) v.findViewById(R.id.all_event_loading_progressbar);
         linearLayoutManager=new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        listEvent = new ArrayList<>();
        load_Events();
        //refresh layout
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {

                    @Override public void run() {
                        load_Events();
                        refreshLayout.setRefreshing(false);

                    }

                }, 4000); // Delay in millis

            }

        });

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                refreshLayout.setEnabled(linearLayoutManager.findFirstCompletelyVisibleItemPosition() == 0); // 0 is for first item position
            }
        });

        return v;
    }

    private void load_Events(){


        mDatabase = FirebaseDatabase.getInstance().getReference().child("All_Events");

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapshot:dataSnapshot.getChildren()){
                    for(DataSnapshot eventShopsht:postSnapshot.getChildren()){
                        Event firebaseEvent=eventShopsht.getValue(Event.class);
                    listEvent.add(firebaseEvent);

                }}

                AlleventsRecyclerAdapter adapter=new AlleventsRecyclerAdapter(getContext(),listEvent);
                mRecyclerView.setHasFixedSize(true);
                mRecyclerView.setAdapter(adapter);


                mProgressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
             mProgressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });




    }

}
