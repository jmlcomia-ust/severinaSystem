package com.example.testois;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.testois.fragments.AddInventoryDiaFragment;

import java.io.ByteArrayOutputStream;

public class EditInventory extends AppCompatActivity {

    EditText ed1,ed2,ed3,ed4;
    Bitmap inv_gas_picu;
    Button btnBack, btnUpdate, btnDelete;
    Inventory inventory = null;
    severinaDB sev;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_inventory);
        final Object object = getIntent().getSerializableExtra("updates");
        if (object instanceof Inventory){
            inventory = (Inventory) object;
        }

        ed1 = findViewById(R.id.inv_id_txtu);  //ed1.setEnabled(false);
        ed2 = findViewById(R.id.inv_name_txtu); //ed2.setEnabled(false);
        ed3 = findViewById(R.id.inv_qty_txtu); //ed3.setEnabled(false);
        ed4 = findViewById(R.id.inv_desc_txtu); //ed4.setEnabled(false);
        //inv_gas_picu = findViewById(R.id.inv_gas_picu);

        if (inventory != null){
            ed1.setText(inventory.getId());
            ed2.setText(inventory.getName());
            ed3.setText(inventory.getQuantity());
            ed4.setText(inventory.getDescription());
            //inv_gas_picu.set(inventory.getImage()));
        }

        btnUpdate = findViewById(R.id.btnUpdate);
        btnDelete = findViewById(R.id.btnDelete);
        btnBack = findViewById(R.id.btnBack);
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


        ed2.setOnClickListener(v -> {
            ed2.setEnabled(true);ed1.setEnabled(false);ed3.setEnabled(false); ed4.setEnabled(false);
            ed2.requestFocus();
            severinaDB severinadb = new severinaDB(this);
            String name = ed2.getText().toString().trim();
            int quantity = Integer.parseInt(ed3.getText().toString().trim());
            String description = ed4.getText().toString().trim();
           // severinadb.updateInventory(ed1.getText().toString(),name, quantity, description);
        });

        ed3.setOnClickListener(v -> {
            ed1.setEnabled(false);ed2.setEnabled(false);ed3.setEnabled(true);ed4.setEnabled(false);
            ed3.requestFocus();
        });
        ed4.setOnClickListener(v -> {
            ed1.setEnabled(false);ed2.setEnabled(false);ed3.setEnabled(false);ed4.setEnabled(true);
            ed4.requestFocus();
        });

        btnDelete.setOnClickListener(v -> {
            Delete();
            Toast.makeText(getApplicationContext(),"Record Deleted",Toast.LENGTH_LONG).show();
        });


        btnBack.setOnClickListener(v -> {
            Intent i1 = new Intent(getApplicationContext(), ViewInventory.class);
            startActivity(i1);
            finish();
        });


        btnUpdate.setOnClickListener(v -> {
            Update();
            Toast.makeText(getApplicationContext(),"Record Updated",Toast.LENGTH_LONG).show();
            Intent i1 = new Intent(getApplicationContext(), ViewInventory.class);
            startActivity(i1);
            finish();
        });

    }
    public static byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }

    // convert from byte array to bitmap
    public static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
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
            severinaDB sev = new severinaDB(this);
            sev.deleteItem(id);
            ed1.setText("");
            ed2.setText("");
            ed3.setText("");
            ed4.setText("");

        }
        catch (Exception ex)
        {
            Toast.makeText(this,"Item Deletion Error.Please try again later.",Toast.LENGTH_LONG).show();
        }
    }

    public void Update()
    { String stringFilePath = Environment.getExternalStorageDirectory().getPath()+"/Download/"+ed2.getText().toString()+".jpeg";
        Bitmap bitmap = BitmapFactory.decodeFile(stringFilePath);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, byteArrayOutputStream);
        byte[] bytesImage = byteArrayOutputStream.toByteArray();
        try
        {
            String id = ed1.getText().toString();
            String name = ed2.getText().toString();
            String quantity = ed3.getText().toString();
            String description = ed4.getText().toString();
           // byte[] image = ;

            sev = new severinaDB(this);
            //sev.updateItem(new Inventory(id,name,quantity, description, image));

            ed1.setText("");
            ed2.setText("");
            ed3.setText("");
            ed1.requestFocus();

        }
        catch (Exception ex)
        {
            Toast.makeText(this,"Item Updating Error.Please try again later.",Toast.LENGTH_LONG).show();
        }

    }


}