package com.goatwalker.utils;

import java.math.BigInteger;

public class LCM_big
{

    public static void main(String[] args)
    {
        System.out.println(lcm(new long[] {3,7,4,2}));
    }

    public static BigInteger lcm(long[] nums)
    {
        BigInteger lcm = lcm2(BigInteger.valueOf(nums[0]), BigInteger.valueOf(nums[1]));
        for (int jj = 2; jj < nums.length; jj++)
            lcm = lcm2(lcm, BigInteger.valueOf(nums[jj]));
        return lcm;
    }

    private static BigInteger lcm2(BigInteger a, BigInteger b)
    {
        return a.multiply(b).divide(gcf2(a,b));
    }

    /**
    * Calculate Greatest Common Factor
    */
    public static BigInteger gcf2(BigInteger  a, BigInteger b) {
        if (b.equals(BigInteger.ZERO)) {
            return a;
        } else {
            return (gcf2(b, a.mod(b)));
        }
    } 
}
