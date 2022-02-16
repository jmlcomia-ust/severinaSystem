package com.example.testois;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBInventory extends SQLiteOpenHelper {
    public static final String DB_NAME="inventorydb";
    public static final String TBL_NAME="inventory";
    public static final int VER=1;

    public DBInventory(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DB_NAME, null, VER);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query="create table " + TBL_NAME + "(id integer primary key, name text, subject text)";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        String query="drop table if exists "+ TBL_NAME+"";
        db.execSQL(query);
    }
}
