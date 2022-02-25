package com.example.testois;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

public class DashboardInventory extends AppCompatActivity {
    Button menu, add_item;
    severinaDB severinadb;
    int id = 0;
    SQLiteDatabase db;
    RecyclerView recyclerView;
    ImageView imageview;
    ArrayList<String> inv_id, inv_name, inv_qty, inv_desc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_inventory);
        severinadb = new severinaDB(DashboardInventory.this);

        inv_id = new ArrayList<>();
        inv_name = new ArrayList<>();
        inv_qty = new ArrayList<>();
        inv_desc = new ArrayList<>();

        menu = findViewById(R.id.menu);
        add_item = findViewById(R.id.add_item);
        imageview = findViewById(R.id.image_product);
        recyclerView = findViewById(R.id.recyclerView);


        storeDataInArrays();

        CustomAdapter customAdapter = new CustomAdapter(DashboardInventory.this, this, inv_id, inv_name, inv_qty, inv_desc);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(DashboardInventory.this));

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), ViewInventory.class);
                startActivity(i);
            }
        });
        add_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), AddInventory.class);
                startActivity(i);
            }
        });


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            recreate();
        }

}
     void storeDataInArrays(){
         Cursor cursor = severinadb.readAllInventory();
     if (!((cursor.getCount()) == 0)){
            while (cursor.moveToNext()){
                inv_id.add(cursor.getString(0));
                inv_name.add(cursor.getString(1));
                inv_qty.add(cursor.getString(2));
                inv_desc.add(cursor.getString(3));
            }
        }
         /* if(cursor.getCount() == 0){
            imageview.setVisibility(View.VISIBLE);

        }else{
            while (cursor.moveToNext()){
                inv_id.add(cursor.getString(0));
                inv_name.add(cursor.getString(1));
                inv_qty.add(cursor.getString(2));
                inv_desc.add(cursor.getString(3));
            }
            imageview.setVisibility(View.GONE);

        } */
    }
}