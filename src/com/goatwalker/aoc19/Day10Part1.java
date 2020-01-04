package com.goatwalker.aoc19;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import com.goatwalker.utils.Pair;

//           1125899906842624
// max long: 9223372036854775807
// max int:  2147483647

public class Day10Part1
{
    public static void main(String[] args) throws Exception
    {
        Day10Part1 game = new Day10Part1();
        game.doit();
    }

    private void doit() throws Exception
    {
        String filename = "C:\\Users\\jasper\\Google Drive\\Fun\\2016AdventOfCode\\EclipseWorkspace\\2019-data\\"
                // + "10p1.txt";
                + "10p1.txt";

        MeteorGrid grid = new MeteorGrid(filename);
        grid.calcVisibles();
        // max x,y: 27 19 sees 314 other meteors

    }

    public class MeteorGrid
    {

        private boolean[][] mets;
        private Integer[][] visibles;
        private int sizeX, sizeY;
        private Set<Ray> rays = new TreeSet<Ray>();
        private int maxX = -1, maxY = -1;
        private int meteorCount;

        public class Ray extends Pair<Integer, Integer>
                implements Comparable<Ray>
        {

            public Ray(Integer first, Integer second)
            {
                super(first, second);
            }

            public boolean colinear(int xx, int yy)
            {
                return xx * getSecond() == yy * getFirst();
            }

            @Override
            public int compareTo(Ray o)
            {
                int aa = getFirst().compareTo(o.getFirst());
                int bb = getSecond().compareTo(o.getSecond());
                return aa != 0 ? aa : bb;
            }

            public Ray meteorsVisibleFrom(int xx, int yy)
            {
                int walk = 1;
                while (true)
                {
                    int cx = xx + getFirst() * walk;
                    int cy = yy + getSecond() * walk;
                    // System.out.println("From "
                    // + (new Ray(xx, yy) + " along "
                    // + this + " checking " + (new Ray(cx,cy))));
                    if (!inbounds(cx, cy)) break;
                    if (mets[cx][cy])
                    { return new Ray(cx, cy); }
                    walk++;
                }
                return null;
            }

        }

        public MeteorGrid(String filename)
                throws FileNotFoundException, IOException
        {
            getData(filename);
            calcRays();
        }

        public boolean inbounds(int cx, int cy)
        {
            return 0 <= cx && cx < sizeX && 0 <= cy && cy < sizeY;
        }

        public void calcVisibles()
        {
            visibles = new Integer[sizeX][sizeY];
            int maxvis = -1;
            for (int xx = 0; xx < sizeX; xx++)
            {
                for (int yy = 0; yy < sizeY; yy++)
                {
                    if (mets[xx][yy])
                    {
                        int numvis = 0;
                        for (Ray ray : rays)
                        {
                            boolean visible = ray.meteorsVisibleFrom(xx,
                                    yy) != null;
                            // System.out.println("ray " + ray + ": " +
                            // visible);
                            if (visible) numvis++;
                        }
                        visibles[xx][yy] = numvis;
                        if (numvis > maxvis)
                        {
                            maxvis = numvis;
                            maxX = xx;
                            maxY = yy;
                        }
                    }
                }
            }
            System.out.println("max x,y: " + maxX + " " + maxY + " sees "
                    + maxvis + " other meteors");
        }

        private void calcRays()
        {
            for (int xx = 1; xx < sizeX; xx++)
            {
                for (int yy = 1; yy < sizeY; yy++)
                {
                    boolean covered = false;
                    for (Ray ray : rays)
                    {
                        if (covered = ray.colinear(xx, yy)) break;
                    }
                    if (!covered) rays.add(new Ray(xx, yy));
                }
            }

            Set<Ray> otherRays = new HashSet<Ray>();

            for (Ray ray : rays)
            {
                otherRays.add(new Ray(-ray.getFirst(), ray.getSecond()));
                otherRays.add(new Ray(ray.getFirst(), -ray.getSecond()));
                otherRays.add(new Ray(-ray.getFirst(), -ray.getSecond()));
            }

            rays.addAll(otherRays);

            rays.add(new Ray(0, 1));
            rays.add(new Ray(1, 0));
            rays.add(new Ray(0, -1));
            rays.add(new Ray(-1, 0));

            // System.out.println(rays);
        }

        private void getData(String filename)
                throws IOException, FileNotFoundException
        {
            ArrayList<ArrayList<Boolean>> bits = new ArrayList<ArrayList<Boolean>>();

            try (BufferedReader br = new BufferedReader(
                    new FileReader(filename)))
            {
                String line;
                while ((line = br.readLine()) != null)
                {
                    // System.out.println(line);
                    int len = line.length();
                    ArrayList<Boolean> bline = new ArrayList<Boolean>(len);

                    for (int i = 0; i < len; i++)
                    {
                        bline.add(line.charAt(i) == '#');
                    }
                    bits.add(bline);
                }
            }

            sizeY = bits.size();
            sizeX = bits.get(0).size();
            mets = new boolean[sizeX][sizeY];
            for (int i = 0; i < sizeX; i++)
            {
                for (int j = 0; j < sizeY; j++)
                {
                    mets[i][j] = bits.get(j).get(i);
                    if (mets[i][j]) meteorCount++;
                }
            }

            showBits("------------mets:");
        }

        private void showBits(String string)
        {
            System.out.println(string);
            for (int i = 0; i < sizeX; i++)
            {
                for (int j = 0; j < sizeY; j++)
                {
                    System.out.print(mets[j][i] ? "#" : '.');
                }
                System.out.println();
            }

        }

        @SuppressWarnings("unused")
        private String showVisibles()
        {
            for (int xx = 0; xx < sizeX; xx++)
            {
                for (int yy = 0; yy < sizeY; yy++)
                {
                    System.out.print(
                            (visibles[yy][xx] == null ? "." : visibles[yy][xx])
                                    + " ");
                }
                System.out.println();
            }
            return null;
        }

        private TreeMap<Double, Ray> sortedRays = new TreeMap<Double, Ray>();

        private void computeSortedRays()
        {
            for (Ray ray : rays)
            {
                double theta = Math.atan2(ray.getFirst(), -ray.getSecond())
                        * 180 / 3.14159;
                if (theta < 0) theta = 360.0 + theta;
                // System.out.printf("%d %d %6.2f\n", ray.getFirst(),
                // ray.getSecond(),
                // theta);

                sortedRays.put(theta, ray);
            }
            // System.out.println(sortedRays);
            // assert(false);
        }

        public void sweepDestroy()
        {
            int destroyedCount = 0;
            System.out.println("meteor count = " + meteorCount);
            while (destroyedCount < meteorCount - 1)
            {
                for (Map.Entry<Double, Ray> entry : sortedRays.entrySet())
                {
                    // System.out.printf("Count %d: %5.2f -> %s\n",
                    // destroyedCount, entry.getKey(), entry.getValue());

                    Ray ray = entry.getValue();
                    Ray meteor = ray.meteorsVisibleFrom(maxX, maxY);
                    if (meteor != null)
                    {
                        // System.out.println("From "
                        // + (new Ray(maxX, maxY) + " along "
                        // + ray + " hit " + meteor));
                        destroyedCount++;
                        System.out.println(
                                "Count " + destroyedCount + ":  " + meteor);

                        mets[meteor.getFirst()][meteor.getSecond()] = false;
                    }
                }
            }
        }
    }

}