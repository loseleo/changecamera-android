package com.beige.camera.common.utils;

import android.text.TextUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import java.util.Locale;

/**
 * 类描述：视频时间格式化
 * Created by 殴打小熊猫 on 2018/4/18.
 */

public class TimeUtils {

    private static final DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

    /**
     * 格式化时间
     *
     * @param timeMs
     * @return
     */
    public static String formatTime(int timeMs) {
        int totalSeconds = timeMs / 1000;
        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;

        StringBuilder mFormatBuilder = new StringBuilder();
        Formatter mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());
        mFormatBuilder.setLength(0);
        if (hours > 0) {
            return mFormatter.format("%d:%02d:%02d", hours, minutes, seconds).toString().trim();
        } else {
            return mFormatter.format("%02d:%02d", minutes, seconds).toString().trim();
        }
    }

    /**
     * date2比date1多的自然天数 比较最小单位为天数 忽略时分秒
     *
     * @param sdata1
     * @param sdata2
     * @return
     */
    public static int differentDays(String sdata1, String sdata2) {
        if (TextUtils.isEmpty(sdata1) || TextUtils.isEmpty(sdata2)
                || sdata1 == "" || sdata2 == "") {
            return -1;
        }
        Date date1 = null;
        Date date2 = null;
        try {
            date1 = formatter.parse(sdata1);
            date2 = formatter.parse(sdata2);
            Calendar cal1 = Calendar.getInstance();
            cal1.setTime(date1);
            Calendar cal2 = Calendar.getInstance();
            cal2.setTime(date2);
            int day1 = cal1.get(Calendar.DAY_OF_YEAR);
            int day2 = cal2.get(Calendar.DAY_OF_YEAR);
            int year1 = cal1.get(Calendar.YEAR);
            int year2 = cal2.get(Calendar.YEAR);
            if (year1 != year2) {  //不同年
                int timeDistance = 0;
                for (int i = year1; i < year2; i++) {
                    if (i % 4 == 0 && i % 100 != 0 || i % 400 == 0) {   //闰年
                        timeDistance += 366;
                    } else {   //不是闰年
                        timeDistance += 365;
                    }
                }
                return timeDistance + (day2 - day1);
            } else {   //同一年
                return day2 - day1;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return -1;
    }


    /**
     * 获取当前时间的YYYY-MM-dd格式的时间
     * @return
     */
    public static String getCurrentDate(){
        return new SimpleDateFormat("YYYY-MM-dd").format(new Date());
    }
}
