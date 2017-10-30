package com.zb.common.utils;

import java.util.Random;

/**
 * Created by zhangbo on 17-7-29.
 */
public class RandomUtil {
    /**
     * 获取随机字符串
     * @param length 字符串长度
     * @return
     */
    public static String getRandomString(int length) {
        StringBuffer buffer = new StringBuffer("0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ");
        StringBuffer sb = new StringBuffer();
        Random r = new Random();
        int range = buffer.length();
        for (int i = 0; i < length; i ++) {
            sb.append(buffer.charAt(r.nextInt(range)));
        }
        return sb.toString();
    }
}
