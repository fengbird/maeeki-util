package com.maeeki.enumration;

/**
 * @author zhaotengchao
 * @since 2018-11-29 14:23
 **/
public enum  UserEnum {
    username("name","userName"),
    password("pwd","PASSWORD"),
    sex("xingbie","xb");
    private String key;
    private String other;
    UserEnum(String key,String other) {
        this.key = key;
        this.other = other;
    }

    public String key() {
        return key;
    }

    public String other() {
        return other;
    }
}
