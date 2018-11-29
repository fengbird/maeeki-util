package com.maeeki.util;

import com.alibaba.fastjson.serializer.NameFilter;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;

/**
 * @author Zhaotengchao
 * @create 2018-11-28-23:25.
 */
public class NameFilterUtil {
    /**
     * 获取NameFilter的代理类对象
     * @param tClass 包含字段映射关系的枚举类对象
     * @param <T> 枚举类型泛型
     * @param convert 是否反转枚举的name和值
     * @param methodName 枚举类获取值的方法名称
     * @return NameFilter的代理类对象
     */
    public static <T extends Enum<T>> NameFilter getNameFilterProxy(Class<T> tClass,boolean convert,String methodName) {
        try {
            Map<String, String> map = EnumUtil.enumToMap(tClass, convert, methodName);
            return  (NameFilter) Proxy.newProxyInstance(NameFilter.class.getClassLoader(), new Class[]{NameFilter.class}, new InvocationHandler() {
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) {
                    Object nameObj = args[1];
                    if (nameObj instanceof String) {
                        if(map.containsKey(nameObj)) {
                            return map.get(nameObj);
                        }
                    }
                    return nameObj;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new NameFilter() {
            @Override
            public String process(Object o, String s, Object o1) {
                return s;
            }
        };
    }

}
