package com.example.testois;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

    public class ViewInventory extends AppCompatActivity {
        EditText sort;
        ImageView search, add;
        ListView lst1;
        ArrayList<String> titles = new ArrayList<String>();
        ArrayAdapter arrayAdapter;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_view_inventory);

            SQLiteDatabase db = openOrCreateDatabase("SliteDb", Context.MODE_PRIVATE, null);
            sort = findViewById(R.id.sort);
            search = findViewById(R.id.search);
            add = findViewById(R.id.add);
            lst1 = findViewById(R.id.lst1);
            final Cursor c = db.rawQuery("select * from records", null);
            int id = c.getColumnIndex("id");
            int name = c.getColumnIndex("name");
            int quantity = c.getColumnIndex("quantity");
            int description = c.getColumnIndex("description");
            titles.clear();


            arrayAdapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, titles);
            lst1.setAdapter(arrayAdapter);

            final ArrayList<Inventory> inv = new ArrayList<Inventory>();


            if (c.moveToFirst()) {
                do {
                    Inventory item = new Inventory();
                    item.id = c.getString(id);
                    item.name = c.getString(name);
                    item.quantity = c.getString(quantity);
                    item.description = c.getString(description);
                    inv.add(item);

                    titles.add(c.getString(id) + " \t " + c.getString(name) + " \t " + c.getString(quantity) + " \t " + c.getString(description));

                } while (c.moveToNext());
                arrayAdapter.notifyDataSetChanged();
                lst1.invalidateViews();


            }

            search.setOnClickListener(v -> {
                if (sort!=null) {
                    String keyword = sort.getText().toString();
                    SQLiteDatabase dbase = openOrCreateDatabase("SliteDb", Context.MODE_PRIVATE, null);
                    String sql = "select * ,order by" + " " + keyword + " asc";
                    SQLiteStatement statement = dbase.compileStatement(sql);
                    statement.execute();
                    arrayAdapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, titles);
                    lst1.setAdapter(arrayAdapter);
                }
            });

            add.setOnClickListener(v-> {
                Intent i = new Intent(getApplicationContext(), AddInventory.class);
                startActivity(i);
                finish();
            });


            lst1.setOnItemClickListener((parent, view, position, id1) -> {
                String aa = titles.get(position);
                Inventory item = inv.get(position);
                Intent i = new Intent(getApplicationContext(), EditInventory.class);
                i.putExtra("id", item.id);
                i.putExtra("name", item.name);
                i.putExtra("quantity", item.quantity);
                i.putExtra("description", item.description);
                startActivity(i);

            });

        }
    }
