package com.example.testois.adapter;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testois.EditInventory;
import com.example.testois.Inventory;
import com.example.testois.Orders;
import com.example.testois.R;
import com.example.testois.severinaDB;

import java.util.ArrayList;
import java.util.List;

public class CustomViewAdapOrd extends RecyclerView.Adapter<CustomViewAdapOrd.MyViewHolder> {
    private static final String TAG = "CustomViewAdapOrd";
    //OrderRecyclerListener mListener;
    Context context;
    List<Orders> orders;
    severinaDB sev;

    public CustomViewAdapOrd(List<Orders> orders, Context context) {
        this.orders = orders;
        this.context = context;
        //this.mListener = mListener;
        sev = new severinaDB(context);

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public CustomViewAdapOrd.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.view_ord_row, parent, false);
        CustomViewAdapOrd.MyViewHolder myViewHolder = new CustomViewAdapOrd.MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewAdapOrd.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        final Orders order = orders.get(position);
        holder.ord_id_txt.setText(String.valueOf(order.getId()));
        holder.ord_name_txt.setText(String.valueOf(order.getName()));
        holder.ord_qty_txt.setText(String.valueOf(order.getQuantity()));
        holder.ord_stat_txt.setText(String.valueOf(order.getStatus().toLowerCase()));
        holder.btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: opening Update Dialog Fragment for Inventory.");
                Bundle args = new Bundle();
                args.putString("id", String.valueOf(order.getId()));
                args.putString("name", order.getName());
                args.putString("qty", order.getQuantity());
                args.putString("desc", order.getStatus());
               // mListener.gotoUpOrderFragment(order, args);
            }
        });
        //Recyclerview onClickListener
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView ord_id_txt, ord_name_txt, ord_qty_txt, ord_stat_txt;
        Button btn_edit, btn_delete;
        //OrderRecyclerListener mListener;
        Orders orders;
        View rootView;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            rootView = itemView;
           // this.mListener = mListener;
            ord_id_txt = itemView.findViewById(R.id.view_ord_id);
            ord_name_txt = itemView.findViewById(R.id.view_ord_name);
            ord_qty_txt = itemView.findViewById(R.id.view_ord_qty);
            ord_stat_txt = itemView.findViewById(R.id.view_ord_stat);
            btn_edit = itemView.findViewById(R.id.edit_ord);
            btn_delete = itemView.findViewById(R.id.delete_ord);

        }
    }


    public interface InventoryRecyclerListener {
        void gotoUpOrderFragment(Orders orders, Bundle args);
        //void gotoDeleteFragment(Orders Orders);
    }

}