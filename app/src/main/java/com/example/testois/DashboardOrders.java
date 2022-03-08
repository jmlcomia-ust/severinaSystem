package com.example.testois;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testois.fragments.AddOrderDiaFragment;

import java.util.List;

public class DashboardOrders extends AppCompatActivity implements AddOrderDiaFragment.OnInputListener {
    private static final String TAG = "DashboardOrders";

    Button dash_menu, add_item;
    severinaDB db;
    RecyclerView rv_current, rv_recent;
    TextView emptyfield1, emptyfield2;
    ImageView imageview;
    String frag_name, frag_qty, frag_stat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_orders);

        db = new severinaDB(DashboardOrders.this);
        List<Orders> orders = db.getOrderList();

        emptyfield1 = findViewById(R.id.emptyRv1);
        emptyfield2 = findViewById(R.id.emptyRv2);
        dash_menu = findViewById(R.id.dash_ord_menu);
        add_item = findViewById(R.id.add_item);
        imageview = findViewById(R.id.ord_img);
        rv_current = findViewById(R.id.rv_current);
        rv_recent = findViewById(R.id.rv_recent);

        CustomAdapterOrd customAdapterOrd = new CustomAdapterOrd(orders, this);
        rv_current.setAdapter(customAdapterOrd);
        rv_current.setLayoutManager(new LinearLayoutManager(DashboardOrders.this));

        dash_menu.setOnClickListener(view -> {
            Intent i = new Intent(this, ViewOrder.class);
            startActivity(i);
        });
        add_item.setOnClickListener(view ->{
                Log.d(TAG, "onClick: opening dialog.");
                AddOrderDiaFragment dialog = new AddOrderDiaFragment();
                dialog.show(getSupportFragmentManager(), "AddOrderDiaFragment");
        });
        ;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            recreate();
        }
    }
    @Override
    public void sendInput(String name, String qty, String stat) {
        Log.d(TAG, "sendInput: got name: " + name + "\n got qty: " + qty + "\ngot desc:" + stat);

//        mInputDisplay.setText(input);
        frag_name = name;   frag_qty=qty;   frag_stat=stat;
        setInputToListView();
    }
    private void setInputToListView(){
        try {
            db = new severinaDB(DashboardOrders.this);
            Orders orders = new Orders (frag_name, frag_qty, frag_stat);
            db.addOrder(orders);
            Toast.makeText(this, "Item Added Successfully!", Toast.LENGTH_LONG).show();
        } catch (Exception ex) {
            Toast.makeText(this, "Record Fail", Toast.LENGTH_LONG).show();
        }
    }
}