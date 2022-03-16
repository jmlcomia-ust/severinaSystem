//Reference: https://www.techypid.com/sqlite-crud-operation-with-example-in-android/

package com.example.testois;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class severinaDB extends SQLiteOpenHelper {

    private static final String DB_NAME="severinadb";
    private static final String TBL_1_NAME="db_user";
    public static final String USR_ID = "id";
    public static final String USR_NAME = "username";
    public static final String USR_PWRD = "password";


    private static final String TBL_2_NAME="db_inventory";
    public static final String INV_ID = "id";
    public static final String INV_NAME = "name";
    public static final String INV_QTY = "quantity";
    public static final String INV_DESC = "description";
    public static final Bitmap INV_IMG = null;


    private static final String TBL_3_NAME="db_order";
    public static final String ORD_ID ="id";
    public static final String ORD_NAME = "name";
    public static final String ORD_QTY = "quantity";
    public static final String ORD_STAT = "status";

    private static final String DB_PATH = "/data/data/com.example.testois/databases/";
    private static final int VER=1;
    private final Context context;
    private ByteArrayOutputStream objectByteArray;
    private byte[] imageInBytes;
    static SQLiteDatabase db;


    public severinaDB(Context context) {
        super(context, DB_NAME, null, VER);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String q1 = "create table " + TBL_1_NAME + " (" + USR_ID + " integer primary key autoincrement, " + USR_NAME + " text, " + USR_PWRD + " text) ";
        //String q2 = "create table " + TBL_2_NAME + " (" + INV_ID + " integer primary key autoincrement, " + INV_NAME + " text, " + INV_QTY + " integer, " + INV_DESC + " text, " + INV_IMG + "blob) ";
        String q2 = "create table " + TBL_2_NAME + " (" + INV_ID + " integer primary key autoincrement, " + INV_NAME + " text, " + INV_QTY + " integer, " + INV_DESC + " text) ";
        String q3 = "create table " + TBL_3_NAME + " (" + ORD_ID + " integer primary key autoincrement, " + ORD_NAME + " text, " + ORD_QTY + " integer, " + ORD_STAT + " text) ";
        db.execSQL(q1);
        db.execSQL(q2);
        db.execSQL(q3);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
            db.execSQL("drop table if exists " + TBL_1_NAME + "");
            db.execSQL("drop table if exists " + TBL_2_NAME + "");
            db.execSQL("drop table if exists " + TBL_3_NAME + "");
            onCreate(db);

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

    private static SQLiteDatabase openDatabase(){
        String path = DB_PATH + DB_NAME;
        db = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READWRITE);
        return db;
    }
    //Add User
    void addUser(String name, String password){
        ContentValues cv = new ContentValues();
        cv.put(severinaDB.USR_NAME,name);
        cv.put(severinaDB.USR_PWRD, password);
        db = this.getWritableDatabase();
        db.insert(severinaDB.TBL_1_NAME, null,cv);
    }

    //ADD INVENTORY ITEM
    void addItem(Inventory inventory){
        try{
            db = this.getWritableDatabase();
            //byte[] inv_img = inventory.getImage();
            //objectByteArray=new ByteArrayOutputStream();
            //getImage(inv_img).compress(Bitmap.CompressFormat.JPEG, 100,objectByteArray);
            //imageInBytes=objectByteArray.toByteArray();
            ContentValues cv = new ContentValues();
            cv.put(severinaDB.INV_NAME, inventory.getName());
            cv.put(severinaDB.INV_QTY, inventory.getQuantity());
            cv.put(severinaDB.INV_DESC, inventory.getDescription());
            //cv.put("image", imageInBytes);
            long result = db.insert(severinaDB.TBL_2_NAME, null,cv);
            if(result != -1){ Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show(); db.close();}
            else { Toast.makeText(context, "Item Added Successfully!", Toast.LENGTH_SHORT).show(); }
        }catch(Exception e){Toast.makeText(context, "Table Addition error.", Toast.LENGTH_SHORT).show();}
    }


    //READ INVENTORY ITEMS
    public List<Inventory> getitemsList(){
        String query="SELECT * FROM " + TBL_2_NAME;
        db = this.getReadableDatabase();
        List<Inventory> items = new ArrayList<>();
        Cursor cursor = db.rawQuery(query,null);
        if(cursor.moveToFirst()){
            do{
                int id = Integer.parseInt(cursor.getString(0));
                String name = cursor.getString(1);
                String quantity = cursor.getString(2);
                String desc = cursor.getString(3);
                //byte[] image = cursor.getBlob(4);
               // items.add(new Inventory(String.valueOf(id),name,quantity, desc, image));
                items.add(new Inventory(String.valueOf(id),name,quantity, desc));
            }while(cursor.moveToNext());
        }
        cursor.close();
        return items;
    }

    //SELECTIVE READING INVENTORY ITEMS
    public Cursor retrieve(String searchterm){
        String[] columns={INV_ID, INV_NAME};
        Cursor c = null;
        if(searchterm != null && searchterm.length()>0){
            String query = "select * from "+TBL_2_NAME+" where "+INV_NAME+" LIKE '%"+searchterm+"%'";
            c=db.rawQuery(query,null);
            return c;
        }
        c=db.query(TBL_2_NAME,columns,null,null,null,null,null);
        return c;
    }
    public List<Inventory> getitemsByName(String searchname){
        String query="SELECT * FROM " + TBL_2_NAME + "WHERE " + INV_NAME + " LIKE '%" + searchname +"%'";
        db = this.getReadableDatabase();
        List<Inventory> searcheditems = new ArrayList<>();
        Cursor cursor = db.rawQuery(query,null);
        if(cursor.moveToFirst()){
            do{
                int id = Integer.parseInt(cursor.getString(0));
                String name = cursor.getString(1);
                String quantity = cursor.getString(2);
                String desc = cursor.getString(3);
                byte[] img = cursor.getBlob(4);
                searcheditems.add(new Inventory(String.valueOf(id),name,quantity, desc));
            }while(cursor.moveToNext());
        }
        cursor.close();
        return searcheditems;
    }

    public void updateItem(Inventory inventory){
        ContentValues cv = new ContentValues();
        cv.put(severinaDB.INV_NAME,inventory.getName());
        cv.put(severinaDB.INV_QTY,inventory.getQuantity());
        cv.put(severinaDB.INV_DESC,inventory.getDescription());
        db= this.getWritableDatabase();
        long result = db.update(TBL_2_NAME,cv,INV_ID + " = ?" , new String[] {String.valueOf(inventory.getId())});
        if(result == -1){
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context, "Item Updated Successfully!", Toast.LENGTH_SHORT).show();
        }
    }

    //search

    public void deleteItem(String row_id){
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TBL_2_NAME, "_id=?", new String[]{row_id});
        if(result == -1){
            Toast.makeText(context, "Failed to Delete.", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "Successfully Deleted.", Toast.LENGTH_SHORT).show();
        }
    }

    void deleteAllData(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TBL_2_NAME);
    }

    //Operations for Order Table Database
    public static boolean checkOrders(String name, int quantity, String description, String status){
        String[] columns = {"name"};
        SQLiteDatabase db = openDatabase();

        String selection = "name=? , quantity=?, description = ?, and status=?";
        String quant = String.valueOf(quantity);
        String[] selectionArgs = {name, quant, description, status};

        Cursor cursor = db.query(TBL_3_NAME, columns, selection, selectionArgs, null, null, null);
        int count = cursor.getCount();

        cursor.close();
        db.close();

        return count > 0;
    }
    void addOrder(Orders order){
        ContentValues cv = new ContentValues();
        cv.put(severinaDB.ORD_NAME, order.getName());
        cv.put(severinaDB.ORD_QTY, order.getQuantity());
        cv.put(severinaDB.ORD_STAT, order.getStatus());
        db = this.getWritableDatabase();
        long result = db.insert(severinaDB.TBL_3_NAME, null,cv);
        if(result == -1){
            Toast.makeText(context, "Adding Orders Failed", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context, "Order Added Successfully!", Toast.LENGTH_SHORT).show();
        }
    }

    //READ INVENTORY ITEMS
    public List<Orders> getOrderList(){
        String query="SELECT * FROM " + TBL_3_NAME;
        db = this.getReadableDatabase();
        List<Orders> orders = new ArrayList<>();
        Cursor cursor = db.rawQuery(query,null);
        if(cursor.moveToFirst()){
            do{
                int id = Integer.parseInt(cursor.getString(0));
                String name = cursor.getString(1);
                String quantity = cursor.getString(2);
                String stat = cursor.getString(3);
                orders.add(new Orders(String.valueOf(id),name,quantity, stat));
            }while(cursor.moveToNext());
        }
        cursor.close();
        return orders;
    }
    public void updateOrder(Orders orders){
        ContentValues cv = new ContentValues();
        cv.put(severinaDB.ORD_NAME,orders.getName());
        cv.put(severinaDB.ORD_QTY,orders.getQuantity());
        cv.put(severinaDB.ORD_STAT,orders.getStatus());
        db = this.getWritableDatabase();
        long result = db.update(TBL_3_NAME,cv,ORD_ID + " = ?" , new String[]{String.valueOf(orders.getId())});
        if(result == -1){
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context, "Updated Successfully!", Toast.LENGTH_SHORT).show();
        }
    }

    void deleteOneRowOrder(String row_id){
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TBL_3_NAME, "_id=?", new String[]{row_id});
        if(result == -1){
            Toast.makeText(context, "Failed to Delete.", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "Successfully Deleted.", Toast.LENGTH_SHORT).show();
        }
    }
    void deleteAllDataOrder(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TBL_3_NAME);
    }
}

