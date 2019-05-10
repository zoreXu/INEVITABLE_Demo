package com.example.jian2020.nuc.httpdemo_94243;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private Button send_request = null;

    private TextView responseTv = null;

    private MyListner myListner = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        myListner = new MyListner();

        send_request.setOnClickListener(myListner);

        send_request.setOnClickListener(myListner);
    }

    private void init(){

        send_request = (Button) findViewById(R.id.send_request);

        responseTv = (TextView) findViewById(R.id.responseTv);
    }

    private class MyListner implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.send_request:
                    //发送网络请求
                    sendRequestWithHttpURLConnection();

                    break;
                case R.id.responseTv:
                    break;
                default:
                    break;
            }
        }
    }

    private void sendRequestWithHttpURLConnection() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;

                BufferedReader reader = null;

                InputStream inputStream = null;


                try {

                    URL url = new URL("http://www.nuc.edu.cn");

                    connection = (HttpURLConnection)url.openConnection();

                    connection.setRequestMethod("GET");

                    connection.setConnectTimeout(5000);

                    connection.setReadTimeout(5000);

                    inputStream = connection.getInputStream();
                    //inputStream字节读取

                    reader = new BufferedReader(new InputStreamReader(inputStream));
                    //BufferStream一行一行读取

                    StringBuilder respondeStr = new StringBuilder();
                    //利用可变长的StringBuilder对象拼接每行数据

                    String lineStr = null;
                    //一行一行数据，将他拼接到respondStr中

                    while ((lineStr = reader.readLine()) != null) {
                        respondeStr.append(lineStr);
                    }

                    showResponse(respondeStr.toString());

                }catch (IOException e){
                    e.printStackTrace();
                } finally {
                    if(reader != null){
                        try{
                            reader.close();
                        }catch (IOException e){
                            e.printStackTrace();
                        }

                    }
                    if(connection != null){
                        connection.disconnect();
                    }
                }

            }
        }).start();


    }

    //线程操作UI控件
    private void showResponse(final String toString) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                responseTv.setText(toString);
            }
        });
    }
}
