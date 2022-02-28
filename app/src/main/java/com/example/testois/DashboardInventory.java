package com.example.testois;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class DashboardInventory extends AppCompatActivity implements AddInventoryDiaFragment.OnInputListener{
    private static final String TAG = "DashboardInventory";

    @Override
    public void sendInput(String name, String qty, String desc) {
        Log.d(TAG, "sendInput: got name: " + name + "\n got qty: " + qty + "\ngot desc:" + desc);

//        mInputDisplay.setText(input);
        frag_name = name;   frag_qty=qty;   frag_desc=desc;
        setInputToListView();
    }

    Button menu, add_item;
    severinaDB db;
    TextView emptyfield;
    RecyclerView recyclerView;
    ImageView imageview;
    ArrayList<String> inv_id, inv_name, inv_qty, inv_desc;
    String frag_name, frag_qty, frag_desc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_inventory);

        db = new severinaDB(DashboardInventory.this);

        inv_id = new ArrayList<>();
        inv_name = new ArrayList<>();
        inv_qty = new ArrayList<>();
        inv_desc = new ArrayList<>();
        emptyfield = findViewById(R.id.emptyRv);
        menu = findViewById(R.id.dashboard_inv_menu);
        add_item = findViewById(R.id.add_item);
        imageview = findViewById(R.id.inv_img);
        recyclerView = findViewById(R.id.recyclerView);

        storeDataInArrays();

        CustomAdapterInv customAdapterInv = new CustomAdapterInv(DashboardInventory.this, this, inv_id, inv_name, inv_qty, inv_desc);
        recyclerView.setAdapter(customAdapterInv);
        recyclerView.setLayoutManager(new LinearLayoutManager(DashboardInventory.this));

        menu.setOnClickListener(view -> {
            Intent i = new Intent(getApplicationContext(), ViewInventory.class);
            startActivity(i);
        });
        add_item.setOnClickListener(view -> {
            Log.d(TAG, "onClick: opening dialog.");
            AddInventoryDiaFragment dialog = new AddInventoryDiaFragment();

            dialog.show(getSupportFragmentManager(), "AddInventoryDiaFragment");
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            recreate();
        }

    }
    private void setInputToListView(){
        try {
            db = new severinaDB(DashboardInventory.this);
            db.addInventory(frag_name, Integer.parseInt(frag_qty), frag_desc);
            storeDataInArrays();
            Toast.makeText(this, "Item Added Successfully!", Toast.LENGTH_LONG).show();
        } catch (Exception ex) {
            Toast.makeText(this, "Record Fail", Toast.LENGTH_LONG).show();
        }
    }
    void storeDataInArrays() {
        Cursor cursor = db.readAllInventory();
        if (!(cursor.getCount() == 0)) {
            emptyfield.setVisibility(View.INVISIBLE);
            while (cursor.moveToNext()) {
                inv_id.add(cursor.getString(0));
                inv_name.add(cursor.getString(1));
                inv_qty.add(cursor.getString(2));
                inv_desc.add(cursor.getString(3));
            }
        }
        else{
            emptyfield.setVisibility(View.VISIBLE);
        }
    }
}
