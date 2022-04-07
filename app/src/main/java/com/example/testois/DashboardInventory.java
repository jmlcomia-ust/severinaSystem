package com.example.testois;

import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
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

import com.example.testois.dao.Inventory;
import com.example.testois.utilities.severinaDB;
import com.example.testois.adapter.CustomAdapterInv;
import com.example.testois.databinding.ActivityDashboardInventoryBinding;

import java.util.ArrayList;
import java.util.List;

public class DashboardInventory extends DrawerBaseActivity implements Filterable {
    private static final String TAG = "DashboardInventory";
    ActivityDashboardInventoryBinding activityDashboardInventoryBinding;
    TextView emptyfield;
    severinaDB db;
    RecyclerView recyclerView;
    ImageView imageview;
    SearchView searchView;
    Button add_btn;
    CustomAdapterInv customAdapterInv;
    int frag_qty, frag_thres;
    String frag_name, frag_desc; Bitmap frag_image;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        inflater.inflate(R.menu.dash_options, menu);
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
                customAdapterInv.getFilter().filter(newText);
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
                        //Intent i = new Intent(this, ProfileSettings.class);
                        Toast.makeText(this, "Profile Settings is clicked", Toast.LENGTH_SHORT).show();
                        return true;

                    case R.id.sort_name:
                        //getItemsByName
                        customAdapterInv = new CustomAdapterInv(items, DashboardInventory.this);
                        recyclerView.setAdapter(customAdapterInv);
                        recyclerView.setLayoutManager(new LinearLayoutManager(DashboardInventory.this));
                        recyclerView.getAdapter().notifyDataSetChanged();
                        return true;

                    case R.id.sort_stocks:
                        //getItemsOrderByStocks
                        customAdapterInv = new CustomAdapterInv(items, DashboardInventory.this);
                        recyclerView.setAdapter(customAdapterInv);
                        recyclerView.setLayoutManager(new LinearLayoutManager(DashboardInventory.this));
                        recyclerView.getAdapter().notifyDataSetChanged();
                        return true;

                    default:
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
        //imageview = findViewById(R.id.inv_img);
        recyclerView = findViewById(R.id.recyclerView);

        customAdapterInv = new CustomAdapterInv(items, this);
        recyclerView.setAdapter(customAdapterInv);
        recyclerView.setLayoutManager(new LinearLayoutManager(DashboardInventory.this));
        if (customAdapterInv.getItemCount() != 0){emptyfield.setVisibility(View.GONE);}

    }

    @Override
    public Filter getFilter() {
        return null;
    }
}