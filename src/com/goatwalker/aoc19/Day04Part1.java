package com.goatwalker.aoc19;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Arrays;

public class Day04Part1 {
  public static void main(String[] args) throws Exception {
    Day04Part1 game = new Day04Part1();
    game.doit();
  }

  private void doit() throws Exception {
    init();

    String filename =
        "C:\\Users\\jasper\\Google Drive\\Fun\\2016AdventOfCode\\EclipseWorkspace\\2019-data\\"
            + "2p1.txt";

    try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
      String line;
      while ((line = br.readLine()) != null) {
        int[] codes = Arrays.stream(line.split(",")).mapToInt(Integer::parseInt).toArray();
        // System.out.printf("found %d codes\n\n", codes.length);

        for (int noun = 0; noun < 100; noun++) {
          for (int verb = 0; verb < 100; verb++) {
            if (intcode(codes, noun, verb) == 19690720) {
              System.out.printf("soln: %d\n", noun * 100 + verb);
            }
          }
        }
      }
    }
  }

  private void init() {}

  private int intcode(int[] origCodes, int noun, int verb) throws Exception {

    int[] codes = Arrays.copyOf(origCodes, origCodes.length);

    codes[1] = noun;
    codes[2] = verb;

    int pos = 0;
    while (true) {
      int op = codes[pos];

      if (op == 99)
        break;

      int p1 = codes[pos + 1];
      int p2 = codes[pos + 2];
      int p3 = codes[pos + 3];

      if (op == 1) {
        codes[p3] = codes[p1] + codes[p2];
      } else if (op == 2) {
        codes[p3] = codes[p1] * codes[p2];
      } else
        throw new Exception("for pos " + pos + " -- unknown opcode value " + codes[pos]
            + "\ncodes: " + Arrays.toString(codes));

      pos += 4;
    }

    return codes[0];
  }
}
