package com.stylefeng.guns.rest.common;

/**
 * @date 2019/6/2 13:27
 */
public class CurrentUser {

    private static final ThreadLocal<String> threadLocal = new ThreadLocal<String>();

    public static void saveUserId(String userId){
        threadLocal.set(userId);
    }

    public static String getCurrentUser(){
       return threadLocal.get();
    }
}
