package com.th.utils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

/**
 * @author TanHaooo
 * @date 2021/4/15 15:21
 */
public class TimeUtil {
    public static String TimeFormatToTimeStamp(long timestamp) {
        return String.valueOf((int) (timestamp / 1000));
    }

    public static String TimeStampToTimeFormat(String timestamp) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long current = Long.parseLong(timestamp);
        Timestamp ts = new Timestamp(current * 1000);// 不除就是精确到ms 这个是到s
        return format.format(ts);
    }
}
