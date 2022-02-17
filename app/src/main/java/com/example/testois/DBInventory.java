packagecom.example.testois;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;

public class DBInventory extends SQLiteOpenHelper {
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "severinadb";
    private static final String TABLE_Inventory = "inventory";
    private static final String KEY_ID = "id";
    private static final String KEY_ITEM_ID = "1011";            //needs a way to randomize or classify each product in unique ids
    private static final String KEY_NAME = "itemname";
    private static final String KEY_Quantity = "quantity";
    private static final String KEY_DESC = "description";

    public DBInventory(Context context){
        super(context,DB_NAME, null, DB_VERSION);
        }
@Override
public void onCreate(SQLiteDatabase TABLE_Inventory){
        String CREATE_TABLE = "CREATE TABLE " + TABLE_Inventory + "("
        + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_NAME + " TEXT,"
        + KEY_Quantity + " TEXT,"
        + KEY_DESC + " TEXT"+ ")";
        TABLE_Inventory.execSQL(CREATE_TABLE);
        }
@Override
public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        // Drop older table if exist
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Inventory);
        // Create tables again
        onCreate(db);
        }
        // **** CRUD (Create, Read, Update, Delete) Operations ***** //

        // Adding new Item Details
        void insertItemDetails(String itemname, String quantity, String description){
        //Get the Data Repository in write mode
        SQLiteDatabase db = this.getWritableDatabase();
        //Create a new map of values, where column names are the keys
        ContentValues cValues = new ContentValues();
        cValues.put(KEY_NAME, itemname);
        cValues.put(KEY_Quantity, quantity);
        cValues.put(KEY_DESC, description);
        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(TABLE_Inventory,null, cValues);
        db.close();
        }


// Get Item Details
public ArrayList<HashMap<String, String>> GetItems(){
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<HashMap<String, String>> itemList = new ArrayList<>();
        String query = "SELECT itemname, quantity, description FROM "+ TABLE_Inventory;
        Cursor cursor = db.rawQuery(query,null);
        while (cursor.moveToNext()){
        HashMap<String,String> item = new HashMap<>();
        item.put("itemname",cursor.getString(cursor.getColumnIndexOrThrow(KEY_NAME)));
        item.put("description",cursor.getString(cursor.getColumnIndexOrThrow(KEY_DESC)));
        item.put("quantity",cursor.getString(cursor.getColumnIndexOrThrow(KEY_Quantity)));
        itemList.add(item);
        }
        return  itemList;
        }

// Get Item Details based on itemid
public ArrayList<HashMap<String, String>> GetItemByItemId(int itemid){
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<HashMap<String, String>> itemList = new ArrayList<>();
        String query = "SELECT itemname, quantity, description, itemid FROM "+ TABLE_Inventory;
        Cursor cursor = db.query(TABLE_Inventory, new String[]{KEY_NAME, KEY_Quantity, KEY_DESC, KEY_ITEM_ID}, KEY_ID+ "=?",new String[]{String.valueOf(itemid)},null, null, null, null, null);
        if (cursor.moveToNext()){
        HashMap<String,String> item = new HashMap<>();
        item.put("itemname",cursor.getString(cursor.getColumnIndexOrThrow(KEY_NAME)));
        item.put("quantity",cursor.getString(cursor.getColumnIndexOrThrow(KEY_Quantity)));
        item.put("description",cursor.getString(cursor.getColumnIndexOrThrow(KEY_DESC)));
        itemList.add(item);
        }
        return  itemList;
        }


// Get Item Details based on itemname
public ArrayList<HashMap<String, String>> GetItemByItemName(String itemname){
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<HashMap<String, String>> itemList = new ArrayList<>();
        String query = "SELECT itemname, quantity, description FROM "+ TABLE_Inventory " WHERE itemname LIKE '%" + KEY_NAME + "%'";  //search keyname as letter or ?
        Cursor cursor = db.query(TABLE_Inventory, new String[]{KEY_NAME, KEY_Quantity, KEY_DESC}, "KEY_NAME LIKE ?", new String[]{itemname+"%"}, null, null, null, null);
        while (cursor.moveToNext()){
        HashMap<String,String> item = new HashMap<>();
        item.put("itemname",cursor.getString(cursor.getColumnIndexOrThrow(KEY_NAME)));
        item.put("description",cursor.getString(cursor.getColumnIndexOrThrow(KEY_DESC)));
        item.put("quantity",cursor.getString(cursor.getColumnIndexOrThrow(KEY_Quantity)));
        itemList.add(item);
        }
        return  itemList;
        }


// Delete Item Details
public void DeleteItem(int itemid){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_Inventory, KEY_ID+" = ?",new String[]{String.valueOf(itemid)});
        db.close();
        }


// Update Item Details
public int UpdateItemDetails(String quantity, String description, int id){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cVals = new ContentValues();
        cVals.put(KEY_Quantity, quantity);
        cVals.put(KEY_DESC, description);
        int count = db.update(TABLE_Inventory, cVals, KEY_ID+" = ?",new String[]{String.valueOf(id)});
        return  count;
        }
        }



/*

Code copied from Course Code Youtube Video
public class DBInventory extends SQLiteOpenHelper {
    private static final int DB_VER = 1;
    private static final String DB_NAME = "severinadb";
    private static final String TBL_NAME = "inventory";
    private static final String KEY_ID = "id";
     private static final int KEY_ITEM_ID = 1011;            //needs a way to randomize or classify each product in unique ids
    private static final String KEY_NAME = "itemname";
    private static final String KEY_Quantity = "quantity";
    private static final String KEY_DESC = "description";

    public DBInventory(@Nullable Context context) {
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
        onCreate(db);
    }
}
*/