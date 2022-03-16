package com.example.testois.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testois.Inventory;
import com.example.testois.R;
import com.example.testois.ViewInventory;
import com.example.testois.fragments.UpdateInventoryDiaFragment;
import com.example.testois.severinaDB;

import java.util.List;

public class CustomViewAdapInv extends RecyclerView.Adapter<CustomViewAdapInv.MyViewHolder>{
    private static final String TAG = "CustomViewAdapInv";

    Context context;
    List<Inventory> items;
    severinaDB sev;

    public CustomViewAdapInv(List<Inventory> items, Context context) {
        this.items = items;
        this.context = context;
        sev = new severinaDB(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_inv_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        try {
            Inventory inventory = items.get(position);
            holder.txt_id.setText(inventory.getId());
            holder.txt_name.setText(inventory.getName());
            holder.txt_qty.setText(inventory.getQuantity());
            holder.txt_desc.setText(inventory.getDescription());
            holder.btn_edit.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       Log.d(TAG, "onClick: opening Update Dialog Fragment for Inventory.");
                       String id = holder.txt_id.getText().toString();
                       String name = holder.txt_name.getText().toString();
                       String qty = holder.txt_qty.getText().toString();
                       String desc = holder.txt_desc.getText().toString();

                       Bundle args = new Bundle();
                       args.putString("id", id);
                       args.putString("name", name);
                       args.putString("qty", qty);
                       args.putString("desc", desc);
                       FragmentActivity act = (FragmentActivity) (context);
                       FragmentManager fm = act.getSupportFragmentManager();
                       UpdateInventoryDiaFragment fragment = new UpdateInventoryDiaFragment();
                       fragment.setArguments(args);
                       fragment.show(fm, "UpdateInvFrag");
                   }
               });
            holder.btn_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
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

        public MyViewHolder(View itemView) {
            super(itemView);
            txt_id = itemView.findViewById(R.id.inv_id_txtv);
            txt_name = itemView.findViewById(R.id.inv_name_txtv);
            txt_qty = itemView.findViewById(R.id.inv_qty_txtv);
            txt_desc = itemView.findViewById(R.id.inv_desc_txtv);
            btn_edit = itemView.findViewById(R.id.update_item);
            btn_delete = itemView.findViewById(R.id.delete_item);
        }
}
}
