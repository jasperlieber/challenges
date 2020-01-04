package com.goatwalker.euler;

import java.math.BigInteger;

public class p97
{

    public static void main(String[] args)
    {
        long pp = 1;
        for (int jj = 0; jj < 10; jj++)
        {
            pp = pp * 2 % 100000000000L;
            System.out.println(pp);
        }
        System.out.println(pp);

        pp = 1;
        for (int jj = 0; jj < 7830457; jj++)
        {
            pp = pp * 2 % 100000000000L;
            //             1234567890
        }
        pp *= 28433L;
        pp += 1L;

        System.out.println(1234567890);
        System.out.println(pp % 10000000000L);
        //                       1234567890
        
        long mod = 10000000000L;
        
        BigInteger modb = new BigInteger("10000000000");
        BigInteger twob = new BigInteger("2");
        BigInteger expb = new BigInteger("7830457");
        
        long tmp = twob.modPow(expb, modb).longValue();
        long result = (28433L * tmp +1) % mod;
        System.out.println(result);
    }
}
