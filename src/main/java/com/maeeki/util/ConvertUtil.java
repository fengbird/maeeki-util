package com.maeeki.util;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.deserializer.ExtraProcessor;
import com.alibaba.fastjson.serializer.NameFilter;
import com.maeeki.config.ConvertConfig;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author zhaotengchao
 * @since 2018-11-29 17:05
 **/
public class ConvertUtil {
    private final static ConvertConfig config = new ConvertConfig();

    /**
     * 按照枚举类中的字段对应关系从一个或多个对象向另一个对象复制数据
     * @param source 待复制的数据来源对象(可以为对象的list集合)
     * @param target 数据复制到的目的对象
     * @param tEnum 定义了字段映射的枚举类
     */
    public static <T extends Enum<T>> void beanToBean(Object source, Object target, Class<T> tEnum) {
        beanToBean(source, target,tEnum,config);
    }

    /**
     * 按照枚举类中的字段对应关系从一个或多个对象向另一个对象复制数据
     * @param source 待复制的数据来源对象(可以为对象的list集合)
     * @param target 数据复制到的目的对象
     * @param tEnum 定义了字段映射的枚举类
     * @param config 该转换工具的配置器
     */
    public static <T extends Enum<T>> void beanToBean(Object source,Object target,Class<T> tEnum,
                                                      ConvertConfig config) {
        Objects.requireNonNull(source);
        CopyOptions copyOptions = CopyOptions.create();
        Map<String, String> map = EnumUtil.enumToMap(tEnum, config.getConvert(), config.getMethodName());
        copyOptions.setFieldMapping(map);
        if (source instanceof List) {
            List list = (List) source;
            if (list.isEmpty()) {
                return;
            }
            for (Object o : list) {
                BeanUtil.copyProperties(o, target, copyOptions);
            }
        } else {
            BeanUtil.copyProperties(source,target,copyOptions);
        }

    }

    /**
     * 按照枚举类中的字段对应关系将bean对象转为json字符串
     * @param source 待转换的bean对象
     * @param tEnum 定义了字段映射的枚举类
     * @return 转换后的json字符串
     */
    public static <T extends Enum<T>> String beanToJson(Object source, Class<T> tEnum) {
        return beanToJson(source, tEnum, config);
    }

    /**
     * 按照枚举类中的字段对应关系将bean对象转为json字符串
     * @param source 待转换的bean对象
     * @param tEnum 定义了字段映射的枚举类
     * @param config 该转换工具的配置器
     * @return 转换后的json字符串
     */
    public static <T extends Enum<T>> String beanToJson(Object source, Class<T> tEnum, ConvertConfig config) {
        NameFilter nameFilter = NameFilterUtil.getNameFilterProxy(tEnum, config.getConvert(), config.getMethodName());
        return JSON.toJSONString(source, nameFilter);
    }

    /**
     * 按照枚举类中的字段对应关系将json字符串转为bean对象
     * @param json 待转换的json字符串
     * @param targetClass 需要转换成的目标类字节码对象
     * @param tEnum 定义了字段映射的枚举类
     * @return 转换后的目标类对象
     */
    public static <E, T extends Enum<T>> E jsonToBean(String json, Class<E> targetClass, Class<T> tEnum) {
        return jsonToBean(json, targetClass, tEnum, config);
    }

    /**
     *  按照枚举类中的字段对应关系将json字符串转为bean对象
     * @param json 待转换的json字符串
     * @param targetClass 需要转换成的目标类字节码对象
     * @param tEnum 定义了字段映射的枚举类
     * @param config 该转换工具的配置器
     * @return 转换后的目标类对象
     */
    public static <E, T extends Enum<T>> E jsonToBean(String json, Class<E> targetClass, Class<T> tEnum,
        ConvertConfig config) {
        ExtraProcessor extraProcessor =
            ExtraProcessorUtil.getExtraProcessorProxy(targetClass, tEnum, config.getConvert(), config.getMethodName());
        return JSONObject.parseObject(json, targetClass, extraProcessor);
    }
}
