package com.maeeki;

import com.maeeki.config.ConvertConfig;
import com.maeeki.enumration.UserEnum;
import com.maeeki.pojo.HerUser;
import com.maeeki.pojo.MyUser;
import com.maeeki.pojo.User;
import com.maeeki.util.ConvertUtil;

import java.util.Arrays;

/**
 * @author zhaotengchao
 * @since 2018-11-29 15:33
 **/
public class Main {
    public static void main(String[] args) {
        User user = new User();
        user.setUsername("zs");
        user.setPassword("123");
        user.setSex("男");
        user.setAge(11);
        String second = ConvertUtil.beanToJson(user, UserEnum.class, new ConvertConfig("second"));
        System.out.println(second);
        User second1 = ConvertUtil.jsonToBean(second, User.class, UserEnum.class, new ConvertConfig(true, "second"));
        System.out.println(second1);
        MyUser myUser = new MyUser();
        ConvertUtil.beanToBean(user,myUser, UserEnum.class);
        System.out.println(myUser);

        String s = ConvertUtil.beanToJson(user, UserEnum.class);
        System.out.println(s);

        User user1 = ConvertUtil.jsonToBean(s, User.class, UserEnum.class, new ConvertConfig(true));
        System.out.println(user1);
        MyUser myUser1 = new MyUser();
        myUser1.setName("zs");
        myUser1.setPwd("123456");
        myUser1.setAge(23);
        HerUser herUser = new HerUser();
        herUser.setXingbie("男");
        User user2 = new User();
        ConvertUtil.beanToBean(myUser,user2, UserEnum.class,new ConvertConfig(true));
        System.out.println(user2);
        ConvertUtil.beanToBean(Arrays.asList(myUser,herUser),user2, UserEnum.class,new ConvertConfig(true));
        System.out.println(user2);
    }
}
