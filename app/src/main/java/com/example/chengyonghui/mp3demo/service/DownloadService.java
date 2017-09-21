package com.example.chengyonghui.mp3demo.service;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import com.example.chengyonghui.mp3demo.R;
import com.example.chengyonghui.mp3demo.download.HttpDownloader;
import com.example.chengyonghui.mp3demo.model.Mp3Info;

/**
 * Created by chengyonghui on 2017/9/21.
 */
public class DownloadService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    //每次用户点击ListActivity当中的一个条目时，就会调用该方法
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //从Intent对象当中将Mp3Info对象取出
        Mp3Info mp3Info = (Mp3Info) intent.getSerializableExtra("mp3Info");
        //每下载一个音乐文件都要在单独的线程中执行
        DownloadThread downloadThread = new DownloadThread(mp3Info);
        Thread thread = new Thread(downloadThread);
        thread.start();
        System.out.println("service------------>" + mp3Info);
        return super.onStartCommand(intent, flags, startId);
    }

    class DownloadThread implements Runnable {
        private Mp3Info mp3Info = null;
        public DownloadThread(Mp3Info mp3Info) {
            this.mp3Info = mp3Info;
        }
        @Override
        public void run() {
            //下载地址：http://localhost:8080/mp3/a1.mp3
            //根据Mp3文件的名字，生成下载地址
            String mp3Url = "http://localhost:8080/mp3/" + mp3Info.getMp3Name();
            //生成下载文件所有的对象
            HttpDownloader httpDownloader = new HttpDownloader();
            //下载并存储到SDCard当中
            int result = httpDownloader.downFile(mp3Url, "mp3/", mp3Info.getMp3Name());
            String resultMessage = null;
            if (result == -1) {
                resultMessage = "下载失败";
            } else if (result == 0) {
                resultMessage = "文件已经存在，不需要重复下载";
            } else if (result == 1) {
                resultMessage = "文件下载成功";
            }

            //使用Notification提示客户下载结果
            UserNotification(resultMessage);
        }
    }

    //发送notification给用户
    public void UserNotification(String result) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Just a Notification")
                .setContentText(result);
        notificationManager.notify(1, builder.build());
    }
}
