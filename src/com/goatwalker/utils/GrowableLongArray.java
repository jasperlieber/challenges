package com.goatwalker.utils;

import java.util.Arrays;

public class GrowableLongArray
{
    public static void main(String[] args) throws Exception
    {
        GrowableLongArray a = new GrowableLongArray();
        System.out.println(a.get(3));

        a = new GrowableLongArray();
        a.set(3, 3);
        System.out.println(a.get(3));
        System.out.println(Arrays.toString(a.get(2,3)));
        
        a = new GrowableLongArray();
        a.initialize(new long[] {0,1,2,3});
        System.out.println(Arrays.toString(a.get(1,4)));
        a.set(0, 4);
        System.out.println(Arrays.toString(a.get(0,4)));
        System.out.println(a.get(3333));
    }

    private static final int maxMemSize = 1048576;
    private long[] mem = new long[0];

    public void initialize(long[] data)
    {
        assert(mem.length == 0);
        mem = Arrays.copyOf(data, data.length); 
    }

    public long get(long pos) throws Exception
    {
        checkBounds(pos);
        return mem[(int)pos];
    }

    private void checkBounds(long pos) throws Exception
    {
        if (pos < 0) throw new Exception("negative memory index (" + pos + ")");
        if (pos > maxMemSize) throw new Exception("memory index (" + pos + ") too large (> " + maxMemSize + ")");
        if (mem.length <= pos) growMem((int)pos+1);
    }

    private void growMem(int newSize)
    {
        long[] newMem = new long[newSize];
        System.arraycopy(mem, 0, newMem, 0, mem.length);
        mem = newMem;
    }

    public long[] get(int ip, int i) throws Exception
    {
        if (ip < 0) throw new Exception("negative memory index (" + ip + ")");
        checkBounds(ip+i-1);
        return Arrays.copyOfRange(mem, ip, ip + i);
    }

    public void set(long index, long val) throws Exception
    {
        checkBounds(index);
        mem[(int)index] = val;
    
    }

    public int memsize()
    {
        return mem.length;
    }
}
