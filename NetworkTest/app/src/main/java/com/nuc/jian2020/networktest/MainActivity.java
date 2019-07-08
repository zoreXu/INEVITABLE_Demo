package com.nuc.jian2020.networktest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import javax.xml.parsers.SAXParserFactory;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private Button sendRequest = null;

    private Button okhttpSendRequestBt = null;

    private Button saxBt = null;

    private Button jsonBt = null;

    private Button gsonBt = null;

    private TextView responseText = null;

    private MyListener myListener = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        sendRequest.setOnClickListener(myListener);

        okhttpSendRequestBt.setOnClickListener(myListener);

        jsonBt.setOnClickListener(myListener);

        saxBt.setOnClickListener(myListener);

        gsonBt.setOnClickListener(myListener);
    }

    private void init(){
        sendRequest = (Button) findViewById(R.id.send_requestBt);

        okhttpSendRequestBt = (Button) findViewById(R.id.okhttp_send_requestBt);

        jsonBt = (Button) findViewById(R.id.jsonBt);

        saxBt = (Button) findViewById(R.id.saxBt);

        gsonBt = (Button) findViewById(R.id.gsonBt);

        responseText = (TextView) findViewById(R.id.reponse_text);

        myListener = new MyListener();
    }

    private class MyListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.send_requestBt:
                    sendRequestWithHttpURLConnection();
                    break;
                case R.id.okhttp_send_requestBt: //pull解析XML
                    sendRequestWithOkHttp();
                    break;
                case R.id.jsonBt:
                    sendRequestWithOkHttpANDWithJSON(); //JSON解析json
                    break;
                case R.id.saxBt:
                    sendRequestWithOkHttpANDWithSAX(); //SAX解析XML
                    break;
                case R.id.gsonBt:
                    sendRequestWithOkHttpANDWithGson();
                    break;
                default:
                    break;

            }
        }
    }

    private void sendRequestWithHttpURLConnection(){
        //开启线程来发起网络请求
        //网络请求需要时耗，利用线程进行请求，避免主程序被浪费

        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;

                BufferedReader reader = null;

                try {
                    URL url = new URL("http://192.168.43.233/get_data.xml");//真实的ip

                    Log.d("MainActivity" , "get_data");

                    connection = (HttpURLConnection) url.openConnection();

                    connection.setRequestMethod("GET");

                    connection.setConnectTimeout(8000);

                    connection.setReadTimeout(8000);

                    InputStream in = connection.getInputStream();

                    //下面对获取到的输入流进行读取
                    reader = new BufferedReader(new InputStreamReader(in));

                    StringBuilder response = new StringBuilder();

                    String line;

                    while ((line = reader.readLine()) != null){
                        response.append(line);
                    }

                    showResponse(response.toString());
                } catch (Exception e){
                    e.printStackTrace();
                } finally {
                    if (reader != null){
                        try {
                            reader.close();
                        } catch (IOException e){
                            e.printStackTrace();
                        }
                    }
                    if (connection != null){
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }

    private void showResponse(final String response){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //这里进行UI操作，将结果显示到界面上
                responseText.setText(response);
            }
        });
    }

    //利用Okhttp获取网页数据
    //pull解析网页数据
    private void sendRequestWithOkHttp(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url("http://192.168.43.233/get_data.xml")
                            .build();

                    Response response = client.newCall(request).execute();

                    String responseData = response.body().string();

                    showResponse(responseData);
                    //Pull解析方式
                    parseXMLWithPull(responseData);

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }


    private void parseXMLWithPull(String xmlData){
        try {

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();

            XmlPullParser xmlPullParser = factory.newPullParser();

            xmlPullParser.setInput(new StringReader(xmlData));

            int eventType = xmlPullParser.getEventType();

            String id = "";

            String name = "";

            String version = "";

            while (eventType != XmlPullParser.END_DOCUMENT){
                String nodeName = xmlPullParser.getName();
                switch (eventType){
                    //开始解析某个节点
                    case XmlPullParser.START_TAG:{
                        if ("id".equals(nodeName)){
                            id = xmlPullParser.nextText();
                        } else if ("name".equals(nodeName)){
                            name = xmlPullParser.nextText();
                        } else if ("version".equals(nodeName)){
                            version = xmlPullParser.nextText();
                        }
                    }
                    break;

                    //完成解析某个节点
                    case XmlPullParser.END_TAG:{
                        if ("app".equals(nodeName)){
                            Log.d("MainActivity", "id is" + id);
                            Log.d("MainActivity", "name is" + name);
                            Log.d("MainActivity", "version is" + version);
                        }
                        break;
                    }

                    default:
                        break;
                }

                eventType = xmlPullParser.next();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //利用OkHttp获取数据
    // 利用SAX解析数据
    private void sendRequestWithOkHttpANDWithSAX(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            //指定访问的服务器地址是电脑本机
                            .url("http://192.168.43.233/get_data.xml")
                            .build();

                    Response response = client.newCall(request).execute();

                    String responseData = response.body().string();

                    showResponse(responseData);

                    //SAX解析
                    parseXMLWithSAX(responseData);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void parseXMLWithSAX(String xmlData){
        try {

            SAXParserFactory factory = SAXParserFactory.newInstance();

            XMLReader xmlReader = factory.newSAXParser().getXMLReader();

            ContentHandler handler = new ContentHandler();

            //将ContentHandler的实例设置到XMLReader中
            xmlReader.setContentHandler(handler);

            //开始执行解析
            xmlReader.parse(new InputSource(new StringReader(xmlData)));
            } catch (Exception e){
            e.printStackTrace();
        }
    }


    //利用OkHttp获取数据
    //利用JSON解析数据
    private void sendRequestWithOkHttpANDWithJSON(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            //指定访问的服务器地址是电脑本机
                            .url("http://192.168.43.233/get_data.json")
                            .build();

                    Response response = client.newCall(request).execute();

                    String responseData = response.body().string();

                    showResponse(responseData);

                    //JSON解析
                    parseJSONWithJSONObject(responseData);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void parseJSONWithJSONObject(String jsonData){
        try {

            JSONArray jsonArray = new JSONArray(jsonData);

            for (int i = 0; i < jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                String id = jsonObject.getString("id");

                String name = jsonObject.getString("name");

                String version = jsonObject.getString("version");

                Log.d("MainActivity", "id is" + id);
                Log.d("MainActivity", "name is" + name);
                Log.d("MainActivity", "version is" + version);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    //利用OkHttp获取数据
    //利用Gson解析数据
    private void sendRequestWithOkHttpANDWithGson(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            //指定访问的服务器地址是电脑本机
                            .url("http://192.168.43.233/get_data.json")
                            .build();

                    Response response = client.newCall(request).execute();

                    String responseData = response.body().string();

                    showResponse(responseData);

                    //Gson解析
                    parseJSONWithGSON(responseData);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void parseJSONWithGSON(String jsonData){
        Gson gson = new Gson();

        List<App> appList = gson.fromJson(jsonData, new TypeToken<List<App>>(){}.getType());

        for (App app : appList){
            Log.d("MainActivity", "id is " + app.getId());
            Log.d("MainActivity", "name is " + app.getName());
            Log.d("MainActivity", "version is " + app.getVersion());
        }
    }
}
