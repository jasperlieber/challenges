package com.goatwalker.aoc19;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.goatwalker.utils.Logger.Log;
import com.goatwalker.utils.Permutation;

public class Day07Part2
{
    public static void main(String[] args) throws Exception
    {
        Day07Part2 game = new Day07Part2();
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
                .permute(new Integer[] { 9,8,7,6,5 });
        
//        program = new int[] { 3,52,1001,52,-5,52,3,53,1,52,56,54,1007,54,5,55,1005,55,26,1001,54,
//                -5,54,1105,1,12,1,53,54,53,1008,54,0,55,1001,55,1,55,2,53,55,53,4,
//                53,1001,56,-1,56,1005,56,6,99,0,0,0,0,10 };
//
//        perms = new ArrayList<Integer[]>();
//        perms.add(new Integer[] {9,7,8,5,6});
//        
//        program = new int[] {3,26,1001,26,-4,26,3,27,1002,27,2,27,1,27,26,
//                27,4,27,1001,28,-1,28,1005,28,6,99,0,0,5 };
//
//        perms = new ArrayList<Integer[]>();
//        perms.add(new Integer[] {9,8,7,6,5});

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

        Integer maxOutput = null;

        for (Integer[] perm : perms)
        {
            Integer output = 0;
            Amp[] amps = new Amp[5];
            int count = 0;
            for (int phase : perm)
            {
                System.out.print(phase);
                amps[count++] = new Amp(program, phase);

            }
            System.out.println("");

            Integer lastOutput = null;
            while (amps[0].stillRunning())
            {
                count = 0;
                for (Amp amp : amps)
                {
                    System.out.println("Running amp #" + count++ + " with input " + output);
                    output = amp.resume(output);
                    System.out.println(" gives output " + output);
                }
                if (output != null) lastOutput = output;
            }
            System.out.println("lastoutput = " + lastOutput);
            if (maxOutput == null || lastOutput != null && lastOutput > maxOutput) maxOutput = lastOutput;
        }

        System.out.println("max output: " + maxOutput);

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
                    : codes[pos + i]; // immediate mode
            return param;
        }

    }

    private class Amp
    {
        private int[] mem = null;
        private boolean running = true;
        private ParamObj po = null;
        private List<Integer> inputs = null;
        private int inputIndex;
        private int ip;

        public Amp(int[] program, int phase)
        {
            mem = Arrays.copyOf(program, program.length);
            po = new ParamObj(mem);
            inputs = new ArrayList<Integer>();
            inputs.add(phase);
            ip = 0;
            inputIndex = 0;
        }

        public boolean stillRunning()
        {
            return running;
        }

        public Integer resume(Integer newInput) throws Exception
        {
            // TODO Auto-generated method stub
            inputs.add(newInput);
            Integer output = null;
            while (running && output == null)
            {
                int op = mem[ip];

                Log.out("pos " + ip + " - op " + op + " - next 4 codes "
                        + Arrays.toString(Arrays.copyOfRange(mem, ip, ip + 4)));

                po.setPos(ip);

                op %= 100;
                
                int p1, p2;

                switch (op)
                {
                case 1:
                    p1 = po.get(1);
                    p2 = po.get(2);
                    mem[mem[ip + 3]] = p1 + p2;
                    Log.out("Add: " + p1 + " + " + p2 + " = "
                            + mem[mem[ip + 3]] + " into pos " + mem[ip + 3]);

                    ip += 4;
                    break;
                case 2:
                    p1 = po.get(1);
                    p2 = po.get(2);
                    mem[mem[ip + 3]] = p1 * p2;
                    Log.out("Mult: " + p1 + " * " + p2 + " = "
                            + mem[mem[ip + 3]] + " into pos " + mem[ip + 3]);

                    ip += 4;
                    break;
                case 3:
                    int input = inputs.get(inputIndex);
                    mem[mem[ip + 1]] = input;
                    Log.out("Input #" + inputIndex + ": " + inputs + " = "
                            + mem[mem[ip + 1]] + " into pos " + mem[ip + 1]);
                    ip += 2;
                    inputIndex++;
                    break;
                case 4:
                    output = po.get(1);

                    Log.out(String.format("Output(pos %d) = %d \n", mem[ip + 1],
                            output));
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
                        ip = po.get(2);
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
                    mem[mem[ip + 3]] = yesno;
                    Log.out((op == 5 ? "Less-than" : "Equal-to") + " p1="
                            + p1 + " p2=" + p2 + " result="
                            + mem[mem[ip + 3]] + " in pos " + mem[ip + 3]);

                    ip += 4;
                    break;
                case 99:
                    running = false;
                    break;
                default:
                    throw new Exception("for pos " + ip
                            + " -- unknown opcode value " + mem[ip]
                            + " - next 4 codes " + Arrays.toString(
                                    Arrays.copyOfRange(mem, ip, ip + 4)));
                }

            }
            return output;
        }

    }
}