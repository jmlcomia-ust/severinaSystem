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

import com.example.testois.adapter.CustomAdapterOrd;
import com.example.testois.dao.Orders;
import com.example.testois.databinding.ActivityCourierDashboardOrderBinding;
import com.example.testois.utilities.severinaDB;
import com.google.android.material.navigation.NavigationView;

import java.util.Collections;
import java.util.List;

public class CourierDashboardOrder  extends CourierDrawerBaseActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "DashboardOrders";
    ActivityCourierDashboardOrderBinding activityCourierDashboardOrderBinding;
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
        MenuItem searchItem = menu.findItem(R.id.nav_search);
        SearchView searchView = (SearchView) searchItem.getActionView();


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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityCourierDashboardOrderBinding = ActivityCourierDashboardOrderBinding.inflate(getLayoutInflater());
        setContentView(activityCourierDashboardOrderBinding.getRoot());
        allocatedActivityTitle("Dashboard Orders");

        severinaDB db = new severinaDB(CourierDashboardOrder.this);
        all_orders = db.getOrderList();
        emptyfield1 = findViewById(R.id.emptyRv1);
        emptyfield2 = findViewById(R.id.emptyRv2);
        rv_current = findViewById(R.id.rv_current);
        rv_recent = findViewById(R.id.rv_recent);

        customAdapterOrd = new CustomAdapterOrd(all_orders, this);
        rv_current.setAdapter(customAdapterOrd);
        rv_current.setLayoutManager(new LinearLayoutManager(CourierDashboardOrder.this));
        if (customAdapterOrd.getItemCount() != 0){emptyfield2.setVisibility(View.GONE); emptyfield1.setVisibility(View.GONE);}

    }
}