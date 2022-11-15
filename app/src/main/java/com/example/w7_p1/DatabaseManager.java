package com.example.w7_p1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DatabaseManager extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "nameDB";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "Friends";
    private static final String ID = "id";
    private static final String FIRST = "first";
    private static final String LAST = "last";
    private static final String EMAIL = "email";

    public DatabaseManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlCreate = "create table " + TABLE_NAME + " ( " + ID;
        sqlCreate += " integer primary key autoincrement, " + FIRST;
        sqlCreate += " text, " + LAST + " text, " + EMAIL + " text ) ";

        db.execSQL(sqlCreate);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insert(String first, String last, String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(FIRST, first);
        contentValues.put(LAST, last);
        contentValues.put(EMAIL, email);


        long result = db.insert(TABLE_NAME, null, contentValues);

        if (result == -1) {
            return false;
        } else
            return true;

    }


    public Cursor search(String email) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME + " where email = ?  ", new String[]{email});

        return cursor;
    }

    public int verify(String email){
        SQLiteDatabase db = this.getWritableDatabase();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME + " where email = ?  ", new String[]{email});
        if(cursor.moveToFirst())
        {
            return -1;
        }

        if (!email.matches(emailPattern)){
            return -2;
        }
        return 1;
    }

    public Cursor update(String id, String first, String last, String email){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(ID, id);
        contentValues.put(FIRST,first);
        contentValues.put(LAST,last);
        contentValues.put(EMAIL, email);
        db.update(TABLE_NAME, contentValues, "ID = ?", new String[] {id});

        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME + " where id = ?  ", new String[]{id});
        return cursor;
    }

    public void delete(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String sqlDelete ="delete from " + TABLE_NAME + " where id = " + id;
        db.execSQL(sqlDelete);
        db.close();

    }


    public String[] autoComplete() {
        ArrayList<String> data =new ArrayList();
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_NAME;
        Cursor cursor = db.rawQuery(sql, null);
        String[] theData = new String[cursor.getCount()];
        if (cursor != null && cursor.getCount()>0){
            cursor.moveToFirst();

            int i=0;
            do {
                theData[i]=cursor.getString(3);
                i++;
            }while(cursor.moveToNext());

        }

        return theData;

    }




}
