package com.goatwalker.aoc19;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.goatwalker.utils.Coord;
import com.goatwalker.utils.GrowableLongArray;
import com.goatwalker.utils.Logger.Log;

//           1125899906842624
// max long: 9223372036854775807
// max int:  2147483647

public class Day11Part2
{
    public class Panels extends HashMap<Coord, Boolean>
    {
        private static final long serialVersionUID = 1L;

        public void addPanel(Coord curPos, boolean color)
        {
            assert (!containsKey(curPos));
            this.put(curPos, color);
        }

        public Boolean getColor(Coord curPos)
        {
            Boolean color = this.get(curPos);
            if (color == null)
            {
                this.put(curPos, false);
                color = false;
            }
            return color;
        }

        public void setColor(Coord curPos, boolean b)
        {
            this.put(curPos, b);
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

            boolean[][] display = new boolean[maxx - minx + 1][maxy - miny + 1];

            for (Entry<Coord, Boolean> e : this.entrySet())
            {
                int x = e.getKey().getFirst();
                int y = e.getKey().getSecond();

                display[x - minx][y - miny] = e.getValue();
            }

            for (int y = maxy - miny; y >= 0; y--)
            {
                for (int x = 0; x < maxx-minx; x++)
                {
                    System.out.print(display[x][y] ? "#" : ".");
                }
                System.out.println();
            }

        }
    }

    public static void main(String[] args) throws Exception
    {
        Day11Part2 game = new Day11Part2();
        game.doit();
    }

    private void doit() throws Exception
    {
        String filename = "C:\\Users\\jasper\\Google Drive\\Fun\\2016AdventOfCode\\EclipseWorkspace\\2019-data\\"
                + "11p1.txt";

        long[] program = null;
        Intcode comp;

        // program = new long[] { 109, 1, 204, -1, 1001, 100, 1, 100, 1008, 100,
        // 16, 101, 1006, 101, 0, 99 };
        // comp = new Intcode(program);
        // while (comp.stillRunning())
        // {
        // Long out = comp.resume();
        // if (out != null) System.out.print(out + " ");
        // }
        // System.out.println("\nmemsize = " + comp.memsize());
        //
        // program = new long[] { 1102, 34915192, 34915192, 7, 4, 7, 99, 0 };
        // comp = new Intcode(program);
        // while (comp.stillRunning())
        // {
        // Long out = comp.resume();
        // if (out != null) System.out.print(out + " ");
        // }
        // System.out.println("\nmemsize = " + comp.memsize());
        //
        // program = new long[] { 104, 1125899906842624L, 99 };
        // comp = new Intcode(program);
        // while (comp.stillRunning())
        // {
        // Long out = comp.resume();
        // if (out != null) System.out.print(out + " ");
        // }
        // System.out.println("\nmemsize = " + comp.memsize());

        try (BufferedReader br = new BufferedReader(new FileReader(filename)))
        {
            String line;
            while ((line = br.readLine()) != null)
            {
                program = Arrays.stream(line.split(","))
                        .mapToLong(Long::parseLong).toArray();
            }
        }
        comp = new Intcode(program);
        Coord curPos = new Coord(0, 0);
        Panels panels = new Panels();
        panels.addPanel(curPos, true);
        int curDir = 0; // 0123 == NESW

        int numMoves = 0;
        while (comp.stillRunning())
        {
            comp.addInput(panels.getColor(curPos) ? 1L : 0L);
            Long newColor = comp.resume();
            if (newColor == null) break;
            Long newTurn = comp.resume();
            if (newTurn == null) break;

            panels.setColor(curPos, newColor == 1L);

            // turn: 0 means left, 1 means right
            int newDir = (curDir + (newTurn == 0 ? 3 : 1)) % 4;
            System.out.println("Setting " + curPos + " to color " + newColor
                    + " and turning " + (newTurn == 0 ? " left" : "right")
                    + ", curDir=" + curDir + ", newDir=" + newDir
                    + ", numMoves=" + numMoves + ", num panels="
                    + panels.size());
            curDir = newDir;
            curPos = curPos.getNeighbor(curDir);

            numMoves++;
            // if (numMoves > 3) return;
        }
        System.out.println("\nmemsize = " + comp.memsize());

        panels.display();

    }

    private class ParamObj
    {
        int pos;
        int relBase = 0;
        GrowableLongArray mem;

        public ParamObj(GrowableLongArray coremem)
        {
            mem = coremem;
            // System.out.println("po codes = " + Arrays.toString(codes));
        }

        public void setPos(int p)
        {
            pos = p;
        }

        private int getParamMode(int i) throws Exception
        {
            assert (i < 6);
            final int[] denom = new int[] { 0, 100, 1000, 10000, 100000,
                    1000000 };
            int paramMode = (int) ((mem.get(pos) / denom[i]) % 10);
            return paramMode;
        }

        public int set(int i, long val) throws Exception
        {
            int paramMode = getParamMode(i);
            long index;
            switch (paramMode)
            {
            case 0:
                // position mode
                index = mem.get(pos + i);
                break;
            case 1:
                // immediate mode
                throw new Exception("no immediate mode for set()");
                // break;
            case 2:
                // relative mode
                index = mem.get(pos + i) + relBase;
                break;
            default:
                throw new Exception("Unknown param mode = " + paramMode);

            }
            mem.set(index, val);
            return (int) index;
        }

        public long get(int i) throws Exception
        {
            int paramMode = getParamMode(i);
            long param;
            switch (paramMode)
            {
            case 0:
                // position mode
                param = mem.get((int) mem.get(pos + i));
                break;
            case 1:
                // immediate mode
                param = mem.get(pos + i);
                break;
            case 2:
                // relative mode
                param = mem.get((int) mem.get(pos + i) + relBase);
                break;
            default:
                throw new Exception("Unknown param mode = " + paramMode);

            }
            return param;
        }

        public void adjustBase(long p1)
        {
            relBase += p1;
        }
    }

    private class Intcode
    {
        private GrowableLongArray mem = new GrowableLongArray();
        private boolean running = true;
        private ParamObj po = null;
        private List<Long> inputs = new ArrayList<Long>();
        private int inputIndex;
        private int ip;

        public Intcode(long[] program)
        {
            mem.initialize(program);
            po = new ParamObj(mem);
            ip = 0;
            inputIndex = 0;
        }

        public int memsize()
        {
            return mem.memsize();
        }

        public boolean stillRunning()
        {
            return running;
        }

        public Long resume() throws Exception
        {
            Long output = null;
            while (running && output == null)
            {
                long op = mem.get(ip);

                Log.out("pos " + ip + " - op " + op + " - next 4 codes "
                        + Arrays.toString(mem.get(ip, 4)));

                po.setPos(ip);

                op %= 100;

                long p1, p2;
                int addr = 0;

                switch ((int) op)
                {
                case 1:
                    p1 = po.get(1);
                    p2 = po.get(2);
                    addr = po.set(3, p1 + p2);
                    Log.out("Add: " + p1 + " + " + p2 + " = " + mem.get(addr)
                            + " into pos " + addr);
                    ip += 4;
                    break;
                case 2:
                    p1 = po.get(1);
                    p2 = po.get(2);
                    addr = po.set(3, p1 * p2);
                    Log.out("Mult: " + p1 + " * " + p2 + " = " + mem.get(addr)
                            + " into pos " + addr);
                    ip += 4;
                    break;
                case 3:
                    long input = inputs.get(inputIndex);
                    addr = po.set(1, input);
                    Log.out("Input #" + inputIndex + ": " + inputs + " = "
                            + mem.get(addr) + " into pos " + addr);
                    ip += 2;
                    inputIndex++;
                    break;
                case 4:
                    output = po.get(1);

                    Log.out(String.format("Output(pos %d) = %d \n",
                            mem.get(ip + 1), output));
                    ip += 2;
                    break;
                case 5:
                case 6:
                    boolean zeroCheck = po.get(1) != 0;
                    if (op == 6) zeroCheck = !zeroCheck;
                    Log.out("Jump-if-" + (op == 5 ? "nonzero" : "zero")
                            + " test=" + po.get(1) + " to=" + po.get(2));
                    if (zeroCheck)
                    {
                        ip = (int) po.get(2);
                    }
                    else
                    {
                        ip += 3;
                    }
                    break;
                case 7:
                case 8:
                    p1 = po.get(1);
                    p2 = po.get(2);
                    boolean lt = p1 < p2;
                    boolean eq = p1 == p2;
                    int yesno = (op == 7 && lt || op == 8 && eq) ? 1 : 0;
                    addr = po.set(3, yesno);
                    Log.out((op == 5 ? "Less-than" : "Equal-to") + " p1=" + p1
                            + " p2=" + p2 + " result=" + mem.get(addr)
                            + " into pos " + addr);
                    ip += 4;
                    break;
                case 9:
                    p1 = po.get(1);
                    po.adjustBase(p1);
                    Log.out("Adjusting relBase by " + p1);
                    ip += 2;
                    break;
                case 99:
                    running = false;
                    break;
                default:
                    throw new Exception(
                            "for pos " + ip + " -- unknown opcode value "
                                    + mem.get(ip) + " - next 4 codes "
                                    + Arrays.toString(mem.get(ip, 4)));
                }

            }
            return output;
        }

        private void addInput(Long newInput)
        {
            inputs.add(newInput);
        }

    }
}