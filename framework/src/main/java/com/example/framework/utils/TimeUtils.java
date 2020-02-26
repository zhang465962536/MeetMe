package com.example.framework.utils;
//时间转换类
public class TimeUtils {

    /**
     * 转换毫秒格式 HH:mm:ss
     * 1s = 1000ms
     * 1m = 60s
     * 1h = 60m
     * 1d = 24h
     *
     * @param ms
     */
    public static String formatDuring(long ms) {
        long hours = ((ms + 28800000) % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
        long minutes = (ms % (1000 * 60 * 60)) / (1000 * 60);
        long seconds = (ms % (1000 * 60)) / 1000;

        //24小时进制  如果小时，分钟，秒为个位数 就需要补零在前面
        String h = hours + "";
        if (hours < 10) {
            h = "0" + h;
        }

        String m = minutes + "";
        if (minutes < 10) {
            m = "0" + m;
        }

        String s = seconds + "";
        if (seconds < 10) {
            s = "0" + s;
        }

        return h + ":" + m + ":" + s;
    }

}
