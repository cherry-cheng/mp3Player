package com.example.chengyonghui.mp3demo.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.example.chengyonghui.mp3demo.model.Mp3Info;
import com.example.chengyonghui.mp3demo.mp3player.AppConstant;

import java.io.File;

/**
 * Created by chengyonghui on 2017/9/21.
 */
public class PlayerService extends Service {
    MediaPlayer mediaPlayer = null;

    private boolean isPlaying = false;
    private boolean isPause = false;
    private boolean isReleased = false;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Mp3Info mp3Info = (Mp3Info) intent.getSerializableExtra("mp3Info");
        int MSG = intent.getIntExtra("MSG", 0);
        if (mp3Info != null) {
            if (MSG == AppConstant.PlayerMsg.PAUSE_MSG) {
                play(mp3Info);
            } else {
                if (MSG == AppConstant.PlayerMsg.PAUSE_MSG) {
                    pause();
                } else if (MSG == AppConstant.PlayerMsg.STOP_MSG) {
                    stop();
                }
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void play(Mp3Info mp3Info) {

        String path = getMp3Path(mp3Info);
        mediaPlayer = MediaPlayer.create(this, Uri.parse("file://" + path));
        mediaPlayer.setLooping(false);
        mediaPlayer.start();
        isPlaying = true;
        isReleased = false;
    }

    private void pause() {
        if (mediaPlayer != null) {
            if (!isReleased) {
                if (!isPause) {
                    mediaPlayer.pause();
                    isPause = true;
                    isPlaying = true;
                }
            }
        }
    }

    private void stop() {
        if (mediaPlayer != null) {
            if (isPlaying) {
                if (!isReleased) {
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    isReleased = true;
                }
                isPlaying = false;
            }
        }
    }

    private String getMp3Path(Mp3Info mp3Info) {
        String SDCardRoot = Environment.getExternalStorageDirectory().getAbsolutePath();
        String path = SDCardRoot + File.separator + "mp3" + File.separator + mp3Info.getMp3Name();
        return path;
    }
}
