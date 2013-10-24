package com.daybook.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static String databaseName = "DayBook";
    private static int databaseVersion = 3;
    private String tableName;


    public DatabaseHandler(Context context, String tableName) {
        super(context, databaseName, null, databaseVersion);
        this.tableName = tableName;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.d("Bill", "on Create");
        sqLiteDatabase.execSQL("CREATE TABLE "+ tableName +" (bill_number INTEGER PRIMARY KEY, bill_amount INTEGER, " +
                "created_at TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
