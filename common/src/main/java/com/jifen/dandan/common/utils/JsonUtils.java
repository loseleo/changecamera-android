package com.jifen.dandan.common.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import org.json.JSONObject;

import java.io.Reader;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * json转换工具类
 * Created by zhangqiang on 2017/8/7.
 */

public class JsonUtils {

    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                @Override
                public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                    return new Date(json.getAsJsonPrimitive().getAsLong());
                }
            })
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .create();

    /**
     * 返回gson对象
     *
     * @return gson对象
     */
    public static Gson getGson() {

        return gson;
    }

    /**
     * json字符串转换为对象
     *
     * @param jsonString   字符串
     * @param mainClass    对象的类型
     * @param genericClass 对象的泛型类
     * @return 转换的对象
     */
    public static <T> T toObject(String jsonString, Class<T> mainClass, Class<?>[] genericClass) {
        try {
            if (genericClass == null) {
                return gson.fromJson(jsonString, mainClass);
            }
            return gson.fromJson(jsonString, type(mainClass, genericClass));
        } catch (JsonParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public static <T> List<T> toSimpleList(String jsonString, Class<T> tClass) {
        return JsonUtils.toObject(jsonString, ArrayList.class, new Class[]{tClass});
    }

    /**
     * json字符串转换为对象
     *
     * @param jsonString 字符串
     * @param mainClass  对象的类型
     * @return 转换的对象
     */
    public static <T> T toObject(String jsonString, Class<T> mainClass) {

        return toObject(jsonString, mainClass, (Class<?>[]) null);
    }

    /**
     * json字符串转换为对象
     *
     * @param reader       输入流
     * @param mainClass    对象的类型
     * @param genericClass 对象的泛型类
     * @return 转换的对象
     */
    public static <T> T toObject(Reader reader, Class<T> mainClass, Class<?> genericClass) {

        try {
            if (genericClass == null) {
                return gson.fromJson(reader, mainClass);
            }

            return gson.fromJson(reader, type(mainClass, new Class<?>[]{genericClass}));
        } catch (JsonParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 对象转化为json字符串
     *
     * @param object 要转换的对象
     * @return json字符串
     */
    public static String toJsonString(Object object) {

        return gson.toJson(object);
    }

    /**
     * 构建泛型类
     *
     * @param rawClass 原始类型
     * @param types    泛型类
     * @return
     */
    private static ParameterizedType type(final Class<?> rawClass, final Type[] types) {

        return new ParameterizedType() {
            @Override
            public Type[] getActualTypeArguments() {
                return types;
            }

            @Override
            public Type getOwnerType() {
                return null;
            }

            @Override
            public Type getRawType() {
                return rawClass;
            }
        };
    }

    /**
     * 是否集成了gson
     *
     * @return 是否集成了gson
     */
    public static boolean hasGson() {

        try {
            return Class.forName("com.google.gson.Gson") != null;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Map 转Json
     */
    public static String mapToJsonString(Map map) {
        String string = "";

        try {
            JSONObject jsonObject = new JSONObject(map);
            string = jsonObject.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return string;
    }
}
