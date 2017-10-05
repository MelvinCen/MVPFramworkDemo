package com.melvin.mvpframworkdemo.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class StringUtil {
    public static boolean isNotEmpty(String s) {
        return s != null && !"".equals(s.trim());
    }

    public static boolean isEmpty(String s) {
        return s == null || "".equals(s.trim());
    }

    public static String format(String src, Object... objects) {
        int k = 0;
        for (Object obj : objects) {
            src = src.replace("{" + k + "}", obj.toString());
            k++;
        }
        return src;
    }

    /**
     * 字符串经过trim处理之后是否为空
     *
     * @param str
     * @return
     */
    public static boolean isNull(String str) {
        boolean b = false;
        if (str == null || str.trim().length() == 0) b = true;

        return b;
    }

    /**
     * to Int
     *
     * @param s
     * @param defaultV 默认值
     * @return
     */
    public static int str2Int(String s, int defaultV) {
        if (s != null && !s.equals("")) {
            int num = defaultV;
            try {
                num = Integer.parseInt(s);
            } catch (Exception ignored) {
            }
            return num;
        } else {
            return defaultV;
        }
    }


    /**
     * 判断是否为null或空值
     *
     * @param str String
     * @return true or false
     */
    public static boolean isNullOrEmpty(String str) {
        return str == null || str.trim().length() == 0;
    }

    /**
     * 判断str1和str2是否相同
     *
     * @param str1 str1
     * @param str2 str2
     * @return true or false
     */
    public static boolean equals(String str1, String str2) {
        return str1 == str2 || str1 != null && str1.equals(str2);
    }

    /**
     * 判断str1和str2是否相同(不区分大小写)
     *
     * @param str1 str1
     * @param str2 str2
     * @return true or false
     */
    public static boolean equalsIgnoreCase(String str1, String str2) {
        return str1 != null && str1.equalsIgnoreCase(str2);
    }

    /**
     * 判断字符串str1是否包含字符串str2
     *
     * @param str1 源字符串
     * @param str2 指定字符串
     * @return true源字符串包含指定字符串，false源字符串不包含指定字符串
     */
    public static boolean contains(String str1, String str2) {
        return str1 != null && str1.contains(str2);
    }

    /**
     * 得到一个字符串的长度,显示的长度,一个汉字或日韩文长度为2,英文字符长度为1
     *
     * @param s 需要得到长度的字符串
     * @return int 得到的字符串长度
     */
    public static int length(String s) {
        if (s == null)
            return 0;
        char[] c = s.toCharArray();
        int len = 0;
        for (int i = 0; i < c.length; i++) {
            len++;
            if (!isLetter(c[i])) {
                len++;
            }
        }
        return len;
    }

    public static String dateFormat(String date) {
        SimpleDateFormat dateFm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //格式化当前系统日期
        String dateTime = dateFm.format(new Date(Long.parseLong(date)));
        return dateTime;
    }

    public static boolean isLetter(char c) {
        int k = 0x80;
        return c / k == 0;
    }

    /**
     * 判断字符串是否为空，为空则返回一个空值，不为空则返回原字符串
     *
     * @param str 待判断字符串
     * @return 判断后的字符串
     */

    /**
     * 方法描述:   把时间转换成需要的时间格式
     * 方法名称:   getTime
     *
     * @return 返回类型:   String
     */
    public static String getToTime(String time, String oldFormat, String newFormat) {
        SimpleDateFormat formart = new SimpleDateFormat(oldFormat);
        Date date = null;
        try {
            date = formart.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new SimpleDateFormat(newFormat).format(date);
    }

    public static String getString(String str) {
        return str == null ? "" : str;
    }


}
