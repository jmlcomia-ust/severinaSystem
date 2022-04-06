package com.example.testois.adapter;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testois.dao.Inventory;
import com.example.testois.R;
import com.example.testois.utilities.severinaDB;

import java.util.List;

public class CustomAdapterInv extends RecyclerView.Adapter<CustomAdapterInv.MyViewHolder>{
    private static final String TAG = "CustomAdapterInv";
    Context context;
    List<Inventory> items;
    severinaDB sev;

    public CustomAdapterInv(List<Inventory> items, Context context){
        this.items = items;
        this.context = context;
        sev = new severinaDB(context);
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

        holder.id.setText(String.valueOf(inventory.getId()));
        holder.name.setText(inventory.getName());
        holder.thres.setText(String.valueOf(inventory.getThreshold()));
        if (inventory.getQuantity() <= inventory.getThreshold()+1 || inventory.getQuantity() <= (inventory.getThreshold()+2) || inventory.getQuantity() <= (inventory.getThreshold())) { holder.qty.setTextColor(Color.parseColor("#FF0000"));  holder.qty.setTextSize(22); holder.qty.setTypeface(Typeface.DEFAULT_BOLD);}
        holder.qty.setText(String.valueOf(inventory.getQuantity()));
        holder.desc.setText(inventory.getDescription());

    }


    @Override
    public int getItemCount() { return items.size(); }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView id, name, qty, desc, thres;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            id = itemView.findViewById(R.id.inv_id_txt);
            name = itemView.findViewById(R.id.inv_name_txt);
            qty = itemView.findViewById(R.id.inv_qty_txt);
            desc = itemView.findViewById(R.id.inv_desc_txt);
            thres = itemView.findViewById(R.id.inv_thres_txt);
        }
    }
}