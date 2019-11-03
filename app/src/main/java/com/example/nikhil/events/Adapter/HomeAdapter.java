package com.example.nikhil.events.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.nikhil.events.BranchActivity;
import com.example.nikhil.events.R;

import java.util.ArrayList;
import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.HomeViewHolder> {
   List<String> items=new ArrayList<>();
   Context mContext;
   List<Integer> images=new ArrayList<>();
   List<Integer> foregroundColors=new ArrayList<>();


    public HomeAdapter(List<String> items, List<Integer> images, List<Integer> foregroundColors, Context mContext) {
        this.items = items;
        this.mContext = mContext;
        this.images=images;
        this.foregroundColors=foregroundColors;
    }

    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.horizontal_recyclrview_single_item,viewGroup, false);
        HomeViewHolder hV=new HomeViewHolder(view,mContext);

        return hV;
    }

    @Override
    public void onBindViewHolder(@NonNull HomeViewHolder holder, int i) {

        holder.titleText.setText(items.get(i));
        Glide.with(mContext).load(images.get(i)).into(holder.imgage);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            holder.imgage.setForeground(mContext.getDrawable(foregroundColors.get(i)));
                                                            }
    }

    @Override
    public int getItemCount() {

        return items.size();
    }

    public class HomeViewHolder extends RecyclerView.ViewHolder{
         ImageView imgage;
         TextView titleText;


        public HomeViewHolder(@NonNull View itemView, final Context mContext) {
            super(itemView);
            imgage=itemView.findViewById(R.id.hoeizontal_Image);
            titleText=(TextView)itemView.findViewById(R.id.horizontal_item_textView);
             itemView.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                    String itmText=items.get(getAdapterPosition());
                     final  Intent intent=new Intent(mContext, BranchActivity.class);


                     Toast.makeText(mContext, itmText, Toast.LENGTH_SHORT).show();
                     int positon=getAdapterPosition();
                     switch (positon){
                         case 0:{
                             intent.putExtra("branch","EEE");
                             intent.putExtra("activity_title",itmText);
                             break;
                         }
                         case 1:{
                             intent.putExtra("branch","ECE");
                             intent.putExtra("activity_title",itmText);
                             break;
                         }
                         case 2:{
                             intent.putExtra("branch","MCT");
                             intent.putExtra("activity_title",itmText);
                             break;
                         }
                         case 3:{
                             intent.putExtra("branch","IT");
                             intent.putExtra("activity_title",itmText);
                             break;
                         }
                         case 4:{
                             intent.putExtra("branch","CIVIL");
                             intent.putExtra("activity_title",itmText);
                             break;
                         }
                         case 5:{
                             intent.putExtra("branch","METALLURGY");
                             intent.putExtra("activity_title",itmText);
                             break;
                         }
                         case 6:{
                             intent.putExtra("branch","CSE");
                             intent.putExtra("activity_title",itmText);
                             break;
                         }
                         case 7:{
                             intent.putExtra("branch","MECH");
                             intent.putExtra("activity_title",itmText);
                             break;
                         }

                     }
                     mContext.startActivity(intent);


                 }
             });
        }

    }
}
