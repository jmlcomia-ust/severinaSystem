package com.example.testois;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class DashboardOrders extends AppCompatActivity{
    private static final String TAG = "MainActivity";
    Button dash_menu, add_item;
    severinaDB severinadb;
    RecyclerView recyclerView;
    ImageView imageview;
    TextView empty1, empty2;
    ArrayList<String> ord_id, ord_name, ord_qty, ord_stat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_inventory);

        severinadb = new severinaDB(DashboardOrders.this);

        ord_id = new ArrayList<>();
        ord_name = new ArrayList<>();
        ord_qty = new ArrayList<>();
        ord_stat = new ArrayList<>();

        dash_menu = findViewById(R.id.dashboard_ord_menu);
        empty1 = findViewById(R.id.emptyRv1);
        empty2 = findViewById(R.id.emptyRv2);
        add_item = findViewById(R.id.add_item);
        imageview = findViewById(R.id.image_product);
        recyclerView = findViewById(R.id.recyclerView);

        severinaDB db  = new severinaDB(this);
        db.addInventory("Juan Dela Cruz",5, "none");

        storeDataInArrays(empty1, empty2);

        CustomAdapterInv customAdapterInv = new CustomAdapterInv(DashboardOrders.this, this, ord_id, ord_name, ord_qty, ord_stat);
        recyclerView.setAdapter(customAdapterInv);
        recyclerView.setLayoutManager(new LinearLayoutManager(DashboardOrders.this));

        dash_menu.setOnClickListener(view -> {
            Intent i = new Intent(this, ViewOrder.class);
            startActivity(i);
        });
        add_item = findViewById(R.id.add_item);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            recreate();
        }

    }
    void storeDataInArrays(TextView empty1, TextView empty2){
        Cursor cursor = severinadb.readAllOrders();
        if (!(cursor.getCount() == 0)){
           // empty1.setVisibility(View.INVISIBLE);
           // empty2.setVisibility(View.INVISIBLE);
            while (cursor.moveToNext()) {
                ord_id.add(cursor.getString(0));
                ord_name.add(cursor.getString(1));
                ord_qty.add(cursor.getString(2));
                ord_stat.add(cursor.getString(3));
            }
        }
    }
}