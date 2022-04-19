package com.example.testois.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testois.dao.Inventory;
import com.example.testois.R;
import com.example.testois.utilities.severinaDB;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
@SuppressLint("all")
public class CustomViewAdapInv extends RecyclerView.Adapter<CustomViewAdapInv.MyViewHolder> implements Filterable {
    private static final String TAG = "CustomViewAdapInv";
    InventoryRecyclerListener mListener;
    //InventoryChecker mChecker;
    Context context;
    List<Inventory> items;
    List<Inventory> itemsAll;
    List<Inventory> fullInv;
    severinaDB sev;

    //public CustomViewAdapInv(List<Inventory> items, Context context, InventoryRecyclerListener mListener, InventoryChecker mChecker) {
    public CustomViewAdapInv(List<Inventory> items, Context context, InventoryRecyclerListener mListener) {
        this.items = items;
        this.context = context;
        this.mListener = mListener;
        //this.mChecker = mChecker;
        itemsAll = new ArrayList<>();
        fullInv = new ArrayList<>(items);
        itemsAll.addAll(items);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_inv_row, parent, false);
        //return new MyViewHolder(view, mListener, mChecker);
        return new MyViewHolder(view, mListener);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        try {
            Inventory inventory = items.get(position);
            //Inventory inventoryForSearch = itemsAll.get(position);

            holder.txt_id.setText(String.valueOf(inventory.getId()));
            holder.txt_name.setText(inventory.getName().toUpperCase());
            holder.txt_thres.setText(String.valueOf(inventory.getThreshold()));
            holder.txt_desc.setText(inventory.getDescription().toUpperCase());
            holder.txt_qty.setText(String.valueOf(inventory.getQuantity()));
            if (inventory.getQuantity() <= inventory.getThreshold()+1 || inventory.getQuantity() <= (inventory.getThreshold())) { holder.txt_qty.setTextColor(Color.parseColor("#FF0000"));  holder.txt_qty.setTextSize(22); holder.txt_qty.setTypeface(Typeface.DEFAULT_BOLD);}
            // holder.img_item.setImageBitmap(inventory.getImage());

            holder.btn_edit.setOnClickListener(v -> {
                Log.d(TAG, "onClick: opening Update Dialog Fragment for Inventory.");
                Bundle args = new Bundle();
                args.putInt("ids", inventory.getId());
                args.putString("name", inventory.getName());
                args.putInt("qty", inventory.getQuantity());
                args.putString("desc", inventory.getDescription());
                args.putInt("thres", inventory.getThreshold());
                //args.putByteArray("image", severinaDB.getImageBytes(inventory.getImage()));
                mListener.gotoUpdateFragment(inventory, args);
            });
            holder.btn_delete.setOnClickListener(v -> {
                Log.d(TAG, "onClick: opening Delete Dialog Fragment for Inventory.");
                Bundle args = new Bundle();
                args.putString("id", String.valueOf(inventory.getId()));
                args.putString("name", inventory.getName());
                args.putString("qty", String.valueOf(inventory.getQuantity()));
                args.putString("desc", inventory.getDescription());
                args.putString("thres", String.valueOf(inventory.getThreshold()));
                //args.putByteArray("image", severinaDB.getImageBytes(inventory.getImage()));
                mListener.gotoDeleteFragment(inventory,args);
                notifyItemRangeChanged(position, items.size());

            });
/*
            Uri selectedImageUri = severinaDB.getImageUri(context.getApplicationContext(),inventory.getImage());
            Bitmap bitImg = severinaDB.decodeUri(context.getApplicationContext(), selectedImageUri, 400);
            holder.img_item.setImageBitmap(bitImg);
 */

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
        TextView txt_id, txt_name, txt_qty, txt_desc, txt_thres; ImageView img_item;
        Button btn_edit, btn_delete;
        int position;
        CustomViewAdapInv.InventoryRecyclerListener mListener;
        View rootView;

        @SuppressLint("ResourceAsColor")
        // public MyViewHolder(View itemView, InventoryRecyclerListener mListener, InventoryChecker mChecker) {
        public MyViewHolder(View itemView, InventoryRecyclerListener mListener) {
            super(itemView);
            rootView = itemView;
            this.mListener = mListener;
            txt_id = itemView.findViewById(R.id.inv_id_txtv);
            txt_name = itemView.findViewById(R.id.inv_name_txtv);
            txt_qty = itemView.findViewById(R.id.inv_qty_txtv);
            //img_item = itemView.findViewById(R.id.img_item);
            txt_desc = itemView.findViewById(R.id.inv_desc_txtv);
            txt_thres = itemView.findViewById(R.id.inv_thres_txtv);
            btn_edit = itemView.findViewById(R.id.update_item);
            btn_delete = itemView.findViewById(R.id.delete_item);
        }
}

public interface InventoryRecyclerListener{
        void gotoUpdateFragment(Inventory inventory, Bundle args);
        void gotoDeleteFragment(Inventory inventory, Bundle args);
}

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }
    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Inventory> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0){
                filteredList.addAll(fullInv);
            }else{
                String filterPattern = constraint.toString().toLowerCase().trim();

                for(Inventory item : fullInv){
                    if (String.valueOf(item.getQuantity()).contentEquals(filterPattern)) {
                        filteredList.add(item);
                    }
                    if (item.getName().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                    if (item.getDescription().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;

        }


        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            items.clear();
            items.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

}

