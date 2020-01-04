package com.goatwalker.utils;

public class LCM_long
{

    public static void main(String[] args)
    {
        System.out.println(lcm(new long[] {3,7,4,2}));
    }

    public static long lcm(long[] nums)
    {
        long lcm = lcm2(nums[0], nums[1]);
        for (int jj = 2; jj < nums.length; jj++)
            lcm = lcm2(lcm, nums[jj]);
        return lcm;
    }

    private static long lcm2(long a, long b)
    {
        return (a * b) / gcf2(a, b);
    }

    /**
    * Calculate Greatest Common Factor
    */
    public static long gcf2(long  a, long b) {
        if (b == 0) {
            return a;
        } else {
            return (gcf2(b, a % b));
        }
    } 
}
