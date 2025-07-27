package com.bitejiuyeke.bitecommoncore.utils;

import com.bitejiuyeke.bitecommondomain.constraints.CommonConstraints;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
public class JsonUtil {

    /**
     * 对象转换器
     */
    private static final ObjectMapper OBJECT_MAPPER;

    static {
        OBJECT_MAPPER = JsonMapper.builder()
                // 反序列化时，json 存在目标对象中未定义的字段，false 表示不会抛出异常，忽略掉属性名字匹配不一致的错误
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                // 序列化时，自动将时间信息转化为时间戳，false 表示不会自动转化
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                // 序列化时，转换一个没有任何属性值的对象，false 表示不会抛出异常
                .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
                // 反序列化时，在面对字段类型不一致的情况时，false 表示不会抛出异常，忽略掉属性类型匹配不一致的错误
                .configure(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE, false)
                // 序列化时，自动将时间信息的 key（针对于 map）转换为时间戳，false 表示不会自动转化
                .configure(SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS, true)
                // 表示 Jackson 是否会支持在 Java 类属性上添加的各种注解来定制序列化和反序列化的具体行为
                .configure(MapperFeature.USE_ANNOTATIONS, false)
                // 这个是使用 LocalDateTime 和 LocalDate 的必要配置
                .addModule(new SimpleModule()
                        // 定义序列化 LocalDateTime 或者 LocalDate 的时间格式
                        .addSerializer(LocalDateTime.class,
                                new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(CommonConstraints.STANDARD_FORMAT)))
                        // 定义反序列化 LocalDateTime 或者 LocalDate 的时间格式
                        .addDeserializer(LocalDateTime.class,
                                new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(CommonConstraints.STANDARD_FORMAT)))
                        // 添加对 LocalDate 的支持
                        .addSerializer(LocalDate.class,
                                new LocalDateSerializer(DateTimeFormatter.ofPattern(CommonConstraints.STANDARD_FORMAT)))
                        .addDeserializer(LocalDate.class,
                                new LocalDateDeserializer(DateTimeFormatter.ofPattern(CommonConstraints.STANDARD_FORMAT))))
                // 定义时间信息的具体格式
                .defaultDateFormat(new SimpleDateFormat(CommonConstraints.STANDARD_FORMAT))
                // 序列化时，只针对对象中非空的字段进行序列化，值为 null 的字段不予以考虑
                .serializationInclusion(JsonInclude.Include.NON_NULL)
                .build();
    }

    /**
     * 单例模式，禁用构造函数
     */
    private JsonUtil() {
    }

    public static String obj2String(Object o) {

        if (null == o) {
            return null;
        }

        try {
            // 首先判断源对象是不是 String，然后再做后续的转化
            return o instanceof String ? (String) o :
                    OBJECT_MAPPER.writeValueAsString(o);
        } catch (Exception e) {
            log.warn("Parse Object to String error: ", e);
            return null;
        }

    }

    public static String obj2StringPretty(Object o) {

        if (null == o) {
            return null;
        }

        try {
            return o instanceof String ? (String) o :
                    OBJECT_MAPPER.writerWithDefaultPrettyPrinter()
                            .writeValueAsString(o);
        } catch (Exception e) {
            log.warn("Parse Object to Pretty String error: ", e);
            return null;
        }

    }

    public static <T> T string2Obj(String str, Class<T> tClass) {

        if (!StringUtils.hasLength(str) || null == tClass) {
            return null;
        }

        try {
            // 首先判断目标类别是否就是 String，然后再做后续的转化
            return tClass.equals(String.class) ? (T) str :
                    OBJECT_MAPPER.readValue(str, tClass);
        } catch (Exception e) {
            log.warn("Parse String to Object error: ", e);
            return null;
        }

    }

    public static <T> T string2Obj(String str, TypeReference<T> typeReference) {

        // Java 会在编译阶段进行泛型擦除，
        // 但是子类会将父类的泛型信息存储在字节码中，所以变相的绕开了这个规则，保存了对应的泛型信息

        if (!StringUtils.hasLength(str) || null == typeReference) {
            return null;
        }

        try {
            return OBJECT_MAPPER.readValue(str, typeReference);
        } catch (Exception e) {
            log.warn("Parse String to Object error: ", e);
            return null;
        }

    }

    public static <T> List<T> string2List(String str, Class<T> elementType) {

        if (!StringUtils.hasLength(str) || null == elementType) {
            return null;
        }

        JavaType javaType = OBJECT_MAPPER.getTypeFactory().constructParametricType(List.class, elementType);
        try {
            return OBJECT_MAPPER.readValue(str, javaType);
        } catch (Exception e) {
            log.warn("Parse String to List error: ", e);
            return null;
        }

    }

    public static <T> Map<String, T> string2Map(String str, Class<T> elementType) {

        if (!StringUtils.hasLength(str) || null == elementType  ) {
            return null;
        }

        JavaType javaType = OBJECT_MAPPER.getTypeFactory().constructMapType(HashMap.class,
                String.class, elementType);
        try {
            return OBJECT_MAPPER.readValue(str, javaType);
        } catch (Exception e) {
            log.warn("Parse String to Map error: ", e);
            return null;
        }

    }

}
