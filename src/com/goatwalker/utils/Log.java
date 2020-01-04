package com.goatwalker.utils;

public class Log
{
    // static boolean verbose = false;
    static boolean verbose = true;
    
    static int cnt = 0;

    // static boolean verbose = false;
    public static void out(String txt)
    {
        if (verbose)
        {
            int line = Thread.currentThread().getStackTrace()[2]
                    .getLineNumber();
            System.out.printf("%4d (%4d): %s\n", cnt++, line, txt);
        }

    }

    public static void enable(boolean enable)
    {
        verbose = enable;
    }
}
