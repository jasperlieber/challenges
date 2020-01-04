package com.goatwalker.aoc19;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Arrays;
import java.util.List;

import com.goatwalker.utils.Logger.Log;
import com.goatwalker.utils.Permutation;

public class Day07Part1
{
    public static void main(String[] args) throws Exception
    {
        Day07Part1 game = new Day07Part1();
        game.doit();
    }

    private void doit() throws Exception
    {
        String filename = "C:\\Users\\jasper\\Google Drive\\Fun\\2016AdventOfCode\\EclipseWorkspace\\2019-data\\"
                + "7p1.txt";

        int[] program = null;

        try (BufferedReader br = new BufferedReader(new FileReader(filename)))
        {
            String line;
            while ((line = br.readLine()) != null)
            {
                program = Arrays.stream(line.split(","))
                        .mapToInt(Integer::parseInt).toArray();
            }
        }

        Permutation<Integer> permGen = new Permutation<Integer>();

        List<Integer[]> perms = permGen
                .permute(new Integer[] { 5,6,7,8,9 });

        // tests
        // program = new int[] { 3,15,3,16,1002,16,10,16,1,16,15,15,4,15,99,0,0
        // };
        // phases = new int[] {4,3,2,1,0};
        //
        // program = new int[] { 3,23,3,24,1002,24,10,24,1002,23,-1,23,
        // 101,5,23,23,1,24,23,23,4,23,99,0,0 };
        // phases = new int[] {0,1,2,3,4};
        //
        // program = new int[] {
        // 3,31,3,32,1002,32,10,32,1001,31,-2,31,1007,31,0,33,
        // 1002,33,7,33,1,33,31,31,1,32,31,31,4,31,99,0,0,0 };
        // phases = new int[] {1,0,4,3,2};

        int maxOutput = -1;
        for (Integer[] perm : perms)
        {
            int output = 0;

            for (int i : perm)
                System.out.print(i);
            System.out.print(": ");

            for (int phase : perm)
            {
                int[] inputs = new int[] { phase, output };
                output = intcode(program, inputs);
            }
            System.out.println(output);
            if (output > maxOutput) maxOutput = output;
        }

        System.out.println("Final output: " + maxOutput);

    }

    private class ParamObj
    {
        int pos;
        int[] codes;

        public ParamObj(int[] c)
        {
            codes = c;
            // System.out.println("po codes = " + Arrays.toString(codes));
        }

        public void setPos(int p)
        {
            pos = p;
        }

        public int get(int i)
        {
            assert (i < 6);
            final int[] denom = new int[] { 0, 100, 1000, 10000, 100000,
                    1000000 };
            int paramMode = (codes[pos] / denom[i]) % 10;
            assert (paramMode == 0 || paramMode == 1);
            int param = paramMode % 10 == 0 ? codes[codes[pos + i]] // position
                                                                    // mode
                    : codes[pos + i]; // immediate mode
            return param;
        }

    }

    private int intcode(int[] origProgram, int[] inputs) throws Exception
    {
        int[] mem = Arrays.copyOf(origProgram, origProgram.length);
        // codes = new int[] { 3, 0, 104, 0, 99 };
        // codes = new int[] { 1002, 4, 3, 4, 33 };

        ParamObj po = new ParamObj(mem);
        int output = -1;
        int inputIndex = 0;
        int pos = 0;
        while (true)
        {
            int op = mem[pos];

            Log.out("pos " + pos + " - op " + op + " - next 4 codes "
                    + Arrays.toString(Arrays.copyOfRange(mem, pos, pos + 4)));

            po.setPos(pos);

            op %= 100;

            switch (op)
            {
            case 1:
                mem[mem[pos + 3]] = po.get(1) + po.get(2);
                Log.out("Add: " + po.get(1) + " + " + po.get(2) + " = "
                        + mem[mem[pos + 3]] + " into pos " + mem[pos + 3]);

                pos += 4;
                break;
            case 2:
                mem[mem[pos + 3]] = po.get(1) * po.get(2);
                Log.out("Mult: " + po.get(1) + " * " + po.get(2) + " = "
                        + mem[mem[pos + 3]] + " into pos " + mem[pos + 3]);

                pos += 4;
                break;
            case 3:
                int input = inputs[inputIndex];
                mem[mem[pos + 1]] = input;
                Log.out("Input #" + inputIndex + ": " + inputs + " = "
                        + mem[mem[pos + 1]] + " into pos " + mem[pos + 1]);
                pos += 2;
                inputIndex++;
                break;
            case 4:
                output = po.get(1);

                Log.out(String.format("Output(pos %d) = %d \n", mem[pos + 1],
                        output));
                pos += 2;
                break;
            case 5:
            case 6:
                boolean zeroCheck = po.get(1) != 0;
                if (op == 6) zeroCheck = !zeroCheck;
                Log.out("Jump-if-" + (op == 5 ? "nonzero" : "zero") + " test="
                        + po.get(1) + " to=" + po.get(2));
                if (zeroCheck)
                {
                    pos = po.get(2);
                }
                else
                {
                    pos += 3;
                }
                break;
            case 7:
            case 8:
                int p1 = po.get(1);
                int p2 = po.get(2);
                boolean lt = p1 < p2;
                boolean eq = p1 == p2;
                int yesno = (op == 7 && lt || op == 8 && eq) ? 1 : 0;
                mem[mem[pos + 3]] = yesno;
                Log.out((op == 5 ? "Less-than" : "Equal-to") + " p1="
                        + po.get(1) + " p2=" + po.get(2) + " result="
                        + mem[mem[pos + 3]] + " in pos " + mem[pos + 3]);

                pos += 4;
                break;
            case 99:
                return output;
            default:
                throw new Exception("for pos " + pos
                        + " -- unknown opcode value " + mem[pos]
                        + " - next 4 codes " + Arrays.toString(
                                Arrays.copyOfRange(mem, pos, pos + 4)));
            }
        }
    }
}