package com.example.jian2020.nuc.broadcastbestpractice;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends BaseActivity {

    private EditText accountET = null;

    private EditText passwordET = null;

    private Button loginBtn = null;

    private CheckBox rememberPass = null;

    private SharedPreferences pref = null;

    private SharedPreferences.Editor editor = null;

    private boolean isRemember;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();

        if (isRemember){ //第一次登入时isRemember是false，第二次登入时如果选择记住密码则isRemember是true
            //将账号和密码都设置到文本框中
            String account = pref.getString("account", "");//以键值对方式读取数据，默认空
            String password = pref.getString("password", "");
            accountET.setText(account);
            passwordET.setText(password);
            rememberPass.setChecked(true);
        }

        loginBtn.setOnClickListener(new MyLinstener());
    }

    //初始化
    public void init(){
        accountET = (EditText) findViewById(R.id.accoutnET);

        passwordET = (EditText) findViewById(R.id.passwordET);

        loginBtn = (Button) findViewById(R.id.loginBtn);

        rememberPass = (CheckBox) findViewById(R.id.remember_pass);

        pref = PreferenceManager.getDefaultSharedPreferences(this);

        isRemember = pref.getBoolean("remember_pass", false);//读取键为remember_pass的值，默认为false
    }

    private class MyLinstener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            String account = accountET.getText().toString().trim();
            String password = passwordET.getText().toString().trim();
            //如果账号是admin且密码是123456,就认为登入成功
            if(account.equals("admin") && password.equals("123456")) {
                editor = pref.edit();
                if (rememberPass.isChecked()){ //检查复选框是否被选中
                    editor.putBoolean("remember_pass", true);
                    editor.putString("account", account);
                    editor.putString("password", password);
                }else {
                    editor.clear();
                }
                editor.apply();//数据提交
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }else {
                Toast.makeText(LoginActivity.this, "account or password is invalid", Toast.LENGTH_LONG).show();
            }
        }
    }
}
