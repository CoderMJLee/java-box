package com.mj.common.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * JSON相关操作
 *
 * @author MJ
 */
public class Jsons {
    private static ObjectMapper MAPPER = newMapper();

    public static void setMapper(ObjectMapper mapper) {
        MAPPER = mapper;
    }

    public static ObjectMapper newMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE);
        // 禁用：遇到未知的属性就报错
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        return mapper;
    }

    public static <T> T getObj(String json, Class<T> cls) {
        if (cls == null) return null;
        if (json == null || json.length() < 2) return null;
        try {
            return MAPPER.readValue(json, cls);
        } catch (Exception e) {
            return null;
        }
    }

    public static <T> List<T> getObjs(String json, Class<T> cls) {
        if (cls == null) return null;
        if (json == null || json.length() < 2) return null;
        try {
            List<T> list = new ArrayList<>();
            for (JsonNode node : MAPPER.readTree(json)) {
                list.add(MAPPER.treeToValue(node, cls));
            }
            return list;
        } catch (Exception e) {
            return null;
        }
    }

    public static <T> T getObj(byte[] bytes, Class<T> cls) {
        if (cls == null || bytes == null) return null;
        return getObj(new String(bytes, StandardCharsets.UTF_8), cls);
    }

    public static String getString(Object obj) {
        if (obj == null) return null;
        try {
            return MAPPER.writeValueAsString(obj);
        } catch (Exception e) {
            return null;
        }
    }

    public static byte[] getBytes(Object obj) {
        String str = getString(obj);
        return str == null
                ? null
                : str.getBytes(StandardCharsets.UTF_8);
    }
}
