package com.example.testois;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class NavHeader extends AppCompatActivity {
    TextView txtinv, txtorder, txtdashi, txtdasho;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_header);
        txtinv = findViewById(R.id.txtviewinv);
        txtorder = findViewById(R.id.txtvieworder);
        txtdashi = findViewById(R.id.txtdashinv);
        txtdasho = findViewById(R.id.txtdashorder);

        txtinv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), ViewInventory.class);
                startActivity(i);
            }
        });
        txtdashi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), DashboardInventory.class);
                startActivity(i);
            }
        });
        txtorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), ViewOrder.class);
                startActivity(i);
            }
        });
        txtdasho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), DashboardOrders.class);
                startActivity(i);
            }
        });

    }
}