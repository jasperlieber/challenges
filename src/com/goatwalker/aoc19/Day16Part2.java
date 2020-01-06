package com.goatwalker.aoc19;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Stream;

public class Day16Part2 {


  public static void main(String[] args) throws Exception {
    Day16Part2 game = new Day16Part2();
    game.doit();
  }

  private void doit() throws Exception {

    String input = getInput();

    String out = FFTn(input, 100);
    System.out.println(out);
  }


  private String FFTn(String inputStr, int cnt) {
    int[] in1 = Stream.of(inputStr.split("")).mapToInt(Integer::parseInt).toArray();
    int sz = in1.length;

    final int reps = 10000;
    final int offset = Integer.parseInt(inputStr.substring(0, 7));
    
    assert(offset > sz/2);
    
    int[] in = new int[sz * reps - offset];
    for (int jj = offset; jj < sz * reps; jj++)
        in[jj-offset] = in1[jj%sz];
    for (int nn = 0; nn < cnt; nn++) {
      in = FFTones(in);
      System.out.println("FFT #" + nn + " = " + first8(in));
    }
    return first8(in);
  }

  private String first8(int[] in) {
    String s="";
    for (int jj = 0; jj < 8; jj++)
      s+= in[jj];
    return s;
  }

  private int[] FFTones(int[] inArray) {
    int sz = inArray.length;
    int[] outArray = new int[sz];
    sz -= 1;
    outArray[sz] = inArray[sz];
    for (; --sz>=0; ) {
      outArray[sz] = (inArray[sz] + outArray[sz+1])%10;
    }
    return outArray;

  }

  // pattern is 0n, 1n, 0n, -1n shifted one element left
  private int FFT1(int[] inArray, int outIx) {
    // System.out.println("FFT1 with outIx = " + outIx);
    long sum = 0;
    int sz = inArray.length;
    for (int inIx = 0; inIx < sz; inIx++) {
      final int[] pat = new int[] {0, 1, 0, -1};
      int mult = pat[(inIx + 1) / (outIx + 1) % 4];
      sum += inArray[inIx] * mult;
    }
    return (int) (Math.abs(sum) % 10);
  }


  private String getInput() throws FileNotFoundException, IOException {
    String dataDirPrefix = System.getProperty("user.dir") + java.io.File.separator + "2019_data"
        + java.io.File.separator;

    String filename = "16.txt";

    try (BufferedReader br = new BufferedReader(new FileReader(dataDirPrefix + filename))) {
      return br.readLine();

      // while ((line = br.readLine()) != null) {
      // process(line));

      // }
    }

  }



}
