package com.maeeki.util;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zhaotengchao
 * @since 2018-11-29 16:26
 **/
public class BeanInfoUtil {
    public static <T> Map<String, Method> getWriterMethodMap(Class<T> targetClass) {
        Map<String, Method> map = new HashMap<>(10);
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(targetClass, Object.class);
            PropertyDescriptor[] descriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor descriptor : descriptors) {
                Method writeMethod = descriptor.getWriteMethod();
                String name = descriptor.getName();
                map.put(name,writeMethod);
            }
            return map;
        } catch (IntrospectionException e) {
            e.printStackTrace();
        }
        return new HashMap<>(0);
    }
}
