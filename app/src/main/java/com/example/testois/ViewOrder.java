package com.example.testois;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
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

import com.example.testois.dao.Inventory;
import com.example.testois.dao.Orders;
import com.example.testois.utilities.severinaDB;
import com.example.testois.adapter.CustomViewAdapOrd;
import com.example.testois.databinding.ActivityViewOrderBinding;
import com.example.testois.fragments.AddOrderDiaFragment;
import com.example.testois.fragments.DeleteOrderDiaFragment;
import com.example.testois.fragments.UpdateOrderDiaFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ViewOrder extends DrawerBaseActivity implements CustomViewAdapOrd.OrderRecyclerListener, AddOrderDiaFragment.OnInputListener, UpdateOrderDiaFragment.OnInputListener, DeleteOrderDiaFragment.OnInputListener{
    private static final String TAG = "ViewOrders";
    ActivityViewOrderBinding activityViewOrderBinding;
    public severinaDB db, db_inv;
    RecyclerView rv_current, rv_recent;
    TextView emptyfield1, emptyfield2;
    ImageView add_btn;
    CustomViewAdapOrd.OrderRecyclerListener nListener;
    CustomViewAdapOrd customViewAdapOrd;
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
                customViewAdapOrd.getFilter().filter(newText);
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
                customViewAdapOrd = new CustomViewAdapOrd(all_orders, ViewOrder.this, nListener);
                rv_current.setAdapter(customViewAdapOrd);
                rv_current.setLayoutManager(new LinearLayoutManager(ViewOrder.this));
                rv_current.getAdapter().notifyDataSetChanged();
                return true;

            case R.id.sort_stocks:
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                Collections.sort(all_orders, (Orders o1, Orders o2) -> String.valueOf(o1.getQuantity()).compareToIgnoreCase(String.valueOf(o2.getQuantity())));
                Collections.reverse(all_orders);
                customViewAdapOrd = new CustomViewAdapOrd(all_orders, ViewOrder.this, nListener);
                rv_current.setAdapter(customViewAdapOrd);
                rv_current.setLayoutManager(new LinearLayoutManager(ViewOrder.this));
                rv_current.getAdapter().notifyDataSetChanged();
                return true;
            case R.id.sort_stat:
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                Collections.sort(all_orders, (Orders o1, Orders o2) -> o1.getStatus().compareToIgnoreCase(o2.getStatus()));
                Collections.reverse(all_orders);
                customViewAdapOrd = new CustomViewAdapOrd(all_orders, ViewOrder.this, nListener);
                rv_current.setAdapter(customViewAdapOrd);
                rv_current.setLayoutManager(new LinearLayoutManager(ViewOrder.this));
                rv_current.getAdapter().notifyDataSetChanged();
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityViewOrderBinding = ActivityViewOrderBinding.inflate(getLayoutInflater());
        setContentView(activityViewOrderBinding.getRoot());
        allocatedActivityTitle("View Order");
        emptyfield1 = findViewById(R.id.emptyRv1);
        emptyfield2 = findViewById(R.id.emptyRv2);
        add_btn = findViewById(R.id.add_ord);
        rv_current = findViewById(R.id.rv_current);
        rv_recent = findViewById(R.id.rv_recent);
        db = new severinaDB(ViewOrder.this);
        all_orders = db.getOrderList();
        customViewAdapOrd = new CustomViewAdapOrd(all_orders, this, this);
        rv_current.setAdapter(customViewAdapOrd);
        rv_current.setLayoutManager(new LinearLayoutManager(ViewOrder.this));
        if (customViewAdapOrd.getItemCount() != 0){emptyfield1.setVisibility(View.GONE);}

        add_btn.setOnClickListener(view ->{
            Log.d(TAG, "onClick: opening dialog.");
            AddOrderDiaFragment dialog = new AddOrderDiaFragment();
            dialog.show(getSupportFragmentManager(), "AddOrderDiaFragment");
        });
}


    @Override
    public void sendInput(String name, int qty, String desc, String date, String stat) {
        Log.d(TAG, "sendInput: got name: " + name + "\n got qty: " + qty + "\ngot desc: " + desc + "\ngot stat: " + stat);
        try{
            db = new severinaDB(ViewOrder.this);
            Orders orders = new Orders (name, qty, desc, date, stat);
           if(stat.equalsIgnoreCase("TODAY")){ db.addOrder(orders); db.NotifyOnOrder(1, desc, String.valueOf(qty)); }
           else if(stat.equalsIgnoreCase("DELIVERED")){ db.addOrder(orders); db.NotifyOnOrder(2,desc, String.valueOf(qty));}
           else{ db.NotifyOnOrder(3, desc, String.valueOf(qty)); Toast.makeText(getApplicationContext(), "Order not Added. Check inventory Stocks if there is enough to make an Order", Toast.LENGTH_LONG).show();}
        }catch (Exception e){ Toast.makeText(this, "Record Fail", Toast.LENGTH_LONG).show(); }
    }

    @Override
    public void UpdateInput(int id, String name, int qty, String desc, String date, String stat) {
        Log.d(TAG, "updateInput: got name: " + name + "\n got qty: " + qty + "\ngot desc: " + desc + "\ngot stat: " + stat);
        try {
            db = new severinaDB(ViewOrder.this);
            //Orders order = new Orders(name,qty, desc, image);
            Orders orders = new Orders(id, name, qty, desc, date, stat);
            if(stat.equalsIgnoreCase("TODAY")){ db.updateOrder(orders); db.NotifyOnOrder(1, name, String.valueOf(qty)); }
            else if(stat.equalsIgnoreCase("DELIVERED")){ db.updateOrder(orders); db.NotifyOnOrder(2,name, String.valueOf(qty));}
            else{ db.NotifyOnOrder(3, name, String.valueOf(qty)); Toast.makeText(getApplicationContext(), "Order not Updated. Check inventory Stocks if there is enough to make an Order", Toast.LENGTH_LONG).show();}

           // db.updateStock(name, qty);
        } catch (Exception ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void DeleteInput(String id){
        Log.d(TAG, "deleteItem:  got id: "+id);
        try {
            db = new severinaDB(ViewOrder.this);
            db.deleteOrder(id);
        } catch (Exception ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void gotoUpdateFragment(Orders orders, Bundle args) {
        androidx.fragment.app.FragmentManager fm = this.getSupportFragmentManager();
        UpdateOrderDiaFragment updafrag = new UpdateOrderDiaFragment();
        updafrag.setArguments(args);
        updafrag.show(fm, "UpdateOrdFrag");
    }

    @Override
    public void gotoDeleteFragment(Orders orders, Bundle args) {
        androidx.fragment.app.FragmentManager fm = this.getSupportFragmentManager();
        DeleteOrderDiaFragment delfrag = new DeleteOrderDiaFragment();
        delfrag.setArguments(args);
        delfrag.show(fm, "DeleteOrdFrag");
    }
    }


