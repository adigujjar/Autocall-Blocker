package com.example.autocallblocker.Models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "BlockCalls";

    private static final int DB_VERSION = 1;
    private static final String TABLE_NAME = "BlackList";

    private static final String KEY_ID = "id";
    private static final String PHONE_NUMBER = "phoneNumber";

    private static final String QUERY = " CREATE TABLE " + TABLE_NAME + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + PHONE_NUMBER + " TEXT " + ")";
    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
       sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
       onCreate(sqLiteDatabase);
    }

    public void addNumber(BlackList blackList)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(PHONE_NUMBER, blackList.getPhoneNumber());

        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public ArrayList<BlackList> getAllContacts() {
        ArrayList<BlackList> contentList = new ArrayList<BlackList>();
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                BlackList content = new BlackList();
                content.setId(Long.parseLong((cursor.getString(0))));
                content.setPhoneNumber(cursor.getString(1));
                contentList.add(content);
            } while (cursor.moveToNext());
        }
        return contentList;
    }
    //Delete Contact
    public void deleteContact(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, PHONE_NUMBER + " = ?", new String[]{name});
        db.close();
    }

}
