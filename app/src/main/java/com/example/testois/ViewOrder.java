package com.example.testois;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.testois.dao.Orders;
import com.example.testois.dao.Report;
import com.example.testois.utilities.severinaDB;
import com.example.testois.adapter.CustomViewAdapOrd;
import com.example.testois.databinding.ActivityViewOrderBinding;
import com.example.testois.fragments.AddOrderDiaFragment;
import com.example.testois.fragments.DeleteOrderDiaFragment;
import com.example.testois.fragments.UpdateOrderDiaFragment;

import java.util.List;

public class ViewOrder extends DrawerBaseActivity implements CustomViewAdapOrd.OrderRecyclerListener, AddOrderDiaFragment.OnInputListener, UpdateOrderDiaFragment.OnInputListener, DeleteOrderDiaFragment.OnInputListener{
    private static final String TAG = "ViewOrders";
    ActivityViewOrderBinding activityViewOrderBinding;
    public severinaDB db;
    RecyclerView rv_current, rv_recent;
    TextView emptyfield1, emptyfield2;
    ImageView add_btn;
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
        db = new severinaDB(this);
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
            if (db.checkAvailability("'"+desc+"'", qty)){             //check if order qty can be processed to inv_qty
                Orders orders = new Orders (name, qty, desc, date, stat);
                db.AddToWorkBook("'"+desc+"'", name, qty, desc, date, stat); //add order data and inv data in SP
                if (db.CheckWorkBook()){       //check if SP data are existing
                    SharedPreferences sharedPref = ViewOrder.this.getSharedPreferences("severinaoistempdata", Context.MODE_PRIVATE);
                    String report_date = sharedPref.getString("date", "");
                    String report_name = sharedPref.getString("name", "");
                    int report_inv_qty = sharedPref.getInt("invqty", 0);
                    int report_ord_qty = sharedPref.getInt("ordqty",0);
                    Report report = new Report(report_date, report_name, report_inv_qty, report_ord_qty);
                    db.addReport(report);           //add report data set from SP data
                }
                if(stat.equalsIgnoreCase("TO DELIVER")){
                    db.addOrder(orders);
                    db.NotifyOnOrder(1, desc, String.valueOf(qty), date);}
                else if(stat.equalsIgnoreCase("DELIVERED")){
                    db.addOrder(orders);
                    db.NotifyOnOrder(2,desc, String.valueOf(qty), date);}
            }
        }catch (Exception e){  e.printStackTrace(); }
    }

    @Override
    public void UpdateInput(int id, String name, int qty, String desc, String date, String stat) {
        Log.d(TAG, "updateInput: got name: " + name + "\n got qty: " + qty + "\ngot desc: " + desc + "\ngot stat: " + stat);
        try {
            if (db.checkAvailability("'"+desc+"'", qty)){
                Orders orders = new Orders (name, qty, desc, date, stat);
                db.AddToWorkBook("'"+desc+"'", name, qty, desc, date, stat); //add order data and inv data in SP
                if (db.CheckWorkBook()){       //check if SP data are existing
                    SharedPreferences sharedPref = ViewOrder.this.getSharedPreferences("severinaoistempdata", Context.MODE_PRIVATE);
                    String report_date = sharedPref.getString("date", "");
                    String report_name = sharedPref.getString("name", "");
                    int report_inv_qty = sharedPref.getInt("invqty", 0);
                    int report_ord_qty = sharedPref.getInt("ordqty",0);
                    Report report = new Report(report_date, report_name, report_inv_qty, report_ord_qty);
                    db.addReport(report);           //add report data set from SP data
                }
                if(stat.equalsIgnoreCase("TO DELIVER")){
                    db.updateOrder(orders);
                    db.NotifyOnOrder(1, desc, String.valueOf(qty), date);}
                else if(stat.equalsIgnoreCase("DELIVERED")){
                    db.updateOrder(orders);
                    db.NotifyOnOrder(2,desc, String.valueOf(qty), date);}
            }
            else{ db.NotifyOnOrder(3, desc, String.valueOf(qty), date);
                Toast.makeText(getApplicationContext(), "Order not Updated. Check inventory Stocks if there is enough to make an Order", Toast.LENGTH_LONG).show();}
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


