package com.goatwalker.aoc19;

import java.io.BufferedReader;
import java.io.FileReader;

//import com.goatwalker.aoc16.day08.InstructionDay08;
//import com.goatwalker.aoc16.day08.InstructionDay08.InstructionEnum;

public class Day01Part1
{
    public static void main(String[] args) throws Exception
    {
        Day01Part1 game = new Day01Part1();
        game.doit();
    }

    private void doit() throws Exception
    {
        init();
        
        
        System.out.println(Test.class.getResource("Test.class"));

        String dataDirPrefix = System.getProperty("user.dir")
                + java.io.File.separator + "2019_data" + java.io.File.separator;
        
        String filename = "1p1.txt";

        long fuelTotal = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(dataDirPrefix + filename)))
        {
            String line;
            while ((line = br.readLine()) != null)
            {
                long mass = Long.parseLong(line);

                boolean done = false;
                while (!done)
                {
                    long fuel = Math.floorDiv(mass, 3) - 2;
                    done = fuel <= 0;
                    if (!done)
                    {
                        fuelTotal += fuel;
                        mass = fuel;
                    }
                }
                // processLine(line);
                // System.out.println("read " + line);
            }
        }

        System.out.println("Final fuel = " + fuelTotal);

    }

    private void init()
    {
    }

    private void processLine(String line) throws Exception
    {
    }

}