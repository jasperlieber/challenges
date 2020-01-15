package com.goatwalker.misc;

import java.util.Arrays;
import java.util.Base64;

public class cipher
{

    public static void main(String[] args)
    {
      byte[] in = "YHt1dzKJgYR9QDJggYkyfneGOYUydXpzgHl3Moh3gId3TDJ2gXV9d4Qygod+fjJ1gYB8h4R7gHVBdneId36BgneEP4OHe4w=".getBytes();
      in = Base64.getDecoder().decode(in);
      
      System.out.println(Arrays.toString(in));
      for (int jj = 0; jj < in.length; jj++)
        System.out.print(in[jj]+18);

    }

}
