package com.maeeki.util;

import com.alibaba.fastjson.parser.deserializer.ExtraProcessor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;

/**
 * @author zhaotengchao
 * @since 2018-11-29 16:04
 **/
public class ExtraProcessorUtil {
    /**
     * 获取ExtraProcessor代理对象
     * @param targetClass 需要由json转换成的bean对象
     * @param tEnum 包含转换字段映射的枚举类
     * @param convert 是否需要反转枚举类中的name和对应值
     * @param methodName 获取枚举值的方法名
     * @return ExtraProcessor代理对象
     */
    public static <E,T extends Enum<T>> ExtraProcessor getExtraProcessorProxy(Class<E> targetClass,Class<T> tEnum,
                                                          boolean convert,String methodName) {
        Map<String, String> map = EnumUtil.enumToMap(tEnum, convert, methodName);
        return (ExtraProcessor) Proxy.newProxyInstance(ExtraProcessor.class.getClassLoader(), new Class[]{ExtraProcessor.class}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                E object = (E) args[0];
                String key = (String) args[1];
                Object value = args[2];
                if (map.containsKey(key)) {
                    Map<String, Method> writerMethodMap = BeanInfoUtil.getWriterMethodMap(targetClass);
                    if (writerMethodMap.containsKey(map.get(key))) {
                        Method writerMethod = writerMethodMap.get(map.get(key));
                        writerMethod.invoke(object,value);
                    }
                }
                return Void.class;
            }
        });
    }
}
