package com.example.chengyonghui.mp3demo.mp3player;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.chengyonghui.mp3demo.R;
import com.example.chengyonghui.mp3demo.model.Mp3Info;
import com.example.chengyonghui.mp3demo.service.PlayerService;

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

//    private ArrayList<Queue> queues = null;
    private TextView lrcTextView = null;
    private Mp3Info mp3Info = null;
/*    private android.os.Handler handler = new android.os.Handler();
    private UpdateTimeCallback updateTimeCallback = null;
    private long begin = 0;
    private long nextTimeMill = 0;
    private long currentTimeMill = 0;
    private String message = null;
    private long pauseTimeMills = 0;*/

    private IntentFilter intentFilter = null;
    private BroadcastReceiver receiver = null;


    //activity不可见
    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

    //activity可见
    @Override
    protected void onResume() {
        super.onResume();
        receiver = new LrcMessageBroadcastReceiver();
        registerReceiver(receiver, getIntentFilter());
    }

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
/*    private void prepareLrc(String lrcName) {
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
    }*/

    class BeginButtonListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            //通知Service开始播放Mp3
            Intent intent = new Intent();
            intent.setClass(PlayerActivity.this, PlayerService.class);
            intent.putExtra("mp3Info", mp3Info);
            intent.putExtra("MSG", AppConstant.PlayerMsg.PLAY_MSG);
            /*
            先读LRC文件再启动播放的Service
             */
            //读取LRC文件
            //prepareLrc(mp3Info.getLrcName());
            //启动service
            startService(intent);
            //将begin的值置为当前毫秒数
            //begin = System.currentTimeMillis();
            //执行updateTimeCallback,延后5毫秒执行
            //handler.postDelayed(updateTimeCallback, 5);
            //isPlaying = true;
        }
    }

    class PauseButtonListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setClass(PlayerActivity.this, PlayerService.class);
            intent.putExtra("MSG", AppConstant.PlayerMsg.PAUSE_MSG);
            startService(intent);

/*            if (isPlaying) {
                handler.removeCallbacks(updateTimeCallback);
                pauseTimeMills = System.currentTimeMillis();
            } else {
                handler.postDelayed(updateTimeCallback, 5);
                begin = System.currentTimeMillis() - pauseTimeMills + begin;
            }
            isPlaying = isPlaying ? false : true;*/
        }
    }

    class StopButtonListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setClass(PlayerActivity.this, PlayerActivity.class);
            intent.putExtra("MSG", AppConstant.PlayerMsg.STOP_MSG);
            startService(intent);
            //从Handler当中移除updateTimeCallback
            //handler.removeCallbacks(updateTimeCallback);
        }
    }

/*    class UpdateTimeCallback implements Runnable {
        ArrayList<Queue> queues = null;
        Queue times = null;
        Queue messages = null;
        public UpdateTimeCallback(ArrayList<Queue> queues) {
            //从ArrayList当中取出相应的对象对象
            //this.queues = queues;
            times = queues.get(0);
            messages = queues.get(1);
        }
        @Override
        public void run() {
            //Queue times = queues.get(0);
            //Queue messages = queues.get(1);
            //计算偏移量，也就是说从开始播放Mp3到现在为止，共消耗了多少时间，以毫秒为单位
            long offset = System.currentTimeMillis() - begin;
            System.out.println(offset);
            if (currentTimeMill == 0) {
                nextTimeMill = (Long) times.poll();
                message = (String) messages.poll();
            }
            if (offset >= nextTimeMill) {
                lrcTextView.setText(message);
                message = (String) messages.poll();
                nextTimeMill = (Long) times.poll();
            }
            currentTimeMill = currentTimeMill + 10;
            //每10毫秒执行一次run函数
            handler.postDelayed(updateTimeCallback, 10);
        }
    }*/

    //广播接收器，主要作用是接受Service所发送的广播，并且更新UI，也就是放置歌词的TextView
    class LrcMessageBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String lrcMessage = intent.getStringExtra("lrcMessage");
            lrcTextView.setText(lrcMessage);
        }
    }

    private IntentFilter getIntentFilter() {
        if (intentFilter == null) {
            intentFilter = new IntentFilter();
            intentFilter.addAction(AppConstant.LRC_MESSAGE_ACTION);
        }
        return intentFilter;
    }


}
