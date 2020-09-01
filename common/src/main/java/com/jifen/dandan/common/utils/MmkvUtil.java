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

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Base64;

import com.tencent.mmkv.MMKV;
import com.zhangqiang.options.Option;
import com.zhangqiang.options.Options;
import com.zhangqiang.options.impl.JsonOption;
import com.zhangqiang.options.store.BooleanStore;
import com.zhangqiang.options.store.IntStore;
import com.zhangqiang.options.store.LongStore;
import com.zhangqiang.options.store.StringStore;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by lfh on 2016/8/13.
 */
public class MmkvUtil {

    private static MmkvUtil mmkvUtil;
    private static final String MMKV_PREFERENCES = "_preference";
    private MMKV mMkv;
    public Context context;

    public synchronized static MmkvUtil getInstance() {
        return mmkvUtil;
    }

    public static void init(Context context) {
        mmkvUtil = new MmkvUtil();
        MMKV.initialize(context);
        mmkvUtil.mMkv = MMKV.mmkvWithID(MMKV_PREFERENCES, MMKV.SINGLE_PROCESS_MODE);
        mmkvUtil.context = context;

    }


    public boolean getBoolean(String key, boolean defaultVal) {
        return this.mMkv.getBoolean(key, defaultVal);
    }

    public boolean getBoolean(String key) {
        return this.mMkv.getBoolean(key, false);
    }


    public String getString(String key, String defaultVal) {
        return this.mMkv.getString(key, defaultVal);
    }

    public String getString(String key) {
        return this.mMkv.getString(key, null);
    }

    public int getInt(String key, int defaultVal) {
        return this.mMkv.getInt(key, defaultVal);
    }

    public int getInt(String key) {
        return this.mMkv.getInt(key, 0);
    }


    public float getFloat(String key, float defaultVal) {
        return this.mMkv.getFloat(key, defaultVal);
    }

    public float getFloat(String key) {
        return this.mMkv.getFloat(key, 0f);
    }

    public long getLong(String key, long defaultVal) {
        return this.mMkv.getLong(key, defaultVal);
    }

    public long getLong(String key) {
        return this.mMkv.getLong(key, 0l);
    }

    public Set<String> getStringSet(String key, Set<String> defaultVal) {
        return this.mMkv.getStringSet(key, defaultVal);
    }

    public Set<String> getStringSet(String key) {
        return this.mMkv.getStringSet(key, null);
    }

    public Map<String, ?> getAll() {
        return this.mMkv.getAll();
    }

    public boolean exists(String key) {
        return mMkv.contains(key);
    }


    public MmkvUtil putString(String key, String value) {
        mMkv.putString(key, value);
        mMkv.commit();
        return this;
    }

    public MmkvUtil putInt(String key, int value) {
        mMkv.putInt(key, value);
        mMkv.commit();
        return this;
    }

    public MmkvUtil putFloat(String key, float value) {
        mMkv.putFloat(key, value);
        mMkv.commit();
        return this;
    }

    public MmkvUtil putLong(String key, long value) {
        mMkv.putLong(key, value);
        mMkv.commit();
        return this;
    }

    public MmkvUtil putBoolean(String key, boolean value) {
        mMkv.putBoolean(key, value);
        mMkv.commit();
        return this;
    }

    public void commit() {
        mMkv.commit();
    }

    public MmkvUtil putStringSet(String key, Set<String> value) {
        mMkv.putStringSet(key, value);
        mMkv.commit();
        return this;
    }

    public void putObject(String key, Object object) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream out = null;
        try {
            out = new ObjectOutputStream(baos);
            out.writeObject(object);
            String objectVal = new String(Base64.encode(baos.toByteArray(), Base64.DEFAULT));
            mMkv.putString(key, objectVal);
            mMkv.commit();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public <T> T getObject(String key, Class<T> clazz) {
        if (mMkv.contains(key)) {
            String objectVal = mMkv.getString(key, null);
            byte[] buffer = Base64.decode(objectVal, Base64.DEFAULT);
            ByteArrayInputStream bais = new ByteArrayInputStream(buffer);
            ObjectInputStream ois = null;
            try {
                ois = new ObjectInputStream(bais);
                T t = (T) ois.readObject();
                return t;
            } catch (StreamCorruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (bais != null) {
                        bais.close();
                    }
                    if (ois != null) {
                        ois.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public MmkvUtil remove(String key) {
        mMkv.remove(key);
        mMkv.commit();
        return this;
    }

    public MmkvUtil removeAll() {
        mMkv.clear();
        mMkv.commit();
        return this;
    }

    public static class Store implements BooleanStore, IntStore, StringStore, LongStore {


        @Override
        public void put(String key, boolean value) {
            MmkvUtil.getInstance().putBoolean(key, value);
        }

        @Override
        public boolean getBoolean(String key, boolean defaultValue) {
            return MmkvUtil.getInstance().getBoolean(key, defaultValue);
        }

        @Override
        public void put(String key, int value) {
            MmkvUtil.getInstance().putInt(key, value);
        }

        @Override
        public int getInt(String key, int defaultValue) {
            return MmkvUtil.getInstance().getInt(key, defaultValue);
        }

        @Override
        public void put(String key, String value) {
            MmkvUtil.getInstance().putString(key, value);
        }

        @Override
        public String getString(String key, String defaultValue) {
            return MmkvUtil.getInstance().getString(key, defaultValue);
        }

        @Override
        public void put(String key, long value) {
            MmkvUtil.getInstance().putLong(key, value);
        }

        @Override
        public long getLong(String key, long defaultValue) {
            return MmkvUtil.getInstance().getLong(key, defaultValue);
        }
    }

    public static class GsonParser<T> implements JsonOption.JsonParser<T> {

        private Class<T> mainClass;
        private Class<?>[] genericClass;

        public GsonParser(Class<T> mainClass, Class<?>... genericClass) {
            this.mainClass = mainClass;
            this.genericClass = genericClass;
        }

        @Override
        public String toJsonString(T obj) {
            if (obj == null) {
                return null;
            }
            return JsonUtils.toJsonString(obj);
        }

        @Override
        public T toObject(String jsonString) {
            if (TextUtils.isEmpty(jsonString)) {
                return null;
            }
            return JsonUtils.toObject(jsonString, mainClass, genericClass);
        }
    }

    public static class GsonListParser<T> implements JsonOption.JsonParser<ArrayList<T>> {

        private GsonParser<ArrayList> gsonParser;

        public GsonListParser(Class<T> genericClass) {
            gsonParser = new GsonParser<>(ArrayList.class,genericClass);
        }

        @Override
        public String toJsonString(ArrayList<T> obj) {
            return gsonParser.toJsonString(obj);
        }

        @SuppressWarnings("unchecked")
        @Override
        public ArrayList<T> toObject(String jsonString) {
            return gsonParser.toObject(jsonString);
        }
    }
}
