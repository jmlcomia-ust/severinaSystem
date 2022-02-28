package com.example.testois;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

    public class CustomViewAdapOrd extends RecyclerView.Adapter<CustomViewAdapOrd.MyViewHolder> {

        private final Context context;
        private final Activity activity;
        private final ArrayList ord_id, ord_name, ord_qty, ord_stat;

        CustomViewAdapOrd(Activity activity, Context context, ArrayList ord_id, ArrayList ord_name, ArrayList ord_qty, ArrayList ord_stat){
            this.activity = activity;
            this.context = context;
            this.ord_id = ord_id;
            this.ord_name = ord_name;
            this.ord_qty = ord_qty;
            this.ord_stat = ord_stat;
        }

        @NonNull
        @Override
        public CustomViewAdapOrd.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.spinner_dropdown_list, parent, false);
            return new CustomViewAdapOrd.MyViewHolder(view);
        }
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
            holder.ord_id_txt.setText(String.valueOf(ord_id.get(position)));
            holder.ord_name_txt.setText(String.valueOf(ord_name.get(position)));
            holder.ord_qty_txt.setText(String.valueOf(ord_qty.get(position)));
            holder.ord_stat_txt.setText(String.valueOf(ord_stat.get(position)));

            //Recyclerview onClickListener
            holder.order_list.setOnClickListener(view -> {
                Intent intent = new Intent(context, EditInventory.class);
                intent.putExtra("id", String.valueOf(ord_id.get(position)));
                intent.putExtra("name", String.valueOf(ord_name.get(position)));
                intent.putExtra("quantity", String.valueOf(ord_qty.get(position)));
                intent.putExtra("status", String.valueOf(ord_stat.get(position)));
                activity.startActivityForResult(intent, 1);
            });
        }

        @Override
        public int getItemCount() {
            return ord_id.size();
        }

        static class MyViewHolder extends RecyclerView.ViewHolder {
            TextView ord_id_txt, ord_name_txt, ord_qty_txt, ord_stat_txt;
            LinearLayout order_list;

            MyViewHolder(@NonNull View itemView) {
                super(itemView);
                ord_id_txt = itemView.findViewById(R.id.ord_id_txt);
                ord_name_txt = itemView.findViewById(R.id.ord_name_txt);
                ord_qty_txt = itemView.findViewById(R.id.ord_qty_txt);
                ord_stat_txt = itemView.findViewById(R.id.ord_stat_txt);
                order_list = itemView.findViewById(R.id.order_list);

            }
        }
    }
