package com.example.testois;

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
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testois.adapter.CustomAdapterOrd;
import com.example.testois.adapter.CustomViewAdapInv;
import com.example.testois.adapter.CustomViewAdapOrd;
import com.example.testois.databinding.ActivityDashboardInventoryBinding;
import com.example.testois.databinding.ActivityViewInventoryBinding;
import com.example.testois.databinding.ActivityViewOrderBinding;
import com.example.testois.fragments.AddInventoryDiaFragment;
import com.example.testois.fragments.AddOrderDiaFragment;
import com.example.testois.fragments.DeleteInventoryDiagFragment;
import com.example.testois.fragments.DeleteOrderDiaFragment;
import com.example.testois.fragments.UpdateInventoryDiaFragment;
import com.example.testois.fragments.UpdateOrderDiaFragment;

import java.util.Collections;
import java.util.List;

public class ViewOrder extends DrawerBaseActivity implements CustomViewAdapOrd.OrderRecyclerListener, AddOrderDiaFragment.OnInputListener, UpdateOrderDiaFragment.OnInputListener, DeleteOrderDiaFragment.OnInputListener{
    private static final String TAG = "ViewOrders";
    ActivityViewOrderBinding activityViewOrderBinding;
    private severinaDB db;
    RecyclerView rv_current, rv_recent;
    TextView emptyfield1, emptyfield2;
    ImageView add_btn;
    Orders orders;
    String frag_name, frag_qty, frag_stat;
    CustomViewAdapOrd.OrderRecyclerListener nListener;
    CustomViewAdapOrd curr_customViewAdapterOrd, recent_customViewAdapterOrd, customViewAdapOrd;
    List<Orders> all_orders;
   // List<Orders> curr_orders;{ try{ curr_orders = db.getCurrOrdList();{curr_orders = db.getCurrOrdList();if (curr_orders.isEmpty()) { curr_orders = db.getOrderList(); } } }catch(Exception e){Log.e(TAG, "Error on Null");}}
  //  List<Orders> recnt_orders;{ try{ recnt_orders = db.getRecntOrdList();{ recnt_orders = db.getRecntOrdList();if (recnt_orders.isEmpty()) { recnt_orders = db.getOrderList(); } } }catch(Exception e){Log.e(TAG, "Error on Null");}}

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
            case R.id.sort_id:
                // User chose the "Settings" item, show the app settings UI...
                Collections.sort(all_orders, (Orders o1, Orders o2) -> o1.getId().compareToIgnoreCase(o2.getId()));
                customViewAdapOrd = new CustomViewAdapOrd(all_orders, ViewOrder.this, nListener);
                rv_current.setAdapter(customViewAdapOrd);
                rv_current.setLayoutManager(new LinearLayoutManager(ViewOrder.this));
                rv_current.getAdapter().notifyDataSetChanged();
                return true;
                /*
                Collections.sort(all_orders, (Orders o1, Orders o2) -> o1.getId().compareToIgnoreCase(o2.getId()));
                recent_customViewAdapterOrd = new CustomViewAdapOrd(recnt_orders, ViewOrder.this, nListener);
                rv_recent.setAdapter(recent_customViewAdapterOrd);
                rv_recent.setLayoutManager(new LinearLayoutManager(ViewOrder.this));
                rv_recent.getAdapter().notifyDataSetChanged();
                return true;
                 */

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
                Collections.sort(all_orders, (Orders o1, Orders o2) -> o1.getQuantity().compareToIgnoreCase(o2.getQuantity()));
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
/*
       recent_customViewAdapterOrd = new CustomViewAdapOrd(recnt_orders, this, nListener);
            rv_recent.setAdapter(recent_customViewAdapterOrd);
            rv_recent.setLayoutManager(new LinearLayoutManager(ViewOrder.this));
            //if (recent_customViewAdapterOrd.getItemCount() != 0){emptyfield2.setVisibility(View.GONE);}

        curr_customViewAdapterOrd = new CustomViewAdapOrd(curr_orders, this, nListener);
            rv_current.setAdapter(curr_customViewAdapterOrd);
            rv_current.setLayoutManager(new LinearLayoutManager(ViewOrder.this));
            //if (curr_customViewAdapterOrd.getItemCount() != 0){emptyfield1.setVisibility(View.GONE);}

 */
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
    public void sendInput(String name, String qty, String stat) {
        Log.d(TAG, "sendInput: got name: " + name + "\n got qty: " + qty + "\ngot stat:" + stat);
        try{
            db = new severinaDB(ViewOrder.this);
            Orders orders = new Orders (name, qty, stat);
            db.addOrder(orders);
        }catch (Exception e){ Toast.makeText(this, "Record Fail", Toast.LENGTH_LONG).show(); }
    }

    @Override
    public void UpdateInput(String id, String name, String qty, String stat) {
        Log.d(TAG, "updateInput: got id: " + id+ "\n got name: " + name + "\n got qty: " + qty + "\ngot desc:" + stat);
        try {
            db = new severinaDB(ViewOrder.this);
            //Orders order = new Orders(name,qty, desc, image);
            Orders order = new Orders(id, name,qty, stat);
            db.updateOrder(order);
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


