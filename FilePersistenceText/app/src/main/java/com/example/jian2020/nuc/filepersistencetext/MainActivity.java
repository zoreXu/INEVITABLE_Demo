package com.example.jian2020.nuc.filepersistencetext;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity {

    private EditText editBtn = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

        //从文件读取信息
        String inputText = load();
        if (!TextUtils.isEmpty(inputText)){
            //能进行两种字符串的判断，null和""
            //null是指没有开辟空间
            //""空间已经开辟，但字符是空的
            editBtn.setText(inputText);
            editBtn.setSelection(inputText.length());
            //将光标移动到文本的末尾进行输入
            Toast.makeText(this, "Restoring succeede", Toast.LENGTH_LONG).show();
        }
    }

    //初始化
    private void init(){
        editBtn = (EditText)findViewById(R.id.editBtn);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        String inputText = editBtn.getText().toString();
        save(inputText);
    }

    //将内容保存在文本中
    public void save(String inputText) {
        FileOutputStream out = null;
        BufferedWriter writer = null;
        try{
            out = openFileOutput("data", Context.MODE_PRIVATE);
            //创建名为data的文件，规定操作模式，覆盖模式，另一种模式是MODE_APPEND，追加模式
            //通过openFileOutput()得到一个FileOutputStream对象
            writer = new BufferedWriter(new OutputStreamWriter(out));
            //借助FileOutputStream对象构建OutputStreamWriter对象
            //在利用OutputStreamWriter对象构建OutputStreamWriter对象一个BufferedWriter对象
            //通过BufferedWriter来将文本内容写入文本中
            writer.write(inputText);
        }catch (IOException e){
            e.printStackTrace();
        } finally {
            try {
                if (writer != null){
                    writer.close();
                }
            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    // 步骤 系统自动加载/data/data/<package name>/files/中加载文件
    // 并返回一个FileInputStream对象
    public String load(){
        FileInputStream in = null;
        BufferedReader reader = null;
        StringBuffer content = new StringBuffer();
        try {
            in = openFileInput("data");
            //通过openFileInput对象获得一个目标文件为data的FileInputStream对象
            reader = new BufferedReader(new InputStreamReader(in));
            //利用FileInputStream构建InputStreamReader对象
            //再利用InputStreamReader对象构建BufferedReader对象
            String line = "";
            while((line = reader.readLine()) != null){
                content.append(line);//content是一个StringBuffer对象
            }
            //判断reader.reaLine()进行一行一行读取，直到文本中所有的文本内容全部读取
            //存放在BufferedReader对象reader中
        } catch (IOException e){
            e.printStackTrace();
        } finally {
            if(reader != null) { //reader不为空，则进行关闭
                try {
                    reader.close();//关闭reader
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
        return content.toString();
    }
}
