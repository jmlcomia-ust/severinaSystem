package com.example.testois;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;

public class ViewInventory extends AppCompatActivity implements AddInventoryDiaFragment.AddInventoryDiaListener {
    EditText sort;
    ImageView search, add, menu;
    ListView lst1;
    ArrayList<String> inv_id, inv_name, inv_qty, inv_desc;
    severinaDB db;
    SQLiteDatabase sq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_inventory);

        sort = findViewById(R.id.sort_inv);
        search = findViewById(R.id.search_inv);
        add = findViewById(R.id.add_inv);
        menu = findViewById(R.id.view_inv_menu);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), ViewInventory.class);
                startActivity(i);
            }
        });

        add.setOnClickListener(view -> {
            showEditDialog();
        });

        search.setOnClickListener(v -> {
            if (sort != null) {

            }
        });

        db = new severinaDB(ViewInventory.this);
        inv_id = new ArrayList<>();
        inv_name = new ArrayList<>();
        inv_qty = new ArrayList<>();
        inv_desc = new ArrayList<>();
        storeDataInArrays();

        ArrayList<HashMap<String, String>> userList = db.GetInventory();
        ListView lv = findViewById(R.id.lst1_inv);
        ListAdapter adapter = new SimpleAdapter(this, userList, R.layout.view_inv_row,new String[]{"name","designation","location"}, new int[]{R.id.inv_name, R.id.inv_qty_txt, R.id.inv_desc_txt});
        lv.setAdapter(adapter);
    }

    private void showEditDialog() {
        FragmentManager fm = getSupportFragmentManager();
        AddInventoryDiaFragment editNameDialogFragment = AddInventoryDiaFragment.newInstance("name", "quantity", "description");
        editNameDialogFragment.show(fm, "fragment_edit_name");
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            recreate();
        }

    }

    void storeDataInArrays() {
        Cursor cursor = db.readAllInventory();
        if (!((cursor.getCount()) == 0)) {
            while (cursor.moveToNext()) {
                inv_id.add(cursor.getString(0));
                inv_name.add(cursor.getString(1));
                inv_qty.add(cursor.getString(2));
                inv_desc.add(cursor.getString(3));
            }
        }
        if (!(cursor.getCount() == 0)) {
            while (cursor.moveToNext()) {
                inv_id.add(cursor.getString(0));
                inv_name.add(cursor.getString(1));
                inv_qty.add(cursor.getString(2));
                inv_desc.add(cursor.getString(3));
            }
        }
    }

    @Override
    public void onFinishEditDialog(String input1, String input2, String input3) {
        try {
            db = new severinaDB(ViewInventory.this);
            db.addInventory(input1.trim(), Integer.parseInt(input2.trim()), input3.trim());
            Toast.makeText(this, "Item Added Successfully!", Toast.LENGTH_LONG).show();
        } catch (Exception ex) {
            Toast.makeText(this, "Record Fail", Toast.LENGTH_LONG).show();
        }
    }
}
