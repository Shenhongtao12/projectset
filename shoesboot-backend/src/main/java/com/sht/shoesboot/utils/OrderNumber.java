package com.sht.shoesboot.utils;

/**
 * @author Aaron
 * @date 2020/12/29 22:34
 */
public class OrderNumber {

    public static String createOrderNumber() {
        return System.currentTimeMillis() + "" + Math.round(Math.random() * 10000);
    }
}
