package com.example.testois;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.ArrayList;

public class ViewOrder extends AppCompatActivity {
    EditText sort;
    ImageView search, add, menu;
    ArrayList<String> ord_id, ord_name, ord_qty, ord_stat;
    RecyclerView lst1;
    severinaDB db;
    SQLiteDatabase sq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_order);
        menu = findViewById(R.id.menu_ord);
        menu.setOnClickListener(view -> {
            Intent i = new Intent(getApplicationContext(), ViewOrder.class);
            startActivity(i);
        });
        add = findViewById(R.id.add_ord);
        add.setOnClickListener(v -> {
            Intent i = new Intent(getApplicationContext(), AddOrderDiaFragment.class);
            startActivity(i);
            finish();
        });
        search = findViewById(R.id.search_ord);
        search.setOnClickListener(v -> {
            if (sort != null) {

            }
        });
        storeDataInArrays();
        CustomViewAdapOrd customAdapter = new CustomViewAdapOrd(ViewOrder.this, this, ord_id, ord_name, ord_qty, ord_stat);
        lst1 = findViewById(R.id.lst1_ord);
        lst1.setAdapter(customAdapter);
        lst1.setLayoutManager(new LinearLayoutManager(ViewOrder.this));
        }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            recreate();
        }

    }
    void storeDataInArrays() {
        Cursor cursor = db.readAllOrders();
        if (!((cursor.getCount()) == 0)) {
            while (cursor.moveToNext()) {
                ord_id.add(cursor.getString(0));
                ord_name.add(cursor.getString(1));
                ord_qty.add(cursor.getString(2));
                ord_stat.add(cursor.getString(3));
            }
        }
        if (!(cursor.getCount() == 0)) {
            while (cursor.moveToNext()) {
                ord_id.add(cursor.getString(0));
                ord_name.add(cursor.getString(1));
                ord_qty.add(cursor.getString(2));
                ord_stat.add(cursor.getString(3));
            }
        }
    }
}
