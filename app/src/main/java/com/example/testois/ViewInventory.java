package com.example.testois;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testois.fragments.AddInventoryDiaFragment;
import com.example.testois.fragments.UpdateInventoryDiaFragment;

import java.util.List;

public class ViewInventory extends AppCompatActivity implements AddInventoryDiaFragment.OnInputListener, UpdateInventoryDiaFragment.OnInputListener {
    private static final String TAG = "ViewInventory";

    @Override
    public void sendInput(String name, String qty, String desc) {
        Log.d(TAG, "sendInput: got name: " + name + "\n got qty: " + qty + "\ngot desc:" + desc);
        frag_name = name;
        frag_qty = qty;
        frag_desc = desc;
        setInputToListView();
    }
    @Override
    public void UpdateInput(String name, String qty, String desc) {
        Log.d(TAG, "updateInput: got name: " + name + "\n got qty: " + qty + "\ngot desc:" + desc);
        frag_name = name;
        frag_qty = qty;
        frag_desc = desc;
        setUpdatesToListView();
    }

    Button menu, update_item, search;
    severinaDB db;
    TextView emptyfield;
    RecyclerView recyclerView;
    ImageView imageview, add_item;
    String frag_name, frag_qty, frag_desc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_inventory);
        search = findViewById(R.id.search);

        db = new severinaDB(this);
        List<Inventory> items = db.getitemsList();


        add_item = findViewById(R.id.add_inv);
        //update_item=findViewById(R.id.update_item);
        imageview = findViewById(R.id.inv_img);
        recyclerView = findViewById(R.id.recyclerView4);

        CustomAdapterInv customAdapterInv = new CustomAdapterInv(items, this);
        recyclerView.setAdapter(customAdapterInv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

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
            db = new severinaDB(ViewInventory.this);
            Inventory inventory = new Inventory(frag_name,frag_qty, frag_desc);
            db.addItem(inventory);
            Toast.makeText(this, "Item Added Successfully!", Toast.LENGTH_LONG).show();
        } catch (Exception ex) {
            Toast.makeText(this, "Record Fail", Toast.LENGTH_LONG).show();
        }
    }

    private void setUpdatesToListView(){
        try {
            db = new severinaDB(ViewInventory.this);
            Inventory items = new Inventory (frag_name, frag_qty, frag_desc);
            db.updateItem(items);
            Toast.makeText(this, "Item Updated Successfully!", Toast.LENGTH_LONG).show();
        } catch (Exception ex) {
            Toast.makeText(this, "Record Fail", Toast.LENGTH_LONG).show();
        }
    }
}
