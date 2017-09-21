package com.example.chengyonghui.mp3demo.mp3player;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.chengyonghui.mp3demo.R;
import com.example.chengyonghui.mp3demo.lrc.LrcProcessor;
import com.example.chengyonghui.mp3demo.model.Mp3Info;
import com.example.chengyonghui.mp3demo.service.PlayerService;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Queue;

/**
 * Created by chengyonghui on 2017/9/21.
 */
public class PlayerActivity extends AppCompatActivity {
    ImageButton beginButton = null;
    ImageButton pauseButton = null;
    ImageButton stopButton = null;
    MediaPlayer mediaPlayer = null;

/*    private boolean isPlaying = false;
    private boolean isPause = false;
    private boolean isReleased = false;*/

    private ArrayList<Queue> queues = null;
    private TextView lrcTextView = null;
    private Mp3Info mp3Info = null;
    private android.os.Handler handler = new android.os.Handler();
    private UpdateTimeCallback updateTimeCallback = null;
    private long begin = 0;
    private long nextTimeMill = 0;
    private long currentTimeMill = 0;
    private String message = null;
    private long pauseTimeMills = 0;
    private boolean isPlaying = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player);
        Intent intent = getIntent();
        mp3Info = (Mp3Info) intent.getSerializableExtra("mp3Info");
        beginButton = (ImageButton) findViewById(R.id.begin);
        beginButton.setOnClickListener(new BeginButtonListener());
        pauseButton = (ImageButton) findViewById(R.id.pause);
        pauseButton.setOnClickListener(new PauseButtonListener());
        stopButton = (ImageButton) findViewById(R.id.stop);
        stopButton.setOnClickListener(new StopButtonListener());
        lrcTextView = (TextView) findViewById(R.id.lrcTextView);
        System.out.println("mp3Infoplayer------------->" + mp3Info);
    }

    /*
    根据歌词文件的名字，来读取歌词文件当中的信息
     */
    private void prepareLrc(String lrcName) {
        try {
            InputStream inputStream = new FileInputStream(Environment.getExternalStorageDirectory().getAbsolutePath());
            LrcProcessor lrcProcessor = new LrcProcessor();
            queues = lrcProcessor.process(inputStream);
            //创建一个UpdateTimeCallback对象
            updateTimeCallback = new UpdateTimeCallback(queues);
            begin = 0;
            currentTimeMill = 0;
            nextTimeMill = 0;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    class BeginButtonListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setClass(PlayerActivity.this, PlayerService.class);
            intent.putExtra("mp3Info", mp3Info);
            intent.putExtra("MSG", AppConstant.PlayerMsg.PLAY_MSG);
            prepareLrc(mp3Info.getLrcName());
            startService(intent);
            begin = System.currentTimeMillis();
            handler.postDelayed(updateTimeCallback, 5);
            isPlaying = true;
        }
    }

    class PauseButtonListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
        }
    }

    class StopButtonListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
        }
    }

    class UpdateTimeCallback implements Runnable {
        ArrayList<Queue> queues = null;
        public UpdateTimeCallback(ArrayList<Queue> queues) {
            this.queues = queues;
        }
        @Override
        public void run() {
            Queue times = queues.get(0);
            Queue messages = queues.get(1);
            long offset = System.currentTimeMillis() - begin;
            System.out.println(offset);
            if (currentTimeMill == 0) {
                nextTimeMill = (Long) times.poll();
                message = (String) messages.poll();
                lrcTextView.setText(message);
            }
            if (offset >= nextTimeMill) {
                lrcTextView.setText(message);
                message = (String) messages.poll();
                nextTimeMill = (Long) times.poll();
            }
            currentTimeMill = currentTimeMill + 10;
            handler.postDelayed(updateTimeCallback, 10);
        }
    }


}
