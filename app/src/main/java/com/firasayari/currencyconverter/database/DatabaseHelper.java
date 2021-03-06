package com.firasayari.currencyconverter.database;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;
public class DatabaseHelper extends SQLiteOpenHelper {


    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "operations_db";
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {

        // create notes table
        db.execSQL(Operations.CREATE_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + Operations.TABLE_NAME);

        // Create tables again
        onCreate(db);
    }


    public long insertOperation(String op) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(Operations.COLUMN_OP, op);

        // insert row
        long id = db.insert(Operations.TABLE_NAME, null, values);

        // close db connection
        db.close();

        // return newly inserted row id
        return id;
    }

    public List<Operations> getAllOp() {
        List<Operations> operations = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + Operations.TABLE_NAME + " ORDER BY " +
                Operations.COLUMN_TIMESTAMP + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Operations op = new Operations();
                op.setId(cursor.getInt(cursor.getColumnIndex(op.COLUMN_ID)));
                op.setOp(cursor.getString(cursor.getColumnIndex(op.COLUMN_OP)));
                op.setTimestamp(cursor.getString(cursor.getColumnIndex(op.COLUMN_TIMESTAMP)));

                operations.add(op);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return notes list
        return operations;
    }




}
