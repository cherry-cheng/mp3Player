package com.example.chengyonghui.mp3demo.mp3player;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.chengyonghui.mp3demo.R;

/**
 * Created by chengyonghui on 2017/9/19.
 */
public class Mp3ListActivity extends ListActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /*
    在点击menu按钮之后，会调用该方法
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 1, 1, R.string.mp3list_update);
        menu.add(0, 2, 2, R.string.mp3list_about);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
