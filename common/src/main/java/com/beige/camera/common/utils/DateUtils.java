package com.beige.camera.common.utils;

import java.util.Calendar;

public class DateUtils {

    /**
     * 获取今日剩余时间
     *
     * @param currentTimeMillions
     * @return
     */
    public static long getTodayRestTimeMillions(long currentTimeMillions) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(currentTimeMillions);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        calendar.clear();
        calendar.set(year, month, day);
        return 24 * 60 * 60 * 1000 - (currentTimeMillions - calendar.getTimeInMillis());
    }

    /**
     * 是否是同一天
     * @param time1
     * @param time2
     * @return
     */
    public static boolean isSameDay(long time1, long time2) {

        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTimeInMillis(time1);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTimeInMillis(time2);
        return calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR)
                && calendar1.get(Calendar.MONTH) == calendar2.get(Calendar.MONTH)
                && calendar1.get(Calendar.DAY_OF_MONTH) == calendar2.get(Calendar.DAY_OF_MONTH);
    }
}
