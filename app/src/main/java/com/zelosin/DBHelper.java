package com.zelosin;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

class DBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 3;
    public static final String DATABASE_NAME = "AND_DB_DATA";
    public static final String TABLE_DATA = "AND_DATA";

    public static final String DATABASE_LOGIN = "AND_LOGIN";
    public static final String DATABASE_PASSWORD = "AND_PASSSWORD";

    public static final String KEY_ID = "_id";
    public static final String KEY_DATA = "data";

    public DBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table " + TABLE_DATA + "(" +
                        KEY_ID + " integer primary key, " +
                        KEY_DATA + " text );"
                );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_DATA);
        onCreate(db);
    }
}
