package com.goatwalker.aoc19;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Arrays;

import com.goatwalker.utils.Logger.Log;

public class Day05Part1
{
    public static void main(String[] args) throws Exception
    {
        Day05Part1 game = new Day05Part1();
        game.doit();
    }

    private void doit() throws Exception
    {
        String filename = "C:\\Users\\jasper\\Google Drive\\Fun\\2016AdventOfCode\\EclipseWorkspace\\2019-data\\"
                + "5p1.txt";

        try (BufferedReader br = new BufferedReader(new FileReader(filename)))
        {
            String line;
            while ((line = br.readLine()) != null)
            {
                int[] codes = Arrays.stream(line.split(","))
                        .mapToInt(Integer::parseInt).toArray();
                // System.out.printf("found %d codes\n\n", codes.length);

                int input = 1;

                intcode(codes, input);

            }
        }
    }

    private void intcode(int[] origCodes, int input) throws Exception
    {
        int[] codes = Arrays.copyOf(origCodes, origCodes.length);
//        codes = new int[] { 3, 0, 104, 0, 99 };
//        codes = new int[] { 1002, 4, 3, 4, 33 };

        int pos = 0;
        while (true)//        codes = new int[] { 1002, 4, 3, 4, 33 };

        {
            int op = codes[pos] % 100;
            int paramModes = codes[pos] / 100;
            int[] params = new int[3];

            Log.out("pos " + pos + " - op " + op + " - paramModes "
                    + paramModes + " - next 4 codes " + Arrays.toString(Arrays.copyOfRange(codes, pos, pos+4)));
            

            if (op == 99) break;

            if (op == 1 || op == 2)
            {
                assert ((paramModes / 100) % 10 == 0);
                for (int pp = 0; pp < 2; pp++)
                {
//                    System.out.println(paramModes + " codes[" + (pos + 1 + pp)
//                            + "]=" + codes[pos + 1 + pp]);

                    assert (paramModes % 10 == 0 || paramModes % 10 == 1);
                    
                    params[pp] = (paramModes % 10 == 0)
                            ? codes[codes[pos + 1 + pp]]  // position mode
                            : codes[pos + 1 + pp]; // immediate mode
                    paramModes /= 10;
                }
                
                params[2] = codes[pos + 3];

                Log.out("  params "
                        + String.join(",", Arrays.toString(params)));

                if (op == 1) // ADD
                {
                    codes[params[2]] = params[0] + params[1];
                }
                else if (op == 2) // MULT
                {
                    codes[params[2]] = params[0] * params[1];
                }
                else
                    throw new Exception("for pos " + pos
                            + " -- unknown opcode value " + codes[pos]
                            + "\ncodes: " + Arrays.toString(codes));

                pos += 4;
            }
            else if (op == 3 || op == 4)
            {
//                assert (paramModes % 10 == 0);
                
                if (op == 3)
                {
                    codes[codes[pos + 1]] = input;
                }
                else
                    System.out.printf("Output[%d]\n", codes[codes[pos + 1]]);

                pos += 2;
            }
        }
    }
}