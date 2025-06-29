//	此资源由 58学课资源站 收集整理
//	想要获取完整课件资料 请访问：58xueke.com
//	百万资源 畅享学习
package com.imooc.utils;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.imooc.configs.GsonLocalDateAdapter;
import com.imooc.configs.GsonLocalDateTimeAdapter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GsonUtils {

    /**
     * 不用创建对象,直接使用Gson.就可以调用方法
     */
    private static Gson gson = null;

    private static JsonParser jsonParser = null;

    /**
     * 判断gson对象是否存在了,不存在则创建对象
     */
    static {
        if (gson == null) {
            //gson = new Gson();
            // 当使用GsonBuilder方式时属性为空的时候输出来的json字符串是有键值key的,显示形式是"key":null，而直接new出来的就没有"key":null的
            gson = new GsonBuilder().
                    registerTypeAdapter(LocalDate.class, new GsonLocalDateAdapter()).
                    registerTypeAdapter(LocalDateTime.class, new GsonLocalDateTimeAdapter()).
                    setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        }

        if (jsonParser == null) {
            jsonParser = new JsonParser();
        }
    }

    private GsonUtils() {
    }

    /**
     * json 转对象
     *
     * @param strJson
     * @return
     */
    public static JsonObject string2Object(String strJson) {
        return jsonParser.parse(strJson).getAsJsonObject();
    }


    /**
     * 将对象转成json格式
     *
     * @param object
     * @return String
     */
    public static String object2String(Object object) {
        String gsonString = null;
        if (gson != null) {
            gsonString = gson.toJson(object);
        }
        return gsonString;
    }

    /**
     * 将json转成特定的cls的对象
     *
     * @param gsonString
     * @param cls
     * @return
     */
    public static <T> T stringToBean(String gsonString, Class<T> cls) {
        T t = null;
        if (gson != null) {
            //传入json对象和对象类型,将json转成对象
            t = gson.fromJson(gsonString, cls);
        }
        return t;
    }

    public static <T> T stringToBean(String gsonString) {

        T o = gson.fromJson(gsonString, new TypeToken<T>() {
        }.getType());
        return o;
    }

//    public static <T> T stringToBean2(String gsonString, Class<T> cls) {
//
//        JsonParser jsonParser = new JsonParser();
//        JsonObject jsonObject = jsonParser.parse(gsonString).getAsJsonObject();
//
//        T t = null;
//        if (gson != null) {
//            //传入json对象和对象类型,将json转成对象
//            t = gson.fromJson(jsonObject, cls);
//        }
//        return t;
//    }

    /**
     * json字符串转成list
     *
     * @param gsonString
     * @param cls
     * @return
     */
    public static <T> List<T> stringToList(String gsonString, Class<T> cls) {
        List<T> list = null;
        if (gson != null) {
            //根据泛型返回解析指定的类型,TypeToken<List<T>>{}.getType()获取返回类型
            list = gson.fromJson(gsonString, new TypeToken<List<T>>() {
            }.getType());
        }
        return list;
    }

    public static <T> List<T> stringToListAnother(String gsonString, Class<T> cls) {
        List<T> list = new ArrayList<>();
        JsonArray jsonArray = new JsonParser().parse(gsonString).getAsJsonArray();
        Gson gson = new Gson();
        for (JsonElement jsonElement : jsonArray) {
            list.add(gson.fromJson(jsonElement, cls));
        }
        return list;
    }


    /**
     * json字符串转成list中有map的
     *
     * @param gsonString
     * @return
     */
    public static <T> List<Map<String, T>> stringToListMaps(String gsonString) {
        List<Map<String, T>> list = null;
        if (gson != null) {
            list = gson.fromJson(gsonString,
                    new TypeToken<List<Map<String, T>>>() {
                    }.getType());
        }
        return list;
    }

    /**
     * json字符串转成map的
     *
     * @param gsonString
     * @return
     */
    public static <T> Map<String, T> stringToMaps(String gsonString) {
        Map<String, T> map = null;
        if (gson != null) {
            map = gson.fromJson(gsonString, new TypeToken<Map<String, T>>() {
            }.getType());
        }
        return map;
    }

    public static String jsonElementAsString(JsonElement jsonElement) {
        return jsonElement == null ? null : jsonElement.getAsString();
    }

    public static Integer jsonElementAsInt(JsonElement jsonElement) {
        return jsonElement == null ? null : jsonElement.getAsInt();
    }


}