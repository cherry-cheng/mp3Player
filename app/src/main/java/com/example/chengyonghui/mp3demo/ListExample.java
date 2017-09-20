package com.example.chengyonghui.mp3demo;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Created by chengyonghui on 2017/9/20.
 */
public class ListExample extends ListActivity {


    private String[] msgStr;
    private ArrayAdapter<String> arrayAdapter;
    private int selectedItem = -1;
    @Override
    public void onCreate(Bundle savedIntanceState) {
        super.onCreate(savedIntanceState);
       // setContentView(R.layout.listexample_layout);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == 1) {
            msgStr = new String[] {
                    getResources().getString(R.string.about),
                    getResources().getString(R.string.main)
            };
            arrayAdapter = new ArrayAdapter<String>(ListExample.this,
                    R.layout.listexample_layout, msgStr);
            //ListExample.this.setListAdapter(arrayAdapter);
            super.setListAdapter(arrayAdapter);
        } else if (item.getItemId() == 2) {
            msgStr = new String[] {
                    getResources().getString(R.string.about),
                    getResources().getString(R.string.main)
            };
            arrayAdapter = new ArrayAdapter<String>(ListExample.this,
                    R.layout.listexample_layout, msgStr);
            //ListExample.this.setListAdapter(arrayAdapter);
            super.setListAdapter(arrayAdapter);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 1, 1, R.string.about);
        menu.add(0, 2, 2, R.string.main);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        selectedItem = position;
        Toast.makeText(this, msgStr[position], Toast.LENGTH_LONG).show();
        super.onListItemClick(l, v, position, id);
    }
}
