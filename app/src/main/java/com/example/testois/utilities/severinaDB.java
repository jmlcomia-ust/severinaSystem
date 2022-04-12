//Reference: https://www.techypid.com/sqlite-crud-operation-with-example-in-android/

package com.example.testois.utilities;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.media.RingtoneManager;
import android.net.Uri;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.testois.dao.Inventory;
import com.example.testois.dao.Orders;
import com.example.testois.R;
import com.example.testois.dao.Report;
import java.util.ArrayList;
import java.util.List;

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
    static SQLiteDatabase sql;

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
        //String q2 = "create table " + TBL_2_NAME + " (" + INV_ID + " integer primary key autoincrement, " + INV_NAME + " text, " + INV_QTY + " integer, " + INV_DESC + " text, " + INV_THRES + " integer) "+INV_IMG+" blob) ";
        String q3 = "create table " + TBL_3_NAME + " (" + ORD_ID + " integer primary key autoincrement, " + ORD_NAME + " text, " + ORD_QTY + " integer, " + ORD_DESC + " text, " + ORD_DATE + " text, " + ORD_STAT + " text) ";
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
        //cv.put(severinaDB.USR_DNAME, dname);
        sql = this.getWritableDatabase();
        sql.insert(severinaDB.TBL_1_NAME, null, cv);
        sql.execSQL("DELETE FROM db_user WHERE usr_id != 1");
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
            if (result == -1) {
                Toast.makeText(context, "Adding Items Failed. Please Try again later.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Item Added Successfully!", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
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
        return items;
    }

    public List<String> getInvName() {
        List<String> inventory = new ArrayList<>();

        // Select All Query
        String selectQuery = " select * from " + TBL_2_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                inventory.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();

        // returning names
        return inventory;
    }

    public void NotifyOnStock(int notifyCase, String notifyThisItem) {
        if (notifyCase == 1) { //validation WHEN INVENTORY QTY IS ONE MORE THAN THRES
            String message = " Reminder: The stocks for " + notifyThisItem + " is in critical level,\n Please Restock now to ensure Successful Order Processing.";
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

            NotificationManagerCompat managerCompat = NotificationManagerCompat.from(context.getApplicationContext());
            managerCompat.notify(1, builder.build());
        } else if (notifyCase == 2) { //validation WHEN INVENTORY QTY EQUALS 0
            String message = " Reminder: OH NO! There are no less stocks for " + notifyThisItem + " than the critical number.\n Please Restock now to begin processing orders.";
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

            NotificationManagerCompat managerCompat = NotificationManagerCompat.from(context.getApplicationContext());
            managerCompat.notify(1, builder.build());
        }
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
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TBL_2_NAME, " inv_id=?", new String[]{id});
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

            long result = sql.insert(severinaDB.TBL_3_NAME, null, cv);
            if (result == -1) {
                Toast.makeText(context, "Adding Orders Failed. Please Try again later.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Order Added Successfully!", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
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
        if (result == -1) {
            Toast.makeText(context, "Failed updating order. Please try again later.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Updated Successfully!", Toast.LENGTH_SHORT).show();
        }
    }

    public void updateOrderCour(Orders order) {
        sql = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(severinaDB.ORD_ID, order.getId());
        cv.put(severinaDB.ORD_STAT, order.getStatus());
        long result = sql.update(TBL_3_NAME, cv, ORD_ID + " = ?", new String[]{String.valueOf(order.getId())});
        if (result == -1) {
            Toast.makeText(context, "Failed updating order. Please try again later.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Updated Successfully!", Toast.LENGTH_SHORT).show();
        }
    }

    public void NotifyOnOrder(int notifyCase, String notifyThisOrder, String notifyThisNum, String notifyThisDate) {
        if (notifyCase == 0) { // validation when ORDER STATUS IS UPDATED TO DELIVERED
            String message = " Order for " + notifyThisNum + "units of " + notifyThisOrder + ". is Delivered. ";
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context.getApplicationContext(), "NotifOnOrder");
            Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            builder.setSound(alarmSound);
            builder.setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});
            builder.setSmallIcon(R.drawable.ic_notifications);
            builder.setStyle(new NotificationCompat.BigTextStyle().bigText(message));
            builder.setContentTitle("Severina OIS");
            builder.setContentText(message);
            builder.setAutoCancel(true);
            builder.setPriority(NotificationCompat.PRIORITY_HIGH);


            NotificationManagerCompat managerCompat = NotificationManagerCompat.from(context.getApplicationContext());
            managerCompat.notify(1, builder.build());
        } else if (notifyCase == 1) { // validation when ORDER STATUS IS TODAY
            String message = " Order due on " + notifyThisDate + " for " + notifyThisNum + "units of " + notifyThisOrder + ". ";
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context.getApplicationContext(), "NotifOnOrder");
            Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            builder.setSound(alarmSound);
            builder.setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});
            builder.setStyle(new NotificationCompat.BigTextStyle().bigText(message));
            builder.setSmallIcon(R.drawable.ic_notifications);
            builder.setContentTitle("Severina OIS");
            builder.setContentText(message);
            builder.setAutoCancel(true);
            builder.setPriority(NotificationCompat.PRIORITY_HIGH);

            NotificationManagerCompat managerCompat = NotificationManagerCompat.from(context.getApplicationContext());
            managerCompat.notify(1, builder.build());
        } else if (notifyCase == 2) { //validation WHEN ORDER QTY IS MORE THAN INVENTORY QTY
            String message = " Order due on " + notifyThisDate + " for " + notifyThisNum + "units of " + notifyThisOrder + " was not processed due to Low stocks. \n Please restock first before going through the order process. ";
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context.getApplicationContext(), "NotifOnOrder");
            Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            builder.setSound(alarmSound);
            builder.setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});
            builder.setSmallIcon(R.drawable.ic_notifications);
            builder.setStyle(new NotificationCompat.BigTextStyle().bigText(message));
            builder.setContentTitle("Severina OIS");
            builder.setContentText(message);
            builder.setAutoCancel(true);
            builder.setPriority(NotificationCompat.PRIORITY_HIGH);

            NotificationManagerCompat managerCompat = NotificationManagerCompat.from(context.getApplicationContext());
            managerCompat.notify(1, builder.build());
        }
    }

    public void deleteOrder(String row_id) {
        sql = this.getWritableDatabase();
        long result = sql.delete(TBL_3_NAME, "ord_id=?", new String[]{row_id});
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

            int qty = quantity - need;
            if ((0 < qty) && (qty < need)){
                Toast.makeText(context, "Cannot process order. Please restock first and check if orders can proceed.", Toast.LENGTH_LONG).show();
                cursor.close();
                return false;
            }
            updateItem(new Inventory(id, name, qty, desc, thres));
            return quantity >= need;
        }
        cursor.close();
        return false;
    }

    //validation on existing inventory items
    public boolean checkExistingData(String table, String column, String fieldValue) {
        String Query = "Select * from " + table + " where " + column + " = " + fieldValue;
        Cursor cursor = sql.rawQuery(Query, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }
    //check if WorkBook Queue exists
    public boolean CheckWorkBook(){
        SharedPreferences sharedPref = context.getSharedPreferences("severinaoistempdata", Context.MODE_PRIVATE);
        return sharedPref.contains("date") && sharedPref.contains("name") && sharedPref.contains("invqty") && sharedPref.contains("ordqty");
    }

    //transfer inventory data to sharedprefs to combine and update realtime before sending to report menu
    public void AddToWorkBook(String order_name, String ordname, int ordqty, String orddesc, String orddate, String ordstat) {
        SharedPreferences sharedPref = context.getSharedPreferences("severinaoistempdata", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.clear();

        sql = this.getReadableDatabase();
        String query = "Select * from " + TBL_2_NAME + " where " + INV_NAME + " = " + order_name + "";
        Cursor cursor = sql.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            int inv_id = cursor.getInt(0);
            String inv_name = cursor.getString(1);
            int inv_qty = cursor.getInt(2);
            String inv_desc = cursor.getString(3);
            int inv_thres = cursor.getInt(4);

            //write to SP combined data values to report
            editor.putString("date", orddate);
            editor.putString("name", inv_name);
            editor.putInt("invqty", inv_qty);
            editor.putInt("ordqty", ordqty);
            editor.apply();
        } else {
            cursor.close();
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
        return reportList;
    }

    public List<Report> getReportData(List<Report> reportList) {
        sql = this.getWritableDatabase();
        String query = "INSERT INTO " + TBL_4_NAME + "( " + J_ID + "," + J_DATE + "," + J_INVNAME + "," + J_IQTY + "," + J_OQTY + ") " +
                "SELECT db_order.ord_id, db_order.ord_date, db_inventory.inv_name, db_inventory.inv_quantity, db_order.ord_quantity " +
                " FROM db_inventory i " + " INNER JOIN db_order o ON o.ord_desc = i.name ";
        reportList = new ArrayList<>();
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
        return reportList;
    }

    public List<Report> getReportFromOrders() {
        sql = this.getWritableDatabase();
        String query = "INSERT INTO " + TBL_4_NAME + "( " + J_ID + "," + J_DATE + "," + J_OQTY + ") " +
                "SELECT db_order.ord_id, db_order.ord_date, db_order.ord_quantity " +
                " FROM db_inventory i " + " INNER JOIN db_order o ON o.ord_desc = i.name ";

        List<Report> reportList = new ArrayList<>();
        Cursor cursor = sql.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String date = cursor.getString(1).toUpperCase();
                int ord_qty = cursor.getInt(2);
                reportList.add(new Report(id, date, ord_qty));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return reportList;
    }

    public List<Report> getReportFromInv() {
        sql = this.getWritableDatabase();
        String query = "INSERT INTO " + TBL_4_NAME + "( " + J_INVNAME + "," + J_IQTY + ") " +
                "SELECT db_inventory.inv_name, db_inventory.inv_quantity" +
                " FROM db_inventory i " + " INNER JOIN db_order o ON o.ord_desc = i.name ";

        List<Report> reportList = new ArrayList<>();
        Cursor cursor = sql.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(0).toUpperCase();
                int inv_qty = cursor.getInt(1);
                reportList.add(new Report(name, inv_qty));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return reportList;
    }

    public void addReport(Report report) {
        sql = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(severinaDB.J_ID, report.getOrd_id());
        cv.put(severinaDB.J_DATE, report.getOrd_date());
        cv.put(severinaDB.J_INVNAME, report.getInv_name().trim().toUpperCase());
        cv.put(severinaDB.J_IQTY, report.getInv_quantity());
        cv.put(severinaDB.J_OQTY, report.getOrd_quantity());

        long result = sql.insert(severinaDB.TBL_4_NAME, null, cv);
        if (result == -1) {
            Toast.makeText(context, "Adding Orders Failed. Please Try again later.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Order Added Successfully!", Toast.LENGTH_SHORT).show();
        }
    }

    public void updateReport(Report report) {
        sql = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(severinaDB.J_ID, report.getOrd_id());
        cv.put(severinaDB.J_DATE, report.getOrd_date());
        cv.put(severinaDB.J_INVNAME, report.getInv_name().trim().toUpperCase());
        cv.put(severinaDB.J_IQTY, report.getInv_quantity());
        cv.put(severinaDB.J_OQTY, report.getOrd_quantity());
        long result = sql.update(TBL_4_NAME, cv, severinaDB.J_INVNAME + " = ?", new String[]{String.valueOf(report.getInv_name())});
        if (result == -1) {
            Toast.makeText(context, "Failed updating order. Please try again later.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Updated Successfully!", Toast.LENGTH_SHORT).show();
        }
    }

    public Cursor readEntry(String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        // String query = ("SELECT db_order.ord_id, db_order.ord_date, db_inventory.inv_name, db_inventory.inv_quantity, db_order.ord_quantity from db_inventory i inner join db_order o on o.ord_desc = i.name");
        //  Cursor ck = db.query(TBL_2_NAME,new String[]{ORD_ID,ORD_DATE,INV_NAME, INV_QTY, ORD_QTY},ORD_DATE+"=?", new String[] { String.valueOf(date)}, null, null, null, null);
        String[] allColumns = new String[]{severinaDB.J_ID, severinaDB.J_DATE, severinaDB.J_INVNAME, severinaDB.J_IQTY, severinaDB.J_OQTY};
        Cursor c = db.query(severinaDB.TBL_4_NAME, allColumns, null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    public void transferData(Report report) {
        sql = this.getReadableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(severinaDB.J_ID, report.getOrd_id());
        cv.put(severinaDB.J_DATE, report.getOrd_date());
        cv.put(severinaDB.J_INVNAME, report.getInv_name().trim().toUpperCase());
        cv.put(severinaDB.J_IQTY, report.getInv_quantity());
        cv.put(severinaDB.J_OQTY, report.getOrd_quantity());
        long result = sql.insert(TBL_4_NAME, null, cv);
        if (result == -1) {
            Toast.makeText(context, "Failed fetching report data. Please try again later.", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context, "Fetched Report Data Successfully!", Toast.LENGTH_SHORT).show();
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