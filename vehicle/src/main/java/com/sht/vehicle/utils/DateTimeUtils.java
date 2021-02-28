package com.sht.vehicle.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Aaron
 * @date 2021/2/28 11:06
 */
public class DateTimeUtils {

    public static LocalDateTime format(String dateTime) {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.parse(dateTime, format);
    }
}
