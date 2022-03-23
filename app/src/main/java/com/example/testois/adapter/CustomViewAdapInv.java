package com.example.testois.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testois.Inventory;
import com.example.testois.R;
import com.example.testois.ViewInventory;
import com.example.testois.severinaDB;

import java.util.List;

public class CustomViewAdapInv extends RecyclerView.Adapter<CustomViewAdapInv.MyViewHolder>{
    private static final String TAG = "CustomViewAdapInv";
    InventoryRecyclerListener mListener;
    Context context;
    List<Inventory> items;
    severinaDB sev;


    public CustomViewAdapInv(List<Inventory> items, Context context, InventoryRecyclerListener mListener) {
        this.items = items;
        this.context = context;
        this.mListener = mListener;
        sev = new severinaDB(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_inv_row, parent, false);
        return new MyViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        try {
            Inventory inventory = items.get(position);
            holder.position = position;
            holder.txt_id.setText(inventory.getId());
            holder.txt_name.setText(inventory.getName());
            holder.txt_qty.setText(inventory.getQuantity());
            holder.txt_desc.setText(inventory.getDescription());
            holder.btn_edit.setOnClickListener(v -> {
                //Log.d(TAG, "onClick: opening Update Dialog Fragment for Inventory.");

                Bundle args = new Bundle();
                args.putString("id", String.valueOf(inventory.getId()));
                args.putString("name", inventory.getName());
                args.putString("qty", inventory.getQuantity());
                args.putString("desc", inventory.getDescription());

                mListener.gotoUpdateFragment(inventory, args);
            });
            holder.btn_delete.setOnClickListener(v -> {
                Bundle args = new Bundle();
                args.putString("id", String.valueOf(inventory.getId()));
                args.putString("name", inventory.getName());
                args.putString("qty", inventory.getQuantity());
                mListener.gotoDeleteFragment(inventory,args);

                //notifyItemRemoved(position);
                //notifyItemRangeChanged(position, items.size()-1);
                //Log.d(TAG, "onClick: opening Update Dialog Fragment for Inventory.");
                //Bundle args = new Bundle();
                //args.putString("id", inventory.getId());
                //mListener.gotoDeleteFragment(inventory);

            });
        }catch(NullPointerException e){
            Log.e(TAG, "onBindViewHolder: Null Pointer: " + e.getMessage());
        }

    }

    @Override
    public int getItemCount() {
        if (items != null) {
            return items.size();
        } else return 0;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txt_id, txt_name, txt_qty, txt_desc;
        Button btn_edit, btn_delete;
        int position;
        InventoryRecyclerListener mListener;
        Inventory inventory;
        View rootView;

        public MyViewHolder(View itemView, InventoryRecyclerListener mListener) {
            super(itemView);
            rootView = itemView;
            this.mListener = mListener;
            txt_id = itemView.findViewById(R.id.inv_id_txtv);
            txt_name = itemView.findViewById(R.id.inv_name_txtv);
            txt_qty = itemView.findViewById(R.id.inv_qty_txtv);
            txt_desc = itemView.findViewById(R.id.inv_desc_txtv);
            btn_edit = itemView.findViewById(R.id.update_item);
            btn_delete = itemView.findViewById(R.id.delete_item);
        }
}

public interface InventoryRecyclerListener{
        void gotoUpdateFragment(Inventory inventory, Bundle args);
        //void gotoDeleteFragment(Inventory inventory);
        void gotoDeleteFragment(Inventory inventory, Bundle args);
}
}
