package com.example.testois;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.testois.fragments.AddInventoryDiaFragment;

public class EditInventory extends AppCompatActivity {

    EditText ed1,ed2,ed3,ed4;
    Button b1,b2,b3,bed2, bed3, bed4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_inventory);

/*
        ed1 = findViewById(R.id.id); ed1.setEnabled(false);
        ed2 = findViewById(R.id.name);ed2.setEnabled(false);
        ed3 = findViewById(R.id.quantity);ed3.setEnabled(false);
        ed4 = findViewById(R.id.description);ed4.setEnabled(false);

        bed2 = findViewById(R.id.bed2);
        bed3 = findViewById(R.id.bed3);
        bed4 = findViewById(R.id.bed4);

        b1 = findViewById(R.id.bt1);
        b2 = findViewById(R.id.bt2);
        b3 = findViewById(R.id.bt3);

*/
        Intent i = getIntent();

        String t1 = i.getStringExtra("id");
        String t2 = i.getStringExtra("name");
        String t3 = i.getStringExtra("quantity");
        String t4 = i.getStringExtra("description");

        ed1.setText(t1);
        ed2.setText(t2);
        ed3.setText(t3);
        ed4.setText(t4);


        bed2.setOnClickListener(v -> {
            ed2.setEnabled(true);ed1.setEnabled(false);ed3.setEnabled(false); ed4.setEnabled(false);
            ed2.requestFocus();
            severinaDB severinadb = new severinaDB(this);
            String name = ed2.getText().toString().trim();
            int quantity = Integer.parseInt(ed3.getText().toString().trim());
            String description = ed4.getText().toString().trim();
           // severinadb.updateInventory(ed1.getText().toString(),name, quantity, description);
        });

        bed3.setOnClickListener(v -> {
            ed1.setEnabled(false);ed2.setEnabled(false);ed3.setEnabled(true);ed4.setEnabled(false);
            ed3.requestFocus();
        });
        bed4.setOnClickListener(v -> {
            ed1.setEnabled(false);ed2.setEnabled(false);ed3.setEnabled(false);ed4.setEnabled(true);
            ed4.requestFocus();
        });

        b2.setOnClickListener(v -> {
            Delete();
            Toast.makeText(getApplicationContext(),"Record Deleted",Toast.LENGTH_LONG).show();
            Intent i1 = new Intent(getApplicationContext(), ViewInventory.class);
            startActivity(i1);
            finish();
        });


        b3.setOnClickListener(v -> {
            Intent i1 = new Intent(getApplicationContext(), ViewInventory.class);
            startActivity(i1);
            finish();
        });




        b1.setOnClickListener(v -> {
            Update();
            Toast.makeText(getApplicationContext(),"Record Updated",Toast.LENGTH_LONG).show();
            Intent i1 = new Intent(getApplicationContext(), ViewInventory.class);
            startActivity(i1);
            finish();
        });

    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(getApplicationContext(), AddInventoryDiaFragment.class);
        startActivity(i);
        finish();
        //super.onBackPressed();
    }

    public void Delete()
    {
        try
        {
            String id = ed1.getText().toString();

            SQLiteDatabase db = openOrCreateDatabase("SliteDb", Context.MODE_PRIVATE,null);


            String sql = "delete from records where id = ?";
            SQLiteStatement statement = db.compileStatement(sql);
            statement.bindString(1,id);
            statement.execute();

            ed1.setText("");
            ed2.setText("");
            ed3.setText("");
            ed4.setText("");


        }
        catch (Exception ex)
        {
            Toast.makeText(this,"Record Fail",Toast.LENGTH_LONG).show();
        }
    }

    public void Update()
    {
        try
        {
            String id = ed1.getText().toString();
            String name = ed2.getText().toString();
            String quantity = ed3.getText().toString();
            String description = ed4.getText().toString();

            SQLiteDatabase db = openOrCreateDatabase("SliteDb",Context.MODE_PRIVATE,null);

            String sql = "update records set name = ?,quantity=?,description=? where id= ?";
            SQLiteStatement statement = db.compileStatement(sql);
            statement.bindString(1,name);
            statement.bindString(2,quantity);
            statement.bindString(3,description);
            statement.bindString(4,id);
            statement.execute();

            ed1.setText("");
            ed2.setText("");
            ed3.setText("");
            ed1.requestFocus();

        }
        catch (Exception ex)
        {
            Toast.makeText(this,"Record Fail",Toast.LENGTH_LONG).show();
        }

    }


}