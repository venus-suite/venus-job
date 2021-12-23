package io.suite.venus.job.admin.core.util;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Objects;

/**
 * @author ljj
 * @Description:
 * @Name: JsonUtils
 * @date 2018/7/3下午12:27
 */
public class JsonUtils {

    public JsonUtils() {
    }

    // private final static ObjectMapper objectMapper = new ObjectMapper();

    public static String toJSONString(Object o) {
        if (!Objects.isNull(o)) {
            return JSON.toJSONString(o);
        }
        return "";
    }

    public static <T> T parse(String json, Class<T> clazz) {
        if (StringUtils.isNotEmpty(json)) {
            return JSON.parseObject(json, clazz);
        }
        return null;
    }

    /**
     * 将json array反序列化为对象
     *
     * @param json
     * @return
     */
    public static <T> List<T> parseList(String json, Class<T> clazz) {
        if (StringUtils.isNotEmpty(json)) {
            return JSON.parseArray(json, clazz);
        }
        return null;
    }
}
