package com.example.testois;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.testois.adapter.CustomAdapterOrd;
import com.example.testois.databinding.ActivityDashboardOrdersBinding;
import com.google.android.material.navigation.NavigationView;

import java.util.Collections;
import java.util.List;

public class DashboardOrders extends DrawerBaseActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "DashboardOrders";

    ActivityDashboardOrdersBinding activityDashboardOrdersBinding;
    private Toolbar toolbar;
    private severinaDB db;
    RecyclerView rv_current, rv_recent;
    TextView emptyfield1, emptyfield2;
    CustomAdapterOrd curr_customAdapterOrd, recent_customAdapterOrd;
    List<Orders> curr_orders;
    List<Orders> recnt_orders;{
        try{
            {
                recnt_orders = db.getRecntOrdList();
                if (recnt_orders.isEmpty()) { recnt_orders = db.getOrderList(); }
            }
        }catch(Exception e){Log.e(TAG, "Error on Null");}}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            recreate();
        }
    }

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
        switch (item.getItemId()) {
            case R.id.sort_id:
                // User chose the "Settings" item, show the app settings UI...
                Collections.sort(curr_orders, (Orders o1, Orders o2) -> o1.getId().compareToIgnoreCase(o2.getId()));
                curr_customAdapterOrd = new CustomAdapterOrd(curr_orders, DashboardOrders.this);
                rv_current.setAdapter(curr_customAdapterOrd);
                rv_current.setLayoutManager(new LinearLayoutManager(DashboardOrders.this));
                rv_current.getAdapter().notifyDataSetChanged();

                Collections.sort(recnt_orders, (Orders o1, Orders o2) -> o1.getId().compareToIgnoreCase(o2.getId()));
                recent_customAdapterOrd = new CustomAdapterOrd(recnt_orders, DashboardOrders.this);
                rv_recent.setAdapter(recent_customAdapterOrd);
                rv_recent.setLayoutManager(new LinearLayoutManager(DashboardOrders.this));
                rv_recent.getAdapter().notifyDataSetChanged();
                return true;

            case R.id.sort_name:
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                Collections.sort(curr_orders, (Orders o1, Orders o2) -> o1.getName().compareToIgnoreCase(o2.getName()));
                Collections.reverse(curr_orders);
                curr_customAdapterOrd = new CustomAdapterOrd(curr_orders, DashboardOrders.this);
                rv_current.setAdapter(curr_customAdapterOrd);
                rv_current.setLayoutManager(new LinearLayoutManager(DashboardOrders.this));
                rv_current.getAdapter().notifyDataSetChanged();

                Collections.sort(recnt_orders, (Orders o1, Orders o2) -> o1.getName().compareToIgnoreCase(o2.getName()));
                Collections.reverse(recnt_orders);
                recent_customAdapterOrd = new CustomAdapterOrd(recnt_orders, DashboardOrders.this);
                rv_recent.setAdapter(recent_customAdapterOrd);
                rv_recent.setLayoutManager(new LinearLayoutManager(DashboardOrders.this));
                rv_recent.getAdapter().notifyDataSetChanged();

            case R.id.sort_stocks:
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                Collections.sort(curr_orders, (Orders o1, Orders o2) -> o1.getQuantity().compareToIgnoreCase(o2.getQuantity()));
                Collections.reverse(curr_orders);
                curr_customAdapterOrd = new CustomAdapterOrd(curr_orders, DashboardOrders.this);
                rv_current.setAdapter(curr_customAdapterOrd);
                rv_current.setLayoutManager(new LinearLayoutManager(DashboardOrders.this));
                rv_current.getAdapter().notifyDataSetChanged();

                Collections.sort(recnt_orders, (Orders o1, Orders o2) -> o1.getQuantity().compareToIgnoreCase(o2.getQuantity()));
                Collections.reverse(recnt_orders);
                recent_customAdapterOrd = new CustomAdapterOrd(recnt_orders, DashboardOrders.this);
                rv_recent.setAdapter(recent_customAdapterOrd);
                rv_recent.setLayoutManager(new LinearLayoutManager(DashboardOrders.this));
                rv_recent.getAdapter().notifyDataSetChanged();
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

        db = new severinaDB(DashboardOrders.this);
        curr_orders = db.getCurrOrdList();
        {
                curr_orders = db.getCurrOrdList();
                if (curr_orders.isEmpty()) { curr_orders = db.getOrderList(); }
        }
        recnt_orders = db.getRecntOrdList();
        {
                recnt_orders = db.getRecntOrdList();
                if (recnt_orders.isEmpty()) { recnt_orders = db.getOrderList(); }
        }

        emptyfield1 = findViewById(R.id.emptyRv1);
        emptyfield2 = findViewById(R.id.emptyRv2);
        rv_current = findViewById(R.id.rv_current);
        rv_recent = findViewById(R.id.rv_recent);

            recent_customAdapterOrd = new CustomAdapterOrd(recnt_orders, this);
            rv_recent.setAdapter(recent_customAdapterOrd);
            rv_recent.setLayoutManager(new LinearLayoutManager(DashboardOrders.this));
            if (recent_customAdapterOrd.getItemCount() != 0){emptyfield2.setVisibility(View.GONE);}

            curr_customAdapterOrd = new CustomAdapterOrd(curr_orders, this);
            rv_current.setAdapter(curr_customAdapterOrd);
            rv_current.setLayoutManager(new LinearLayoutManager(DashboardOrders.this));
            if (curr_customAdapterOrd.getItemCount() != 0){emptyfield1.setVisibility(View.GONE);}
    }
}
