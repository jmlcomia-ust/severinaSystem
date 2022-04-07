package com.example.testois;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testois.dao.Inventory;
import com.example.testois.utilities.severinaDB;
import com.example.testois.adapter.CustomViewAdapInv;
import com.example.testois.databinding.ActivityViewInventoryBinding;
import com.example.testois.fragments.AddInventoryDiaFragment;
import com.example.testois.fragments.DeleteInventoryDiagFragment;
import com.example.testois.fragments.UpdateInventoryDiaFragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
//hello
@SuppressLint("NewApi")
public class ViewInventory extends DrawerBaseActivity implements CustomViewAdapInv.InventoryRecyclerListener,AddInventoryDiaFragment.OnInputListener, UpdateInventoryDiaFragment.OnInputListener, DeleteInventoryDiagFragment.OnInputListener{
    private static final String TAG = "ViewInventory";
    ActivityViewInventoryBinding activityViewInventoryBinding;
    Inventory inventory = null;
    CustomViewAdapInv.InventoryRecyclerListener mListener;
    CustomViewAdapInv customViewAdapInv;
    SearchView searchView;
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

        // Initialise menu item search bar
        // with id and take its object
        MenuItem searchViewItem = menu.findItem(R.id.nav_search);
        searchView = (SearchView) searchViewItem.getActionView();
        searchView.clearFocus();

        db = new severinaDB(this);
        List<Inventory> items = db.getitemsList();
        items = new ArrayList<>();

        MenuItem searchItem = menu.findItem(R.id.nav_search);
        SearchView searchView = (SearchView) searchItem.getActionView();


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                customViewAdapInv.getFilter().filter(newText);
                return false;
            }
        });

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

    //public void sendInput(String name, String qty, String desc, int thres, Bitmap image){
     public void sendInput(String name, int qty, String desc, int thres){
        //Log.d(TAG, "sendInput: got name: " + name + "\n got qty: " + qty + "\ngot desc:" + desc + "\ngot thres: "+thres+"\ngot image@:"+image);
        Log.d(TAG, "sendInput: got name: " + name + "\n got qty: " + qty + "\ngot desc:" + desc + "\ngot thres: "+thres);
         try {
             db = new severinaDB(ViewInventory.this);
             //Inventory inventory = new Inventory(name,qty, desc, thres, image);
             Inventory inventory = new Inventory(name,qty, desc, thres);
             db.addItem(inventory);
             if(qty == (thres+1) || qty ==(thres+2)){ db.NotifyOnStock(1,name); }
             else if(qty < thres){ db.NotifyOnStock(2,name); }
             else if(qty <= thres){db.NotifyOnStock(3,name);}
         } catch (Exception ex) {
             Toast.makeText(this, "Record Fail", Toast.LENGTH_LONG).show();
         }
    }
    //public void UpdateInput(String id, String name, int qty, String desc, int thres,Bitmap image) {
    public void UpdateInput(int id, String name, int qty, String desc, int thres) {
        //Log.d(TAG, "updateInput: got id: " + id+ "\n got name: " + name + "\n got qty: " + qty + "\ngot desc:" + desc + "\ngot image@: "+image);
        Log.d(TAG, "updateInput: got id: " + id+ "\n got name: " + name + "\n got qty: " + qty + "\ngot desc:" + desc);
        try {
            db = new severinaDB(ViewInventory.this);
            //Inventory inventory = new Inventory(name,qty, desc, thres, image);
            Inventory items = new Inventory (id, name, qty, desc, thres);
            db.updateItem(items);
            if(qty == (thres+1) || qty ==(thres+2)){ db.NotifyOnStock(1,name); }
            else if(qty < thres){ db.NotifyOnStock(2,name); }
            else if(qty <= thres){db.NotifyOnStock(3,name);}
            db.close();
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

       /* if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            NotificationChannel channel = new NotificationChannel("NotifOnStock", "NotifOnStock", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        */
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
