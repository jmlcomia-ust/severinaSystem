package com.example.testois;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testois.fragments.AddInventoryDiaFragment;
import com.example.testois.fragments.UpdateInventoryDiaFragment;

import java.util.List;

public class DashboardInventory extends AppCompatActivity implements AddInventoryDiaFragment.OnInputListener, UpdateInventoryDiaFragment.OnInputListener {
    private static final String TAG = "DashboardInventory";

    @Override
    public void sendInput(String name, String qty, String desc) {
        Log.d(TAG, "sendInput: got name: " + name + "\n got qty: " + qty + "\ngot desc:" + desc);

        frag_name = name;
        frag_qty = qty;
        frag_desc = desc;
        setInputToListView();
    }

    @Override
    public void UpdateInput(String name, String qty, String stat) {
        Log.d(TAG, "sendInput: got name: " + name + "\n got qty: " + qty + "\ngot status:" + stat);

        frag_inv_name = name;
        frag_inv_qty = qty;
        frag_inv_stat = stat;
        setInputToListView();
    }

    Button add_item, search;
    TextView emptyfield;
    severinaDB db;
    RecyclerView recyclerView;
    ImageView imageview;
    String frag_name, frag_qty, frag_desc, frag_inv_name, frag_inv_qty, frag_inv_stat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_inventory);
        search = findViewById(R.id.search);

        db = new severinaDB(this);
        List<Inventory> items = db.getitemsList();

        emptyfield = findViewById(R.id.emptyRv);
        add_item = findViewById(R.id.add_item);
        imageview = findViewById(R.id.inv_img);
        recyclerView = findViewById(R.id.recyclerView);

        CustomAdapterInv customAdapterInv = new CustomAdapterInv(items, this);
        recyclerView.setAdapter(customAdapterInv);
        recyclerView.setLayoutManager(new LinearLayoutManager(DashboardInventory.this));

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

    private void setInputToListView() {
        try {
            db = new severinaDB(DashboardInventory.this);
            Inventory inventory = new Inventory(frag_name,frag_qty, frag_desc);
            db.addItem(inventory);
            Toast.makeText(this, "Item Added Successfully!", Toast.LENGTH_LONG).show();
        } catch (Exception ex) {
            Toast.makeText(this, "Record Fail", Toast.LENGTH_LONG).show();
        }
    }
}