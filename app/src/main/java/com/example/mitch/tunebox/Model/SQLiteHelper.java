package com.example.mitch.tunebox.Model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Mitch on 9/29/16.
 */
public class SQLiteHelper extends SQLiteOpenHelper {
    public static final String tableName = "settings";
    public static final String columnName = "shuffleStatus";

    //create table statement
    private static final String DATABASE_CREATE = "create table " + tableName + " (" + columnName + " text);";

    public SQLiteHelper(Context context) {
        super(context, "Settings", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + tableName);
        onCreate(db);
    }

    public boolean updateStatus(String shuffle) {

        SQLiteDatabase database = this.getWritableDatabase();
        database.execSQL("DROP TABLE IF EXISTS " + tableName);          //drops table if it exists and creates it again
        database.execSQL(DATABASE_CREATE);

        ContentValues values = new ContentValues();
        values.put(columnName, shuffle);


        // Inserting Row
        long result = database.insert(tableName, null, values);     //inserts shuffle values into table
        database.close(); // Closing database connection
        if(result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Cursor getStatus() {
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor res = database.rawQuery("select * from " + tableName, null);
        return res;
    }
}