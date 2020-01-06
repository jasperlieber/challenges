package com.goatwalker.aoc19;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Stream;

public class Day16Part1 {


  public static void main(String[] args) throws Exception {
    Day16Part1 game = new Day16Part1();
    game.doit();
  }

  private void doit() throws Exception {
    String input = "12345678";


    String out = FFTn(input, 1);
    assert (out.compareTo("48226158") == 0);

    out = FFTn(out, 1);
    assert (out.compareTo("34040438") == 0);

    out = FFTn(out, 1);
    assert (out.compareTo("03415518") == 0);

    out = FFTn(out, 1);
    assert (out.compareTo("01029498") == 0);

    input = "80871224585914546619083218645595";
    out = FFTn(input, 100);
    assert (out.substring(0, 8).compareTo("24176176") == 0);

    input = getInput();
    
    System.out.println(input.length());
    out = FFTn(input, 100);
    System.out.println(out.substring(0, 8));
  }


  private String FFTn(String inputStr, int cnt) {
    int[] in = Stream.of(inputStr.split("")).mapToInt(Integer::parseInt).toArray();
    for (int nn = 0; nn < cnt; nn++) {
      in = FFT(in);
    }
    return String.join("", Arrays.stream(in).mapToObj(String::valueOf).toArray(String[]::new));
  }

  private int[] FFT(int[] inArray) {
    int sz = inArray.length;
    int[] outArray = new int[sz];
    for (int outIx = 0; outIx < sz; outIx++) {
      outArray[outIx] = FFT1(inArray, outIx);
    }
    return outArray;

  }

  // pattern is 0n, 1n, 0n, -1n shifted one element left
  private int FFT1(int[] inArray, int outIx) {
    int sum = 0;
    for (int inIx = 0; inIx < inArray.length; inIx++) {
      final int[] pat = new int[] {0, 1, 0, -1};
      int mult = pat[(inIx + 1) / (outIx + 1) % 4];
      sum += inArray[inIx] * mult;
    }
    return Math.abs(sum) % 10;
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
