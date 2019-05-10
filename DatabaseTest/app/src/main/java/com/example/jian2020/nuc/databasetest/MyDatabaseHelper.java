package com.example.jian2020.nuc.databasetest;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class MyDatabaseHelper extends SQLiteOpenHelper {

    private static final String CREATE_BOOK = "CREATE TABLE Book ("
            + "id integer primary key autoincrement,"
            + "author text,"
            + "price real,"
            + "pages integer,"
            + "name text)";

    private static final String CREATE_CATEGORY = "CREATE TABLE Category ("
            + "id integer primary key autoincrement,"
            + "category_name text,"
            + "category_code integer)";

    private Context mContext = null;

    public MyDatabaseHelper(Context context,
                            String name,
                            SQLiteDatabase.CursorFactory factory,
                            int version){
        super(context, name, factory, version);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_BOOK);
        db.execSQL(CREATE_CATEGORY);
        Toast.makeText(mContext, "Create succeeded", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists Book");
        db.execSQL("drop table if exists Category");
        onCreate(db);
    }
}
