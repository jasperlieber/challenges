package com.goatwalker.utils;

public class Coord extends Pair<Integer, Integer>
{
    public Coord(Integer first, Integer second)
    {
        super(first, second);
    }

    public Coord(Coord c)
    {
        super(c.getFirst(), c.getSecond());
    }

    public static void main(String[] args)
    {
    }

    public Coord getNeighbor(int curDir)
    {
        Coord newPos = new Coord(this);

        // dirs are 0123 meaning NESW
        switch (curDir)
        {
        case 0:
            return new Coord(getFirst(), getSecond() + 1);
        case 1:
            return new Coord(getFirst() + 1, getSecond());
        case 2:
            return new Coord(getFirst(), getSecond() - 1);
        case 3:
            return new Coord(getFirst() - 1, getSecond());
        default:
            assert (false);
        }

        return null;
    }

    public int dist()
    {
        return Math.abs(getFirst()) + Math.abs(getSecond());
    }
}
