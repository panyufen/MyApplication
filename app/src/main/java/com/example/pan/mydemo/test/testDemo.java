package com.example.pan.mydemo.test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by PAN on 2016/9/29.
 */
public class testDemo {

    public static void main(String[] args) {
        System.out.println("testDemo Start");

//        print9DataType();

//        printIntInteger();
//        printSwitchStr("abc");

//        printCharString();

//        printUrlParams();

//        printTestArrayListAndLinkedList();

        // 127  0111 1111
        // 125  0111 1101
        // 2    0000 0010


        //1 0001
        //2 0010
        //4 0100
        //3 0011
        //6 0110
        //7 0111
        //8 1000

//        int s = 1|2|4|8;
//        System.out.println("1248: "+s);

//        int a = (7&2);

//        System.out.println("testDemo End "+Integer.toBinaryString(a)+" ");


//        printRegex();


        String rasStr = "\n" +
                "MIICXQIBAAKBgQCtuvl8359i4LkBbUKmW+hVS2j4QsrcakYKIGsipFqiuhxQ5M76\n" +
                "lg5MsL6n0LUcbaw+3XMGsAX0E6GyTfdBoGbsEk4AAY6KA0g5KxONo6yBLIXH7oLT\n" +
                "peaMqiiib4XMeLuHA+Mw/Ht1Ls4ERz+p74SVRN/2cQA5KTPVoZD8LL728QIDAQAB\n" +
                "AoGAdaddVJshFtZ3lCbJb15ovnLeeNrLBXCXVD5WL9S8aN2/VW1PWtcKghdkVsP6\n" +
                "E3GWp9BTGOFWdtBgvh8c5v3ZFMJzN9VNSSFlL8EmrI43h9T4+tLrn3628NI60L2w\n" +
                "MJRal89cin1YR2udTUQCiKt4XFr0ylYUpFxCufmVgFMOc50CQQDjRjAef6zzYzUN\n" +
                "3vvFu5kueKOOzZZj1rJtL+syDN7T+rqZNAqhtVsSKyxCc2F5PTlVF1IsSy8932bV\n" +
                "ZYde4LOPAkEAw7BK/t7cJZYLvkAf7stoiNjhMGb4o+nAfqXV/3Gouv5RmvRPBnV8\n" +
                "ADIh7YIQBzRmmwc+3j65OFfi7xSfEw5tfwJAMl4Ps/KfpaDK1TQhoASfQglCVheB\n" +
                "yMZ/7Gq+OO1ZRKlASUubP5Mth61BtchURcYnuo/ciixvX8ruw0Qo6AHsSQJBAITh\n" +
                "Myu6fVbwemzn8kcezI8QZmmNooz7b4EQby4UKfVAOH22I+tI37jqUeuLhALAbGQd\n" +
                "GW2kvfUqiKi8IgC4rVUCQQCvOuZmaO3PFUw3aYb7zYsHKRTftWCu3gyxCIci6/rr\n" +
                "gNkd3vIM6vw5g+R5QsT4bsRvNmdWxnMDqmQyowVKqIUo";
        System.out.println(rasStr.length());


        System.out.println("testDemo End");
    }


    private static void print9DataType() {
        //九种数据类型
        //byte
        System.out.println("Class对象:" + byte.class.getName());
        System.out.println("数组Class对象:" + byte[].class.getName());
        System.out.println("包装类Class对象:" + Byte.class.getName());
        System.out.println("包装类数组Class对象:" + Byte[].class.getName());
        System.out.println("二进制位数:" + Byte.SIZE);
        System.out.println("最小值:" + Byte.MIN_VALUE);
        System.out.println("最大值:" + Byte.MAX_VALUE);

        //short
        System.out.println();
        System.out.println("Class对象:" + short.class.getName());
        System.out.println("数组Class对象:" + short[].class.getName());
        System.out.println("包装类Class对象:" + Short.class.getName());
        System.out.println("包装类数组Class对象:" + Short[].class.getName());
        System.out.println("二进制位数:" + Short.SIZE);
        System.out.println("最小值:" + Short.MIN_VALUE);
        System.out.println("最大值:" + Short.MAX_VALUE);

        //int
        System.out.println();
        System.out.println("Class对象:" + int.class.getName());
        System.out.println("数组Class对象:" + int[].class.getName());
        System.out.println("包装类Class对象:" + Integer.class.getName());
        System.out.println("包装类数组Class对象:" + Integer[].class.getName());
        System.out.println("二进制位数:" + Integer.SIZE);
        System.out.println("最小值:" + Integer.MIN_VALUE);
        System.out.println("最大值:" + Integer.MAX_VALUE);

        //long
        System.out.println();
        System.out.println("Class对象:" + long.class.getName());
        System.out.println("数组Class对象:" + long[].class.getName());
        System.out.println("包装类Class对象:" + Long.class.getName());
        System.out.println("包装类数组Class对象:" + Long[].class.getName());
        System.out.println("二进制位数:" + Long.SIZE);
        System.out.println("最小值:" + Long.MIN_VALUE);
        System.out.println("最大值:" + Long.MAX_VALUE);

        //float
        System.out.println();
        System.out.println("Class对象:" + float.class.getName());
        System.out.println("数组Class对象:" + float[].class.getName());
        System.out.println("包装类Class对象:" + Float.class.getName());
        System.out.println("包装类数组Class对象:" + Float[].class.getName());
        System.out.println("二进制位数:" + Float.SIZE);
        System.out.println("最小值:" + Float.MIN_VALUE);
        System.out.println("最大值:" + Float.MAX_VALUE);

        //double
        System.out.println();
        System.out.println("Class对象:" + double.class.getName());
        System.out.println("数组Class对象:" + double[].class.getName());
        System.out.println("包装类Class对象:" + Double.class.getName());
        System.out.println("包装类数组Class对象:" + Double[].class.getName());
        System.out.println("二进制位数:" + Double.SIZE);
        System.out.println("最小值:" + Double.MIN_VALUE);
        System.out.println("最大值:" + Double.MAX_VALUE);

        //char
        System.out.println();
        System.out.println("Class对象:" + char.class.getName());
        System.out.println("数组Class对象:" + char[].class.getName());
        System.out.println("包装类Class对象:" + Character.class.getName());
        System.out.println("包装类数组Class对象:" + Character[].class.getName());
        System.out.println("二进制位数:" + Character.SIZE);
        System.out.println("最小值:" + Character.MIN_VALUE);
        System.out.println("最大值:" + Character.MAX_VALUE);

        //boolean
        System.out.println();
        System.out.println("Class对象:" + boolean.class.getName());
        System.out.println("数组Class对象:" + boolean[].class.getName());
        System.out.println("包装类Class对象:" + Boolean.class.getName());
        System.out.println("包装类数组Class对象:" + Boolean[].class.getName());
//        System.out.println("二进制位数:" + Boolean.SIZE);
//        System.out.println("最小值:" + Boolean.MIN_VALUE);
//        System.out.println("最大值:" + Boolean.MAX_VALUE);

        //void
        System.out.println();
        System.out.println("Class对象:" + void.class.getName());
        //System.out.println("数组Class对象:" + void[].class.getName());
        System.out.println("包装类Class对象:" + Void.class.getName());
        System.out.println("包装类数组Class对象:" + Void[].class.getName());
        //System.out.println("二进制位数:" + Void.SIZE);
        //System.out.println("最小值:" + Void.MIN_VALUE);
        //System.out.println("最大值:" + Void.MAX_VALUE);
    }

    private static void printIntInteger() {
        int i = 128;
        Integer i2 = 128;
        Integer i3 = new Integer(128);
        //Integer会自动拆箱为int，所以为true
        System.out.println(i == i2);
        System.out.println(i == i3);
        System.out.println("**************");
        Integer i5 = 127;//java在编译的时候,被翻译成-> Integer i5 = Integer.valueOf(127);
        Integer i6 = 127;
        System.out.println(i5 == i6);//true

        Integer i51 = 128;
        Integer i61 = 128;
        System.out.println(i51 == i61);//false

        Integer ii5 = new Integer(127);
        System.out.println(i5 == ii5); //false
        Integer i7 = new Integer(128);
        Integer i8 = new Integer(128);
        System.out.println(i7 == i8);  //false
    }

    private static void printSwitchStr(String str) {
        switch (str) {
            case "abc":
                System.out.println("abc");
                break;
            case "def":
                System.out.println("def");
                break;
            default:
                System.out.println("default");
        }
    }

    private static void printCharString() {
        String hanzi = "潘钰汾panyufen";
        char[] hanziChar = hanzi.toCharArray();
        for (char w : hanziChar) {
            System.out.println(w + " " + (int) w);
        }

        String ab = "ab" + "cd";
        String cd = "abcd";
        System.out.println((ab == cd) + " " + ab.equals(cd));
        ab = "abcd";
        System.out.println(ab == cd);

    }

    private static void printRegex() {
        // 按指定模式在字符串查找
        String line = "This order was placed for QT3000! 9OK?";
        String pattern = "(\\D*)(\\d+)(.*)";

        // 创建 Pattern 对象
        Pattern r = Pattern.compile(pattern);

        // 现在创建 matcher 对象
        Matcher m = r.matcher(line);
        if (m.find()) {
            System.out.println("Found value"+m.groupCount()+":0 " + m.group(0));
            System.out.println("Found value"+m.groupCount()+":1 " + m.group(1));
            System.out.println("Found value"+m.groupCount()+":2 " + m.group(2));
            System.out.println("Found value"+m.groupCount()+":3 " + m.group(3));
        } else {
            System.out.println("NO MATCH");
        }
    }

    private static void printUrlParams() {
        try {
            String url = "https://mapi.alipay.com/gateway.do?type=1&no=2";

//            URLEncodedUtils.parse(query, Charset.forName("UTF-8"));
//
//            MultiMap<String> values = new MultiMap<String>();
//            UrlEncoded.decodeTo(query, values, "UTF-8", 1000);
//
//            Map<String, String> values1 = new HashMap<String, String>();
//            RequestUtil.parseParameters(values1, query, "UTF-8");


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //测试ArrayList LinkedList 性能
    private static void printTestArrayListAndLinkedList(){
        ArrayList<Integer> arrayList = new ArrayList<Integer>();
        LinkedList<Integer> linkedList = new LinkedList<Integer>();

        int
//                times = 10 * 1000;
                times = 100 * 1000;
//                times = 1000 * 1000;
        System.out.println("Test times = " + times);
        System.out.println("-------------------------");
        // ArrayList add
        long startTime = System.nanoTime();

        for (int i = 0; i < times; i++) {
            arrayList.add(i);
        }
        long endTime = System.nanoTime();
        long duration = endTime - startTime;
        System.out.println(duration + " <--ArrayList add");

        // LinkedList add
        startTime = System.nanoTime();

        for (int i = 0; i < times; i++) {
            linkedList.add(i);
        }
        endTime = System.nanoTime();
        duration = endTime - startTime;
        System.out.println(duration + " <--LinkedList add");
        System.out.println("-------------------------");
        // ArrayList get
        startTime = System.nanoTime();

        for (int i = 0; i < times; i++) {
            arrayList.get(i);
        }
        endTime = System.nanoTime();
        duration = endTime - startTime;
        System.out.println(duration + " <--ArrayList get");

        // LinkedList get
        startTime = System.nanoTime();

        for (int i = 0; i < times; i++) {
            linkedList.get(i);
        }
        endTime = System.nanoTime();
        duration = endTime - startTime;
        System.out.println(duration + " <--LinkedList get");
        System.out.println("-------------------------");

        // ArrayList remove
        startTime = System.nanoTime();

        for (int i = times - 1; i >= 0; i--) {
            arrayList.remove(i);
        }
        endTime = System.nanoTime();
        duration = endTime - startTime;
        System.out.println(duration + " <--ArrayList remove");

        // LinkedList remove
        startTime = System.nanoTime();

        for (int i = times - 1; i >= 0; i--) {
            linkedList.remove(i);
        }
        endTime = System.nanoTime();
        duration = endTime - startTime;
        System.out.println(duration + " <--LinkedList remove");

    }
}
