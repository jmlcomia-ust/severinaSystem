//Source: https://www.tutussfunny.com/android-sqlite-crud-tutorial/

package com.example.testois;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddInventory extends AppCompatActivity {

        EditText ed1,ed2,ed3;
        Button b1,b2;
        severinaDB severinadb;
        SQLiteDatabase db;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_add_inventory);

            ed1 = findViewById(R.id.name);
            ed2 = findViewById(R.id.quantity);
            ed3 = findViewById(R.id.description);

            b1 = findViewById(R.id.bt1);
            b2 = findViewById(R.id.bt2);

            b2.setOnClickListener(v -> {
                Intent i = new Intent(getApplicationContext(), DashboardInventory.class);
                startActivity(i);
            });
            b1.setOnClickListener(v -> insert());
        }
    @Override
    public void onBackPressed() {
        Intent i = new Intent(getApplicationContext(),LoginActivity.class);
        startActivity(i);
    }

        public void insert()
        {
            try {
                 severinadb = new severinaDB(AddInventory.this);
                severinadb.addInventory(ed1.getText().toString().trim(),
                        Integer.parseInt(ed2.getText().toString().trim()),
                        ed3.getText().toString().trim());
            }
            catch (Exception ex)
            {
                Toast.makeText(this,"Record Fail",Toast.LENGTH_LONG).show();
            }
        }
    }