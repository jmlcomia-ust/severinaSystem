package com.example.testois;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class simplenavi extends AppCompatActivity {
    Button btnDashInv, btnDashOrd, btnViewInv, btnViewOrd, btnRepGen, btnLogout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simplenavi);

        btnDashInv = (Button) findViewById(R.id.btnDashInv);
        btnDashOrd = (Button) findViewById(R.id.btnDashOrd);
        btnViewInv = (Button) findViewById(R.id.btnViewInv);
        btnViewOrd = (Button) findViewById(R.id.btnViewOrd);
        btnRepGen = (Button) findViewById(R.id.btnRepGen);
        btnLogout = (Button) findViewById(R.id.btnLogout);

        btnDashInv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), DashboardInventory.class);
                startActivity(i);
            }
        });

        btnDashOrd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i2 = new Intent(getApplicationContext(), DashboardOrders.class);
                startActivity(i2);
            }
        });

        btnViewInv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i3 = new Intent(getApplicationContext(), ViewInventory.class);
                startActivity(i3);
            }
        });

        btnViewOrd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i4 = new Intent(getApplicationContext(), ViewOrder.class);
                startActivity(i4);
            }
        });

        btnRepGen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i5 = new Intent(getApplicationContext(), ReportGenerationMenu.class);
                startActivity(i5);
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i6 = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(i6);
            }
        });

    }
}