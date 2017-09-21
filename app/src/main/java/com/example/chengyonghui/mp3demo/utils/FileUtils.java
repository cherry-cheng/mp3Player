package com.example.chengyonghui.mp3demo.utils;

import android.os.Environment;

import com.example.chengyonghui.mp3demo.model.Mp3Info;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chengyonghui on 2017/9/20.
 */
public class FileUtils {

    private String SDcardRoot;
    public String getSDcardRoot() {
        return SDcardRoot;
    }
    public FileUtils() {
        //得到当前外部存储设备的目录
        SDcardRoot = Environment.getExternalStorageDirectory().getAbsolutePath();
    }

    /*
    在SD卡上创建文件
     */
    public File createFileInSDCard(String fileName, String dir) throws IOException {
        File file = new File(SDcardRoot + dir + File.separator + fileName);
        System.out.println("file ------->" + file);
        file.createNewFile();
        return file;
    }

    /*
    在SD卡上创建目录
     */
    public File createSDDir(String dir) {
        File dirFile = new File(SDcardRoot + dir + File.separator);
        dirFile.mkdir();
        return dirFile;
    }

    /*
    判断SD上的文件是否存在
     */
    public boolean isFileExist(String fileName, String path) {
        File file = new File(SDcardRoot + path + File.separator + fileName);
        return file.exists();
    }

    /*
    将一个InputStream里面的数据写入到SD卡中
     */
    public File write2SDFromInput(String path, String fileName, InputStream inputStream) {
        File file = null;
        OutputStream output = null;
        try {
            //调用自己类中写的方法
            createSDDir(path);
            file = createFileInSDCard(fileName, path);
            output = new FileOutputStream(file);
            byte buffer [] = new byte[4 * 1024];
            int temp;
            while ((temp = inputStream.read(buffer)) != -1) {
                output.write(buffer, 0, temp);
            }
            output.flush();//写完之后，记得清除缓存
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                output.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    /*
    读取目录中的Mp3文件的名字和大小
     */
    public List<Mp3Info> getMp3Files(String path) {
        List<Mp3Info> mp3Infos = new ArrayList<Mp3Info>();
        File file = new File(SDcardRoot + File.separator + path);
        File [] files = file.listFiles();
        for (int i = 0; i < files.length; i ++) {
            if (files[i].getName().endsWith("mp3")) {
                Mp3Info mp3Info = new Mp3Info();
                mp3Info.setMp3Name(files[i].getName());
                mp3Info.setMp3Size(files[i].length() + "");
                mp3Infos.add(mp3Info);
            }
        }
        return mp3Infos;
    }

}
