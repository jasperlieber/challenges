package com.goatwalker.aoc19;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.ListIterator;
import com.goatwalker.aoc16.Day10Part1.Log;
import com.goatwalker.utils.Coord;
import com.goatwalker.utils.Tiles;

public class Day16Part1 {


  public static void main(String[] args) throws Exception {
    Day16Part1 game = new Day16Part1();
    game.doit();
  }

  private void doit() throws Exception {
    init();

  

  }


  private void init() throws IOException, FileNotFoundException {
    String dataDirPrefix = System.getProperty("user.dir") + java.io.File.separator + "2019_data"
        + java.io.File.separator;

    String filename = "16.txt";

    try (BufferedReader br = new BufferedReader(new FileReader(dataDirPrefix + filename))) {
      String line;
      while ((line = br.readLine()) != null) {
        Log.out(line);
      }
    }

  }

}
