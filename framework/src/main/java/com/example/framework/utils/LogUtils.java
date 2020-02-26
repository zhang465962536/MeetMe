package com.example.framework.utils;

import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.example.framework.BuildConfig;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;

//LogUtil 不仅仅可以打印日志 还可以将日志写入文件中 进行记录
public class LogUtils {

    private static SimpleDateFormat mSimpleDateFormat =
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static void i(String text) {
        //因为打印log 只有在调试运行才使用 正式发布并不会使用 所以先判断是否是调试运行 再去判空比较稳妥
        if (BuildConfig.LOG_DEBUG) {
            if (!TextUtils.isEmpty(text)) {
                Log.i(BuildConfig.LOG_TAG, text);
                //writeToFile(text);
            }
        }
    }

    public static void e(String text) {
        if (BuildConfig.LOG_DEBUG) {
            if (!TextUtils.isEmpty(text)) {
                Log.e(BuildConfig.LOG_TAG, text);
                //writeToFile(text);
            }
        }
    }

    //将日志写入 存储空间
    public static void writeToFile(String text) {
        //开始写入
        FileOutputStream fileOutputStream = null;
        BufferedWriter bufferedWriter = null;
        try {
            //文件路径
            String fileRoot = Environment.getExternalStorageDirectory().getPath() + "/Meet/";
            String fileName = "Meet.log";
            //log 写入内容    时间 + 内容
            String log = mSimpleDateFormat.format(new Date()) + " " + text + "\n";
            //检查父路径
            File fileGroup = new File(fileRoot);
            //如果不存在根目录 就创建
            if (!fileGroup.exists()) {
                fileGroup.mkdirs();
            }
            //创建文件
            File fileChild = new File(fileRoot + fileName);
            if (!fileChild.exists()) {
                //如果该存储日志文件不存在 就创建
                fileChild.createNewFile();
            }
            //true 代表追加到原来得文本 不是覆盖文本
            fileOutputStream = new FileOutputStream(fileRoot + fileName, true);
            //编码问题 确定是 GBK编码 才能 正确的 写入文件
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(fileOutputStream, Charset.forName("gbk")));
            bufferedWriter.write(log);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            e(e.toString());
        } catch (IOException e) {
            e.printStackTrace();
            e(e.toString());
        } finally {
            if (bufferedWriter != null) {
                try {
                    bufferedWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
