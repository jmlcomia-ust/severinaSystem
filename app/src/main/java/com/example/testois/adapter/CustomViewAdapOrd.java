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
import android.widget.Spinner;
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
    OrderRecyclerListener nListener;
  // CustomViewAdapInv.InventoryChecker
    Context context;
    List<Orders> orders;
    severinaDB db;

    public CustomViewAdapOrd(List<Orders> orders, Context context, OrderRecyclerListener nListener) {
        this.orders = orders;
        this.context = context;
        this.nListener = nListener;
        db = new severinaDB(context);

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.view_ord_row, parent, false);
        return new MyViewHolder(view, nListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        try {
            Orders ord = orders.get(position);
            holder.position = position;
            holder.ord_id_txt.setText(String.valueOf(ord.getId()));
            holder.ord_name_txt.setText(ord.getName());
            holder.ord_qty_txt.setText(String.valueOf(ord.getQuantity()));
            holder.ord_desc_txt.setText(ord.getDescription().toUpperCase());
            holder.ord_stat_txt.setText(ord.getStatus().toUpperCase());
            holder.btn_edit.setOnClickListener(v -> {
                Log.d(TAG, "onClick: opening Update Dialog Fragment for Orders.");
                Bundle args = new Bundle();
                args.putInt("pos", position);
                args.putString("id", String.valueOf(ord.getId()));
                args.putString("name", ord.getName());
                args.putInt("qty", ord.getQuantity());
                args.putString("desc", ord.getDescription());
                args.putString("stat", ord.getStatus());
                nListener.gotoUpdateFragment(ord, args);
            });
            holder.btn_delete.setOnClickListener(v -> {
                Log.d(TAG, "onClick: opening Delete Dialog Fragment for Orders.");
                Bundle args = new Bundle();
                args.putString("id", String.valueOf(ord.getId()));
                args.putString("name", ord.getName());
                args.putString("qty", String.valueOf(ord.getQuantity()));
                args.putString("desc", ord.getDescription());
                args.putString("stat", ord.getStatus());
                nListener.gotoDeleteFragment(ord, args);
            });
        } catch (NullPointerException e) {
            Log.e(TAG, "onBindViewHolder: Null Pointer: " + e.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        if(orders != null){
            return orders.size();
        }else return 0;

    }


    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView ord_id_txt, ord_name_txt, ord_qty_txt, ord_stat_txt, ord_desc_txt;
        Button btn_edit, btn_delete;
        int position;
        CustomViewAdapOrd.OrderRecyclerListener nListener;
        View rootView;

        MyViewHolder(@NonNull View itemView, OrderRecyclerListener nListener) {
            super(itemView);
            rootView = itemView;
           this.nListener = nListener;
            ord_id_txt = itemView.findViewById(R.id.view_ord_id);
            ord_name_txt = itemView.findViewById(R.id.view_ord_name);
            ord_qty_txt = itemView.findViewById(R.id.view_ord_qty);
            ord_desc_txt = itemView.findViewById(R.id.view_ord_desc);
            ord_stat_txt = itemView.findViewById(R.id.view_ord_stat);
            btn_edit = itemView.findViewById(R.id.edit_ord);
            btn_delete = itemView.findViewById(R.id.delete_ord);
        }
    }


    public interface OrderRecyclerListener{
        void gotoUpdateFragment(Orders orders, Bundle args);
        void gotoDeleteFragment(Orders orders, Bundle args);
    }  }