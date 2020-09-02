package com.jifen.dandan.ringtone.permission;

public class SystemProperties {

    public static String getString(String str, String str2) {
        try {
            return (String) Class.forName("android.os.SystemProperties").getMethod("get", new Class[]{String.class, String.class}).invoke(null, new Object[]{str, str2});
        } catch (Exception unused) {
            return str2;
        }
    }

    public static String getString(String str) {
        try {
            return (String) Class.forName("android.os.SystemProperties").getMethod("get", new Class[]{String.class}).invoke(null, new Object[]{str});
        } catch (Exception unused) {
            return null;
        }
    }

    public static long getLong(String str, long j) {
        try {
            return ((Long) Class.forName("android.os.SystemProperties").getMethod("getLong", new Class[]{String.class, Long.TYPE}).invoke((Object) null, new Object[]{str, Long.valueOf(j)})).longValue();
        } catch (Exception unused) {
            return j;
        }
    }

    public static int getInt(String str, int i) {
        try {
            return ((Integer) Class.forName("android.os.SystemProperties").getMethod("getInt", new Class[]{String.class, Integer.TYPE}).invoke((Object) null, new Object[]{str, Integer.valueOf(i)})).intValue();
        } catch (Exception unused) {
            return i;
        }
    }

}
