package com.goatwalker.euler;

import java.math.BigInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class p206
{

    public static void main(String[] args)
    {
        int max = 999999999;
        for (long nn = 0; nn < 100; nn++)
        {
            
            /*
               1,020,304,052,423,222,121
               1,020,304,052,607080900 
            
             */
            // 0123456789012345678
            //      max long 9223372036854775807
            String target = "1020304050607080900";
            long val = nn;
            for (int kk = 0; kk < 10 && val > 0; kk++)
            {
                int digit = (int) (val % 10L);
                val /= 10;
                target = target.substring(0, 17 - kk * 2) + digit
                        + target.substring(18 - kk * 2);
            }
            // System.out.printf("%9d %s\n", nn, target);
            BigInteger bb = new BigInteger(target);
            long root = (long) Math.sqrt(bb.doubleValue());
            System.out.printf("%d %9s %s\n", root, root * root + "", target);
            if (root * root + "" == target) System.out.println(root);

        }
    }
}
