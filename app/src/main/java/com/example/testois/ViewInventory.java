package com.example.testois;
import android.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.testois.adapter.CustomViewAdapInv;
import com.example.testois.fragments.AddInventoryDiaFragment;
import com.example.testois.fragments.UpdateInventoryDiaFragment;
import java.util.List;

public class ViewInventory extends AppCompatActivity implements AddInventoryDiaFragment.OnInputListener,UpdateInventoryDiaFragment.OnInputListener {
    private static final String TAG = "ViewInventory";
    List<Inventory> items;
    Inventory inventory = null;
    AlertDialog.Builder builder;


    //public void sendInput(String name, String qty, String desc, byte[] bytesImage)
     public void sendInput(String name, String qty, String desc){
        Log.d(TAG, "sendInput: got name: " + name + "\n got qty: " + qty + "\ngot desc:" + desc);
        frag_name = name;
        frag_qty = qty;
        frag_desc = desc;
        setInputToListView();
    }

    //public void UpdateInput(String name, String qty, String desc, byte[] image) {
    public void UpdateInput(String name, String qty, String desc) {
        Log.d(TAG, "updateInput: got name: " + name + "\n got qty: " + qty + "\ngot desc:" + desc);
        frag_name = name;
        frag_qty = qty;
        frag_desc = desc;
        setUpdatesToListView();
    }

    Button menu, search;
    severinaDB db;
    TextView emptyfield;
    RecyclerView recyclerView;
    ImageView add_item;
    String frag_name, frag_qty, frag_desc; byte[] frag_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_inventory);
        search = findViewById(R.id.search);

        db = new severinaDB(this);
        List<Inventory> items = db.getitemsList();


        add_item = findViewById(R.id.add_view_inv);
        recyclerView = findViewById(R.id.rv_view_inv);
        //new ItemTouchHelper(itemTouchHelperCallback)
        FragmentManager fragmentManager = getFragmentManager();
        CustomViewAdapInv customViewAdapInv = new CustomViewAdapInv(items, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(customViewAdapInv);


            add_item.setOnClickListener(view -> {
            Log.d(TAG, "onClick: opening Add Dialog Fragment for Inventory.");
            AddInventoryDiaFragment dialog = new AddInventoryDiaFragment();
            dialog.show(getSupportFragmentManager(), "AddInventoryDiaFragment");
        });

    }
/*
    ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            deleteNote(mNotes.get(viewHolder.getAdapterPosition()));
        }
    };
 */

    private void setInputToListView() {
        try {
            db = new severinaDB(ViewInventory.this);
            //Inventory inventory = new Inventory(frag_name,frag_qty, frag_desc, frag_image);
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
            //Inventory items = new Inventory (frag_name, frag_qty, frag_desc, frag_image);
            Inventory items = new Inventory (frag_name, frag_qty, frag_desc);
            db.updateItem(items);
            Toast.makeText(this, "Item Updated Successfully!", Toast.LENGTH_LONG).show();
        } catch (Exception ex) {
            Toast.makeText(this, "Record Fail", Toast.LENGTH_LONG).show();
        }
    }
}
