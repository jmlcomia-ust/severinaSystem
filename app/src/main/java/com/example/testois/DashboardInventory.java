package com.example.testois;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import java.util.ArrayList;

public class DashboardInventory extends AppCompatActivity {
    SQLiteDatabase db;
    ArrayList<Inventory> invArrayList;
    InventoryAdapter invAdapter;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_inventory);
/*
        invArrayList=new ArrayList<>();
        dbInventory=new DBInventory(this);

        //create methods

        findid();
        invArrayList=displayData();
        invAdapter=new InventoryAdapter(invArrayList, DashboardInventory.this);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(DashboardInventory.this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(invAdapter);
    }

    private void findid(){ recyclerView=findViewById(R.id.inventoryRV); }

    private ArrayList<Inventory> displayData(){
        db=dbInventory.getReadableDatabase();
        Cursor cursor=db.rawQuery("select * from inventory", null);
        ArrayList<Inventory>inventoryArrayList=new ArrayList<>();
        if(cursor.moveToFirst()){
            do{
                inventoryArrayList.add(new Inventory(cursor.getString(1), cursor.getString(2)));
            }while (cursor.moveToNext());
        }
        cursor.close();
        return inventoryArrayList;
    }
*/
    }
}