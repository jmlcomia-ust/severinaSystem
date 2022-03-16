package com.example.testois;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.testois.adapter.CustomAdapterOrd;
import com.example.testois.fragments.AddOrderDiaFragment;

import java.util.List;

public class ViewOrder extends AppCompatActivity implements AddOrderDiaFragment.OnInputListener{
    private static final String TAG = "ViewOrder";

   @Override
    public void sendInput(String name, String qty, String stat) {
        Log.d(TAG, "sendInput: got name: " + name + "\n got qty: " + qty + "\ngot desc:" + stat);

//        mInputDisplay.setText(input);
        frag_name = name;   frag_qty=qty;   frag_stat=stat;
        setInputToListView();
    }
    EditText sort;
    ImageView search, add, menu;
    String frag_name, frag_qty, frag_stat;
    RecyclerView lst1;
    severinaDB db;
    SQLiteDatabase sq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_order);
        db = new severinaDB(this);
        List<Orders> orders = db.getOrderList();
        menu = findViewById(R.id.menu_ord);
        menu.setOnClickListener(view -> {
            Intent i = new Intent(getApplicationContext(), ViewOrder.class);
            startActivity(i);
        });
        add = findViewById(R.id.add_ord);
        add.setOnClickListener(v -> {
            Intent i = new Intent(getApplicationContext(), AddOrderDiaFragment.class);
            startActivity(i);
            finish();
        });
        search = findViewById(R.id.search_ord);
        search.setOnClickListener(v -> {
            if (sort != null) {

            }
        });

        CustomAdapterOrd customAdapterOrd = new CustomAdapterOrd(orders, this);
        lst1.setAdapter(customAdapterOrd);
        lst1.setLayoutManager(new LinearLayoutManager(ViewOrder.this));
        }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            recreate();
        }
    }
    private void setInputToListView(){
        try {
            db = new severinaDB(ViewOrder.this);
            Orders orders = new Orders (frag_name, frag_qty, frag_stat);
            db.addOrder(orders);
            Toast.makeText(this, "Item Added Successfully!", Toast.LENGTH_LONG).show();
        } catch (Exception ex) {
            Toast.makeText(this, "Record Fail", Toast.LENGTH_LONG).show();
        }
    }
}
