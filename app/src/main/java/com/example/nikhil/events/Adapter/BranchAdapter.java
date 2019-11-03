package com.example.nikhil.events.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

public class BranchAdapter extends RecyclerView.Adapter<BranchAdapter.BranchHolder> {

    @NonNull
    @Override
    public BranchHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull BranchHolder branchHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class BranchHolder extends RecyclerView.ViewHolder{

        public BranchHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
