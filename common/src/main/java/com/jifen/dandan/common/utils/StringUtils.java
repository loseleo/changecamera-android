/**
 * Copyright 2016 JustWayward Team
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jifen.dandan.common.utils;

import android.text.TextUtils;

import java.text.DecimalFormat;

/**
 * Created by lfh on 2016/9/10.
 */
public class StringUtils {

    public static String creatAcacheKey(Object... param) {
        String key = "";
        for (Object o : param) {
            key += "-" + o;
        }
        return key.replaceFirst("-","");
    }

    /**
     * 格式化小说内容。
     * <p/>
     * <li>小说的开头，缩进2格。在开始位置，加入2格空格。
     * <li>所有的段落，缩进2格。所有的\n,替换为2格空格。
     *
     * @param str
     * @return
     */
    public static String formatContent(String str) {
        str = str.replaceAll("[ ]*", "");//替换来自服务器上的，特殊空格
        str = str.replaceAll("[ ]*", "");//
        str = str.replace("\n\n", "\n");
        str = str.replace("\n", "\n" + getTwoSpaces());
        str = getTwoSpaces() + str;
//        str = convertToSBC(str);
        return str;
    }

    /**
     * Return a String that only has two spaces.
     *
     * @return
     */
    public static String getTwoSpaces() {
        return "\u3000\u3000";
    }
    public static int toInt(String obj) {

        try {
            return Integer.parseInt(obj);
        } catch (Exception e) {
        }

        return 0;
    }

    public static float toFloat(String obj) {

        try {
            return Float.parseFloat(obj);
        } catch (Exception e) {
        }

        return 0;
    }
    public static double toDouble(String obj) {

        try {
            return Double.parseDouble(obj);
        } catch (Exception e) {
        }

        return 0;
    }

    public static String getHostName(String urlString) {
        String head = "";
        int index = urlString.indexOf("://");
        if (index != -1) {
            head = urlString.substring(0, index + 3);
            urlString = urlString.substring(index + 3);
        }
        index = urlString.indexOf("/");
        if (index != -1) {
            urlString = urlString.substring(0, index + 1);
        }
        return head + urlString;
    }

    public static String getDataSize(long var0) {
        DecimalFormat var2 = new DecimalFormat("###.00");
        return var0 < 1024L ? var0 + "bytes" : (var0 < 1048576L ? var2.format((double) ((float) var0 / 1024.0F))
                + "KB" : (var0 < 1073741824L ? var2.format((double) ((float) var0 / 1024.0F / 1024.0F))
                + "MB" : (var0 < 0L ? var2.format((double) ((float) var0 / 1024.0F / 1024.0F / 1024.0F))
                + "GB" : "error")));
    }

    public static String filterVersionMessage(String msg) {
        if (TextUtils.isEmpty(msg))
            return "";
        msg = msg.trim();
        msg = msg.replaceAll("\\s*", "");
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < msg.length(); i++) {
            if (isNumeric(msg.charAt(i) + "")) {
                sb.append("\n");
            }
            sb.append(msg.charAt(i));
        }
        return sb.toString();
    }

    public static boolean isNumeric(String str) {

        for (int i = str.length(); --i >= 0; ) {

            int chr = str.charAt(i);

            if (chr < 48 || chr > 57)

                return false;

        }

        return true;

    }

}
