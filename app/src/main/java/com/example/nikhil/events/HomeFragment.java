package com.example.nikhil.events;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import com.example.nikhil.events.Adapter.HomeAdapter;

import java.util.ArrayList;

import technolifestyle.com.imageslider.FlipperLayout;
import technolifestyle.com.imageslider.FlipperView;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    private FlipperLayout mFlipper;
    private RecyclerView mHorizontalRecyclerView;
    private ImageView flipperImage;


    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_home, container, false);



        mFlipper = (FlipperLayout)view.findViewById(R.id.flipper);



        setUpFlipper();
        setUpHomeRecyclerView(view);


        return view;
    }


    private void setUpFlipper(){

        String[] url=new String[]{

                "https://firebasestorage.googleapis.com/v0/b/events-a864d.appspot.com/o/Flipper%2FWhatsApp%20Image%202019-04-19%20at%2012.29.14%20PM.jpeg?alt=media&token=403a529d-be9d-4a76-9a8d-53d39ff0ac87",                "https://firebasestorage.googleapis.com/v0/b/events-a864d.appspot.com/o/Flipper%2Feee.jpg?alt=media&token=976b37cd-684d-4973-8f20-96ab4655d1a5",
                "https://firebasestorage.googleapis.com/v0/b/events-a864d.appspot.com/o/Flipper%2Fese.jpg?alt=media&token=3ec83899-7016-47a9-a86b-d3489aa60606",
                "https://firebasestorage.googleapis.com/v0/b/events-a864d.appspot.com/o/Flipper%2Fece.jpg?alt=media&token=8fa65e11-063d-4a73-a84a-8d3d07fb1d40"

        };

        for(int i=0;i<url.length;i++) {

            FlipperView view=new FlipperView(getContext());

            view.setImageUrl(url[i]);
            mFlipper.addFlipperView(view);


        }

    }

    private void setUpHomeRecyclerView(View view){
        mHorizontalRecyclerView=(RecyclerView)view.findViewById(R.id.horizontal_RecyeclerView);
        mHorizontalRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        mHorizontalRecyclerView.setHasFixedSize(true);

        ArrayList<String> list=new ArrayList<>();
         list.add("Potenzia");
        list.add("Microcosm");
        list.add(" Technovation");
        list.add("Yukti");
        list.add("Cinfra");
        list.add("Metallon");
        list.add("Qubit");
        list.add("Ignito");

        ArrayList<Integer> images=new ArrayList<>();
        images.add(R.drawable.eee);
        images.add(R.drawable.microcosm);
        images.add(R.drawable.mct);
        images.add(R.drawable.it);
        images.add(R.drawable.civil);
        images.add(R.drawable.mme);
        images.add(R.drawable.cse);
        images.add(R.drawable.mech);
        ArrayList<Integer> foreground=new ArrayList<>();

        foreground.add(R.drawable.gradient_back);
          foreground.add(R.drawable.gradient_back1);
        foreground.add(
                R.drawable.gradient_back6);
          foreground.add(
                R.drawable.gradient_back2);
          foreground.add(
                R.drawable.gradient_back3);
          foreground.add(
                R.drawable.gradient_back4);
          foreground.add(
                R.drawable.gradient_back5);
          foreground.add(R.drawable.gradient_back);

        HomeAdapter mAdapter=new HomeAdapter(list,images,foreground,getContext());
         mHorizontalRecyclerView.setAdapter(mAdapter);



    }

}
