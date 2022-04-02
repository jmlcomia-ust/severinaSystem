package com.example.testois;
import android.Manifest;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
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

public class ViewInventory extends DrawerBaseActivity implements CustomViewAdapInv.InventoryRecyclerListener,AddInventoryDiaFragment.OnInputListener, UpdateInventoryDiaFragment.OnInputListener, DeleteInventoryDiagFragment.OnInputListener{
    private static final String TAG = "ViewInventory";
    ActivityViewInventoryBinding activityViewInventoryBinding;
    Inventory inventory = null;
    CustomViewAdapInv.InventoryRecyclerListener mListener;
    CustomViewAdapInv customViewAdapInv;
    Button search;
    SQLiteDatabase sql;
    severinaDB db;
    TextView emptyfield;
    RecyclerView recyclerView;
    ImageView add_item;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        inflater.inflate(R.menu.dash_options, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        List<Inventory> items = db.getitemsList();
        switch (item.getItemId()) {
            case R.id.nav_profile:
                //startActivity(new Intent(this, ProfileSettings.class));
                Toast.makeText(this, "Going to Profile Settings", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.sort_id:
                // User chose the "Settings" item, show the app settings UI...
                Collections.sort(items, (Inventory o1, Inventory o2) -> o1.getId().compareToIgnoreCase(o2.getId()));
                customViewAdapInv = new CustomViewAdapInv(items, ViewInventory.this, mListener);
                recyclerView.setAdapter(customViewAdapInv);
                recyclerView.setLayoutManager(new LinearLayoutManager(ViewInventory.this));
                recyclerView.getAdapter().notifyDataSetChanged();
                return true;

            case R.id.sort_name:
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                Collections.sort(items, (Inventory o1, Inventory o2) -> o1.getName().compareToIgnoreCase(o2.getName()));
                Collections.reverse(items);
                customViewAdapInv = new CustomViewAdapInv(items, ViewInventory.this, mListener);
                recyclerView.setAdapter(customViewAdapInv);
                recyclerView.setLayoutManager(new LinearLayoutManager(ViewInventory.this));
                recyclerView.getAdapter().notifyDataSetChanged();
                return true;

            case R.id.sort_stocks:
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                Collections.sort(items, (Inventory o1, Inventory o2) -> String.valueOf(o1.getQuantity()).compareToIgnoreCase(String.valueOf(o2.getQuantity())));
                Collections.reverse(items);
                customViewAdapInv = new CustomViewAdapInv(items, ViewInventory.this, mListener);
                recyclerView.setAdapter(customViewAdapInv);
                recyclerView.setLayoutManager(new LinearLayoutManager(ViewInventory.this));
                recyclerView.getAdapter().notifyDataSetChanged();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
            }
    }

    //public void sendInput(String name, String qty, String desc, Bitmap image){
     public void sendInput(String name, int qty, String desc, int thres, Bitmap image){
        Log.d(TAG, "sendInput: got name: " + name + "\n got qty: " + qty + "\ngot desc:" + desc + "\ngot thres: "+thres+"\ngot image@:"+image);
         try {
             db = new severinaDB(ViewInventory.this);
             //Inventory inventory = new Inventory(name,qty, desc, image);
             Inventory inventory = new Inventory(name,qty, desc, thres, image);
             db.addItem(inventory);
         } catch (Exception ex) {
             Toast.makeText(this, "Record Fail", Toast.LENGTH_LONG).show();
         }
    }
    //public void UpdateInput(String id, String name, int qty, String desc, int thres, byte[] image) {
    public void UpdateInput(String id, String name, int qty, String desc, int thres) {
        Log.d(TAG, "updateInput: got id: " + id+ "\n got name: " + name + "\n got qty: " + qty + "\ngot desc:" + desc);
        try {
            db = new severinaDB(ViewInventory.this);
            //Inventory inventory = new Inventory(name,qty, desc, image);
            Inventory items = new Inventory (id, name, qty, desc, thres);
            db.updateItem(items);
        } catch (Exception ex) {
            Toast.makeText(this, "Record Fail", Toast.LENGTH_LONG).show();
        }
    }

    public void DeleteInput(String id){
        Log.d(TAG, "deleteItem:  got id: "+id);
        try {
            db = new severinaDB(ViewInventory.this);
            db.deleteItem(id);
        } catch (Exception ex) {
            Toast.makeText(this, "Record Fail", Toast.LENGTH_LONG).show();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityViewInventoryBinding = ActivityViewInventoryBinding.inflate(getLayoutInflater());
        setContentView(activityViewInventoryBinding.getRoot());
        allocatedActivityTitle("Manage Inventory");
        ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);

        search = findViewById(R.id.search);
        db = new severinaDB(this);
        List<Inventory> items = db.getitemsList();

        add_item = findViewById(R.id.add_view_inv);
        recyclerView = findViewById(R.id.rv_view_inv);
       customViewAdapInv = new CustomViewAdapInv(items, this, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(customViewAdapInv);
        emptyfield = findViewById(R.id.emptyRv);
        if (customViewAdapInv.getItemCount() != 0){emptyfield.setVisibility(View.GONE);}

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
    protected void onActivityResult(int requestcode, int resultcode, Intent data) {
        super.onActivityResult(requestcode, resultcode, data);
        try{
            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.add_dialogfrag);
            fragment.onActivityResult(requestcode, resultcode, data);
            startActivity(data);
        }catch(Exception e){Log.e(TAG, e.getMessage());}

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
