package com.example.testois;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    EditText ed1, ed2;
    Button btn1, btn2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        SQLiteDatabase db = openOrCreateDatabase("oisdb", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS user(id INTEGER PRIMARY KEY AUTOINCREMENT,user VARCHAR,pass VARCHAR )");
        String sql = "insert into user(user,pass)values(?,?)";
        SQLiteStatement statement = db.compileStatement(sql);
        statement.bindString(1, "owner.severina@gmail.com");
        statement.bindString(2, "owner.severina");
        statement.execute();
        ed1 = findViewById(R.id.user);
        ed2 = findViewById(R.id.pass);
        btn1 = findViewById(R.id.btn1);
        btn2 = findViewById(R.id.btn2);
        btn2.setOnClickListener(v -> passby()); //change to reset password function
        btn1.setOnClickListener(v -> login());
    }

    public void login() {
        String user = ed1.getText().toString();
        String pass = ed2.getText().toString();
        if (user.equals("") || pass.equals("")) {
            Toast.makeText(this, "Username or Password is blank", Toast.LENGTH_LONG).show();
        } else if (null != checkUser(user, pass)) {
            String userFromDb = checkUser(user, pass);

            Intent i = new Intent(getApplicationContext(), AddInventory.class);
            i.putExtra("uname", userFromDb);
            startActivity(i);
        } else {
            Toast.makeText(this, "Username or Password does not match", Toast.LENGTH_LONG).show();
            ed1.setText("");
            ed2.setText("");
            ed1.requestFocus();
        }
    }

    public  void  passby() {
        Intent i = new Intent(getApplicationContext(), DashboardOrders.class);
        startActivity(i);
        Toast.makeText(this, "Should go to Summary of Orders seen by courier", Toast.LENGTH_SHORT).show();
    }

    public String checkUser(String name, String pass)
    {
        SQLiteDatabase db = openOrCreateDatabase("oisdb", Context.MODE_PRIVATE, null);
        Cursor cursor=db.rawQuery("SELECT id,user,pass FROM user WHERE user=? AND pass=?",new String[]{name,pass});
        if(cursor.getCount()>0) {
            cursor.moveToFirst();
            String username = cursor.getString(1);
            String password = cursor.getString(2);
            SharedPreferences.Editor sp = getSharedPreferences("username", MODE_PRIVATE).edit();
            sp.putString("uname", username);
            sp.apply();
            cursor.close();
            return username;
        }
        return null;
    }}
