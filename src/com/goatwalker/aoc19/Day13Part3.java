package com.goatwalker.aoc19;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Set;

import com.goatwalker.utils.Coord;
import com.goatwalker.utils.Tiles;

public class Day13Part3
{
    public class OrderedPlayList extends LinkedList<G13PaddleMoves>
    {
        private static final long serialVersionUID = 1L;

        public boolean orderedAdd(G13PaddleMoves element)
        {
            ListIterator<G13PaddleMoves> itr = listIterator();
            while (true)
            {
                if (itr.hasNext() == false)
                {
                    itr.add(element);
                    return (true);
                }

                G13PaddleMoves elementInList = itr.next();
                if (elementInList.finalNumBlocks > element.finalNumBlocks)
                {
                    itr.previous();
                    itr.add(element);
                    return (true);
                }
            }
        }
    }

    private static final boolean showPaddleMoves = false;
    private static final boolean showBallMoves = false;
    private static final boolean showBallPaddleHits = false;

    public static void main(String[] args) throws Exception
    {
        Day13Part3 game = new Day13Part3();
        game.doit();
    }

    private int numMoves = 0;
    private int lastBall16X;

    private long[] program;

    private void doit() throws Exception
    {
        program = loadProgram();

        OrderedPlayList playList = new OrderedPlayList();
        // LinkedList<G13PaddleMoves> playList = new
        // LinkedList<G13PaddleMoves>();

        // first game, paddle not moved
        G13PaddleMoves firstPlay = new G13PaddleMoves();
        G13PaddleMove move = new G13PaddleMove();
        firstPlay.add(move);
        playList.add(firstPlay);

        // second game, paddle moved one right
        G13PaddleMoves secondPlay = new G13PaddleMoves();
        move = new G13PaddleMove();
        move.numMoves = 1;
        move.paddleDir = 1;
        move.startTime = 0;
        secondPlay.add(move);
        playList.add(secondPlay);

        int numPlays = 0;

        while (true)
        {
            G13PaddleMoves curPlay = playList.poll();

            if (curPlay == null)
            {
                System.out.println("no more plays");
                return;
            }
            System.out.println("Play #" + numPlays + ", curPlay = " + curPlay);

            G13PaddleMoves curPlayCopy1 = (G13PaddleMoves) curPlay.clone();
            G13PaddleMoves curPlayCopy2 = (G13PaddleMoves) curPlay.clone();

            G13Result result = runGame(curPlay);

            System.out.println(" result = " + result);

            if (result == null) continue;

            if (result != null)
            {
                curPlayCopy1.finalNumBlocks = result.numBlocks;
                curPlayCopy2.finalNumBlocks = result.numBlocks;

                if (result.win()) return;

                int num1s = 0;
                int num2s = 0;
                for (G13PaddleMove pmove : curPlayCopy1)
                {
                    if (pmove.numBlocksRemaining == 1) num1s++;
                    else if (pmove.numBlocksRemaining == 2) num2s++;
                }

                if (num2s > 3 || num1s > 2)
                {
                    System.out.println("** too many 1s");
                    continue;
                }

                G13PaddleMoves paddleMoveCase1 = getNextPaddleMove(curPlayCopy1,
                        result, "left");
                G13PaddleMoves paddleMoveCase2 = getNextPaddleMove(curPlayCopy2,
                        result, "right");
                if (paddleMoveCase1 != null)
                    playList.orderedAdd(paddleMoveCase1);
                if (paddleMoveCase2 != null)
                    playList.orderedAdd(paddleMoveCase2);

                numPlays++;
            }
        }
    }

    private G13PaddleMoves getNextPaddleMove(G13PaddleMoves curPlay,
            G13Result result, String side)
    {
        // System.out.printf("getNextPaddleMove(), side=%5s \n", side);

        int lastStartTime = curPlay.getLast().startTime;

        int movePaddle = result.lastBall17X - result.lastPaddleX;

        if (result.lastBallDir == -1) movePaddle--;
        if (side.compareTo("right") == 0) movePaddle++;

        int newPaddleX = result.lastPaddleX + movePaddle;
        if (newPaddleX < 1 || newPaddleX > 38)
        {
            // System.out.println("** newPaddleX=" + newPaddleX
            // + " in wall, discarding this game");
            return null;
        }

        G13PaddleMove move = new G13PaddleMove();
        move.numMoves = Math.abs(movePaddle);
        move.paddleDir = Integer.signum(movePaddle);
        move.startTime = result.prevNumMovesAt17 + 2;
        move.numBlocksRemaining = result.numBlocks;

        curPlay.add(move);

        // System.out.println(" curPlay now = " + curPlay);

        if (move.startTime == lastStartTime)
        {
            System.out.println("** same start time");
            return null;
        }

        return curPlay;
    }

    private G13Result runGame(G13PaddleMoves pmoves) throws Exception
    {
        G13Result gres = new G13Result();

        IntComp13 comp = new IntComp13(program);

        Tiles tiles = new Tiles();

        numMoves = 0;
        int lastNumMovesAt17 = 0;
        // int prevBallDir = 0;
        // int prevBall17X = 0;
        int prevNumBlocks = 0;
        int identicalBlockCount = 0;

        G13PaddleMove curPaddleMove = pmoves.poll();
        Set<BounceRec> bounceRecs = new HashSet<BounceRec>();

        while (comp.stillRunning())// & numMoves < 5000)
        {

            if (curPaddleMove != null && curPaddleMove.startTime <= numMoves)
            {
                // System.out
                // .println(numMoves + ": queing moves " + curPaddleMove);
                for (int jj = 0; jj < curPaddleMove.numMoves; jj++)
                {
                    // System.out.println(numMoves + ": queing dir "
                    // + curPaddleMove.paddleDir);
                    comp.addInput(curPaddleMove.paddleDir);
                }

                curPaddleMove = pmoves.poll();
            }
            Long xx = comp.resume();
            if (xx == null) break;

            Long yy = comp.resume();
            Long idl = comp.resume();

            int x = Math.toIntExact(xx);
            int y = Math.toIntExact(yy);
            int id = Math.toIntExact(idl);

            if (x == -1 && yy == 0)
            {
                // System.out.println(numMoves + "," + tiles.getCount(2)
                // + ": new score=" + id);
                gres.lastScore = id;
            }
            else
            {
                tiles.setTile(new Coord(x, y), id);

                // if (id == 0) System.out.println(" blank to " + x + " " + y);

                if (id == 3)
                {
                    gres.lastPaddleX = x;

                    if (showPaddleMoves) System.out.println(numMoves + ","
                            + tiles.getCount(2) + ": paddle to " + x + " " + y);
                }
                if (showBallMoves) if (id == 4) System.out.println(numMoves
                        + "," + tiles.getCount(2) + ": ball to " + x + " " + y);

                if (id == 4)
                {

                    if (y == 16) lastBall16X = x;
                    else if (y == 17)
                    {
                        // prevBall17X = gres.lastBall17X;
                        gres.lastBall17X = x;

                        // prevBallDir = gres.lastBallDir;
                        gres.lastBallDir = Integer.signum(x - lastBall16X);

                        gres.prevNumMovesAt17 = lastNumMovesAt17;
                        lastNumMovesAt17 = numMoves;

                        prevNumBlocks = gres.numBlocks;
                        gres.numBlocks = tiles.getCount(2);
                        if (prevNumBlocks == gres.numBlocks)
                            identicalBlockCount++;

                        // if last NN blockcounts are same, probably not working
                        if (identicalBlockCount > 100)
                        {
                            System.out.println("** identical block count ");
                            return null;
                        }

                        if (showBallPaddleHits)
                            System.out.println(numMoves + ","
                                    + tiles.getCount(2) + ": at 17, paddle at "
                                    + gres.lastPaddleX + " ball at " + x
                                    + " moving " + gres.lastBallDir);

                        // check for loop
                        BounceRec b = new BounceRec(gres);
                        boolean looping = bounceRecs.contains(b);

                        // boolean sameDir = gres.lastBallDir == prevBallDir;
                        // boolean samePos = gres.lastBall17X == prevBall17X;
                        // boolean sameBlockCount = gres.numBlocks ==
                        // prevNumBlocks;
                        if (looping)
                        {
                            System.out.println("** loop ");
                            return null;
                        }
                        bounceRecs.add(b);
                    }
                }
            }
            numMoves++;
        }
        // System.out.println(
        // "(post run memsize = " + comp.memsize() + ")\n\nTiles:\n");

        tiles.display();

        gres.numMoves = numMoves;
        gres.numBlocks = tiles.getCount(2);

//        System.out.println("Num blocks = " + tiles.getCount(2));

        return gres;
    }

    private long[] loadProgram() throws IOException, FileNotFoundException
    {
        String filename = "C:\\Users\\jasper\\Google Drive\\Fun\\2016AdventOfCode\\EclipseWorkspace\\2019-data\\"
                + "13.txt";

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

        program[0] = 2;
        return program;
    }

    public class G13Result
    {
        public int lastScore;
        public int lastBallDir;
        public int lastBall17X;
        public int lastPaddleX;
        public int numMoves;
        public int numBlocks;
        public int prevNumMovesAt17;

        public boolean win()
        {
            return numBlocks == 0;
        }

        @Override
        public String toString()
        {
            return "G13Result [lastScore=" + lastScore + ", lastBallDir="
                    + lastBallDir + ", lastBall17X=" + lastBall17X
                    + ", lastPaddleX=" + lastPaddleX + ", numMoves=" + numMoves
                    + ", numBlocks=" + numBlocks + ", prevNumMovesAt17="
                    + prevNumMovesAt17 + "]";
        }

    }

    public class G13PaddleMove
    {

        @Override
        public String toString()
        {
            return "G13PaddleMove [startTime=" + startTime + ", paddleDir="
                    + paddleDir + ", numMoves=" + numMoves
                    + ", numBlocksRemaining=" + numBlocksRemaining + "]";
        }
        int startTime; // when these moves should be added to queue
        long paddleDir; // paddle movement, -1 for left, +1 for right
        int numMoves; // how many moves to make
        int numBlocksRemaining;
    }

    public class G13PaddleMoves extends LinkedList<G13PaddleMove>
    {
        @Override
        public String toString()
        {
            String res = "G13PaddleMoves (" + this.size() + "): ";
            for (G13PaddleMove m : this)
            {
                res += m;
            }
            return res;
        }

        private static final long serialVersionUID = 1L;

        public int finalNumBlocks;
    }

    public class BounceRec
    {
        @Override
        public int hashCode()
        {
            final int prime = 31;
            int result = 1;
            // result = prime * result + getEnclosingInstance().hashCode();
            result = prime * result + lastBall17X;
            result = prime * result + lastBallDir;
            result = prime * result + numBlocks;
            return result;
        }

        // @Override
        public boolean equals(BounceRec obj)
        {
            // if (this == obj) return true;
            // if (obj == null) return false;
            // if (getClass() != obj.getClass()) return false;
            // BounceRec other = (BounceRec) obj;
            // if (!getEnclosingInstance().equals(other.getEnclosingInstance()))
            // return false;
            if (lastBall17X != obj.lastBall17X) return false;
            if (lastBallDir != obj.lastBallDir) return false;
            if (numBlocks != obj.numBlocks) return false;
            return true;
        }

        int lastBallDir, lastBall17X, numBlocks;

        public BounceRec(G13Result gres)
        {

            lastBallDir = gres.lastBallDir;
            lastBall17X = gres.lastBall17X;
            numBlocks = gres.numBlocks;
        }

        // private Day13Part3 getEnclosingInstance()
        // {
        // return Day13Part3.this;
        // }

    }

}