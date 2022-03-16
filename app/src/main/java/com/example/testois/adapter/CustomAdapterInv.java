package com.example.testois.adapter;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testois.Inventory;
import com.example.testois.R;
import com.example.testois.fragments.UpdateInventoryDiaFragment;
import com.example.testois.severinaDB;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CustomAdapterInv extends RecyclerView.Adapter<CustomAdapterInv.MyViewHolder>{
    private static final String TAG = "CustomAdapterInv";
    Activity context;
    List<Inventory> items;
    List<Inventory> filtereditem;
    List<Inventory> allItems = new ArrayList<>();
    severinaDB sev;

    public CustomAdapterInv(List<Inventory> items, Activity context){
        this.items = items;
        this.context = context;
        sev = new severinaDB(context);
    }
    public void CustomViewAdapInv(List<Inventory> filtereditem){
        this.filtereditem=filtereditem;
        this.allItems=new ArrayList<>(filtereditem);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.dashboard_inv_row, parent, false);
        return new MyViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        final Inventory inventory = items.get(position);

        holder.id.setText(inventory.getId());
        holder.name.setText(inventory.getName());
        holder.qty.setText(inventory.getQuantity());
        holder.desc.setText(inventory.getDescription());
        //Recyclerview onClickListener

    }


    @Override
    public int getItemCount() {
        if (items != null){
            return items.size();
        }
        else
            return 0;
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView id, name, qty, desc;
        Button btn_edit;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            id = itemView.findViewById(R.id.inv_id_txt);
            name = itemView.findViewById(R.id.inv_name_txt);
            qty = itemView.findViewById(R.id.inv_qty_txt);
            desc = itemView.findViewById(R.id.inv_desc_txt);
        }
    }
}