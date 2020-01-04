package com.goatwalker.aoc19;

public class CatAlgebra
{

    public static void main(String[] args)
    {
        for (int c = 1; c < 9; c++)
        {
            for (int a = 1; a < 9; a++)
            {
                for (int t = 1; t < 9; t++)
                {
                    int p = (c + a + t) * c * a * t;
                    if (p > 999) continue;
                    int d1 = p % 10;
                    int d2 = p / 10 % 10;
                    int d3 = p / 100 % 10;
                    if (d1 == t && d2 == a && d3 == c)
                        System.out.println(p + ": " + c + a + t + " = "
                                + (c + a + t) + " * " + (c * a * t));
                }
            }
        }

    }

}
