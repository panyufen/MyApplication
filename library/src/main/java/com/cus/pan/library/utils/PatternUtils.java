package com.cus.pan.library.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by PAN on 2017/3/22.
 */
public class PatternUtils {

    /**
     * 验证是否是身份证号
     *
     * @param num
     * @return
     */
    public static boolean isIdCardNum(String num) {
        if (num != null) {
            Pattern pattern = Pattern.compile("^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9]|X)$");
            return pattern.matcher(num).matches();
        } else {
            return false;
        }
    }

    /**
     * 判断是否全是汉字
     *
     * @param str
     * @return
     */
    public static boolean isChineseAll(String str) {
        if (str != null) {
            Pattern pattern = Pattern.compile("[\u0391-\uFFE5]+$");
            return pattern.matcher(str).matches();
        } else {
            return false;
        }
    }


    /**
     * 验证是否是手机号
     *
     * @param num
     * @return
     */
    public static boolean isTelephoneNum(String num) {
        if (num != null) {
            Pattern pattern = Pattern.compile("1[0-9]{10}");
            Matcher matcher = pattern.matcher(num);
            if (matcher.matches()) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * 是否是邮箱
     *
     * @param str
     * @return
     */
    public static boolean isEmail(String str) {
        if (str != null) {
            Pattern pattern = Pattern.compile(
                    "[//w//.//-]+@([//w//-]+//.)+[//w//-]+",
                    Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(str);
            return matcher.matches();
        } else {
            return false;
        }
    }

    /**
     * 判断一个字符串是否都为数字
     *
     * @param strNum
     * @return
     */
    public static boolean isDigitAll(String strNum) {
        if (strNum != null) {
            Pattern pattern = Pattern.compile("[0-9]+");
            Matcher matcher = pattern.matcher(strNum);
            return matcher.matches();
        } else {
            return false;
        }
    }


    /**
     * 是否是数字
     *
     * @param value
     * @return
     */
    public static boolean isNumber(String value) {
        if (value != null) {
            return isInteger(value) || isDouble(value);
        } else {
            return false;
        }
    }


    /**
     * 判断首字母是否是字母
     * @param s
     * @return
     */
    public static boolean isPinyinFirst(String s) {
        char c = s.charAt(0);
        int i = (int) c;
        if ((i >= 65 && i <= 90) || (i >= 97 && i <= 122)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 是否是整数
     *
     * @param value
     * @return
     */
    public static boolean isInteger(String value) {
        if (value != null) {
            try {
                Integer.parseInt(value);
                return true;
            } catch (NumberFormatException e) {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * 是否是浮点数
     *
     * @param value
     * @return
     */
    public static boolean isDouble(String value) {
        if (value != null) {
            try {
                Double.parseDouble(value);
                if (value.contains("."))
                    return true;
                return false;
            } catch (NumberFormatException e) {
                return false;
            }
        } else {
            return false;
        }
    }


}
