package com.goatwalker.utils;

public class Logger
{
    public static class Log
    {
        static boolean verbose = false;
//        static boolean verbose = true;

        // static boolean verbose = false;
        public static void out(String string)
        {
            if (verbose) System.out.println(string);

        }

        public static void enable(boolean enable)
        {
            verbose = enable;
        }

    }

}
