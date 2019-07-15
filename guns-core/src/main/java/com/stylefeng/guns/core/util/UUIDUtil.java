package com.stylefeng.guns.core.util;

import java.util.UUID;

/**
 * @date 2019/7/6 11:08
 */
public class UUIDUtil {

    public static String generateUuid(){
        return UUID.randomUUID().toString().replace("-","");
    }

    public static void main(String[] args) {
        System.out.println(generateUuid());
    }
}
