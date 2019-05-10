package com.example.jian2020.nuc.providertest;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button addDatabt = null;

    private Button queryDatabt = null;

    private Button updateDatabt = null;

    private Button deleteDatabt = null;

    private String newId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        addDatabt.setOnClickListener(new MyListener());;

        queryDatabt.setOnClickListener(new MyListener());

        updateDatabt.setOnClickListener(new MyListener());

        deleteDatabt.setOnClickListener(new MyListener());
    }

    private void init(){
        addDatabt = (Button) findViewById(R.id.add_databt);

        queryDatabt = (Button) findViewById(R.id.query_databt);

        updateDatabt = (Button) findViewById(R.id.update_databt);

        deleteDatabt = (Button) findViewById(R.id.delete_databt);
    }

    private class MyListener implements View.OnClickListener{

        Uri uri = null;

        ContentValues values = null;

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.add_databt:
                    //添加数据
                    uri = Uri.parse("content://com.example.jian2020.nuc.databasetest.provider/book");
                    values = new ContentValues();
                    values.put("name", "A Clash of Kings");
                    values.put("author", "Georage Martin");
                    values.put("pages", 1040);
                    values.put("price", 22.85);
                    Uri newUri = getContentResolver().insert(uri, values);
                    newId = newUri.getPathSegments().get(1);
                    break;
                case R.id.query_databt:
                    //查询数据
                    uri = Uri.parse("content://com.example.jian2020.nuc.databasetest.provider/book");
                    Cursor cursor = getContentResolver().query(uri, null, null,
                            null, null, null);

                    if (cursor != null){
                        while (cursor.moveToNext()){
                            String name = cursor.getString(cursor.getColumnIndex("name"));

                            String author = cursor.getString(cursor.getColumnIndex("author"));

                            int pages = cursor.getInt(cursor.getColumnIndex("pages"));

                            double price = cursor.getDouble(cursor.getColumnIndex("price"));

                            Log.d("MainActivity", "book name is " + name);
                            Log.d("MainActivity", "book author is " + author);
                            Log.d("MainActivity", "book pages is " + pages);
                            Log.d("MainActivity", "book author is " + author);
                        }
                        cursor.close();
                    }
                    break;
                case R.id.update_databt:
                    //更新数据
                    uri = Uri.parse("content://com.example.jian2020.nuc.databasetest.provider/book/" +
                    newId);
                    values = new ContentValues();
                    values.put("name", "A Storm of Swords");
                    values.put("pages", 1216);
                    values.put("price", 24.05);
                    getContentResolver().update(uri, values, null, null);
                    break;
                case R.id.delete_databt:
                    //删除数据
                    uri = Uri.parse("content://com.example.jian2020.nuc.databasetest.provider/book/" +
                    newId);
                    getContentResolver().delete(uri, null, null);
                    break;
                default:
                    break;
            }
        }
    }
}
