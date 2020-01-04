package com.goatwalker.euler;

import java.math.BigInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class p206_fail
{

    public static void main(String[] args)
    {

        Pattern pattern = Pattern
                .compile("1\\d2\\d3\\d4\\d5\\d6\\d7\\d8\\d9");

        // 1_2_3_4_5_6_7_8_9_0
        BigInteger lb = new BigInteger("10203040506070809");

        System.out.println(pattern.matcher(lb.toString()).find());
        System.out.println(pattern.matcher("13233343536373839").find());

        long start = (long) Math.sqrt(lb.doubleValue());
        System.out.println(start);
        BigInteger test;
        int cnt;
        for (cnt = 0;; cnt++)
        {
            test = BigInteger.valueOf(start);
            test = test.multiply(test);
            if (pattern.matcher(test.toString()).find()) break;
//            System.out.printf("%5d:  %7d squared = %s\n", cnt, start, test);
            start++;
        }
        System.out.printf("%d:  %d %7d squared = %s\n", cnt, start*10, start, test);
    }
}
