package com.goatwalker.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Permutation<T>
{
    public static void main(String[] args)
    {
        Integer[] str = new Integer[] { 0, 1, 2, 3, 4 };
        Permutation<Integer> permutation = new Permutation<Integer>();
        List<Integer[]> perms = permutation.permute(str);

        System.out.println(permutation.stringify(perms));
    }

    private T[] cur = null;
    private List<T[]> perms = new ArrayList<T[]>();

    public List<T[]> permute(T[] str)
    {
        int n = str.length;
        cur = Arrays.copyOf(str, n);
        subPermute(0, n - 1);
        return perms;
    }

    private void subPermute(int l, int r)
    {
        if (l == r) perms.add(Arrays.copyOf(cur, cur.length));
        else
        {
            for (int i = l; i <= r; i++)
            {
                swap(l, i);
                subPermute(l + 1, r);
                swap(l, i);
            }
        }
    }

    private String stringify(List<T[]> perms2)
    {

        String out = "[";
        int jj = 0;
        for (T[] perm : perms2)
        {
            out = out + " " + ++jj + ":";
            for (T n : perm)
                out = out + n.toString() + "";
        }
        out = out + " ]";
        return out;
    }

    @SuppressWarnings("unused")
    private String stringify(T[] arr)
    {
        String out = "";
        for (T n : arr)
            out = out + n.toString();
        return out;
    }

    private void swap(int i, int j)
    {
        T temp = cur[i];
        cur[i] = cur[j];
        cur[j] = temp;
    }

}
