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
import com.example.testois.dao.Orders;
import com.example.testois.databinding.ActivityCourierViewOrderBinding;
import com.example.testois.fragments.CourUpdateOrderDiaFragment;
import com.example.testois.utilities.severinaDB;

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
        inflater.inflate(R.menu.dash_options, menu);

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
                customCourViewAdapOrd = new CustomCourViewAdapOrd(all_orders, CourierViewOrder.this, nListener);
                rv_current.setAdapter(customCourViewAdapOrd);
                rv_current.setLayoutManager(new LinearLayoutManager(CourierViewOrder.this));
                rv_current.getAdapter().notifyDataSetChanged();
                return true;

            case R.id.sort_stocks:
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                Collections.sort(all_orders, (Orders o1, Orders o2) -> String.valueOf(o1.getQuantity()).compareToIgnoreCase(String.valueOf(o2.getQuantity())));
                Collections.reverse(all_orders);
                customCourViewAdapOrd = new CustomCourViewAdapOrd(all_orders, CourierViewOrder.this, nListener);
                rv_current.setAdapter(customCourViewAdapOrd);
                rv_current.setLayoutManager(new LinearLayoutManager(CourierViewOrder.this));
                rv_current.getAdapter().notifyDataSetChanged();
                return true;
            case R.id.sort_stat:
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                Collections.sort(all_orders, (Orders o1, Orders o2) -> o1.getStatus().compareToIgnoreCase(o2.getStatus()));
                Collections.reverse(all_orders);
                customCourViewAdapOrd = new CustomCourViewAdapOrd(all_orders, CourierViewOrder.this, nListener);
                rv_current.setAdapter(customCourViewAdapOrd);
                rv_current.setLayoutManager(new LinearLayoutManager(CourierViewOrder.this));
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
