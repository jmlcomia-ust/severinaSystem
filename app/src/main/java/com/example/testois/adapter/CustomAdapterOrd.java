package com.example.testois.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
public class CustomAdapterOrd extends RecyclerView.Adapter<CustomAdapterOrd.MyViewHolder> {
    private static final String TAG = "CustomAdapterOrd";
    Context context;
    severinaDB db;
    List<Orders> orders;
    List<Orders> ordersAll;
    List<Orders> fullOrd;

    public CustomAdapterOrd(List<Orders> orders, Context context){
        this.orders = orders;
        this.context = context;
        db = new severinaDB(context);
        ordersAll = new ArrayList<>();
        fullOrd = new ArrayList<>(orders);
        ordersAll.addAll(orders);

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.dashboard_ord_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        try {
            Orders order = orders.get(position);
            holder.ord_id_txt.setText(String.valueOf(order.getId()));
            holder.ord_name_txt.setText(order.getName());
            holder.ord_qty_txt.setText(String.valueOf(order.getQuantity()));
            holder.ord_desc_txt.setText(order.getDescription());
            holder.ord_date_txt.setText(order.getDate());
            holder.ord_stat_txt.setText(order.getStatus());
        } catch (Exception e) {
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
        TextView ord_id_txt, ord_name_txt, ord_qty_txt,ord_stat_txt, ord_desc_txt, ord_date_txt;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ord_id_txt = itemView.findViewById(R.id.ord_id_txt);
            ord_name_txt = itemView.findViewById(R.id.ord_name_txt);
            ord_qty_txt = itemView.findViewById(R.id.ord_qty_txt);
            ord_desc_txt= itemView.findViewById(R.id.ord_desc_txt);
            ord_date_txt = itemView.findViewById(R.id.ord_date_txt);
            ord_stat_txt = itemView.findViewById(R.id.ord_stat_txt);
        }

    }
    public Filter getFilter() {
        return exampleFilter;
    }
    private Filter exampleFilter = new Filter() {
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
                    if (item.getStatus().toLowerCase().contains(filterPattern)) {
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
            orders.addAll((List) results.values);
            notifyDataSetChanged();

        }
    };

}