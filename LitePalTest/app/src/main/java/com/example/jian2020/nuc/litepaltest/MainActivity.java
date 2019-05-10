package com.example.jian2020.nuc.litepaltest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.stetho.Stetho;

import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.util.List;

import pl.com.salsoft.sqlitestudioremote.SQLiteStudioService;

public class MainActivity extends AppCompatActivity {

    private Button createDatabase = null;

    private Button addData = null;

    private Button updateData = null;

    private Button deteleDate = null;

    private Button queryData = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Stetho.initializeWithDefaults(this);

        createDatabase = (Button)findViewById(R.id.create_databas);
        addData = (Button) findViewById(R.id.add_data);
        updateData = (Button) findViewById(R.id.update_data);
        deteleDate = (Button) findViewById(R.id.delete_data);
        queryData = (Button) findViewById(R.id.query_data);

        createDatabase.setOnClickListener(new MyLinstener());
        addData.setOnClickListener(new MyLinstener());
        updateData.setOnClickListener(new MyLinstener());
        deteleDate.setOnClickListener(new MyLinstener());
        queryData.setOnClickListener(new MyLinstener());

        //开启
//        SQLiteStudioService.instance().start(MainActivity.this);

    }

    private class MyLinstener implements View.OnClickListener{

        private Book book = null;

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.create_databas:
                    Connector.getDatabase();
                    Toast.makeText(MainActivity.this, "Create BookStore success", Toast.LENGTH_LONG).show();
                    break;
                case R.id.add_data:
                    book = new Book();
                    book.setName("The Da Vinci Code");
                    book.setAuthor("Dan Brown");
                    book.setPages(454);
                    book.setPrice(16.96);
                    book.setPress("Unknow");
                    book.save();
                    break;
                case R.id.update_data:
                    //添加一个书目
                    book = new Book();
                    book.setName("The Lost Symbol");
                    book.setAuthor("Dan Brown");
                    book.setPages(510);
                    book.setPrice(19.95);
                    book.setPress("Unknow");
                    book.save();
//                    book.setPrice(10.99);
//                    book.save();
                    //进行修改
                    book =  new Book();
                    book.setPrice(14.95);
                    book.setPress("Anchor");
                    book.updateAll("name = ? and author = ?", "The Lost Symbol", "Dan Brown");

//                    //统一将pages属性设置默认值,无约束条件
//                    book = new Book();
//                    book.setToDefault("pages");
//                    book.updateAll();
                    break;
                case R.id.delete_data:
                    DataSupport.deleteAll(Book.class, "price < ?", "15");
                    break;
                case R.id.query_data:
                    List<Book> books = DataSupport.findAll(Book.class);
                    for (Book book : books){
                        Log.d("MainActivity", "Book name is " + book.getName());
                        Log.d("MainActivity", "Book author is " + book.getAuthor());
                        Log.d("MainActivity", "Book pages is " + book.getPages());
                        Log.d("MainActivity", "Book price is " + book.getPrice());
                        Log.d("MainActivity", "Book press is " + book.getPress());
                    }

                default:
                    break;
            }
        }
    }
}
