package com.example.testois;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testois.adapter.CustomAdapterInv;
import com.example.testois.adapter.CustomViewAdapInv;
import com.example.testois.databinding.ActivityDashboardBinding;
import com.example.testois.databinding.ActivityDashboardInventoryBinding;
import com.example.testois.databinding.ActivityDashboardOrdersBinding;
import com.google.android.material.appbar.AppBarLayout;

import java.util.Collections;
import java.util.List;

public class DashboardInventory extends DrawerBaseActivity implements Filterable {
    private static final String TAG = "DashboardInventory";
    ActivityDashboardInventoryBinding activityDashboardInventoryBinding;
    TextView emptyfield;
    severinaDB db;
    RecyclerView recyclerView;
    ImageView imageview;
    Button add_btn;
    CustomAdapterInv customAdapterInv;
    int frag_qty, frag_thres;
    String frag_name, frag_desc; Bitmap frag_image;

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
                        //Intent i = new Intent(this, ProfileSettings.class);
                        Toast.makeText(this, "Profile Settings is clicked", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.sort_id:
                        // User chose the "Settings" item, show the app settings UI...
                        Collections.sort(items, (Inventory o1, Inventory o2) -> String.valueOf(o1.getId()).compareToIgnoreCase(String.valueOf(o2.getId())));
                        customAdapterInv = new CustomAdapterInv(items, DashboardInventory.this);
                        recyclerView.setAdapter(customAdapterInv);
                        recyclerView.setLayoutManager(new LinearLayoutManager(DashboardInventory.this));
                        recyclerView.getAdapter().notifyDataSetChanged();
                        return true;

                    case R.id.sort_name:
                        // User chose the "Favorite" action, mark the current item
                        // as a favorite...
                        Collections.sort(items, (Inventory o1, Inventory o2) -> o1.getName().compareToIgnoreCase(o2.getName()));
                        Collections.reverse(items);
                        customAdapterInv = new CustomAdapterInv(items, DashboardInventory.this);
                        recyclerView.setAdapter(customAdapterInv);
                        recyclerView.setLayoutManager(new LinearLayoutManager(DashboardInventory.this));
                        recyclerView.getAdapter().notifyDataSetChanged();
                        return true;

                    case R.id.sort_stocks:
                        // User chose the "Favorite" action, mark the current item
                        // as a favorite...
                        Collections.sort(items, (Inventory o1, Inventory o2) -> String.valueOf(o1.getQuantity()).compareToIgnoreCase(String.valueOf(o2.getQuantity())));
                        Collections.reverse(items);
                        customAdapterInv = new CustomAdapterInv(items, DashboardInventory.this);
                        recyclerView.setAdapter(customAdapterInv);
                        recyclerView.setLayoutManager(new LinearLayoutManager(DashboardInventory.this));
                        recyclerView.getAdapter().notifyDataSetChanged();
                        return true;

                    default:
                        // If we got here, the user's action was not recognized.
                        // Invoke the superclass to handle it.
                        return super.onOptionsItemSelected(item);

                }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityDashboardInventoryBinding = ActivityDashboardInventoryBinding.inflate(getLayoutInflater());
        setContentView(activityDashboardInventoryBinding.getRoot());
        allocatedActivityTitle("Dashboard Inventory");
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);


        db = new severinaDB(this);
        List<Inventory> items = db.getitemsList();
        emptyfield = findViewById(R.id.emptyRv);
        //searchview = findViewById(R.id.settings);
        imageview = findViewById(R.id.inv_img);
        recyclerView = findViewById(R.id.recyclerView);

        CustomAdapterInv customAdapterInv = new CustomAdapterInv(items, this);
        recyclerView.setAdapter(customAdapterInv);
        recyclerView.setLayoutManager(new LinearLayoutManager(DashboardInventory.this));
        if (customAdapterInv.getItemCount() != 0){emptyfield.setVisibility(View.GONE);}

    }

    @Override
    public Filter getFilter() {
        return null;
    }
    //public void sendInput(String name, String qty, String desc, byte[] bytesImage) {
        public void sendInput(String name, int qty, String desc, int thres) {
        //Log.d(TAG, "sendInput: got name: " + name + "\n got qty: " + qty + "\ngot desc:" + desc + "\ngot image:" + bytesImage);
            Log.d(TAG, "sendInput: got name: " + name + "\n got qty: " + qty + "\ngot desc:" + desc);
        frag_name = name;
        frag_qty = qty;
        frag_desc = desc;
        frag_thres = thres;
       //frag_image = bytesImage;
        setInputToListView();
    }
    private void setInputToListView() {
        try {
            db = new severinaDB(DashboardInventory.this);
            //Inventory inventory = new Inventory(frag_name,frag_qty, frag_desc, frag_image);
            Inventory inventory = new Inventory(frag_name,frag_qty, frag_desc, frag_thres);
            db.addItem(inventory);
            Toast.makeText(this, "Item Added Successfully!", Toast.LENGTH_LONG).show();
        } catch (Exception ex) {
           // Toast.makeText(this, "Record Fail", Toast.LENGTH_LONG).show();
        }
    }
}