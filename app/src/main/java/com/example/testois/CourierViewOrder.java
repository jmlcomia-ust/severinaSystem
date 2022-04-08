package com.example.testois;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testois.adapter.CustomCourViewAdapOrd;
import com.example.testois.dao.Inventory;
import com.example.testois.dao.Orders;
import com.example.testois.databinding.ActivityCourierViewOrderBinding;
import com.example.testois.fragments.CourUpdateOrderDiaFragment;
import com.example.testois.utilities.severinaDB;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CourierViewOrder extends CourierDrawerBaseActivity implements CustomCourViewAdapOrd.OrderRecyclerListener, CourUpdateOrderDiaFragment.OnInputListener{
    private static final String TAG = "ViewOrders";
    ActivityCourierViewOrderBinding activityCourierViewOrderBinding;
    private severinaDB db;
    SearchView search_ord;
    RecyclerView rv_current, rv_recent;
    TextView emptyfield1, emptyfield2;
    ImageView add_btn;
    SearchView searchView;
    CustomCourViewAdapOrd.OrderRecyclerListener nListener;
    CustomCourViewAdapOrd customCourViewAdapOrd;
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
                customCourViewAdapOrd.getFilter().filter(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityCourierViewOrderBinding = ActivityCourierViewOrderBinding.inflate(getLayoutInflater());
        setContentView(activityCourierViewOrderBinding.getRoot());
        allocatedActivityTitle("View Order");
        emptyfield1 = findViewById(R.id.emptyRv1);
        emptyfield2 = findViewById(R.id.emptyRv2);
        //search_ord = findViewById(R.id.search_ord);
        add_btn = findViewById(R.id.add_ord);
        rv_current = findViewById(R.id.rv_current);
        rv_recent = findViewById(R.id.rv_recent);
        db = new severinaDB(CourierViewOrder.this);
        all_orders = db.getOrderList();
        customCourViewAdapOrd = new CustomCourViewAdapOrd(all_orders, this, this);
        rv_current.setAdapter(customCourViewAdapOrd);
        rv_current.setLayoutManager(new LinearLayoutManager(CourierViewOrder.this));
        if (customCourViewAdapOrd.getItemCount() != 0){emptyfield1.setVisibility(View.GONE);}

    }

    @Override
    public void CourUpdateInput(int id, String stat) {
        Log.d(TAG, "updateInput: got stat: "+ stat);
        try {
            db = new severinaDB(CourierViewOrder.this);
            Orders order = new Orders(id, stat);
            db.updateOrderfromCour(order);
        } catch (Exception ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void gotoCourUpdateFragment(Orders orders, Bundle args) {
        androidx.fragment.app.FragmentManager fm = this.getSupportFragmentManager();
        CourUpdateOrderDiaFragment updafrag = new CourUpdateOrderDiaFragment();
        updafrag.setArguments(args);
        updafrag.show(fm, "UpdateOrdFrag");
    }
}
