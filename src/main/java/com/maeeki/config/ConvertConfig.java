package com.maeeki.config;

import cn.hutool.core.util.StrUtil;

/**
 * @author zhaotengchao
 * @since 2018-11-29 17:28
 **/
public class ConvertConfig {
    /**
     * 是否将枚举的name与值反转,默认为不反转,即将枚举的name转为对应的值
     */
    private boolean convert = false;
    /**
     * 获取枚举值的方法,默认方法为key
     */
    private String methodName = "key";

    public ConvertConfig(){}

    public ConvertConfig(boolean convert) {
        this.convert = convert;
    }

    public ConvertConfig(String methodName) {
        this.methodName = methodName;
    }

    public ConvertConfig(boolean convert, String methodName) {
        this.convert = convert;
        this.methodName = methodName;
    }


    public boolean getConvert() {
        return convert;
    }

    public void setConvert(boolean convert) {
        this.convert = convert;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        if(StrUtil.isBlankOrUndefined(methodName)) {
            return;
        }
        this.methodName = methodName;
    }
}
