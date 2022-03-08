package com.example.testois;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testois.fragments.AddInventoryDiaFragment;
import com.example.testois.fragments.UpdateInventoryDiaFragment;

import java.util.List;

public class CustomAdapterInv extends RecyclerView.Adapter<CustomAdapterInv.MyViewHolder>{
    private static final String TAG = "CustomAdapterInv";
    Activity context;
    List<Inventory> items;
    severinaDB sev;

    CustomAdapterInv(List<Inventory> items, Activity context){
        this.items = items;
        this.context = context;
        sev = new severinaDB(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.view_inv_row, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
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
        holder.btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: opening dialog.");
                String name = holder.name.getText().toString();
                String qty = holder.qty.getText().toString();
                String desc = holder.desc.getText().toString();

                UpdateInventoryDiaFragment fragment = new UpdateInventoryDiaFragment();
                Bundle args = new Bundle();
                args.putString("name", name);
                args.putString("qty", qty);
                args.putString("desc", desc);
                fragment.setArguments(args);
                AppCompatActivity act = new AppCompatActivity();
               // fragment.show(act.getFragmentManager().beginTransaction().add(R.id.fragment_update_inv, fragment).commit());
                sev.updateItem(new Inventory(inventory.getId(), inventory.getName(), inventory.getQuantity(), inventory.getDescription()));
            }
        });
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
            id = itemView.findViewById(R.id.inv_id_txtv);
            name = itemView.findViewById(R.id.inv_name_txtv);
            qty = itemView.findViewById(R.id.inv_qty_txtv);
            desc = itemView.findViewById(R.id.inv_desc_txtv);
            btn_edit = itemView.findViewById(R.id.edit_item);
        }
    }
}