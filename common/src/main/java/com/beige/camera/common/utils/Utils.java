package com.beige.camera.common.utils;

import java.math.BigDecimal;

/**
 * Author: maxiaoxun@qutoutiao.net
 * Time: 2019/8/16 15:19
 */
public class Utils {
    public static String formattingNum(int number) {
        String strNumber = number + "";
        if (number <= 0) {
            return "0";
        } else if (number >= 10000 && number < 100000000) {
            double num = (double) number / 10000 * 1.0d;//1.将数字转换成以万为单位的数字
            BigDecimal b = new BigDecimal(num);
            double result = b.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();//2.转换后的数字四舍五入保留小数点后两位;
            return result + "万";
        } else if (number >= 100000000) {
            double num = (double) number / 100000000 * 1.0d;//1.将数字转换成以亿为单位的数字
            BigDecimal b = new BigDecimal(num);
            double result = b.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();//2.转换后的数字四舍五入保留小数点后两位;
            return result + "亿";
        } else {
            return strNumber;
        }
    }

    public static String formattingNum(long number) {
        String strNumber = number + "";
        if (number <= 0) {
            return "0";
        } else if (number >= 10000 && number < 100000000) {
            double num = (double) number / 10000 * 1.0d;//1.将数字转换成以万为单位的数字
            BigDecimal b = new BigDecimal(num);
            double result = b.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();//2.转换后的数字四舍五入保留小数点后两位;
            return result + "W";
        } else if (number >= 100000000) {
            double num = (double) number / 100000000 * 1.0d;//1.将数字转换成以亿为单位的数字
            BigDecimal b = new BigDecimal(num);
            double result = b.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();//2.转换后的数字四舍五入保留小数点后两位;
            return result + "亿";
        } else {
            return strNumber;
        }
    }

}
