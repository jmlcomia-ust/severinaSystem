package com.example.testois;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomAdapterOrd extends RecyclerView.Adapter<CustomAdapterOrd.MyViewHolder> {

    private Context context;
    private Activity activity;
    private ArrayList ord_id, ord_name, ord_qty, ord_stat;

    CustomAdapterOrd(Activity activity, Context context, ArrayList ord_id, ArrayList ord_name, ArrayList ord_qty, ArrayList ord_stat){
        this.activity = activity;
        this.context = context;
        this.ord_id = ord_id;
        this.ord_name = ord_name;
        this.ord_qty = ord_qty;
        this.ord_stat = ord_stat;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dashboard_ord_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.ord_id_txt.setText(String.valueOf(ord_id.get(position)));
        holder.ord_name_txt.setText(String.valueOf(ord_name.get(position)));
        holder.ord_qty_txt.setText(String.valueOf(ord_qty.get(position)));
        holder.ord_stat_txt.setText(String.valueOf(ord_stat.get(position)));
        //Recyclerview onClickListener
        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, EditInventory.class);
                intent.putExtra("id", String.valueOf(ord_id.get(position)));
                intent.putExtra("name", String.valueOf(ord_name.get(position)));
                intent.putExtra("quantity", String.valueOf(ord_qty.get(position)));
                intent.putExtra("status", String.valueOf(ord_stat.get(position)));
                activity.startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    public int getItemCount() {
        return ord_id.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView ord_id_txt, ord_name_txt, ord_qty_txt,ord_stat_txt;
        LinearLayout mainLayout;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ord_id_txt = itemView.findViewById(R.id.ord_id_txt);
            ord_name_txt = itemView.findViewById(R.id.ord_name_txt);
            ord_qty_txt = itemView.findViewById(R.id.ord_qty_txt);
            ord_stat_txt = itemView.findViewById(R.id.ord_stat_txt);
            mainLayout = itemView.findViewById(R.id.mainLayout);
            //Animate Recyclerview
            Animation translate_anim = AnimationUtils.loadAnimation(context, R.anim.translate_anim);
            mainLayout.setAnimation(translate_anim);
        }

    }

}