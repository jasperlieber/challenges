package com.goatwalker.aoc19;

import java.io.BufferedReader;
import java.io.FileReader;

//import com.goatwalker.aoc16.day08.InstructionDay08;
//import com.goatwalker.aoc16.day08.InstructionDay08.InstructionEnum;

public class Day08Part2
{
    public static void main(String[] args) throws Exception
    {
        Day08Part2 game = new Day08Part2();
        game.doit();
    }

    final int width = 25;
    final int height = 6;
    final int layerSize = width*height;

    char[][] image = new char[width][height];
    
    private void doit() throws Exception
    {
        String filename = "C:\\Users\\jasper\\Google Drive\\Fun\\2016AdventOfCode\\EclipseWorkspace\\2019-data\\"
                 + "8p1.txt";
//                + "8p1-test.txt";
        

        for (int x=0; x < width; x++)
        {
            for ( int y=0; y < height; y++)
            {
                image[x][y] = '2';
            }
        }

        try (BufferedReader br = new BufferedReader(new FileReader(filename)))
        {
            char[] cbuf = new char[layerSize];
            int rc;
            while ((rc = br.read(cbuf, 0, layerSize)) == layerSize)
            {
//                System.out.println("read count: " + rc);
//                System.out.println(cbuf);
                
                for (int x=0; x < width; x++)
                {
                    for ( int y=0; y < height; y++)
                    {
                        int off =y*width + x ;
                        if (image[x][y] == '2') image[x][y] = cbuf[off];
//                        System.out.println("x=" + x + " y=" + y + " off=" + off + " p=" + cbuf[off]);
//                        printImage();
                    }
                }
            }
        }

System.out.println("final:");
        printImage();

    }

    private void printImage()
    {
        for ( int y=0; y < height; y++)
        {
            for (int x=0; x < width; x++)
            {
                System.out.print(image[x][y] == '0' ? 'X' : ' ');
            }
            System.out.println();
        }
    }
}