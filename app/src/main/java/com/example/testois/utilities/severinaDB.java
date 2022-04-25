//Reference: https://www.techypid.com/sqlite-crud-operation-with-example-in-android/

package com.example.testois.utilities;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.testois.dao.Inventory;
import com.example.testois.dao.Orders;
import com.example.testois.R;
import com.example.testois.dao.Report;
import java.util.ArrayList;
import java.util.List;
@SuppressLint("all")
public class severinaDB extends SQLiteOpenHelper {

    private static final String DB_NAME = "severinadb";
    private static final String SQLITE_SEQUENCE = "0";
    private static final String TBL_1_NAME = "db_user";
    public static final String USR_ID = "usr_id";
    //public static final String USR_DNAME = "displayname";
    public static final String USR_NAME = "usr_name";
    public static final String USR_PWRD = "usr_pwd";

    private static final String TBL_2_NAME = "db_inventory";
    public static final String INV_ID = "inv_id";
    public static final String INV_NAME = "inv_name";
    public static final String INV_QTY = "inv_quantity";
    public static final String INV_DESC = "inv_description";
    public static final String INV_THRES = "inv_threshold";
    //public static final String INV_IMG = "image";

    private static final String TBL_3_NAME = "db_order";
    public static final String ORD_ID = "ord_id";
    public static final String ORD_NAME = "ord_name";
    public static final String ITMID = "inv_id";
    public static final String ORD_QTY = "ord_quantity";
    public static final String ORD_DESC = "ord_description";
    public static final String ORD_DATE = "ord_date";
    public static final String ORD_STAT = "ord_status";

    private static final String TBL_4_NAME = "db_report";
    private static final String J_ID = "joined_id";
    private static final String J_DATE = "joined_date";
    private static final String J_INVNAME = "joined_invname";
    private static final String J_IQTY = "joined_iqty";
    private static final String J_OQTY = "joined_oqty";

    private static final String DB_PATH = "/data/data/com.example.testois/databases/";
    private static final int VER = 1;
    private final Context context;
    String  inv_notif;
    public static SQLiteDatabase sql;
    private NotificationManagerCompat managerCompat;

    public severinaDB(Context context) {
        super(context, DB_NAME, null, VER);
        this.context = context;
    }

    public severinaDB open() throws SQLException {
        sql = this.getWritableDatabase();
        return this;
    }


    @Override
    public void onCreate(SQLiteDatabase sql) {
        String q1 = "create table " + TBL_1_NAME + " (" + USR_ID + " integer primary key autoincrement, " + USR_NAME + " text, " + USR_PWRD + " text) ";
        String q2 = "create table " + TBL_2_NAME + " (" + INV_ID + " integer primary key autoincrement, " + INV_NAME + " text, " + INV_QTY + " integer, " + INV_DESC + " text, " + INV_THRES + " integer) ";
        String q3 = "create table " + TBL_3_NAME + " (" + ORD_ID + " integer primary key autoincrement, " + ORD_NAME + " text, " + ORD_QTY + " integer, " + ORD_DESC + " text, " + ORD_DATE + " text, " + ORD_STAT + " text ) ";
        //String q3 = "create table " + TBL_3_NAME + " (" + ORD_ID + " integer primary key autoincrement, " + ORD_NAME + " text, " + ORD_QTY + " integer, " + ORD_DESC + " text, " + ORD_DATE + " text, " + ORD_STAT + " text, "+ ITMID + " integer, foreign key (inv_id) references \" + TBL_2_NAME + \" ( \" + INV_ID + \" )) ";
        String q4 = "create table " + TBL_4_NAME + " (" + J_ID + " integer primary key autoincrement, " + J_DATE + " integer, " + J_INVNAME + " text, " + J_IQTY + " integer," + J_OQTY + " integer) ";

        sql.execSQL(q1);
        sql.execSQL(q2);
        sql.execSQL(q3);
        sql.execSQL(q4);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sql, int i, int i1) {
        sql.execSQL("drop table if exists " + TBL_1_NAME + "");
        sql.execSQL("drop table if exists " + TBL_2_NAME + "");
        sql.execSQL("drop table if exists " + TBL_3_NAME + "");
        sql.execSQL("drop table if exists " + TBL_4_NAME + "");
        onCreate(sql);
    }

    private static SQLiteDatabase openDatabase() {
        String path = DB_PATH + DB_NAME;
        sql = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READWRITE);
        return sql;
    }

    //Add User
    public void addUser(String name, String password) {
        ContentValues cv = new ContentValues();
        cv.put(severinaDB.USR_NAME, name);
        cv.put(severinaDB.USR_PWRD, password);
        sql = this.getWritableDatabase();
        sql.insert(severinaDB.TBL_1_NAME, null, cv);
        sql.execSQL("DELETE FROM db_user WHERE usr_id != 1");
        sql.close();
    }

    //ADD INVENTORY ITEM
    public void addItem(Inventory inventory) {
        try {
            //for storing image to object inventory
            sql = this.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put(severinaDB.INV_NAME, inventory.getName());
            cv.put(severinaDB.INV_QTY, inventory.getQuantity());
            cv.put(severinaDB.INV_DESC, inventory.getDescription());
            cv.put(severinaDB.INV_THRES, inventory.getThreshold());
            //cv.put(severinaDB.INV_IMG,severinaDB.getImageBytes(inventory.getImage()));
            long result = sql.insert(severinaDB.TBL_2_NAME, null, cv);
            sql.close();
            if (result == -1) {
                Toast.makeText(context, "Adding Items Failed. Please Try again later.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Item Added Successfully!", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            sql.close();
            Toast.makeText(context, "Table Addition error.", Toast.LENGTH_SHORT).show();
        }
    }

    //READ INVENTORY ITEMS
    public List<Inventory> getitemsList() {
        String query = "SELECT * FROM " + TBL_2_NAME;
        sql = this.getReadableDatabase();
        List<Inventory> items = new ArrayList<>();
        Cursor cursor = sql.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                int quantity = cursor.getInt(2);
                String desc = cursor.getString(3);
                int thres = cursor.getInt(4);
                //byte[] imageInBytes = cursor.getBlob(5);
                //items.add(new Inventory(String.valueOf(id),name,Integer.parseInt(quantity), desc, Integer.parseInt(thres), severinaDB.getImage(imageInBytes)));
                items.add(new Inventory(id, name, quantity, desc, thres));
            } while (cursor.moveToNext());
        }
        cursor.close();
        sql.close();
        return items;
    }

    public List<String> getInvName() {
        List<String> inventory = new ArrayList<>();

        // Select All Query
        String selectQuery = " select * from " + TBL_2_NAME;
        sql = this.getReadableDatabase();
        Cursor cursor = sql.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                inventory.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        sql.close();

        // returning names
        return inventory;
    }


    public void NotifyOnStock(int notifyCase, String notifyThisItem) {
        if (notifyCase == 1) { //validation WHEN INVENTORY QTY IS ONE MORE THAN THRES
            String message = " Reminder: The stocks for " + notifyThisItem + " is in critical level. Please Restock now to ensure Successful Order Processing.";
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context.getApplicationContext(), "NotifOnStock");
            Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            builder.setSound(alarmSound);
            builder.setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});
            builder.setSmallIcon(R.drawable.ic_notifications);
            builder.setContentTitle("Severina OIS");
            builder.setStyle(new NotificationCompat.BigTextStyle().bigText(message));
            builder.setContentText(message);
            builder.setAutoCancel(true);
            builder.setPriority(NotificationCompat.PRIORITY_HIGH);

           managerCompat = NotificationManagerCompat.from(context.getApplicationContext());
            managerCompat.notify(1, builder.build());
        } else if (notifyCase == 2) { //validation WHEN INVENTORY QTY EQUALS 0
            String message = " Reminder: OH NO! Stocks for " + notifyThisItem + " are less than the critical number.\n Please Restock now to begin processing orders.";
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context.getApplicationContext(), "NotifOnStock");
            Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            builder.setSmallIcon(R.drawable.ic_notifications);
            builder.setContentTitle("Severina OIS");
            builder.setStyle(new NotificationCompat.BigTextStyle().bigText(message));
            builder.setContentText(message);
            builder.setAutoCancel(true);
            builder.setSound(alarmSound);
            builder.setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});
            builder.setPriority(NotificationCompat.PRIORITY_HIGH);

            managerCompat = NotificationManagerCompat.from(context.getApplicationContext());
            managerCompat.notify(1, builder.build());
        }
        else if (notifyCase == 3){Log.e("Pass Through", "no need for notifs");}
    }

    public void updateItem(Inventory inventory) {
        try {
            sql = this.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put(severinaDB.INV_ID, inventory.getId());
            cv.put(severinaDB.INV_NAME, inventory.getName());
            cv.put(severinaDB.INV_QTY, inventory.getQuantity());
            cv.put(severinaDB.INV_DESC, inventory.getDescription());
            cv.put(severinaDB.INV_THRES, inventory.getThreshold());
            // cv.put(severinaDB.INV_IMG, inventory.getImage());
            long result = sql.update(TBL_2_NAME, cv, INV_ID + " = ?", new String[]{String.valueOf(inventory.getId())});
            sql.close();
            if (result == -1) {
                Toast.makeText(context, "Failed updating item.Please Try again Later.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, " Item Updated Successfully!", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteItem(String id) {
        sql = this.getWritableDatabase();
        long result = sql.delete(TBL_2_NAME, " inv_id=?", new String[]{id});
        sql.close();
        if (result == -1) {
            Toast.makeText(context, "Failed to Delete. Please Try Again later.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Successfully Deleted.", Toast.LENGTH_SHORT).show();
        }
    }

    //Operations for Order Table Database
    public void addOrder(Orders order) {
        try {
            sql = this.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put(severinaDB.ORD_NAME, order.getName().trim().toUpperCase());
            cv.put(severinaDB.ORD_QTY, order.getQuantity());
            cv.put(severinaDB.ORD_DESC, order.getDescription().trim().toUpperCase());
            cv.put(severinaDB.ORD_DATE, order.getDate());
            cv.put(severinaDB.ORD_STAT, order.getStatus().trim().toUpperCase());
           // cv.put(severinaDB.ITMID, order.getStatus().trim().toUpperCase());

            long result = sql.insert(severinaDB.TBL_3_NAME, null, cv);
            sql.close();
            if (result == -1) {
                Toast.makeText(context, "Adding Orders Failed. Please Try again later.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Order Added Successfully!", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            sql.close();
            Toast.makeText(context, "Table Addition error.Please Try again later.", Toast.LENGTH_SHORT).show();
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
                String name = cursor.getString(1);
                int quantity = cursor.getInt(2);
                String desc = cursor.getString(3);
                String date = cursor.getString(4);
                String stat = cursor.getString(5);
                orders.add(new Orders(id, name, quantity, desc, date, stat));
            } while (cursor.moveToNext());
        }
        cursor.close();
        sql.close();
        return orders;
    }

    public void updateOrder(Orders order) {
        sql = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(severinaDB.ORD_ID, order.getId());
        cv.put(severinaDB.ORD_NAME, order.getName());
        cv.put(severinaDB.ORD_QTY, order.getQuantity());
        cv.put(severinaDB.ORD_DESC, order.getDescription());
        cv.put(severinaDB.ORD_DATE, order.getDate());
        cv.put(severinaDB.ORD_STAT, order.getStatus());
        long result = sql.update(TBL_3_NAME, cv, ORD_ID + " = ?", new String[]{String.valueOf(order.getId())});
        sql.close();
        if (result == -1) {
            Toast.makeText(context, "Failed updating order. Please try again later.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Updated Successfully!", Toast.LENGTH_SHORT).show();
        }
    }
    //for numcase
    public int getCase(int qty, int thres){
        if(qty == 0) {
            return 1;
        }
        else if(qty == (thres+1) || qty == thres){
            return 2;
        }
        else if (qty < thres && qty > 0){
            return 3;
        }
        else {
            return 4;
        }
    }

    public void NotifyOnOrder(int notifyCase, String notifyThisOrder, String notifyThisNum, String notifyThisDate, int numCase, String notifyThisItem, String stat) {
        String message;
        if (notifyCase == 0) { // validation of acknowledged order
            if (numCase == 1){ inv_notif = " Reminder: OH NO! There are no more stocks for " + notifyThisItem + ". Please restock now to begin processing orders."; }
            if (numCase == 2){ inv_notif = " Reminder: The stocks for " + notifyThisItem + " is in critical level. Please restock now to ensure Successful Order Processing."; }
            else if (numCase == 3){ inv_notif = " Reminder: OH NO! Stocks for " + notifyThisItem + " are less than the critical number. Please restock now to begin processing orders."; }
            else {inv_notif = "";}

            if(stat.equalsIgnoreCase("To Deliver")){
                message =  " Order due on " + notifyThisDate + " for " + notifyThisNum + " units of " + notifyThisOrder + ". ";
            }
            else{
                message = " Order for " + notifyThisNum + " units of " + notifyThisOrder + ". is Delivered. ";
            }

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context.getApplicationContext(), "NotifOnOrder");
            Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            builder.setSound(alarmSound);
            builder.setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});
            builder.setSmallIcon(R.drawable.ic_notifications);
            builder.setStyle(new NotificationCompat.BigTextStyle().bigText(message + "\n\n" + inv_notif));
            builder.setContentTitle("Severina OIS");
            builder.setContentText(message);
            builder.setContentText(inv_notif);
            builder.setAutoCancel(true);
            builder.setPriority(NotificationCompat.PRIORITY_HIGH);


            managerCompat = NotificationManagerCompat.from(context.getApplicationContext());
            managerCompat.notify(1, builder.build());
        }
        else if (notifyCase == 1) { // validation of critical level
            if (numCase == 1){ inv_notif = " Reminder: OH NO! There are no more stocks for " + notifyThisItem + ". Please restock now to begin processing orders."; }
            if (numCase == 2){ inv_notif = " Reminder: The stocks for " + notifyThisItem + " is in critical level. Please restock now to ensure Successful Order Processing."; }
            else if (numCase == 3){ inv_notif = " Reminder: OH NO! Stocks for " + notifyThisItem + " are less than the critical number. Please restock now to begin processing orders."; }
            else {inv_notif = "";}

            if(stat.equalsIgnoreCase("To Deliver")){
                message =  " Order due on " + notifyThisDate + " for " + notifyThisNum + " units of " + notifyThisOrder + ". ";
            }
            else{
                message = " Order for " + notifyThisNum + " units of " + notifyThisOrder + ". is Delivered. ";
            }
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context.getApplicationContext(), "NotifOnOrder");
            Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            builder.setSound(alarmSound);
            builder.setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});
            builder.setStyle(new NotificationCompat.BigTextStyle().bigText(message + "\n\n" + inv_notif));
            builder.setSmallIcon(R.drawable.ic_notifications);
            builder.setContentTitle("Severina OIS");
            builder.setContentText(message);
            builder.setAutoCancel(true);
            builder.setPriority(NotificationCompat.PRIORITY_HIGH);

            managerCompat = NotificationManagerCompat.from(context.getApplicationContext());
            managerCompat.notify(1, builder.build());
        }
    }

    public void deleteOrder(String row_id) {
        sql = this.getWritableDatabase();
        long result = sql.delete(TBL_3_NAME, "ord_id=?", new String[]{row_id});
        sql.close();
        if (result == -1) {
            Toast.makeText(context, "Failed to Delete.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Successfully Deleted.", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean checkAvailability(String value, int need) {
        sql = this.getReadableDatabase();
        //String query = "Select "+ INV_QTY +" from "+TBL_2_NAME+" where "+INV_NAME+" = '"+value.toUpperCase()+"'";
        String query = "Select * from " + TBL_2_NAME + " where " + INV_NAME + " = " + value;
        Cursor cursor = sql.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            int quantity = cursor.getInt(2);
            String desc = cursor.getString(3);
            int thres = cursor.getInt(4);

            if ((quantity < 0) && (quantity < need)) {
                cursor.close();
                sql.close();
                return false;
            }
            else if (quantity >= need){
                int qty = quantity - need;
                ContentValues cv = new ContentValues();
                cv.put(severinaDB.INV_ID, id);
                cv.put(severinaDB.INV_NAME, name);
                cv.put(severinaDB.INV_QTY, qty);
                cv.put(severinaDB.INV_DESC, desc);
                cv.put(severinaDB.INV_THRES, thres);
                sql.update(TBL_2_NAME,cv, INV_ID + " = ?", new String[]{String.valueOf(id)});
                sql.close();
                return true;
            }

        }
        cursor.close();
        return false;
    }

    //validation on existing inventory items
    public boolean checkExistingData(String table, String column, String fieldValue) {
        sql = this.getReadableDatabase();
        String Query = "Select * from " + table + " where " + column + " = " + fieldValue;
        Cursor cursor = sql.rawQuery(Query, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            sql.close();
            return false;
        }
        cursor.close();
        sql.close();
        return true;
    }

    //check if WorkBook Queue exists
    public boolean CheckWorkBook() {
        SharedPreferences sharedPref = context.getSharedPreferences("sevois_owntempdata", Context.MODE_PRIVATE);
        return sharedPref.contains("date") && sharedPref.contains("name") && sharedPref.contains("invqty") && sharedPref.contains("ordqty");
    }

   /* //transfer inventory data to sharedprefs to combine and update realtime before sending to report menu
    public void dataJoined() {
        sql = this.getReadableDatabase();
        String query = "Select " + " o. " + ORD_DATE + " ,o. " + ORD_DESC + " ,o. " + ORD_QTY + " ,i.  " + INV_QTY + " from " + TBL_3_NAME + " o " + " join " + TBL_2_NAME + " i " + " on i." + ITMID + " = o." + ITMID;

        Cursor cursor = sql.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            String orddate = cursor.getString(0);
            String orddesc = cursor.getString(1);
            int inv_qty = cursor.getInt(2);
            //String inv_desc = cursor.getString(3);
            int inv_thres = cursor.getInt(4);

            //write to SP combined data values to report
            editor.putString("date", orddate);
            editor.putString("name", inv_name);
            editor.putInt("invqty", inv_qty);
            editor.putInt("ordqty", ordqty);
            //add inventory data specifics for notifs
            editor.putInt("invthres", inv_thres);
            editor.apply();
            sql.close();
        } else {
            cursor.close();
            sql.close();
        }
    }
    
    */

    //transfer inventory data to sharedprefs to combine and update realtime before sending to report menu
    public void AddToWorkBook(String order_name, int ordqty, String orddate) {
        SharedPreferences sharedPref = context.getSharedPreferences("sevois_owntempdata", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.clear();

        sql = this.getReadableDatabase();
        String query = "Select * from " + TBL_2_NAME + " where " + INV_NAME + " = " + order_name + "";
        Cursor cursor = sql.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            //int inv_id = cursor.getInt(0);
            String inv_name = cursor.getString(1);
            int inv_qty = cursor.getInt(2);
            //String inv_desc = cursor.getString(3);
            int inv_thres = cursor.getInt(4);

            //write to SP combined data values to report
            editor.putString("date", orddate);
            editor.putString("name", inv_name);
            editor.putInt("invqty", inv_qty);
            editor.putInt("ordqty", ordqty);
            //add inventory data specifics for notifs
            editor.putInt("invthres", inv_thres);
            editor.apply();
            sql.close();
        } else {
            cursor.close();
            sql.close();
        }
    }

    // String query = "Select * from " + TBL_3_NAME + " where " + ORD_ID + " = '" + ord_id + "' AND ORD_STAT = 'TO DELIVER'"  ;

    public void CourAddToWorkBook(int ord_id, String ord_stat) {
        SharedPreferences sharedPref = context.getSharedPreferences("sevois_coutempdata", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.clear();

        //get whole order dataset of individual to-update data
        sql = this.getWritableDatabase();
        String query = "Select * from " + TBL_3_NAME + " where " + ORD_ID + " = '" + ord_id + "'";
        Log.e("Start Query1","Starting query 1");
        Cursor cursor = sql.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            //int ord_id = cursor.getInt(0);
            String ord_name = cursor.getString(1);
            int ord_qty = cursor.getInt(2);
            String ord_desc = cursor.getString(3);
            String ord_date = cursor.getString(4);
            //String ord_stat = cursor.getString(5);

            ContentValues cv = new ContentValues();
            cv.put(severinaDB.ORD_ID, ord_id);
            cv.put(severinaDB.ORD_NAME, ord_name);
            cv.put(severinaDB.ORD_QTY, ord_qty);
            cv.put(severinaDB.ORD_DESC, ord_desc);
            cv.put(severinaDB.ORD_DATE, ord_date);
            cv.put(severinaDB.ORD_STAT, ord_stat);

            editor.putInt("ordid", ord_id);
            editor.putString("ordname", ord_name);
            editor.putInt("ordqty", ord_qty);
            editor.putString("orddesc", ord_desc);
            editor.putString("orddate", ord_date);
            editor.putString("orddate", ord_stat);
            Log.e("SP pt1 done", "order data set applied to SP.");
            sql.update(TBL_3_NAME, cv, ORD_ID + " = ?", new String[]{String.valueOf(ord_id)});
            Log.e("Order Update Complete","Done updating Order using ID and Stat");
            cursor.close();

            String query2 = "Select * from " + TBL_2_NAME + " where " + INV_NAME + " = '" + ord_name + "'";
            Log.e("Start Query 2","Starting query 2");
            Cursor cursor2 = sql.rawQuery(query2, null);
            if (cursor2.moveToFirst()) {
                //int inv_id = cursor.getInt(0);
                String inv_name = cursor.getString(1);
                int inv_qty = cursor.getInt(2);
                //String inv_desc = cursor.getString(3);
                int inv_thres = cursor.getInt(4);

                //write to SP combined data values to report
                editor.putString("date", ord_date);
                editor.putString("name", inv_name);
                editor.putInt("invqty", inv_qty);
                editor.putInt("ordqty", ord_qty);

                //add inventory data specifics for notifs
                editor.putInt("invthres", inv_thres);


                editor.apply();
                Log.e("Order To SP","Done applying order to SP.");
                sql.close();

            } else {
                Log.e("Error in Q2", "check query 2.");
                cursor.close();
                sql.close();
            }
        } else {
            Log.e("Error in Q1", "check query 1.");
            cursor.close();
            sql.close();
        }
    }

    public List<Report> getReportList() {
        sql = this.getWritableDatabase();
        String query = "SELECT * FROM " + TBL_4_NAME;
        List<Report> reportList = new ArrayList<>();
        Cursor cursor = sql.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String date = cursor.getString(1).toUpperCase();
                String name = cursor.getString(2).toUpperCase();
                int inv_qty = cursor.getInt(3);
                int ord_qty = cursor.getInt(4);
                reportList.add(new Report(id, date, name, inv_qty, ord_qty));
            } while (cursor.moveToNext());
        }
        cursor.close();
        sql.close();
        return reportList;
    }

    public void addReport(Report report) {
        try{
            sql = this.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put(severinaDB.J_DATE, report.getOrd_date());
            cv.put(severinaDB.J_INVNAME, report.getInv_name().trim().toUpperCase());
            cv.put(severinaDB.J_IQTY, report.getInv_quantity());
            cv.put(severinaDB.J_OQTY, report.getOrd_quantity());
            sql.insert(severinaDB.TBL_4_NAME, null, cv);
            sql.close();
        }catch(Exception e){e.printStackTrace();}

    }

    public void updateReport(Report report) {
        sql = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(severinaDB.J_ID, report.getOrd_id());
        cv.put(severinaDB.J_DATE, report.getOrd_date());
        cv.put(severinaDB.J_INVNAME, report.getInv_name().trim().toUpperCase());
        cv.put(severinaDB.J_IQTY, report.getInv_quantity());
        cv.put(severinaDB.J_OQTY, report.getOrd_quantity());
        long result = sql.update(TBL_4_NAME, cv, severinaDB.J_ID + " = ?" + " AND "+ severinaDB.J_INVNAME + " = ?", new String[]{"'"+report.getOrd_id()+"'", "'"+report.getInv_name()+"'"});
        sql.close();
        if (result == -1) {
            Toast.makeText(context, "Failed updating order. Please try again later.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Updated Successfully!", Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteReports(){
        sql = this.getWritableDatabase();
        sql.delete(TBL_4_NAME,null,null);
        sql.close();
    }

    public boolean isSDpresent() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
           return true;
        } else {
           return false;
        }
    }

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