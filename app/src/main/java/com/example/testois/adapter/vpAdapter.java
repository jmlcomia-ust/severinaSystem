package com.example.testois.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testois.ViewPager.ViewPagerTutorial;

import java.util.ArrayList;

public class vpAdapter extends RecyclerView.Adapter<vpAdapter.ViewHolder> {

    ArrayList<ViewPagerTutorial> viewPagerItemArrayList;
    public vpAdapter(ArrayList<ViewPagerTutorial> viewPagerItemArrayList) {
        this.viewPagerItemArrayList = viewPagerItemArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //View view = LayoutInflater.from(parent.getContext())
             //   .inflate(R.layout.activity_view_pager_tutorial,parent,false);

        //return new ViewHolder(view);
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        ViewPagerTutorial viewPagerItem = viewPagerItemArrayList.get(position);
/*
        holder.imageView.setImageResource(viewPagerItem.imageID);
        holder.tcHeading.setText(viewPagerItem.heading);
        holder.tvDesc.setText(viewPagerItem.description);



 */
    }

    @Override
    public int getItemCount() {
        return viewPagerItemArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView tcHeading, tvDesc;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

           // imageView = itemView.findViewById(R.id.ivimage);
           //tcHeading = itemView.findViewById(R.id.tvHeading);
            //tvDesc = itemView.findViewById(R.id.tvDesc);
        }
    }

}

