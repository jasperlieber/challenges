package com.goatwalker.aoc19;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class MostFreq
{
    public static void main(String[] args) throws Exception
    {

        ArrayList<String> vins = new ArrayList<String>(
                Arrays.asList("aaa", "bbb", "ccc", "aaa", "aaa", "bbb", "ccc",
                        "ddd", "eee", "fff", "fff", "fff", "fff"));

        Map<String, Integer> counts = new HashMap<String, Integer>();

        // get frequency counts
        for (String vin : vins)
        {
            counts.put(vin, counts.getOrDefault(vin, 0) + 1);
        }

        // create an array list contain pairs containing the frequency count as
        // well as a list of vins
        // with that frequency count
        ArrayList<Pair<Integer, HashSet<String>>> freqs = new ArrayList<Pair<Integer, HashSet<String>>>();

        for (Map.Entry<String, Integer> vinCount : counts.entrySet())
        {
            String vin = vinCount.getKey();
            int count = vinCount.getValue();

            // look for where to insert this count
            int jj;
            for (jj = 0; jj < freqs.size()
                    && count < freqs.get(jj).getFirst(); jj++)
                ;

            HashSet<String> vinset = new HashSet<String>();
            vinset.add(vin);

            Pair<Integer, HashSet<String>> pair = new Pair<Integer, HashSet<String>>(
                    count, vinset);

            if (jj == freqs.size())
            {
                // create new entry
                freqs.add(pair);
            }
            else if (count == freqs.get(jj).getFirst())
            {
                vinset.addAll(freqs.get(jj).getSecond());
                pair.setSecond(vinset);
                freqs.set(jj, pair);
            }
            else
            {
                freqs.add(jj, pair);
            }
        }

        System.out.println(vins);
        System.out.println(counts);
        System.out.println(freqs);

        // 
        // At this point, the freqs array contains the sets of VINs with same frequency, in
        // highest frequency order.  Example output is this:
        //    [(4, [fff]), (3, [aaa]), (2, [ccc, bbb]), (1, [eee, ddd])]
        // meaning there are vin "fff" appears 4 times, "aaa" 3 times, 
        // and "ccc" and "bbb" appear twice.
        //
        // The frequency array could be culled to a max of N after construction in order
        // to give the top N most frequent VINs.
        //
    }
}

// This is a generic pair class that I wrote & have used in other projects 
// Jasper Lieber
class Pair<A, B>
{
    private A first;
    private B second;

    public Pair(A first, B second)
    {
        super();
        this.first = first;
        this.second = second;
    }

    public int hashCode()
    {
        int hashFirst = first != null ? first.hashCode() : 0;
        int hashSecond = second != null ? second.hashCode() : 0;

        return (hashFirst + hashSecond) * hashSecond + hashFirst;
    }

    public boolean equals(Object other)
    {
        if (other instanceof Pair)
        {
            Pair<?, ?> otherPair = (Pair<?, ?>) other;
            return ((this.first == otherPair.first
                    || (this.first != null && otherPair.first != null
                            && this.first.equals(otherPair.first)))
                    && (this.second == otherPair.second
                            || (this.second != null && otherPair.second != null
                                    && this.second.equals(otherPair.second))));
        }

        return false;
    }

    public String toString()
    {
        return "(" + first + ", " + second + ")";
    }

    public A getFirst()
    {
        return first;
    }

    public void setFirst(A first)
    {
        this.first = first;
    }

    public B getSecond()
    {
        return second;
    }

    public void setSecond(B second)
    {
        this.second = second;
    }
}