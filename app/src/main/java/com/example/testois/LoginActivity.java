package com.example.testois;
// PATH: LOGIN > VPTUTORIAL > DBINVENTORY > SEARCHINV > ADDINV OR UPDATEINV OR DELETEINV > DBORDER > SEARCHORD > ADDORD OR UPDATEORD OR DELETEORD > TRACKORD > SHIPSTAT
// ALT: LOGIN > VPTUTORIAL > TRACK ORD > SHIPSTAT > SETTINGS
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
    User user = new User();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        severinadb = new severinaDB(LoginActivity.this);
        severinadb.addUser(user.getName(), user.getPassword());

        btn1 = (Button) findViewById(R.id.btn1);
        btn2 = (Button) findViewById(R.id.btn2);
        edtuname = (EditText) findViewById(R.id.user);
        edtpword = (EditText) findViewById(R.id.pass);

        btn1.setOnClickListener(v -> {
            String name = edtuname.getText().toString();
            String password = edtpword.getText().toString();
            if (name.equals(user.getName()) && password.equals(user.getPassword())) {
                Toast.makeText(LoginActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
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

