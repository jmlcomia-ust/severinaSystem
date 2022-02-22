package com.example.testois;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    EditText edtuname, edtpword;
    Button btn1, btn2;
    severinaDB severinadb;
    int id = 0;
    SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        severinadb = new severinaDB(LoginActivity.this);
        btn1 = (Button) findViewById(R.id.btn1);
        btn2 = (Button) findViewById(R.id.btn2);
        edtuname = (EditText) findViewById(R.id.user);
        edtpword = (EditText) findViewById(R.id.pass);

        btn1.setOnClickListener(v -> {
            boolean isExist;
            isExist = severinaDB.checkUser(edtuname.getText().toString(), edtpword.getText().toString());

            if(isExist){
                Intent intent = new Intent(LoginActivity.this, DashboardInventory.class);
                intent.putExtra("username", edtuname.getText().toString());
                startActivity(intent);
            } else {
                edtpword.setText(null);
                Toast.makeText(LoginActivity.this, "Login failed. Invalid username or password.", Toast.LENGTH_SHORT).show();
            }
        });
        btn2.setOnClickListener(view -> {
            Intent i = new Intent(getApplicationContext(), DashboardOrders.class);
            startActivity(i);
        });
    }
}

