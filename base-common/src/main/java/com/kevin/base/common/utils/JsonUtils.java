package com.kevin.base.common.utils;

import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Description: Json工具类
 * @Author: kevin
 * @Date: 2019/7/2 11:24
 */
public class JsonUtils {

    private static final Gson gson = new GsonBuilder().serializeNulls().setDateFormat("yyyy-MM-dd HH:mm:ss")
            .registerTypeHierarchyAdapter(Date.class, new JsonSerializer<Date>() {
                @Override
                public JsonElement serialize(Date date, Type typeOfSrc, JsonSerializationContext context) {
                    if (date == null) {
                        return JsonNull.INSTANCE;
                    }
                    return new JsonPrimitive(DateUtils.normalDateFormat(date));
                }
            }).create();

    private JsonUtils() {
    }

    /**
     * 转换json字符串为java bean对象.
     * @param jsonStr json字符串，不能为blank。
     * @param cls 目标类的class
     * @return 目标bean实例.
     */
    public static <T> T fromJson(String jsonStr, Class<T> cls) {
        Preconditions.checkNotNull(jsonStr);
        Preconditions.checkArgument(StringUtils.isNotEmpty(jsonStr));

        return gson.fromJson(jsonStr, cls);
    }

    /**
     * 转换jsonObject为java对象
     * @param jsonObj json对象，不能为null
     * @param cls 目标类的class
     * @return 目标实例
     */
    public static <T> T fromJson(JsonObject jsonObj, Class<T> cls) {
        Preconditions.checkNotNull(jsonObj);

        return gson.fromJson(jsonObj, cls);
    }

    /**
     * 将src转换为json字符串.
     * @param src 待转换的对象.
     * @return 转换后的json字符串
     */
    public static String toJson(Object src) {
        Preconditions.checkNotNull(src);

        return gson.toJson(src);
    }

    /**
     * 将对象src转换为json元素
     * @param src 待转换对象
     * @return 转换后的json元素
     */
    public static JsonElement toJsonTree(Object src) {
        Preconditions.checkNotNull(src);

        return gson.toJsonTree(src);
    }

    /**
     * 将传入的json字符串转换成相应对象的list.
     * @param jsonStr 待转换的json字符串，不能为null和blank.
     * @param cls 目标List中元素的class
     * @return json数组对应的java 对象list.
     */
    public static <T> List<T> listFromJson(String jsonStr, Class<T> cls) {
        Preconditions.checkNotNull(jsonStr);
        Preconditions.checkArgument(StringUtils.isNotEmpty(jsonStr));

        JsonParser parser = new JsonParser();
        JsonElement jsonElement = parser.parse(jsonStr);
        JsonArray jsonArray = jsonElement.getAsJsonArray();
        List<T> objs = new ArrayList<T>();
        for (JsonElement e : jsonArray) {
            T obj = gson.fromJson(e, cls);
            objs.add(obj);
        }
        return objs;
    }

    /**
     * 将传入的jsonarray转换成相应对象的list.
     * @param jsonArray 待转换的json数组，不能为null.
     * @param cls 目标List中元素的class
     * @return json数组对应的java 对象list.
     */
    public static <T> List<T> listFromJson(JsonArray jsonArray, Class<T> cls) {
        Preconditions.checkNotNull(jsonArray);

        List<T> objs = new ArrayList<T>();
        for (JsonElement e : jsonArray) {
            T obj = gson.fromJson(e, cls);
            objs.add(obj);
        }
        return objs;
    }
}
