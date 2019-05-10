package com.example.jian2020.nuc.accesscontactsdemo_94243;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.security.Permission;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private EditText nameEt = null;

    private EditText numberEt = null;

    private Button addBtn = null;

    private Button queryBnt = null;

    private LinearLayout titlell = null;

    private ListView contentsLV = null;

    private ContentResolver resolver = null;

    private MyListener myListener = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        myListener = new MyListener();

        addBtn.setOnClickListener(myListener);

        queryBnt.setOnClickListener(myListener);
    }

    private void init(){
        nameEt = (EditText) findViewById(R.id.nameEt);

        numberEt = (EditText) findViewById(R.id.numberEt);

        addBtn = (Button) findViewById(R.id.addBtn);

        queryBnt = (Button) findViewById(R.id.queryBtn);

        titlell = (LinearLayout) findViewById(R.id.titlell);

        contentsLV = (ListView) findViewById(R.id.contentLV);

        resolver = getContentResolver();
    }


    private class MyListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.addBtn:
                    //检查权限
                    if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_CONTACTS)
                            != PackageManager.PERMISSION_GRANTED){
                        ActivityCompat.requestPermissions(MainActivity.this,
                                new String[]{Manifest.permission.WRITE_CONTACTS}, 1);
                    }
                    addContacts();
                    break;
                case R.id.queryBtn:
                    if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_CONTACTS)
                            != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(MainActivity.this,
                                new String[]{Manifest.permission.READ_CONTACTS}, 2);
                    }
                    titlell.setVisibility(View.VISIBLE);
                    ArrayList<Map<String, String>> persons = queryContacts();
                    //将查询通讯录结果放入person中
                    SimpleAdapter adapter = new SimpleAdapter(MainActivity.this,
                            persons,
                            R.layout.result,
                            new String[]{"id", "name", "number"},
                            new int[]{R.id.idTV, R.id.nameTV, R.id.numberTV});
                    contentsLV.setAdapter(adapter);
                    break;
                default:
                    break;
            }
        }
    }

    //获取写入通讯录权限
    private void addContacts(){
        String nameStr = nameEt.getText().toString().trim();

        String numberStr = numberEt.getText().toString().trim();

        ContentValues values = new ContentValues();
        // 向RawContacts.CONTENT_URI执行一个空值插入，目的是获取返回的ID号。

        Uri rawContactUri = resolver.insert(ContactsContract.RawContacts.CONTENT_URI,
                values);

        long contactId = ContentUris.parseId(rawContactUri);

        values.clear();

        values.put(ContactsContract.Data.RAW_CONTACT_ID, contactId);
        //获取当前的通讯录id,利用插入一个空的通讯录，获得id号
        //设置id

        values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE);
        //设置类型

        //添加姓名
        values.put(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, nameStr);

        resolver.insert(ContactsContract.Data.CONTENT_URI, values);

        values.clear();

        values.put(ContactsContract.Data.RAW_CONTACT_ID, contactId);

        values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);

        values.put(ContactsContract.CommonDataKinds.Phone.NUMBER, numberStr);

        values.put(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE);

        resolver.insert(ContactsContract.Data.CONTENT_URI, values);

        Toast.makeText(MainActivity.this, "添加联系人成功", Toast.LENGTH_LONG).show();
    }

    //获取读取通讯录权限
    private ArrayList<Map<String, String>> queryContacts() {
        ArrayList<Map<String, String>> details = new ArrayList<Map<String, String>>();
        Cursor cursor = resolver.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null,null);
        if (cursor != null){//将通讯录中的每个联系人添加到map中，在
            while (cursor.moveToNext()){
                Map<String, String> person = new HashMap<String, String>();
                String personId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                String personName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                String personNumber = null;
                Cursor numbercursor = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + personId, null, null);

                if (numbercursor != null){
                    while (numbercursor.moveToNext()){
                        personNumber = numbercursor.getString(numbercursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    }
                    numbercursor.close();
                }

                person.put("id", personId);
                person.put("name", personName);
                person.put("number", personNumber);
                details.add(person);
            }
            cursor.close();
        }
        return  details;
    }

}
