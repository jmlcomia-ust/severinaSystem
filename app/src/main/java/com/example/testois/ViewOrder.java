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
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testois.adapter.CustomAdapterOrd;
import com.example.testois.adapter.CustomViewAdapOrd;
import com.example.testois.databinding.ActivityDashboardInventoryBinding;
import com.example.testois.databinding.ActivityViewInventoryBinding;
import com.example.testois.databinding.ActivityViewOrderBinding;
import com.example.testois.fragments.AddOrderDiaFragment;

import java.util.Collections;
import java.util.List;

public class ViewOrder extends DrawerBaseActivity{
    private static final String TAG = "ViewOrders";
    ActivityViewOrderBinding activityViewOrderBinding;
    private Toolbar toolbar;
    private severinaDB db;
    RecyclerView rv_current, rv_recent;
    TextView emptyfield1, emptyfield2;
    ImageView add_btn;
    String frag_name, frag_qty, frag_stat;
    CustomViewAdapOrd curr_customViewAdapterOrd, recent_customViewAdapterOrd;
    List<Orders> curr_orders;{
        try{
            curr_orders = db.getCurrOrdList();
            {
                curr_orders = db.getCurrOrdList();
                if (curr_orders.isEmpty()) { curr_orders = db.getOrderList(); }
            }
        }catch(Exception e){Log.e(TAG, "Error on Null");}}

    List<Orders> recnt_orders;{
        try{
            recnt_orders = db.getRecntOrdList();
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
    //public void sendInput(String name, String qty, String desc, byte[] bytesImage)
    public void sendInput(String name, String qty, String stat){
        Log.d(TAG, "sendInput: got name: " + name + "\n got qty: " + qty + "\ngot desc:" + stat);
        frag_name = name;
        frag_qty = qty;
        frag_stat = stat;
        setInputToListView();
    }

    //public void UpdateInput(String name, String qty, String desc, byte[] image) {
    public void UpdateInput(String name, String qty, String stat) {
        Log.d(TAG, "updateInput: got name: " + name + "\n got qty: " + qty + "\ngot desc:" + stat);
        frag_name = name;
        frag_qty = qty;
        frag_stat = stat;
        setUpdatesToListView();
    }
    private void setInputToListView(){
        try {
            db = new severinaDB(ViewOrder.this);
            Orders orders = new Orders (frag_name, frag_qty, frag_stat);
            db.addOrder(orders);
            if (frag_stat.equalsIgnoreCase("delivered")){
                recent_customViewAdapterOrd = new CustomViewAdapOrd(recnt_orders, this);
                rv_recent.setAdapter(recent_customViewAdapterOrd);
                rv_recent.setLayoutManager(new LinearLayoutManager(ViewOrder.this));
                if (recent_customViewAdapterOrd.getItemCount() != 0){emptyfield2.setVisibility(View.GONE);}
                Toast.makeText(this, "Item Added Successfully!", Toast.LENGTH_LONG).show();
            }
            else if (frag_stat.equalsIgnoreCase("today")){
                curr_customViewAdapterOrd = new CustomViewAdapOrd(curr_orders, this);
                rv_current.setAdapter(curr_customViewAdapterOrd);
                rv_current.setLayoutManager(new LinearLayoutManager(ViewOrder.this));
                if (curr_customViewAdapterOrd.getItemCount() != 0){emptyfield1.setVisibility(View.GONE);}
                Toast.makeText(this, "Item Added Successfully!", Toast.LENGTH_LONG).show();
            }else{
            Toast.makeText(this, "Error Adding Orders!", Toast.LENGTH_LONG).show();
        }} catch (Exception ex) {
            // Toast.makeText(this, "Record Fail", Toast.LENGTH_LONG).show();
        }
    }
    private void setUpdatesToListView(){
        try {
            db = new severinaDB(ViewOrder.this);
            //  Orders orders = new Orders (frag_name, frag_qty, frag_stat, frag_image);
            Orders orders = new Orders (frag_name, frag_qty, frag_stat);
            db.updateOrder(orders);
            Toast.makeText(this, "Item Updated Successfully!", Toast.LENGTH_LONG).show();
        } catch (Exception ex) {
            Toast.makeText(this, "Record Fail", Toast.LENGTH_LONG).show();
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
                curr_customViewAdapterOrd = new CustomViewAdapOrd(curr_orders, ViewOrder.this);
                rv_current.setAdapter(curr_customViewAdapterOrd);
                rv_current.setLayoutManager(new LinearLayoutManager(ViewOrder.this));
                rv_current.getAdapter().notifyDataSetChanged();

                Collections.sort(recnt_orders, (Orders o1, Orders o2) -> o1.getId().compareToIgnoreCase(o2.getId()));
                recent_customViewAdapterOrd = new CustomViewAdapOrd(recnt_orders, ViewOrder.this);
                rv_recent.setAdapter(recent_customViewAdapterOrd);
                rv_recent.setLayoutManager(new LinearLayoutManager(ViewOrder.this));
                rv_recent.getAdapter().notifyDataSetChanged();
                return true;

            case R.id.sort_name:
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                Collections.sort(curr_orders, (Orders o1, Orders o2) -> o1.getName().compareToIgnoreCase(o2.getName()));
                Collections.reverse(curr_orders);
                curr_customViewAdapterOrd = new CustomViewAdapOrd(curr_orders, ViewOrder.this);
                rv_current.setAdapter(curr_customViewAdapterOrd);
                rv_current.setLayoutManager(new LinearLayoutManager(ViewOrder.this));
                rv_current.getAdapter().notifyDataSetChanged();

                Collections.sort(recnt_orders, (Orders o1, Orders o2) -> o1.getName().compareToIgnoreCase(o2.getName()));
                Collections.reverse(recnt_orders);
                recent_customViewAdapterOrd = new CustomViewAdapOrd(recnt_orders, ViewOrder.this);
                rv_recent.setAdapter(recent_customViewAdapterOrd);
                rv_recent.setLayoutManager(new LinearLayoutManager(ViewOrder.this));
                rv_recent.getAdapter().notifyDataSetChanged();

            case R.id.sort_stocks:
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                Collections.sort(curr_orders, (Orders o1, Orders o2) -> o1.getQuantity().compareToIgnoreCase(o2.getQuantity()));
                Collections.reverse(curr_orders);
                curr_customViewAdapterOrd = new CustomViewAdapOrd(curr_orders, ViewOrder.this);
                rv_current.setAdapter(curr_customViewAdapterOrd);
                rv_current.setLayoutManager(new LinearLayoutManager(ViewOrder.this));
                rv_current.getAdapter().notifyDataSetChanged();

                Collections.sort(recnt_orders, (Orders o1, Orders o2) -> o1.getQuantity().compareToIgnoreCase(o2.getQuantity()));
                Collections.reverse(recnt_orders);
                recent_customViewAdapterOrd = new CustomViewAdapOrd(recnt_orders, ViewOrder.this);
                rv_recent.setAdapter(recent_customViewAdapterOrd);
                rv_recent.setLayoutManager(new LinearLayoutManager(ViewOrder.this));
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
        activityViewOrderBinding = ActivityViewOrderBinding.inflate(getLayoutInflater());
        setContentView(activityViewOrderBinding.getRoot());
        allocatedActivityTitle("View Order");


        db = new severinaDB(ViewOrder.this);
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
        add_btn = findViewById(R.id.add_ord);
        rv_current = findViewById(R.id.rv_current);
        rv_recent = findViewById(R.id.rv_recent);



        recent_customViewAdapterOrd = new CustomViewAdapOrd(recnt_orders, this);
            rv_recent.setAdapter(recent_customViewAdapterOrd);
            rv_recent.setLayoutManager(new LinearLayoutManager(ViewOrder.this));
            if (recent_customViewAdapterOrd.getItemCount() != 0){emptyfield2.setVisibility(View.GONE);}

        curr_customViewAdapterOrd = new CustomViewAdapOrd(curr_orders, this);
            rv_current.setAdapter(curr_customViewAdapterOrd);
            rv_current.setLayoutManager(new LinearLayoutManager(ViewOrder.this));
            if (curr_customViewAdapterOrd.getItemCount() != 0){emptyfield1.setVisibility(View.GONE);}


        add_btn.setOnClickListener(view ->{
            Log.d(TAG, "onClick: opening dialog.");
            AddOrderDiaFragment dialog = new AddOrderDiaFragment();
            dialog.show(getSupportFragmentManager(), "AddOrderDiaFragment");
        });
    }
/*
    @Override
    public void gotoUpdateFragment(Orders orders, Bundle args) {
        androidx.fragment.app.FragmentManager fm = this.getSupportFragmentManager();
        UpdateOrderDiaFragment updafrag = new UpdateOrderDiaFragment();
        updafrag.setArguments(args);
        updafrag.show(fm, "UpdateOrdFrag");


 */
    }



