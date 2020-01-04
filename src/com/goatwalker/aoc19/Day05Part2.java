package com.goatwalker.aoc19;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Arrays;
import com.goatwalker.aoc19.Day05Part2.ParamObj;
import com.goatwalker.utils.Logger.Log;

public class Day05Part2 {
  public static void main(String[] args) throws Exception {
    Day05Part2 game = new Day05Part2();
    game.doit();
  }

  private void doit() throws Exception {
    String filename =
        "C:\\Users\\jasper\\Google Drive\\Fun\\2016AdventOfCode\\EclipseWorkspace\\2019-data\\"
            + "5p1.txt";

    try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
      String line;
      while ((line = br.readLine()) != null) {
        int[] codes;

        codes = Arrays.stream(line.split(",")).mapToInt(Integer::parseInt).toArray();

        System.out.println(intcode(codes, 5));


        // System.out.printf("found %d codes\n\n", codes.length);

        // codes = new int[] { 3, 9, 8, 9, 10, 9, 4, 9, 99, -1, 8 };
        // assert(intcode(codes, 1) == 0);
        // assert(intcode(codes, 8) == 1);
        //
        // codes = new int[] { 3,9,7,9,10,9,4,9,99,-1,8};
        // assert(intcode(codes, 1) == 1);
        // assert(intcode(codes, 8) == 0);
        //
        // codes = new int[] { 3,3,1108,-1,8,3,4,3,99};
        // assert(intcode(codes, 1) == 0);
        // assert(intcode(codes, 8) == 1);
        //
        // codes = new int[] {3,3,1107,-1,8,3,4,3,99};
        // assert(intcode(codes, 1) == 1);
        // assert(intcode(codes, 8) == 0);
        //
        // codes = new int[] {3,12,6,12,15,1,13,14,13,4,13,99,-1,0,1,9};
        // assert(intcode(codes, 0) == 0);
        // assert(intcode(codes, 1) == 1);

        // codes = new int[] {3,3,1105,-1,9,1101,0,0,12,4,12,99,1};
        // assert(intcode(codes, 0) == 0);
        // codes = new int[] {3,3,1105,-1,9,1101,0,0,12,4,12,99,1};
        // assert(intcode(codes, 1) == 1);

        // codes = new int[] { 3, 21, 1008, 21, 8, 20, 1005, 20, 22, 107,
        // 8, 21, 20, 1006, 20, 31, 1106, 0, 36, 98, 0, 0, 1002,
        // 21, 125, 20, 4, 20, 1105, 1, 46, 104, 999, 1105, 1, 46,
        // 1101, 1000, 1, 20, 4, 20, 1105, 1, 46, 98, 99 };
        // assert (intcode(codes, 0) == 999);
        //
        //
        // codes = new int[] { 3, 21, 1008, 21, 8, 20, 1005, 20, 22, 107,
        // 8, 21, 20, 1006, 20, 31, 1106, 0, 36, 98, 0, 0, 1002,
        // 21, 125, 20, 4, 20, 1105, 1, 46, 104, 999, 1105, 1, 46,
        // 1101, 1000, 1, 20, 4, 20, 1105, 1, 46, 98, 99 };
        // assert (intcode(codes, 8) == 1000);
        //
        //
        // codes = new int[] { 3, 21, 1008, 21, 8, 20, 1005, 20, 22, 107,
        // 8, 21, 20, 1006, 20, 31, 1106, 0, 36, 98, 0, 0, 1002,
        // 21, 125, 20, 4, 20, 1105, 1, 46, 104, 999, 1105, 1, 46,
        // 1101, 1000, 1, 20, 4, 20, 1105, 1, 46, 98, 99 };
        // assert (intcode(codes, 10) == 1001);



      }
    }
  }

  public class ParamObj {
    int pos;
    int[] codes;

    public ParamObj(int[] c) {
      codes = c;
      // System.out.println("po codes = " + Arrays.toString(codes));
    }

    public void setPos(int p) {
      pos = p;
    }

    public int get(int i) {
      assert (i < 6);
      final int[] denom = new int[] {0, 100, 1000, 10000, 100000, 1000000};
      int paramMode = (codes[pos] / denom[i]) % 10;
      assert (paramMode == 0 || paramMode == 1);
      int param = paramMode % 10 == 0 ? codes[codes[pos + i]] // position
                                                              // mode
          : codes[pos + i]; // immediate mode
      return param;
    }

  }

  private int intcode(int[] codes, int input) throws Exception {
    // int[] codes = Arrays.copyOf(origCodes, origCodes.length); // 16225258
    // codes = new int[] { 3, 0, 104, 0, 99 };
    // codes = new int[] { 1002, 4, 3, 4, 33 };

    ParamObj po = new ParamObj(codes);
    int output = -1;

    int pos = 0;
    while (true) {
      int op = codes[pos];

      Log.out("pos " + pos + " - op " + op + " - next 4 codes "
          + Arrays.toString(Arrays.copyOfRange(codes, pos, pos + 4)));

      po.setPos(pos);

      op %= 100;

      switch (op) {
        case 1:
          codes[codes[pos + 3]] = po.get(1) + po.get(2);
          Log.out("Add: " + po.get(1) + " + " + po.get(2) + " = " + codes[codes[pos + 3]]
              + " into pos " + codes[pos + 3]);

          pos += 4;
          break;
        case 2:
          codes[codes[pos + 3]] = po.get(1) * po.get(2);
          Log.out("Mult: " + po.get(1) + " * " + po.get(2) + " = " + codes[codes[pos + 3]]
              + " into pos " + codes[pos + 3]);

          pos += 4;
          break;
        case 3:
          codes[codes[pos + 1]] = input;
          Log.out(
              "Input: " + input + " = " + codes[codes[pos + 1]] + " into pos " + codes[pos + 1]);
          pos += 2;
          break;
        case 4:
          output = po.get(1);
          System.out.printf("Output(pos %d) = %d \n", codes[pos + 1], output);
          pos += 2;
          break;
        case 5:
        case 6:
          boolean zeroCheck = po.get(1) != 0;
          if (op == 6)
            zeroCheck = !zeroCheck;
          Log.out("Jump-if-" + (op == 5 ? "nonzero" : "zero") + " test=" + po.get(1) + " to="
              + po.get(2));
          if (zeroCheck) {
            pos = po.get(2);
          } else {
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
          codes[codes[pos + 3]] = yesno;
          Log.out((op == 5 ? "Less-than" : "Equal-to") + " p1=" + po.get(1) + " p2=" + po.get(2)
              + " result=" + codes[codes[pos + 3]] + " in pos " + codes[pos + 3]);

          pos += 4;
          break;
        case 99:
          return output;
        default:
          throw new Exception("for pos " + pos + " -- unknown opcode value " + codes[pos]
              + " - next 4 codes " + Arrays.toString(Arrays.copyOfRange(codes, pos, pos + 4)));
      }

    }
  }
}
