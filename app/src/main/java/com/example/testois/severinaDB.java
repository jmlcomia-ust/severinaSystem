package com.example.testois;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class severinaDB extends SQLiteOpenHelper {
    private static final String DB_NAME="severinadb";
    private static final String TBL_1_NAME="user_db";
    private static final String TBL_2_NAME="inventory_db";
    private static final String TBL_3_NAME="orders_db";
    private static final String DB_PATH = "/data/data/com.example.testois/databases/";
    private static final int VER=1;
    private final Context context;
    static SQLiteDatabase db;

    public severinaDB(Context context) {
        super(context, DB_NAME, null, VER);
        this.context = context;
        createDb();
    }
    private void createDb() {
        boolean dbExist = checkDbExist();
        if(!dbExist){
            this.getReadableDatabase();
            copyDatabase();
        }
    }

    private void copyDatabase() {
        try {
            InputStream inputStream = context.getAssets().open(DB_NAME);

            String outFileName = DB_PATH + DB_NAME;

            OutputStream outputStream = new FileOutputStream(outFileName);

            byte[] b = new byte[1024];
            int length;

            while ((length = inputStream.read(b)) > 0){
                outputStream.write(b, 0, length);
            }

            outputStream.flush();
            outputStream.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private boolean checkDbExist() {
        SQLiteDatabase sqLiteDatabase = null;

        try{
            String path = DB_PATH + DB_NAME;
            sqLiteDatabase = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
        } catch (Exception ex){
        }

        if(sqLiteDatabase != null){
            sqLiteDatabase.close();
            return true;
        }

        return false;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TBL_1_NAME + "(id integer primary key, username text, password text)");
        ContentValues values = new ContentValues();
        values.put("username","owner.severina@gmail.com" );
        values.put("password", "owner.severina");
        long newRowId = db.insert(TBL_1_NAME, null, values);

        db.execSQL("create table " + TBL_2_NAME + "(id integer primary key, name text, quantity text, description text)");
        db.execSQL("create table " + TBL_3_NAME + "(id integer primary key, name text, subject text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
            createDb();
            db.execSQL("drop table if exists " + TBL_1_NAME + "");
            db.execSQL("drop table if exists " + TBL_2_NAME + "");
            db.execSQL("drop table if exists " + TBL_3_NAME + "");

    }

    private static SQLiteDatabase openDatabase(){
        String path = DB_PATH + DB_NAME;
        db = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READWRITE);
        return db;
    }

    //Operations for Users
   public static boolean checkUser(String username, String password){
        String[] columns = {"username"};
        SQLiteDatabase db = openDatabase();

        String selection = "username=? and password = ?";
        String[] selectionArgs = {username, password};

        Cursor cursor = db.query(TBL_1_NAME, columns, selection, selectionArgs, null, null, null);
        int count = cursor.getCount();

        cursor.close();
       db.close();

       return count > 0;
    }

}

