package jian2020.nuc.playvideotest.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;

import jian2020.nuc.playvideotest.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private VideoView videoView = null;

    private Button playBt = null;

    private Button pauseBt = null;

    private Button replayBt = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_layout);

        init();

        playBt.setOnClickListener(this);

        pauseBt.setOnClickListener(this);

        replayBt.setOnClickListener(this);

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        } else {
            initVideoPath(); //初始化MediaPlayer
        }
    }

    private void init(){
        playBt = (Button) findViewById(R.id.playBt);

        pauseBt = (Button) findViewById(R.id.pauseBt);

        replayBt = (Button) findViewById(R.id.replayBt);

        videoView = (VideoView) findViewById(R.id.video_view);
    }

    private void initVideoPath(){
        File file = new File(Environment.getExternalStorageDirectory(), "movie.mp4");
        videoView.setVideoPath(file.getPath());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    initVideoPath();
                } else {
                    Toast.makeText(this, "拒绝权限将无法使用程序", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
                default:
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.playBt:
                if (!videoView.isPlaying()){
                    videoView.start();//开始播放
                }
                break;
            case R.id.pauseBt:
                if (videoView.isPlaying()){
                    videoView.pause();//暂停播放
                }
                break;
            case R.id.replayBt:
                if (videoView.isPlaying()){
                    videoView.resume();//重新播放
                }
                break;
                default:
        }
    }

    protected void onDestory(){
        super.onDestroy();
        if (videoView != null){
            videoView.suspend();
        }
    }
}
