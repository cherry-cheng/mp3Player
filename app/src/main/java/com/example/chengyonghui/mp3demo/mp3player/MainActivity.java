package com.example.chengyonghui.mp3demo.mp3player;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;

import com.example.chengyonghui.mp3demo.R;

/**
 * Created by chengyonghui on 2017/9/21.
 */
public class MainActivity extends TabActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        //得到TabHost对象，对TabActivity的操作通常都有这个对象完成
        TabHost tabHost = getTabHost();
        //生成一个Intent对象，该对象指向一个Activity
        Intent remoteIntent = new Intent();
        remoteIntent.setClass(this, Mp3ListActivity.class);
        //生成一个TabSpec对象，这个对象代表一个页面
        TabHost.TabSpec remoteSpec = tabHost.newTabSpec("Remote");
        Resources res = getResources();
        //生成一个指示器
        remoteSpec.setIndicator("Remote", res.getDrawable(android.R.drawable.btn_dialog));
        //设置该页的内容
        remoteSpec.setContent(remoteIntent);
        //将设置好的TabSpec对象添加到TabHost当中
        tabHost.addTab(remoteSpec);

        Intent localIntent = new Intent();
        localIntent.setClass(this, LocalMp3ListActivity.class);
        TabHost.TabSpec localSpec = tabHost.newTabSpec("Local");
        localSpec.setIndicator("Local", res.getDrawable(android.R.drawable.btn_plus));
        localSpec.setContent(localIntent);
    }
}
