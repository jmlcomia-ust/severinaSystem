package com.example.testois;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.example.testois.dao.Report;
import com.example.testois.databinding.ActivityCourierViewOrderBinding;
import com.example.testois.fragments.CourUpdateOrderDiaFragment;
import com.example.testois.utilities.severinaDB;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
@SuppressLint("all")
public class CourierViewOrder extends CourierDrawerBaseActivity implements CustomCourViewAdapOrd.OrderRecyclerListener, CourUpdateOrderDiaFragment.OnInputListener{
    private static final String TAG = "CourierViewOrders";
    ActivityCourierViewOrderBinding activityCourierViewOrderBinding;
    private severinaDB db;
    RecyclerView rv_current, rv_recent;
    TextView emptyfield1, emptyfield2;
    ImageView add_btn;
    SearchView searchView;
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
        add_btn = findViewById(R.id.add_ord);
        rv_current = findViewById(R.id.rv_current);
        rv_recent = findViewById(R.id.rv_recent);
        db = new severinaDB(CourierViewOrder.this);
        all_orders = db.getOrderList();
        customCourViewAdapOrd = new CustomCourViewAdapOrd(all_orders, this, this);
        rv_current.setAdapter(customCourViewAdapOrd);
        rv_current.setLayoutManager(new LinearLayoutManager(CourierViewOrder.this));
        if (customCourViewAdapOrd.getItemCount() != 0){
            emptyfield1.setVisibility(View.GONE);}
        else{
            emptyfield1.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void CourUpdateInput(int id, String stat) {
        Log.d(TAG, "updateInput: got stat: " + stat);
        try {
            db = new severinaDB(CourierViewOrder.this);
            SharedPreferences sharedPref = CourierViewOrder.this.getSharedPreferences("sevois_coutempdata", Context.MODE_PRIVATE);
            String name = sharedPref.getString("ordname", "");
            int qty = sharedPref.getInt("ordqty", 0);
            String desc = sharedPref.getString("orddesc", "");
            String date = sharedPref.getString("orddate", "");
            Orders orders = new Orders (name, qty, desc, date, stat);

            String invname = sharedPref.getString("name","");
            int invqty = sharedPref.getInt("invqty", 0);
            int invthres = sharedPref.getInt("invthres",0);

            if (db.checkExistingData("db_order", "ord_id", "'"+id+"'")){
                db.CourAddToWorkBook(id, stat);
                if (db.CheckWorkBook()){       //check if SP data are existing
                    String report_date = sharedPref.getString("date", "");
                    String report_name = sharedPref.getString("name", "");
                    int report_inv_qty = sharedPref.getInt("invqty", 0);
                    int report_ord_qty = sharedPref.getInt("ordqty",0);
                    Report report = new Report(report_date, report_name, report_inv_qty, report_ord_qty);
                    db.updateReport(report);           //add report data set from SP data
                }
                if(stat.equalsIgnoreCase("TO DELIVER")){
                    db.updateOrder(orders);
                }
                else if(stat.equalsIgnoreCase("DELIVERED")){
                    db.updateOrder(orders);
                }
            }
            else{
                Toast.makeText(getApplicationContext(), "Order not Updated. Check inventory Stocks if there is enough to make an Order", Toast.LENGTH_LONG).show();}
        } catch (Exception ex) {
           ex.printStackTrace();
        }
    }

    @Override
    public void gotoCourUpdateFragment(Orders orders, Bundle args) {
        androidx.fragment.app.FragmentManager fm = this.getSupportFragmentManager();
        CourUpdateOrderDiaFragment updafrag = new CourUpdateOrderDiaFragment();
        updafrag.setArguments(args);
        updafrag.show(fm, "CourUpdateOrdFrag");
    }
}
