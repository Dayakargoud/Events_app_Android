package com.example.nikhil.events.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.nikhil.events.Event;
import com.example.nikhil.events.Event_Details_Activity;
import com.example.nikhil.events.R;

import java.util.List;

public class AlleventsRecyclerAdapter extends RecyclerView.Adapter<AlleventsRecyclerAdapter.AlleventHolder> {
    private Context mContext;
    private List<Event> allEvents;

    public AlleventsRecyclerAdapter(Context mContext, List<Event> allEvents) {
        this.mContext = mContext;
        this.allEvents = allEvents;
    }

    @NonNull
    @Override
    public AlleventHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.reycliersingleitem, parent, false);
        AlleventHolder eventHolder = new AlleventHolder(view);


        return eventHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AlleventHolder holder, int position) {

        Event events = allEvents.get(position);
        holder.eventTitles.setText(events.getTitle());
        holder.evetDesc.setText(events.getDesc());
        Glide.with(mContext).load(events.getImage()).into(holder.eventimages);


    }

    @Override
    public int getItemCount() {
        return allEvents.size();
    }


    public class AlleventHolder extends RecyclerView.ViewHolder{

        public ImageView eventimages;
        public TextView eventTitles;
        public TextView evetDesc;


        public AlleventHolder(View itemView) {
            super(itemView);
            eventimages = (ImageView) itemView.findViewById(R.id.eventImage);
            eventTitles = (TextView) itemView.findViewById(R.id.eventtitle);
            evetDesc = (TextView) itemView.findViewById(R.id.eventDescription);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Event ev=allEvents.get(getAdapterPosition());
                    final  Intent intent=new Intent(mContext, Event_Details_Activity.class);
                    intent.putExtra("image",ev.getImage());
                    intent.putExtra("desc",ev.getDesc());
                    intent.putExtra("title",ev.getTitle());
                    intent.putExtra("contact",ev.getContact_info());
                    intent.putExtra("branch",ev.getBranch());
                    intent.putExtra("reg_fee",ev.getReg_fee());
                    intent.putExtra("venue",ev.getVenue());
                    intent.putExtra("postId",ev.getPostId());
                    intent.putExtra("position",getAdapterPosition());


                    //applying shared element transition
                    Pair<View, String> p1 = Pair.create((View)eventimages, ViewCompat.getTransitionName(eventimages));
                    Pair<View, String> p2 = Pair.create((View)eventTitles, ViewCompat.getTransitionName(eventTitles));
                    ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity)mContext,p1,p2);


                    if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.JELLY_BEAN){
                        mContext.startActivity(intent,options.toBundle());}
                    else{
                        mContext.startActivity(intent);
                    }

                }
            });



            }

    }

    private void remove(int position) {
        allEvents.remove(position);
        notifyItemRemoved(position);
    }
}
