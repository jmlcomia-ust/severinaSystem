//Reference: https://www.techypid.com/sqlite-crud-operation-with-example-in-android/

package com.example.testois;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class severinaDB extends SQLiteOpenHelper {

    private static final String DB_NAME="severinadb";
    private static final String SQLITE_SEQUENCE = "0";
    private static final String TBL_1_NAME="db_user";
    public static final String USR_ID = "id";
    //public static final String USR_DNAME = "displayname";
    public static final String USR_NAME = "username";
    public static final String USR_PWRD = "password";

    private static final String TBL_2_NAME="db_inventory";
    public static final String INV_ID = "id";
    public static final String INV_NAME = "name";
    public static final String INV_QTY = "quantity";
    public static final String INV_DESC = "description";
    public static final String INV_THRES = "threshold";
    //public static final String INV_IMG = "image";

    private static final String TBL_3_NAME="db_order";
    public static final String ORD_ID ="id";
    public static final String ORD_NAME = "name";
    public static final String ORD_QTY = "quantity";
    public static final String ORD_DESC = "description";
    public static final String ORD_STAT = "status";

    private static final String DB_PATH = "/data/data/com.example.testois/databases/";
    private static final int VER=1;
    private final Context context;
    private ByteArrayOutputStream objectByteArray;
    private byte[] imageInBytes;
    static SQLiteDatabase sql;
    static severinaDB sev;

    public severinaDB(Context context) {
        super(context, DB_NAME, null, VER);
        this.context = context;
    }

    public severinaDB open() throws SQLException {
        sql = this.getWritableDatabase();
        return this;
    }

    public void close() {
        this.close();
    }

    @Override
    public void onCreate(SQLiteDatabase sql) {
        String q1 = "create table " + TBL_1_NAME + " (" + USR_ID + " integer primary key autoincrement, " + USR_NAME + " text, " + USR_PWRD + " text) ";
        String q2 = "create table " + TBL_2_NAME + " (" + INV_ID + " integer primary key autoincrement, " + INV_NAME + " text, " + INV_QTY + " integer, " + INV_DESC + " text, " + INV_THRES + " integer) ";
        //String q2 = "create table " + TBL_2_NAME + " (" + INV_ID + " integer primary key autoincrement, " + INV_NAME + " text, " + INV_QTY + " integer, " + INV_DESC + " text, " + INV_THRES + " integer) "+INV_IMG+" blob) ";
        String q3 = "create table " + TBL_3_NAME + " (" + ORD_ID + " integer primary key autoincrement, " + ORD_NAME + " text, " + ORD_QTY + " integer, " + ORD_DESC + " text, " + ORD_STAT + " text) ";

        sql.execSQL(q1);
        sql.execSQL(q2);
        sql.execSQL(q3);
        //sql.execSQL(q4);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sql, int i, int i1) {
        sql.execSQL("drop table if exists " + TBL_1_NAME + "");
        sql.execSQL("drop table if exists " + TBL_2_NAME + "");
        sql.execSQL("drop table if exists " + TBL_3_NAME + "");
        //sql.execSQL("drop table if exists " + TBL_4_NAME + "");
            onCreate(sql);

    }

    //DATABASE OPERATIONS FOR IMAGES IN DATABASE AND ACTIVITIES
/*
    //convert bitmap to byte[]
    public static byte[] getImageBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }
    //convert byte[] to Bitmap
    public static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

    //convert Bitmap to URI
    public static Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    //to resize image for faster uploading to db
    public static Bitmap decodeUri(Context c, Uri selectedImage, int REQUIRED_SIZE) {

        try {

            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(c.getContentResolver().openInputStream(selectedImage), null, o);

            // The new size we want to scale to
            // final int REQUIRED_SIZE =  size;

            // Find the correct scale value. It should be the power of 2.
            int width_tmp = o.outWidth, height_tmp = o.outHeight;
            int scale = 1;
            while (true) {
                if (width_tmp / 2 < REQUIRED_SIZE
                        || height_tmp / 2 < REQUIRED_SIZE) {
                    break;
                }
                width_tmp /= 2;
                height_tmp /= 2;
                scale *= 2;
            }

            // Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeStream(c.getContentResolver().openInputStream(selectedImage), null, o2);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    //convert ImageView to byte[]
    public byte[] ImageViewToByte(ImageView image){
        Bitmap bmp = ((BitmapDrawable) image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG,80,stream);
        return stream.toByteArray();
    }

    void loadImageFromDB(AppCompatImageView imgLoaded) {
        severinaDB db = new severinaDB(context.getApplicationContext());
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    db.open();
                   // final byte[] bytes = db.retreiveImageFromDB();
                    db.close();
                    // Show Image from DB in ImageView
                    imgLoaded.post(new Runnable() {
                        @Override
                        public void run() {
                     //       imgLoaded.setImageBitmap(severinaDB.getImage(bytes));

                        }
                    });
                } catch (Exception e) {
                    Log.e("Database", "<loadImageFromDB> Error : " + e.getLocalizedMessage());
                    db.close();
                }
            }
        }).start();
    }
 */
    private static SQLiteDatabase openDatabase(){
        String path = DB_PATH + DB_NAME;
        sql = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READWRITE);
        return sql;
    }

    //Add User
    void addUser(String name, String password){
        ContentValues cv = new ContentValues();
        cv.put(severinaDB.USR_NAME,name);
        cv.put(severinaDB.USR_PWRD, password);
        //cv.put(severinaDB.USR_DNAME, dname);
        sql = this.getWritableDatabase();
        sql.insert(severinaDB.TBL_1_NAME, null,cv);
    }
    void checkUser(String name){

    }

    //ADD INVENTORY ITEM
    void addItem(Inventory inventory){
        try{
            //for storing image to object inventory
            sql = this.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put(severinaDB.INV_NAME, inventory.getName());
            cv.put(severinaDB.INV_QTY, inventory.getQuantity());
            cv.put(severinaDB.INV_DESC, inventory.getDescription());
            cv.put(severinaDB.INV_THRES, inventory.getThreshold());
            //cv.put(severinaDB.INV_IMG,severinaDB.getImageBytes(inventory.getImage()));

            long result = sql.insert(severinaDB.TBL_2_NAME, null,cv);
            if(result != -1){ Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show(); sql.close();}
            else { Toast.makeText(context, "Item Added Successfully!", Toast.LENGTH_SHORT).show(); }
        }catch(Exception e){Toast.makeText(context, "Table Addition error.", Toast.LENGTH_SHORT).show();}
    }


    //READ INVENTORY ITEMS
    public List<Inventory> getitemsList(){
        String query="SELECT * FROM " + TBL_2_NAME;
        sql = this.getReadableDatabase();
        List<Inventory> items = new ArrayList<>();
        Cursor cursor = sql.rawQuery(query,null);

        if(cursor.moveToFirst()){
            do{
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                int quantity = cursor.getInt(2);
                String desc = cursor.getString(3);
                int thres = cursor.getInt(4);
                //byte[] imageInBytes = cursor.getBlob(5);
               //items.add(new Inventory(String.valueOf(id),name,Integer.parseInt(quantity), desc, Integer.parseInt(thres), severinaDB.getImage(imageInBytes)));
                items.add(new Inventory(id,name,quantity, desc, thres));
            }while(cursor.moveToNext());
        }
        cursor.close();
        return items;

    }
    public List<String> getInventoryItems(){
        sql=this.getReadableDatabase();
        String query = "select " + INV_NAME + " from " + TBL_2_NAME;
        List<String> items = new ArrayList<>();
        Cursor cursor = sql.rawQuery(query, null);
        int ctr = 0;
        if (cursor.moveToFirst()){
            do{
                items.add(ctr, cursor.getString(1));
                ctr+=1;
            }while(cursor.moveToNext());
        }
        cursor.close();
        sql.close();
        return items;
    }

    public void updateStock(String itemname, int qty_ordered){
        sql = this.getWritableDatabase();
        String query = "update "+TBL_2_NAME+" set quantity = quantity - "+qty_ordered+" where name = "+itemname+"";
        sql.execSQL(query);
    }

    public void updateItem(Inventory inventory){
        sql = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(severinaDB.INV_ID, inventory.getId());
        cv.put(severinaDB.INV_NAME,inventory.getName());
        cv.put(severinaDB.INV_QTY,inventory.getQuantity());
        cv.put(severinaDB.INV_DESC,inventory.getDescription());
        cv.put(severinaDB.INV_THRES,inventory.getThreshold());
       // cv.put(severinaDB.INV_IMG, inventory.getImage());
        long result = sql.update(TBL_2_NAME,cv,INV_ID + " = ?" , new String[] {String.valueOf(inventory.getId())});

        if(result == -1){
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context, "Item Updated Successfully!", Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteItem(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TBL_2_NAME, " id=?", new String[]{id});
        if(result == -1){
            Toast.makeText(context, "Failed to Delete.", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "Successfully Deleted.", Toast.LENGTH_SHORT).show();
        }
    }
    public void truncateItems(){
        SQLiteDatabase db = this.getWritableDatabase();
        String q3 = "update sqlite_sequence set seq = 0 where name='"+TBL_2_NAME+"'";
        db.execSQL(q3);
    }

    //Operations for Order Table Database
    void addOrder(Orders order){
        sql = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(severinaDB.ORD_NAME, order.getName().trim().toUpperCase());
        cv.put(severinaDB.ORD_QTY, order.getQuantity());
        cv.put(severinaDB.ORD_DESC, order.getDescription().trim().toUpperCase());
        cv.put(severinaDB.ORD_STAT, order.getStatus().trim().toUpperCase());

        long result = sql.insert(severinaDB.TBL_3_NAME, null,cv);
        if(result == -1){
            Toast.makeText(context, "Adding Orders Failed", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context, "Order Added Successfully!", Toast.LENGTH_SHORT).show();
        }
    }

    //READ INVENTORY ITEMS
    public List<Orders> getOrderList() {
            String query = "SELECT * FROM " + TBL_3_NAME;
        sql = this.getReadableDatabase();
        List<Orders> orders = new ArrayList<>();
            Cursor cursor = sql.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(0);
                    String name = cursor.getString(1).toUpperCase();
                    int quantity = cursor.getInt(2);
                    String desc = cursor.getString(3).toUpperCase();
                    String stat = cursor.getString(4).toUpperCase();
                    orders.add(new Orders(id, name, quantity, desc, stat));
                } while (cursor.moveToNext());
            }
            cursor.close();
            return orders;
    }

    public void updateOrder(Orders order){
        sql = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(severinaDB.ORD_ID, order.getId());
        cv.put(severinaDB.ORD_NAME, order.getName().trim().toUpperCase());
        cv.put(severinaDB.ORD_QTY, order.getQuantity());
        cv.put(severinaDB.ORD_DESC, order.getDescription().trim().toUpperCase());
        cv.put(severinaDB.ORD_STAT, order.getStatus().trim().toUpperCase());
        long result = sql.update(TBL_3_NAME,cv,ORD_ID + " = ?" , new String[]{String.valueOf(order.getId())});
        if(result == -1){
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context, "Updated Successfully!", Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteOrder(String row_id){
        sql = this.getWritableDatabase();
        long result = sql.delete(TBL_3_NAME, "id=?", new String[]{row_id});
        if(result == -1){
            Toast.makeText(context, "Failed to Delete.", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "Successfully Deleted.", Toast.LENGTH_SHORT).show();
        }
    }

    //OPERATIONS FOR STOCKS
    /*
    public void checkStocks(Orders orders, Inventory inventory){
        SQLiteDatabase db = this.getReadableDatabase();
        if (Integer.parseInt(orders.getQuantity()) > Integer.parseInt(inventory.getQuantity())){

        }
    }
     */
}

