package com.example.chengyonghui.mp3demo.mp3player;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.chengyonghui.mp3demo.R;
import com.example.chengyonghui.mp3demo.download.HttpDownloader;
import com.example.chengyonghui.mp3demo.model.Mp3Info;
import com.example.chengyonghui.mp3demo.service.DownloadService;
import com.example.chengyonghui.mp3demo.xml.Mp3ListContentHandler;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.SAXParserFactory;

/**
 * Created by chengyonghui on 2017/9/19.
 */
public class Mp3ListActivity extends AppCompatActivity {
    private static final int UPDATE = 1;
    private static final int ABOUT = 2;
    private ListView listView = null;
    private List<Mp3Info> mp3Infos = null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.remote_mp3_list);
        listView = (ListView) findViewById(R.id.listView);
        listView.setOnItemClickListener(new ListViewItemClickListener());
        updateListView();
        //getListView();
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.remote_mp3_list);
    }

    /*
    在点击menu按钮之后，会调用该方法
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, UPDATE, 1, R.string.mp3list_update);
        menu.add(0, ABOUT, 2, R.string.mp3list_about);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == UPDATE) {
            updateListView();
        } else if (item.getItemId() == ABOUT) {
            //用户点击了关于按钮

        }
        System.out.println("getItem---------->" + item.getItemId());
        return super.onOptionsItemSelected(item);
    }

    private String downloadXML(String urlStr) {
        HttpDownloader httpDownloader = new HttpDownloader();
        String result = httpDownloader.download(urlStr);
        return result;
    }

    private List<Mp3Info> parse(String xmlStr) {
        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        List<Mp3Info> infos = new ArrayList<Mp3Info>();
        try {
            XMLReader xmlReader = saxParserFactory.newSAXParser().getXMLReader();
            Mp3ListContentHandler mp3ListContentHandler = new Mp3ListContentHandler(infos);
            xmlReader.setContentHandler(mp3ListContentHandler);
            xmlReader.parse(new InputSource(new StringReader(xmlStr)));
            for (Iterator iterator = infos.iterator(); iterator.hasNext();) {
                Mp3Info mp3Info = (Mp3Info) iterator.next();
                System.out.println(mp3Info.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return infos;
    }

    private SimpleAdapter buildSimpleAdapter(List<Mp3Info> mp3Infos) {
        List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        for (Iterator iterator = mp3Infos.iterator(); iterator.hasNext();) {
            Mp3Info mp3Info = (Mp3Info) iterator.next();
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("map3_name", mp3Info.getMp3Name());
            map.put("map3_size", mp3Info.getMp3Size());
            list.add(map);
        }
        SimpleAdapter simpleAdapter = new SimpleAdapter(this, list, R.layout.mp3info_item, new String[]{"map3_name", "mp3_size"}, new int[]{R.id.mp3_name, R.id.mp3_size});
        return simpleAdapter;
    }

    private void updateListView() {
        //用户点击了更新列表按钮
        String xml = downloadXML("http://localhost:8080/mp3/resources.xml");
        mp3Infos = parse(xml);
        SimpleAdapter simpleAdapter = buildSimpleAdapter(mp3Infos);
//        setListAdapter(simpleAdapter);
        listView.setAdapter(simpleAdapter);
        System.out.println("xml--------->" + xml);
    }

    class ListViewItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            /*
            1、得到用户所要下载的MP3文件的名称
            2、访问网络下载MP3文件
                  需要创建一个Service；
                  每个文件的下载，都需要在一个独立的线程当中进行；
            3、通知客户下载的结果
             */

            //根据用户点击列表当中的位置来得到响应的Mp3Info对象
            Mp3Info mp3Info = mp3Infos.get(position);
            System.out.println("mp3Info---------->" + mp3Info);
            Intent intent = new Intent();
            //将Mp3Info对象存入到Intent对象当中，需要mp3Info是可序列化的
            intent.putExtra("mp3Info", mp3Info);
            intent.setClass(Mp3ListActivity.this, DownloadService.class);
            startService(intent);
        }
    }
}
