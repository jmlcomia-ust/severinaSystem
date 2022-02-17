package com.example.testois;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class AddInventory extends AppCompatActivity {
    EditText ed1,ed2,ed3;
    Button b1,b2;

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
            Intent i = new Intent(getApplicationContext(), ViewInventory.class);
            startActivity(i);
        });
        b1.setOnClickListener(v -> insert());
    }

    public void insert()
    {
        try
        {
            String name = ed1.getText().toString();
            String quantity = ed2.getText().toString();
            String description = ed3.getText().toString();

            SQLiteDatabase db = openOrCreateDatabase("SliteDb", Context.MODE_PRIVATE,null);
            db.execSQL("CREATE TABLE IF NOT EXISTS records(id INTEGER PRIMARY KEY AUTOINCREMENT,name VARCHAR,quantity VARCHAR,description VARCHAR)");

            String sql = "insert into records(name,quantity,description)values(?,?,?)";
            SQLiteStatement statement = db.compileStatement(sql);
            statement.bindString(1,name);
            statement.bindString(2,quantity);
            statement.bindString(3,description);
            statement.execute();
            Toast.makeText(this,"Record added",Toast.LENGTH_LONG).show();

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
/*
        //display data
        btnView.setOnClickListener(view -> startActivity(new Intent(AddInventory.this, ViewInventory.class)));

        //edit data
        /*
        edit.setOnClickListener(new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            ContentValues contentValues=new ContentValues();
            contentValues.put("name", edtname.getText().toString());
            db=dbInventory.getWritableDatabase();
            long=db.update("thing", contentValues, "id", null);
            if(recid!=-1){
                Toast.makeText(AddInventory.this, "Data Updated Successfully", Toast.LENGTH_SHORT).show();
                //when successful edit occurs
                update.setVisibility(View.VISIBLE);
                edit.setVisibility(View.GONE);
                clear();
            }else{
                Toast.makeText(AddInventory.this, "something wrong try again", Toast.LENGTH_SHORT).show();

            }

        }
        });
        */
