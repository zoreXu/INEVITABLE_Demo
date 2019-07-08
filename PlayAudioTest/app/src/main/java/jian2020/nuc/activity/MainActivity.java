package jian2020.nuc.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private Button playbt = null;

    private Button pausebt = null;

    private Button stopbt = null;

    private MyLinstener myLinstener = null;

    private MediaPlayer mediaPlayer = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        playbt.setOnClickListener(myLinstener);

        pausebt.setOnClickListener(myLinstener);

        stopbt.setOnClickListener(myLinstener);

        //对SD卡读写权限的检查
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        } else {
            initMediaPlayer(); //初始化MediaPlayer
        }
    }

    private void initMediaPlayer(){
        try {
            File file = new File(Environment.getExternalStorageDirectory(),
                    "music.mp3");
            mediaPlayer.setDataSource(file.getPath());
            //指定音频文件的路径

            mediaPlayer.prepare();
            //让MediaPlayer进入到准备状态
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void init(){

        playbt = (Button) findViewById(R.id.playbt);

        pausebt = (Button) findViewById(R.id.pausebt);

        stopbt = (Button) findViewById(R.id.stopbtn);

        mediaPlayer = new MediaPlayer();

        myLinstener = new MyLinstener();

    }

    private class MyLinstener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.playbt:
                    if (!mediaPlayer.isPlaying()){
                        //判断mediaPlayer是否在播放
                        //没在播放，则点击start按钮开始
                        mediaPlayer.start();
                        Log.d("MainActivity", "paly");
                    }
                    break;
                case R.id.pausebt:
                    if (mediaPlayer.isPlaying()){
                        //判断mediaPlayer是否在播放
                        //在播放，则点击pause按钮暂停
                        mediaPlayer.pause();
                        Log.d("MainActivity", "pause");
                    }
                    break;
                case R.id.stopbtn:
                    if (mediaPlayer.isPlaying()){
                        //判断mediaPlayer是否在播放
                        //在播放，点击stop按钮，则停止播放
                        mediaPlayer.reset();
                        initMediaPlayer();
                        Log.d("MainActivity", "stop");
                    }
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer.release();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    initMediaPlayer();
                }else {
                    Toast.makeText(this, "拒绝权限无法使用程序",
                            Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
                break;
        }
    }
}
