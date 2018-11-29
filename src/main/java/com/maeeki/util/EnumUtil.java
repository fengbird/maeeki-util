package com.maeeki.util;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zhaotengchao
 * @since 2018-11-29 16:16
 **/
public class EnumUtil {
    public static <T extends Enum<T>> Map<String,String> enumToMap(Class<T> tEnum,boolean convert,String methodName) {
        try {
            Method valuesMethod = tEnum.getMethod("values");
            Method keyMethod = tEnum.getMethod(methodName);
            final HashMap<String, String> map = new HashMap<>(10);
            if (valuesMethod != null && keyMethod != null) {
                T[] tArrays = (T[]) valuesMethod.invoke(tEnum);
                for (T t : tArrays) {
                    Object keyObj = keyMethod.invoke(t);
                    if (keyObj != null && keyObj instanceof String) {
                        String key = (String) keyObj;
                        String name = t.name();
                        if (!convert) {
                            map.put(name,key);
                        } else {
                            map.put(key,name);
                        }
                    }
                }
            }
            return map;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new HashMap<>(0);
    }
}
