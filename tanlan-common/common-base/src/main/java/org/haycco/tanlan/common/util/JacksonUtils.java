package org.haycco.tanlan.common.util;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.io.IOException;
import java.util.Collection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 使用 com.fasterxml.jackson 进行 json 的转换 对于多层的嵌套 json 数据, jackson 也会逐级转换成 Map 或 Object
 *
 * @author liangyi
 */
public final class JacksonUtils {

    private static final Logger logger = LoggerFactory.getLogger(JacksonUtils.class);

    private static ObjectMapper objectMapper;

    private static ObjectMapper objectMapperWithSnake;

    private static ObjectMapper ObjectMapperWithSnakeAndNoNull;

    private static ObjectMapper ObjectMapperNoNull;

    static {
        objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
        // 是否将允许使用非双引号属性名字,默认为 false,由于 json 标准中使用双引号做属性名,建议不打开
        /*objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);*/
        // 无效的映射字段不会抛出异常
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

        objectMapperWithSnake = new ObjectMapper();
        objectMapperWithSnake.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
        // 是否将允许使用非双引号属性名字,默认为 false,由于 json 标准中使用双引号做属性名,建议不打开
        /*objectMapperWithSnake.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);*/
        // 无效的映射字段不会抛出异常
        objectMapperWithSnake.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapperWithSnake.setPropertyNamingStrategy(
                SnakeCaseStrategy.SNAKE_CASE);
        objectMapperWithSnake.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

        ObjectMapperWithSnakeAndNoNull = new ObjectMapper();
        ObjectMapperWithSnakeAndNoNull.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
        // 是否将允许使用非双引号属性名字,默认为 false,由于 json 标准中使用双引号做属性名,建议不打开
        /*objectMapperWithSnake.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);*/
        // 无效的映射字段不会抛出异常
        ObjectMapperWithSnakeAndNoNull.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        ObjectMapperWithSnakeAndNoNull.setPropertyNamingStrategy(
                SnakeCaseStrategy.SNAKE_CASE);
        ObjectMapperWithSnakeAndNoNull.setSerializationInclusion(Include.NON_NULL);
        ObjectMapperWithSnakeAndNoNull.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

        ObjectMapperNoNull = new ObjectMapper();
        ObjectMapperNoNull.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
        // 是否将允许使用非双引号属性名字,默认为 false,由于 json 标准中使用双引号做属性名,建议不打开
        /*objectMapperWithSnake.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);*/
        // 无效的映射字段不会抛出异常
        ObjectMapperNoNull.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        ObjectMapperNoNull.setSerializationInclusion(Include.NON_NULL);
        ObjectMapperNoNull.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

    }

    /**
     * 将对象转为 json 字符串
     *
     * @param obj 目标对象
     * @return json 字符串
     */
    public static String toJson(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            logger.error("Jackson to json error! Obj: {}", obj, e);
        }
        return null;
    }

    /**
     * 将对象转为 json 字符串 不包含null
     *
     * @param obj 目标对象
     * @return json 字符串
     */
    public static String toJsonNotNull(Object obj) {
        try {
            return ObjectMapperNoNull.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            logger.error("Jackson to json error! Obj: {}", obj, e);
        }
        return null;
    }

    /**
     * 将对象转为 json Tree Node
     *
     * @param obj 目标对象
     * @return JsonNode 对象
     */
    public static JsonNode toJsonNode(Object obj) {
        try {
            return objectMapper.readTree(toJson(obj));
        } catch (IOException e) {
            logger.error("Jackson to json error! Obj: {}", obj, e);
        }
        return null;
    }

    public static JsonNode toJsonNode(String jsonStr) {
        try {
            return objectMapper.readTree(jsonStr);
        } catch (IOException e) {
            logger.error("Jackson to json error! Obj: {}", jsonStr, e);
        }
        return null;
    }

    /**
     * 将对象转为 json 字符串 -- 以下划线格式序列化
     *
     * @param obj 目标对象
     * @return json 字符串
     */
    public static String toJsonWithSnake(Object obj) {
        try {
            return objectMapperWithSnake.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            logger.error("Jackson to json error! Obj: {}", obj, e);
        }
        return null;
    }

    /**
     * 将对象转为 json 字符串 -- 以下划线格式序列化
     *
     * @param obj 目标对象
     * @return json 字符串
     */
    public static String toJsonWithSnakeAndNoNull(Object obj) {
        try {
            return ObjectMapperWithSnakeAndNoNull.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            logger.error("Jackson to json error! Obj: {}", obj, e);
        }
        return null;
    }

    /**
     * 将 json 字符串转换为指定的目标类
     *
     * @param jsonStr json 字符串
     * @param clazz 目标类 Class
     */
    public static <T> T toBean(String jsonStr, Class<T> clazz) {
        try {
            return objectMapper.readValue(jsonStr, clazz);
        } catch (IOException e) {
            logger.error("Jackson to Bean error! JsonStr: {}, ClassName: {}",
                    jsonStr, clazz.getName(), e);
        }
        return null;
    }

    /**
     * 将 json 字符串转换为指定的目标类 -- 将下划线格式反序列化
     *
     * @param jsonStr json 字符串
     * @param clazz 目标类 Class
     */
    public static <T> T toBeanWithSnake(String jsonStr, Class<T> clazz) {
        try {
            return objectMapperWithSnake.readValue(jsonStr, clazz);
        } catch (IOException e) {
            logger.error("Jackson to Bean error! JsonStr: {}, ClassName: {}",
                    jsonStr, clazz.getName(), e);
        }
        return null;
    }

    /**
     * 将 json 字符串转换为指定的目标类 -- 将下划线格式反序列化
     *
     * @param jsonStr json 字符串
     * @param clazz 目标类 Class
     */
    public static <T> T toBeanWithSnakeThrowEx(String jsonStr, Class<T> clazz)
            throws IOException {
        return objectMapperWithSnake.readValue(jsonStr, clazz);
    }

    /**
     * 将 json字符串转为指定类的集合(Collection)类型
     *
     * @param jsonStr 待转换的 json 字符串
     * @param collectionClazz 集合的 Class
     * @param clazz 目标类的 Class
     */
    public static <T> Collection<T> toBeanCollection(String jsonStr, Class<? extends Collection> collectionClazz, Class<T> clazz) {
        JavaType type = objectMapper.getTypeFactory().constructParametricType(collectionClazz, clazz);
        try {
            return objectMapper.readValue(jsonStr, type);
        } catch (IOException e) {
            logger.error("Jackson to Bean Collection error! JsonStr: {}, ClassName: {}",
                    jsonStr, clazz.getName(), e);
        }
        return null;
    }

    /**
     * 将 json字符串转为指定类的集合(Collection)类型 -- 将下划线格式反序列化
     *
     * @param jsonStr 待转换的 json 字符串
     * @param collectionClazz 集合的 Class
     * @param clazz 目标类的 Class
     */
    public static <T> Collection<T> toBeanCollectionWithSnake(String jsonStr, Class<? extends Collection> collectionClazz, Class<T> clazz) {
        JavaType type = objectMapperWithSnake.getTypeFactory().constructParametricType(collectionClazz, clazz);
        try {
            return objectMapperWithSnake.readValue(jsonStr, type);
        } catch (IOException e) {
            logger.error("Jackson to Bean Collection error! JsonStr: {}, ClassName: {}",
                    jsonStr, clazz.getName(), e);
        }
        return null;
    }

}