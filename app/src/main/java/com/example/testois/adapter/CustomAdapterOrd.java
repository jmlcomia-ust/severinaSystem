package com.example.testois.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testois.Inventory;
import com.example.testois.Orders;
import com.example.testois.R;
import com.example.testois.severinaDB;

import java.util.List;
import java.util.Locale;

public class CustomAdapterOrd extends RecyclerView.Adapter<CustomAdapterOrd.MyViewHolder> {
    private static final String TAG = "CustomAdapterOrd";
    Context context;
    List<Orders> orders;
    severinaDB sev;

    public CustomAdapterOrd(List<Orders> orders, Context context){
        this.orders = orders;
        this.context = context;
        sev = new severinaDB(context);

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.dashboard_ord_row, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        final Orders order = orders.get(position);

        holder.ord_id_txt.setText(String.valueOf(order.getId()));
        holder.ord_name_txt.setText(String.valueOf(order.getName()));
        holder.ord_qty_txt.setText(String.valueOf(order.getQuantity()));
        holder.ord_stat_txt.setText(String.valueOf(order.getStatus().toLowerCase()));
        //Recyclerview onClickListener

    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView ord_id_txt, ord_name_txt, ord_qty_txt,ord_stat_txt;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ord_id_txt = itemView.findViewById(R.id.ord_id_txt);
            ord_name_txt = itemView.findViewById(R.id.ord_name_txt);
            ord_qty_txt = itemView.findViewById(R.id.ord_qty_txt);
            ord_stat_txt = itemView.findViewById(R.id.ord_stat_txt);
        }

    }

}