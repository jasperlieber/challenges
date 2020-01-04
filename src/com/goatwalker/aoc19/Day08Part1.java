package com.goatwalker.aoc19;

import java.io.BufferedReader;
import java.io.FileReader;

// import com.goatwalker.aoc16.day08.InstructionDay08;
// import com.goatwalker.aoc16.day08.InstructionDay08.InstructionEnum;

public class Day08Part1 {
  public static void main(String[] args) throws Exception {
    Day08Part1 game = new Day08Part1();
    game.doit();
  }

  final int layerSize = 25 * 6;

  private void doit() throws Exception {
    String filename =
        "C:\\Users\\jasper\\Google Drive\\Fun\\2016AdventOfCode\\EclipseWorkspace\\2019-data\\"
            + "8p1.txt";

    try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
      char[] cbuf = new char[layerSize];
      int rc;
      int leastZeroCount = 25 * 6;
      while ((rc = br.read(cbuf, 0, layerSize)) == layerSize) {
        // System.out.println("read count: " + rc);
        // System.out.println(cbuf);
        int zeroCount = 0;
        for (char c : cbuf) {
          if (c == '0')
            zeroCount++;
        }
        if (zeroCount < leastZeroCount) {
          leastZeroCount = zeroCount;

          int onesCount = 0;
          int twosCount = 0;

          for (char c : cbuf) {
            if (c == '1')
              onesCount++;
            else if (c == '2')
              twosCount++;
          }

          System.out.println("New least zeros output = " + onesCount * twosCount);
          System.out.println(cbuf);
          System.out.println(zeroCount);
          System.out.println(onesCount);
          System.out.println(twosCount);

        }
      }
      // System.out.println("read count: " + rc);
      // System.out.println((int)cbuf[0]);
    }
  }

  // private void processLine(String line) throws Exception
  // {
  // }

}
