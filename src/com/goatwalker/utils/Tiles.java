package com.goatwalker.utils;

import java.util.Collections;
import java.util.HashMap;

public class Tiles extends HashMap<Coord, Integer>
{
    private static final long serialVersionUID = 1L;

    public void addTile(Coord curPos, int color)
    {
        assert (!containsKey(curPos));
        this.put(curPos, color);
    }

    public int getTile(Coord curPos)
    {
        Integer color = this.get(curPos);
        if (color == null)
        {
            color = 0;
            this.put(curPos, 0);
        }
        return color;
    }

    public void setTile(Coord curPos, int b)
    {
//        if (getTile(curPos) == 2 && b == 0)
//        {
//            System.out.println("removing " + curPos);
//        }
        this.put(curPos, b);
    }
    
    private char[] tileChars = new char[] {'.','X', 'O', '-', 'b'};
    
    private char getTileChar(int i)
    {
        return tileChars[i];
    }
    
    public void setTileChars(char[] chars)
    {
        tileChars = chars;
    }



    public void display(String s)
    {
        System.out.println(s);
        display();
    }

    public void display()
    {
        int minx = 0, miny = 0, maxx = 0, maxy = 0;
        for (Coord c : this.keySet())
        {
            int x = c.getFirst();
            int y = c.getSecond();
            if (x < minx) minx = x;
            if (x > maxx) maxx = x;
            if (y < miny) miny = y;
            if (y > maxy) maxy = y;
        }
        
//        System.out.println(maxy);assert(false);

        int[][] display = new int[maxx - minx + 1][maxy - miny + 1];

        for (Entry<Coord, Integer> e : this.entrySet())
        {
            int x = e.getKey().getFirst();
            int y = e.getKey().getSecond();

            display[x - minx][y - miny] = e.getValue();
        }

        System.out.print("    ");
        for (int x = minx; x <= maxx ; x++)
        {
            System.out.print(x % 10 == 0 ? x/10 : " ");
        }
        System.out.println();
        System.out.print("    ");
        for (int x = minx; x <= maxx ; x++)
        {
            System.out.print(Math.abs(x) % 10);
        }
        System.out.println();
        for (int y = maxy-miny; y >= 0; y--)
        {
            System.out.printf("%-4d", y+miny);
            for (int x = 0; x <= maxx - minx; x++)
            {
                System.out.print(getTileChar(display[x][y]));
            }
            System.out.println();
        }

    }

    public int getCount(int i)
    {
        return Collections.frequency(this.values(), i);
    }
}