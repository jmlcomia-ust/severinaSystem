package com.example.testois;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testois.dao.Orders;
import com.example.testois.utilities.severinaDB;
import com.example.testois.adapter.CustomAdapterOrd;
import com.example.testois.databinding.ActivityDashboardOrdersBinding;
import com.google.android.material.navigation.NavigationView;

import java.util.Collections;
import java.util.List;

public class DashboardOrders extends DrawerBaseActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "DashboardOrders";

    ActivityDashboardOrdersBinding activityDashboardOrdersBinding;
    RecyclerView rv_current, rv_recent;
    TextView emptyfield1, emptyfield2;
    CustomAdapterOrd customAdapterOrd;
    List<Orders> all_orders;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            recreate();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        inflater.inflate(R.menu.dash_options, menu);

        MenuItem searchItem = menu.findItem(R.id.nav_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.clearFocus();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                customAdapterOrd.getFilter().filter(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_profile:
                //Intent i = new Intent(this, ProfileSettings.class);
                Toast.makeText(this, "Profile Settings is clicked", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.sort_name:
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                Collections.sort(all_orders, (Orders o1, Orders o2) -> o1.getName().compareToIgnoreCase(o2.getName()));
                Collections.reverse(all_orders);
                customAdapterOrd = new CustomAdapterOrd(all_orders, DashboardOrders.this);
                rv_current.setAdapter(customAdapterOrd);
                rv_current.setLayoutManager(new LinearLayoutManager(DashboardOrders.this));
                rv_current.getAdapter().notifyDataSetChanged();
                return true;
            case R.id.sort_stocks:
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                Collections.sort(all_orders, (Orders o1, Orders o2) -> String.valueOf(o1.getQuantity()).compareToIgnoreCase(String.valueOf(o2.getQuantity())));
                Collections.reverse(all_orders);
                customAdapterOrd = new CustomAdapterOrd(all_orders, DashboardOrders.this);
                rv_current.setAdapter(customAdapterOrd);
                rv_current.setLayoutManager(new LinearLayoutManager(DashboardOrders.this));
                rv_current.getAdapter().notifyDataSetChanged();
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
        activityDashboardOrdersBinding = ActivityDashboardOrdersBinding.inflate(getLayoutInflater());
        setContentView(activityDashboardOrdersBinding.getRoot());
        allocatedActivityTitle("Dashboard Orders");

        severinaDB db = new severinaDB(DashboardOrders.this);
        all_orders = db.getOrderList();
        emptyfield1 = findViewById(R.id.emptyRv1);
        emptyfield2 = findViewById(R.id.emptyRv2);
        rv_current = findViewById(R.id.rv_current);
        rv_recent = findViewById(R.id.rv_recent);

        customAdapterOrd = new CustomAdapterOrd(all_orders, this);
            rv_current.setAdapter(customAdapterOrd);
        rv_current.setLayoutManager(new LinearLayoutManager(DashboardOrders.this));
            if (customAdapterOrd.getItemCount() != 0){emptyfield2.setVisibility(View.GONE); emptyfield1.setVisibility(View.GONE);}

    }
}
