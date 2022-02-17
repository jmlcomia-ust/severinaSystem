package com.example.testois;

import android.content.Context;
import android.graphics.ColorSpace;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Objects;

public class InventoryAdapter extends RecyclerView.Adapter<InventoryAdapter.ViewHolder> {
    private ArrayList<Inventory> inventoryAL;
    private Context context;

    //constructor
    public InventoryAdapter(ArrayList<Inventory> inventoryAL, Context context) {
        this.inventoryAL = inventoryAL;
        this.context = context;
    }
    @NonNull
    @Override
    public InventoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.inventory_row,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InventoryAdapter.ViewHolder holder, int position) {
        Inventory item = inventoryAL.get(position);
        holder.name.setText(item.getName());
        if(holder.name.equals("Petron Large")){  holder.image.setImageResource(R.drawable.petron_fifty);}
        else if (holder.name.equals("Petron Gasul")) {  holder.image.setImageResource(R.drawable.petron_eleven);}
        else if (holder.name.equals("Solane Mini")){  holder.image.setImageResource(R.drawable.solane_mini);}
        holder.quantity.setText(item.getQuantity());


    }

    @Override
    public int getItemCount() {

        return inventoryAL.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView name, quantity;
        private ImageView image;

        public ViewHolder(@NonNull View itemView){
            super(itemView);
            name= (TextView) itemView.findViewById(R.id.txtname);
            quantity= (TextView) itemView.findViewById(R.id.txtquant);
            image= (ImageView) itemView.findViewById(R.id.imgstock);
        }
    }
}
