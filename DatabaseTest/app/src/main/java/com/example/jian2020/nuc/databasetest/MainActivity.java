package com.example.jian2020.nuc.databasetest;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.facebook.stetho.Stetho;

import pl.com.salsoft.sqlitestudioremote.SQLiteStudioService;

public class MainActivity extends AppCompatActivity {

    private MyDatabaseHelper dbHelper = null;

    private Button createDatabase = null;

    private Button addData = null;

    private Button updataData = null;

    private Button deleteData = null;

    private Button queryData = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SQLiteStudioService.instance().start(MainActivity.this);
        //Stetho.initializeWithDefaults(this);

        dbHelper = new MyDatabaseHelper(this, "BookStore.db", null, 2);

        createDatabase = (Button)findViewById(R.id.create_database);
        addData = (Button)findViewById(R.id.add_data);
        updataData = (Button)findViewById(R.id.update_data);
        deleteData = (Button)findViewById(R.id.delete_data);
        queryData = (Button) findViewById(R.id.query_data);

        createDatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper.getWritableDatabase();
            }
        });

        addData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                //开始组装第一条数据
                values.put("name", "The Da Vinci Code");
                values.put("author", "Dan Brown");
                values.put("pages", 454);
                values.put("price", 16.96);
                db.insert("Book", null, values);
                //插入第一条数据
                values.clear();
                //清除values中的值
                //开始插入第二条数据
                values.put("name", "The Lost Symbol");
                values.put("author", "Dan Brown");
                values.put("pages", 510);
                values.put("price", 19.95);
                db.insert("Book", null, values);

                //SQL语句进行插入
//                db.execSQL("insert into Book (name, author, pages, price) values(?, ?, ?, ?)",
//                        new String[]{"The Da Vinci Code", "Dan Brown", "454", "16.96"});
//
//                db.execSQL("insert into Book (name, author, pages, price) values(?, ?, ?, ?)",
//                        new String[]{"The Lost Symbol", "Dan Brown", "510", "19.95"});
            }
        });

        updataData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("price", 10.99);
                db.update("Book", values, "name = ?", new String[]{"The Da Vinci Code"});

//                db.execSQL("update Book set price ? where name = ?",
//                        new String[]{"10.99", "The Da Vinci Code"});
            }
        });

        deleteData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();

                db.delete("Book", "pages > ?", new String[]{"500"});

//                db.execSQL("delete from Book where pages > ?",
//                        new String[]{"500"});
            }
        });

        queryData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                //查询Book表中所以的数据
                Cursor cursor = db.query("Book", null, null,
                        null, null,null, null);
                if(cursor.moveToFirst()){
                    do {
                        //遍历Cursor对象，取出数据并打印
                        String name = cursor.getString(cursor.getColumnIndex("name"));

                        String author = cursor.getString(cursor.getColumnIndex("author"));

                        int pages = cursor.getInt(cursor.getColumnIndex("pages"));

                        double price = cursor.getDouble(cursor.getColumnIndex("price"));

                        Log.d("MainActivity", "Book name is " + name);

                        Log.d("MainActivity", "Book author is " + author);

                        Log.d("MainActivity", "Book pages is " + pages);

                        Log.d("MainActivity", "Book price is " + price);


                    }while (cursor.moveToNext());
                }
                cursor.close();

//                db.rawQuery("select * from Book", null);
            }
        });

        SQLiteStudioService.instance().start(this);
    }

}
