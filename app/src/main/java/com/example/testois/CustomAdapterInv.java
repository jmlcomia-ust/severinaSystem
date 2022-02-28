package com.example.testois;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomAdapterInv extends RecyclerView.Adapter<CustomAdapterInv.MyViewHolder> {

    private Context context;
    private Activity activity;
    private ArrayList inv_id, inv_name, inv_qty, inv_desc;

    CustomAdapterInv(Activity activity, Context context, ArrayList inv_id, ArrayList inv_name, ArrayList inv_qty, ArrayList inv_desc){
        this.activity = activity;
        this.context = context;
        this.inv_id = inv_id;
        this.inv_name = inv_name;
        this.inv_qty = inv_qty;
        this.inv_desc = inv_desc;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dashboard_inv_row, parent, false);
        return new MyViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.inv_id_txt.setText(String.valueOf(inv_id.get(position)));
        holder.inv_name_txt.setText(String.valueOf(inv_name.get(position)));
        holder.inv_qty_txt.setText(String.valueOf(inv_qty.get(position)));
        holder.inv_desc_txt.setText(String.valueOf(inv_desc.get(position)));
        //Recyclerview onClickListener
        holder.inv_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, EditInventory.class);
                intent.putExtra("id", String.valueOf(inv_id.get(position)));
                intent.putExtra("name", String.valueOf(inv_name.get(position)));
                intent.putExtra("quantity", String.valueOf(inv_qty.get(position)));
                intent.putExtra("description", String.valueOf(inv_desc.get(position)));
                activity.startActivityForResult(intent, 1);
            }
        });


    }

    @Override
    public int getItemCount() {
        return inv_id.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView inv_id_txt, inv_name_txt, inv_qty_txt, inv_desc_txt;
        LinearLayout inv_list;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            inv_id_txt = itemView.findViewById(R.id.inv_id_txt);
            inv_name_txt = itemView.findViewById(R.id.inv_name_txt);
            inv_qty_txt = itemView.findViewById(R.id.inv_qty_txt);
            inv_desc_txt = itemView.findViewById(R.id.inv_desc_txt);
            inv_list = itemView.findViewById(R.id.inv_list);
        }

    }

}