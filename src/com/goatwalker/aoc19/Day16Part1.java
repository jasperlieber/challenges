package com.goatwalker.aoc19;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.ListIterator;

import com.goatwalker.utils.Coord;
import com.goatwalker.utils.Tiles;

public class Day15Part2
{
    final long[] dirs = new long[] { 1, 2, 3, 4 }; // nswe
    final long[] reverse = new long[] { -1, 2, 1, 4, 3 };

    // Coord class has diff idea of dirs (nesw)
    final int[] convDir = new int[] { -1, 0, 2, 3, 1 };

    private int numMoves = 0;

    private static final int tileUnknown = 0;
    private static final int tileKnown = 1;
    private static final int tileWall = 2;
    private static final int tileDroid = 3;
    private static final int tileSystem = 4;
    private static final int tileOx = 5;

    public static void main(String[] args) throws Exception
    {
        Day15Part2 game = new Day15Part2();
        game.doit();
    }

    Tiles tiles = null;
    IntComp15 comp = null;

    private void doit() throws Exception
    {
        long[] program = loadProgram();

        comp = new IntComp15(program);

        tiles = new Tiles();

        // tile chars: 0=unknown, 1=known, 2=wall, 3=droid, 4=system
        tiles.setTileChars(new char[] { ' ', '.', '#', 'D', 'S', 'o' });

        numMoves = 0;

        Coord pos = new Coord(0, 0);
        Coord lastpos = pos;

        tiles.put(pos, tileKnown);

        ClosestUnexplored explore = new ClosestUnexplored();

        explore.add(pos);

        Coord systemPos = null;
        while (true)// && numMoves < 3)
        {
            pos = explore.poll();
            if (pos == null) break;
            // Log.out("Polled " + pos + " lastpos = " + lastpos);

            if (!pos.equals(lastpos))
            {
                moveDroid(lastpos, pos);
            }
            
            tiles.setTile(pos, tileKnown);

            for (long dir : dirs)
            {
                Coord neighbor = (Coord) pos.getNeighbor(convDir[(int) dir]);

                if (tiles.getTile(neighbor) == tileUnknown)
                {

                    // Log.out(" checking neighbor " + neighbor + " for dir " +
                    // dir);
                    // request move in dir
                    comp.addInput(dir);
                    Long res = comp.resume();

                    // Log.out(" result " + res);
                    switch (Math.toIntExact(res))
                    {
                    case 0: // wall
                        tiles.setTile(neighbor, tileWall);
                        // Log.out(" wall - set " + neighbor + " to " + tileWall
                        // + " tiles=" + tiles);
                        explore.remove(neighbor);
                        break;
                    case 1: // moved
                    case 2: // moved & found system

                        navigation.put(neighbor, dir);
                        explore.add(neighbor);

                        if (res == 2L) systemPos = neighbor;

                        // Log.out(" not wall - set " + neighbor + " to " +
                        // tiles.getTile(neighbor));

                        // move back
                        comp.addInput(reverse[(int) dir]);
                        res = comp.resume();
                        assert (res != 0);

                        break;
                    // case 2: // moved & found system
                    // tiles.setTile(pos, tileKnown);
                    // tiles.setTile(neighbor, tileSystem);
                    // navigation.put(neighbor, dir);
                    // systemPos = neighbor;
                    // break;
                    default:
                        assert (false);
                    }
                }
            }

            lastpos = pos;

//            tiles.display("Board #" + numMoves);

            numMoves++;
        }

        tiles.setTile(systemPos, tileSystem);
        tiles.display("Board #" + numMoves);

        System.out.println("path length to system is " + pathlen(systemPos));
        System.out.println("longest path from oxygen system is " + getLongestPath(systemPos));
    }


    public class CoordDist extends Pair<Coord, Integer>
    {
        public CoordDist(Coord first, Integer second)
        {
            super(first, second);
        }

    }

    private int getLongestPath(Coord systemPos)
    {
        int maxDist = 0;
        tiles.put(systemPos, tileOx);
        LinkedList<CoordDist> bfs = new LinkedList<CoordDist>();
        bfs.add(new CoordDist(systemPos,0));
        while(!bfs.isEmpty())
        {
            CoordDist cur = bfs.poll();
            Coord curcoord = cur.getFirst();
            int curDist = cur.getSecond();
            if (curDist > maxDist) maxDist = curDist;
            tiles.put(curcoord, tileOx);
            
            tiles.display("curDist = " + curDist);
             
            
            final int[] coorddirs = new int[] {0,1,2,3};
            for (int dir : coorddirs)
            {
                Coord neighbor = curcoord.getNeighbor(dir);
                if (tiles.get(neighbor) == tileKnown)
                {
                    bfs.add(new CoordDist(neighbor, curDist+1));
                }
            }
        }
                
        return maxDist;
    }

    private Coord before(Coord pos)
    {
        long dir = navigation.get(pos);
        assert (dir > 0);
        dir = reverse[(int) dir];
        return pos.getNeighbor(convDir[(int) dir]);
    }

    private void moveDroid(Coord from, Coord to) throws Exception
    {
        // move droid to origin by reversing the dirs in navigation
        LinkedList<Long> dirs = getDirsToCoord(from);

        Coord pos = from;

        while (!dirs.isEmpty())
        {
            long dir = dirs.pollLast();
            comp.addInput(reverse[(int) dir]);
            Long res = comp.resume();
            // assert (res != 0L);
            pos = pos.getNeighbor(convDir[(int) dir]);
        }

        dirs = getDirsToCoord(to);
        pos = new Coord(0, 0);
        while (!dirs.isEmpty())
        {
            long dir = dirs.pollFirst();
            comp.addInput(dir);
            Long res = comp.resume();
            // assert (res != 0L);
            pos = pos.getNeighbor(convDir[(int) dir]);
        }

        assert (pos.equals(to));

    }

    private LinkedList<Long> getDirsToCoord(Coord to)
    {
        Coord pos;
        // get the dirs to "to" by stacking the forward dirs
        LinkedList<Long> dirs = new LinkedList<Long>();
        for (pos = to; pos.dist() != 0;)
        {
            long dir = navigation.get(pos);
            dirs.addFirst(dir);
            pos = before(pos);
        }
        return dirs;
    }

    HashMap<Coord, Long> navigation = new HashMap<Coord, Long>();

    public class ClosestUnexplored extends LinkedList<Coord>
    {
        private static final long serialVersionUID = 1L;

        public void addSorted(Coord pos)
        {

            boolean added = false;
            for (ListIterator<Coord> i = listIterator(); i.hasNext();)
            {
                if (pathlen(pos) < pathlen(i.next()))
                {
                    i.add(pos);
                    added = true;
                }
            }
            if (!added) add(pos);
            // Log.out(" added " + pos + " - list now " + toString());
        }
    }

    private long[] loadProgram() throws IOException, FileNotFoundException
    {
        String filename = "C:\\Users\\jasper\\Google Drive\\Fun\\2016AdventOfCode\\EclipseWorkspace\\2019-data\\"
                + "15.txt";

        long[] program = null;

        try (BufferedReader br = new BufferedReader(new FileReader(filename)))
        {
            String line;
            while ((line = br.readLine()) != null)
            {
                program = Arrays.stream(line.split(","))
                        .mapToLong(Long::parseLong).toArray();
            }
        }

        return program;
    }
    
    
    

    public int pathlen(Coord c)
    {
        return getDirsToCoord(c).size();
    }

}