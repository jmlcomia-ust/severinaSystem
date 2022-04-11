package com.example.testois;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testois.dao.Inventory;
import com.example.testois.dao.Report;
import com.example.testois.utilities.severinaDB;
import com.example.testois.adapter.CustomViewAdapInv;
import com.example.testois.databinding.ActivityViewInventoryBinding;
import com.example.testois.fragments.AddInventoryDiaFragment;
import com.example.testois.fragments.DeleteInventoryDiagFragment;
import com.example.testois.fragments.UpdateInventoryDiaFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.navigation.NavigationView.OnNavigationItemSelectedListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
//hello
@SuppressLint("NewApi")
public class ViewInventory extends DrawerBaseActivity implements CustomViewAdapInv.InventoryRecyclerListener,AddInventoryDiaFragment.OnInputListener, UpdateInventoryDiaFragment.OnInputListener, DeleteInventoryDiagFragment.OnInputListener{
    private static final String TAG = "ViewInventory";
    ActivityViewInventoryBinding activityViewInventoryBinding;
    CustomViewAdapInv customViewAdapInv;
    SearchView searchView;
    SQLiteDatabase sql;
    severinaDB db;
    TextView emptyfield, view_order;
    RecyclerView recyclerView;
    ImageView add_item;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        MenuItem searchViewItem = menu.findItem(R.id.nav_search);
        searchView = (SearchView) searchViewItem.getActionView();
        searchView.clearFocus();

        db = new severinaDB(this);
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

    //public void sendInput(String name, String qty, String desc, int thres, Bitmap image){
     public void sendInput(String name, int qty, String desc, int thres){
        //Log.d(TAG, "sendInput: got name: " + name + "\n got qty: " + qty + "\ngot desc:" + desc + "\ngot thres: "+thres+"\ngot image@:"+image);
        Log.d(TAG, "sendInput: got name: " + name + "\n got qty: " + qty + "\ngot desc:" + desc + "\ngot thres: "+thres);
         try {
             db = new severinaDB(ViewInventory.this);
             Inventory inventory = new Inventory(name,qty, desc, thres);
             if(db.checkExistingData("'"+name+"'")){
                Toast.makeText(this, "Sorry there is already an item named "+name+" on the database.Select the existing item to add stocks.",Toast.LENGTH_LONG).show();
            }
             else if(qty == (thres+1) || qty ==(thres+2)){   db.addItem(inventory);db.NotifyOnStock(1,name);}
             else if(qty < thres){   db.addItem(inventory);db.NotifyOnStock(2,name); }
             else if(qty <= thres){  db.addItem(inventory);db.NotifyOnStock(3,name); }
         } catch (Exception ex) {
             Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
         }
    }

    //public void UpdateInput(String id, String name, int qty, String desc, int thres,Bitmap image) {
    public void UpdateInput(int id, String name, int qty, String desc, int thres) {
        //Log.d(TAG, "updateInput: got id: " + id+ "\n got name: " + name + "\n got qty: " + qty + "\ngot desc:" + desc + "\ngot image@: "+image);
        Log.d(TAG, "updateInput: got id: " + id+ "\n got name: " + name + "\n got qty: " + qty + "\ngot desc:" + desc);
        try {
            Inventory items = new Inventory (id, name, qty, desc, thres);
            db = new severinaDB(ViewInventory.this);
            //Inventory inventory = new Inventory(name,qty, desc, thres, image);
            if(db.checkExistingData("'"+name+"'")){
                Toast.makeText(this, "Sorry there is an existing item on the database. Select the existing item to add stocks.",Toast.LENGTH_LONG).show();
            }

            else if(qty == (thres+1) || qty ==(thres+2)){

                db.updateItem(items);
                db.NotifyOnStock(1,name);
            }
            else if(qty < thres){
                db.updateItem(items);
                db.NotifyOnStock(2,name);
            }
            else if(qty <= thres){
                db.updateItem(items);
                db.NotifyOnStock(3,name);
            }
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
