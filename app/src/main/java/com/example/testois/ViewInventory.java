package com.example.testois;
import android.app.FragmentManager;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testois.adapter.CustomAdapterInv;
import com.example.testois.adapter.CustomViewAdapInv;
import com.example.testois.databinding.ActivityDashboardInventoryBinding;
import com.example.testois.databinding.ActivityViewInventoryBinding;
import com.example.testois.fragments.AddInventoryDiaFragment;
import com.example.testois.fragments.DeleteInventoryDiagFragment;
import com.example.testois.fragments.UpdateInventoryDiaFragment;

import java.util.Collections;
import java.util.List;

public class ViewInventory extends DrawerBaseActivity implements CustomViewAdapInv.InventoryRecyclerListener,AddInventoryDiaFragment.OnInputListener, DeleteInventoryDiagFragment.OnInputListener{
    private static final String TAG = "ViewInventory";
    ActivityViewInventoryBinding activityViewInventoryBinding;
    List<Inventory> items;
    Inventory inventory = null;
    CustomViewAdapInv customViewAdapInv;
    Button menu, search;
    SQLiteDatabase sql;
    severinaDB db;
    TextView emptyfield;
    RecyclerView recyclerView;
    ImageView add_item;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.nav_search);
        SearchView searchView =
                (SearchView) searchItem.getActionView();

        getMenuInflater().inflate(R.menu.dash_options, menu);

        // Configure the search info and add any event listeners...

        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        CustomAdapterInv customAdapterInv;
        List<Inventory> items = db.getitemsList();
        switch (item.getItemId()) {
            case R.id.sort_id:
                // User chose the "Settings" item, show the app settings UI...
                Collections.sort(items, (Inventory o1, Inventory o2) -> o1.getId().compareToIgnoreCase(o2.getId()));
                customAdapterInv = new CustomAdapterInv(items, ViewInventory.this);
                recyclerView.setAdapter(customAdapterInv);
                recyclerView.setLayoutManager(new LinearLayoutManager(ViewInventory.this));
                recyclerView.getAdapter().notifyDataSetChanged();
                return true;

            case R.id.sort_name:
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                Collections.sort(items, (Inventory o1, Inventory o2) -> o1.getName().compareToIgnoreCase(o2.getName()));
                Collections.reverse(items);
                customAdapterInv = new CustomAdapterInv(items, ViewInventory.this);
                recyclerView.setAdapter(customAdapterInv);
                recyclerView.setLayoutManager(new LinearLayoutManager(ViewInventory.this));
                recyclerView.getAdapter().notifyDataSetChanged();
                return true;

            case R.id.sort_stocks:
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                Collections.sort(items, (Inventory o1, Inventory o2) -> o1.getQuantity().compareToIgnoreCase(o2.getQuantity()));
                Collections.reverse(items);
                customAdapterInv = new CustomAdapterInv(items, ViewInventory.this);
                recyclerView.setAdapter(customAdapterInv);
                recyclerView.setLayoutManager(new LinearLayoutManager(ViewInventory.this));
                recyclerView.getAdapter().notifyDataSetChanged();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }


     public void sendInput(String name, String qty, String desc, Bitmap image){
        Log.d(TAG, "sendInput: got name: " + name + "\n got qty: " + qty + "\ngot desc:" + desc);
         try {
             db = new severinaDB(ViewInventory.this);
             Inventory inventory = new Inventory(name,qty, desc, image);
             db.addItem(inventory);
             Toast.makeText(this, "Item Added Successfully!", Toast.LENGTH_LONG).show();
         } catch (Exception ex) {
             Toast.makeText(this, "Record Fail", Toast.LENGTH_LONG).show();
         }
    }
/*
    public void UpdateInput(String id, String name, String qty, String desc, Bitmap image) {
        Log.d(TAG, "updateInput: got id: " + id+ "\n got name: " + name + "\n got qty: " + qty + "\ngot desc:" + desc);
        try {
            db = new severinaDB(ViewInventory.this);
            Inventory items = new Inventory (id, name, qty, desc, image);
            db.updateItem(items);
            Toast.makeText(this, "Item Updated Successfully!", Toast.LENGTH_LONG).show();
        } catch (Exception ex) {
            Toast.makeText(this, "Record Fail", Toast.LENGTH_LONG).show();
        }
    }

 */
    public void DeleteInput(String id){
        Log.d(TAG, "deleteItem:  got id: "+id);
        try {
            db = new severinaDB(ViewInventory.this);
            db.deleteItem(id);
            Toast.makeText(this, "Item Updated Successfully!", Toast.LENGTH_LONG).show();
        } catch (Exception ex) {
            Toast.makeText(this, "Record Fail", Toast.LENGTH_LONG).show();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityViewInventoryBinding = ActivityViewInventoryBinding.inflate(getLayoutInflater());
        setContentView(activityViewInventoryBinding.getRoot());
        allocatedActivityTitle("View Inventory");


        search = findViewById(R.id.search);

        db = new severinaDB(this);

        List<Inventory> items = db.getitemsList();


        add_item = findViewById(R.id.add_view_inv);
        recyclerView = findViewById(R.id.rv_view_inv);
        //new ItemTouchHelper(itemTouchHelperCallback)
        FragmentManager fragmentManager = getFragmentManager();
       customViewAdapInv = new CustomViewAdapInv(items, this, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(customViewAdapInv);
        //if (customViewAdapInv.getItemCount() != 0){emptyfield.setVisibility(View.GONE);}


            add_item.setOnClickListener(view -> {
            Log.d(TAG, "onClick: opening Add Dialog Fragment for Inventory.");
            AddInventoryDiaFragment dialog = new AddInventoryDiaFragment();
            dialog.show(getSupportFragmentManager(), "AddInventoryDiaFragment");
        });

    }
    public void onResume()
    {
        super.onResume();
        customViewAdapInv.notifyDataSetChanged();
        recyclerView.setAdapter(customViewAdapInv);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void gotoUpdateFragment(Inventory inventory, Bundle args) {
            androidx.fragment.app.FragmentManager fm = this.getSupportFragmentManager();
            UpdateInventoryDiaFragment updafrag = new UpdateInventoryDiaFragment();
            updafrag.setArguments(args);
            updafrag.show(fm, "UpdateInvFrag");

    }

    @Override
    public void gotoDeleteFragment(Inventory inventory, Bundle args) {
        androidx.fragment.app.FragmentManager fm = this.getSupportFragmentManager();
        DeleteInventoryDiagFragment delfrag = new DeleteInventoryDiagFragment();
        delfrag.setArguments(args);
        delfrag.show(fm, "DeleteInvFrag");
    }
}
