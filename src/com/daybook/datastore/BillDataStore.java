package com.daybook.datastore;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import com.daybook.db.DatabaseHandler;
import com.daybook.model.Bill;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class BillDataStore {
    private SQLiteDatabase database;
    private DatabaseHandler databaseHandler;
    private static String tableName = "BILLS";
    private String billNumber = "bill_number";
    private String billAmount = "bill_amount";
    private String createdAt = "created_at";
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public BillDataStore(Context context){
        databaseHandler = new DatabaseHandler(context, tableName);
    }

    public void open() throws SQLException {
        database = databaseHandler.getWritableDatabase();
    }

    public void close() {
        databaseHandler.close();
        database.close();
    }

    public long addBill(Bill bill){
        database = databaseHandler.getWritableDatabase();
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());

        ContentValues values = new ContentValues();

        values.put(billNumber, bill.getBillNumber());
        values.put(billAmount, bill.getBillAmount());
        values.put(createdAt, simpleDateFormat.format(c.getTime()));

        return database.insert(tableName, null, values);
    }

    public List<Bill> getAllBill() {
        List<Bill> bills = new ArrayList<Bill>();

        Cursor cursor = database.query(tableName, new String[] {billNumber,
                billAmount}, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                Integer billNumber = Integer.valueOf(cursor.getString(0));
                Integer billAmount = Integer.valueOf(cursor.getString(1));
                Bill bill = new Bill(billNumber, billAmount);
                bills.add(bill);
            } while (cursor.moveToNext());
        }
        return bills;
    }

    public Bill findBillWith(int billNumber) {
        Bill result = null;
        Integer resultBillNumber, resultBillAmount;

        database = databaseHandler.getReadableDatabase();
        Cursor cursor = database.query(tableName, new String[]{this.billNumber, this.billAmount},
                this.billNumber + " = " + billNumber, null, null, null, null);

        if(cursor.moveToFirst()){
            resultBillNumber = cursor.getInt(0);
            resultBillAmount = cursor.getInt(1);
            result = new Bill(resultBillNumber, resultBillAmount);
        }
        return result;
    }

    public List<Bill> getBillsOf(int numberOfDays){
        List<Bill> billList = new ArrayList<Bill>();
        int resultBillNumber, resultBillAmount;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.DATE, 1);
        String tomorrow = dateFormat.format(c.getTime()) + " 00:00:00";
        c.add(Calendar.DATE, -(numberOfDays));
        String previousDay = dateFormat.format(c.getTime()) + " 00:00:00";

        database = databaseHandler.getReadableDatabase();

        Cursor cursor = database.query(tableName, new String[]{billNumber, billAmount},
                createdAt + " >= ? and " +
                createdAt + " < ?" , new String[] {previousDay, tomorrow}, null, null, null);

        while(cursor.moveToNext() && cursor != null) {
            resultBillNumber = cursor.getInt(0);
            resultBillAmount = cursor.getInt(1);
            billList.add(new Bill(resultBillNumber, resultBillAmount));
        }

        return billList;
    }

//    public String getDate(){
//
//        database = databaseHandler.getReadableDatabase();
//
//        Calendar c = Calendar.getInstance();
//        c.setTime(new Date());
//
//        Cursor cursor = database.query(tableName, new String[] {createdAt}, "created_at >= ?",
//                new String[] {simpleDateFormat.format(c.getTime())},
//                null,
//                null,
//                null);
//
//        if(cursor != null && cursor.moveToFirst()){
//            return cursor.getString(0);
//        }
//        return null;
//    }
}
