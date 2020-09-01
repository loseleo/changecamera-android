package com.jifen.dandan.common.utils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.Serializable;

/**
 * @author wuyufeng 2019/11/06.
 */
public class BundleUtil {

    @NonNull
    public static Bundle safeCopyOf(@Nullable Bundle bundle) {
        return (bundle != null) ? new Bundle(bundle) : new Bundle();
    }

    @NonNull
    public static Bundle getExtras(@Nullable Activity activity) {
        return getExtras(getIntent(activity));
    }

    @NonNull
    public static Bundle getExtras(@Nullable Intent intent) {
        if (intent != null) {
            Bundle extras = intent.getExtras();
            if (extras != null) {
                return extras;
            }
        }
        return new Bundle();
    }

    @Nullable
    private static Intent getIntent(@Nullable Activity activity) {
        return activity == null ? null : activity.getIntent();
    }

    public static <T extends Serializable> T getSerializable(@Nullable Bundle bundle, String key, Class<T> cls) {
        if (bundle == null) {
            return null;
        }
        try {
            return (T) bundle.getSerializable(key);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getString(@Nullable Bundle bundle, String key, String defaultValue) {
        if (bundle == null) {
            return defaultValue;
        }

        Object object = bundle.get(key);
        if (object == null) {
            return defaultValue;
        }

        if (object instanceof String) {
            return (String) object;
        }

        if (object instanceof Number ||
                object instanceof Character ||
                object instanceof Boolean) {
            return object.toString();
        }
        return defaultValue;
    }

    public static boolean getBoolean(@Nullable Bundle bundle, String key, boolean defaultValue) {
        if (bundle == null) {
            return defaultValue;
        }

        Object object = bundle.get(key);
        if (object == null) {
            return defaultValue;
        }

        if (object instanceof Boolean) {
            return (Boolean) object;
        }

        if (object instanceof String) {
            try {
                return parseBoolean((String) object);
            } catch (Exception ignored) {
            }
        }
        return defaultValue;

    }

    public static int getInt(@Nullable Bundle bundle, String key, int defaultValue) {
        if (bundle == null) {
            return defaultValue;
        }

        Object object = bundle.get(key);
        if (object == null) {
            return defaultValue;
        }

        if (object instanceof Integer) {
            return (Integer) object;
        }

        if (object instanceof String) {
            try {
                return parseInt((String) object);
            } catch (Exception ignored) {

            }
        }
        return defaultValue;
    }

    public static float getFloat(@Nullable Bundle bundle, String key, float defaultValue) {
        if (bundle == null) {
            return defaultValue;
        }

        Object object = bundle.get(key);
        if (object == null) {
            return defaultValue;
        }

        if (object instanceof Float) {
            return (Float) object;
        }

        if (object instanceof String) {
            try {
                return parseFloat((String) object);
            } catch (Exception ignored) {
            }
        }
        return defaultValue;
    }

    public static long getLong(@Nullable Bundle bundle, String key, long defaultValue) {
        if (bundle == null) {
            return defaultValue;
        }

        Object object = bundle.get(key);
        if (object == null) {
            return defaultValue;
        }

        if (object instanceof Long) {
            return (Long) object;
        }

        if (object instanceof String) {
            try {
                return parseLong((String) object);
            } catch (Exception ignored) {

            }
        }
        return defaultValue;
    }

    public static double getDouble(@Nullable Bundle bundle, String key, double defaultValue) {
        if (bundle == null) {
            return defaultValue;
        }

        Object object = bundle.get(key);
        if (object == null) {
            return defaultValue;
        }

        if (object instanceof Double) {
            return (Double) object;
        }

        if (object instanceof String) {
            try {
                return parseDouble((String) object);
            } catch (Exception ignored) {
            }
        }
        return defaultValue;
    }

    private static int parseInt(String value) {
        return Integer.parseInt(value);
    }

    private static boolean parseBoolean(String value) {
        return Boolean.parseBoolean(value);
    }

    private static long parseLong(String value) {
        return Long.parseLong(value);
    }

    private static float parseFloat(String value) {
        return Float.parseFloat(value);
    }

    private static double parseDouble(String value) {
        return Double.parseDouble(value);
    }
}
