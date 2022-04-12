package com.example.testois.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testois.dao.Orders;
import com.example.testois.R;
import com.example.testois.utilities.severinaDB;

import java.util.ArrayList;
import java.util.List;
@SuppressLint("all")
public class CustomCourViewAdapOrd extends RecyclerView.Adapter<CustomCourViewAdapOrd.MyViewHolder> {
    private static final String TAG = "CustomCourViewAdapOrd";
    OrderRecyclerListener nListener;
    Context context;
    List<Orders> orders;
    List<Orders> ordersAll;
    List<Orders> fullOrd;
    severinaDB db;

    public CustomCourViewAdapOrd(List<Orders> orders, Context context, OrderRecyclerListener nListener) {
        this.orders = orders;
        this.context = context;
        this.nListener = nListener;
        db = new severinaDB(context);
        ordersAll = new ArrayList<>();
        fullOrd = new ArrayList<>(orders);
        ordersAll.addAll(orders);

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.cour_view_ord_row, parent, false);
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
            holder.ord_desc_txt.setText(ord.getDescription());
            holder.ord_date_txt.setText(ord.getDate());
            holder.ord_stat_txt.setText(ord.getStatus());
            holder.btn_edit.setOnClickListener(v -> {
                Log.d(TAG, "onClick: opening Update Dialog Fragment for Orders.");
                Bundle args = new Bundle();
                args.putInt("pos", position);
                args.putString("id", String.valueOf(ord.getId()));
                args.putString("name", ord.getName());
                args.putString("qty", String.valueOf(ord.getQuantity()));
                args.putString("desc", ord.getDescription());
                args.putString("stat", ord.getStatus());
                nListener.gotoCourUpdateFragment(ord, args);
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
        TextView ord_id_txt, ord_name_txt, ord_qty_txt, ord_stat_txt, ord_desc_txt, ord_date_txt;
        Button btn_edit, btn_delete;
        int position;
        CustomCourViewAdapOrd.OrderRecyclerListener nListener;
        View rootView;

        MyViewHolder(@NonNull View itemView, CustomCourViewAdapOrd.OrderRecyclerListener nListener) {
            super(itemView);
            rootView = itemView;
            this.nListener = nListener;
            ord_id_txt = itemView.findViewById(R.id.view_ord_id);
            ord_name_txt = itemView.findViewById(R.id.view_ord_name);
            ord_qty_txt = itemView.findViewById(R.id.view_ord_qty);
            ord_desc_txt = itemView.findViewById(R.id.view_ord_desc);
            ord_date_txt = itemView.findViewById(R.id.view_ord_date);
            ord_stat_txt = itemView.findViewById(R.id.view_ord_stat);
            btn_edit = itemView.findViewById(R.id.edit_ord);
            btn_delete = itemView.findViewById(R.id.delete_ord);
        }
    }


    public interface OrderRecyclerListener{
        void gotoCourUpdateFragment(Orders orders, Bundle args);
    }

    public Filter getFilter() {
        return exampleFilter;
    }
    private final Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Orders> filteredList = new ArrayList<>();

            if(constraint == null || constraint.length() == 0) {
                filteredList.addAll(fullOrd);

            }
            else
            {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Orders item: fullOrd){

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
            orders.clear();
            orders.addAll((List)results.values);
            notifyDataSetChanged();

        }
    };


}
