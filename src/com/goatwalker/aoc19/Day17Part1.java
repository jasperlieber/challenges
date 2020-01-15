package com.goatwalker.aoc19;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.goatwalker.utils.GrowableLongArray;
import com.goatwalker.utils.Logger.Log;

public class Day17Part1 {
  public static void main(String[] args) throws Exception {
    Day17Part1 game = new Day17Part1();
    game.doit();
  }


  private void doit() throws Exception {
    long[] program = readInput();
    char[][] image = getImage(program);

    int apSum = 0;

    for (int yy = 1; yy < image.length - 1; yy++) {
      for (int xx = 1; xx < image[yy].length - 1; xx++) {
        if (isIntersection(image, xx, yy))
          apSum += xx * yy;
      }
    }

    System.out.println(apSum);

  }


  private boolean isIntersection(char[][] image, int xx, int yy) {
    return image[yy][xx] == '#' && image[yy - 1][xx] == '#' && image[yy + 1][xx] == '#'
        && image[yy][xx - 1] == '#' && image[yy][xx + 1] == '#';
  }


  private char[][] getImage(long[] program) throws Exception {
    Intcode comp;

    comp = new Intcode(program);

    ArrayList<Character> row = new ArrayList<Character>();
    ArrayList<char[]> image = new ArrayList<char[]>();

    while (comp.stillRunning()) {
      Long ascii = comp.resume();
      if (ascii == null)
        break;

      char ch = (char) ascii.intValue();

      if (ch == 10) {
        if (row.size() > 0) {
          char[] rowa = new char[row.size()];
          int jj = 0;
          for (char c : row)
            rowa[jj++] = c;
          image.add(rowa);
          row.clear();
        }
      } else {
        row.add(ch);
      }
    }

    char[][] imagea = new char[image.size()][];
    int jj = 0;
    for (char[] rowa : image)
      imagea[jj++] = rowa;

    for (char[] rowa : imagea) {
      for (char ch : rowa)
        System.out.print(ch);
      System.out.println();
    }

    return imagea;
  }

  private long[] readInput() throws IOException, FileNotFoundException {
    String dataDirPrefix = System.getProperty("user.dir") + java.io.File.separator + "2019_data"
        + java.io.File.separator;

    String filename = "17.txt";
    long[] program = null;
    String line;

    try (BufferedReader br = new BufferedReader(new FileReader(dataDirPrefix + filename))) {

      line = br.readLine();
    }
    program = Arrays.stream(line.split(",")).mapToLong(Long::parseLong).toArray();
    return program;
  }

  private class Intcode {
    private class ParamObj {
      int pos;
      int relBase = 0;
      GrowableLongArray mem;

      private ParamObj(GrowableLongArray coremem) {
        mem = coremem;
        // System.out.println("po codes = " + Arrays.toString(codes));
      }

      public void setPos(int p) {
        pos = p;
      }

      private int getParamMode(int i) throws Exception {
        assert (i < 6);
        final int[] denom = new int[] {0, 100, 1000, 10000, 100000, 1000000};
        int paramMode = (int) ((mem.get(pos) / denom[i]) % 10);
        return paramMode;
      }

      public int set(int i, long val) throws Exception {
        int paramMode = getParamMode(i);
        long index;
        switch (paramMode) {
          case 0:
            // position mode
            index = mem.get(pos + i);
            break;
          case 1:
            // immediate mode
            throw new Exception("no immediate mode for set()");
            // break;
          case 2:
            // relative mode
            index = mem.get(pos + i) + relBase;
            break;
          default:
            throw new Exception("Unknown param mode = " + paramMode);

        }
        mem.set(index, val);
        return (int) index;
      }

      public long get(int i) throws Exception {
        int paramMode = getParamMode(i);
        long param;
        switch (paramMode) {
          case 0:
            // position mode
            param = mem.get((int) mem.get(pos + i));
            break;
          case 1:
            // immediate mode
            param = mem.get(pos + i);
            break;
          case 2:
            // relative mode
            param = mem.get((int) mem.get(pos + i) + relBase);
            break;
          default:
            throw new Exception("Unknown param mode = " + paramMode);

        }
        return param;
      }

      public void adjustBase(long p1) {
        relBase += p1;
      }
    }

    private GrowableLongArray mem = new GrowableLongArray();
    private boolean running = true;
    private ParamObj po = null;
    private List<Long> inputs = new ArrayList<Long>();
    private int inputIndex;
    private int ip;

    public Intcode(long[] program) {
      mem.initialize(program);
      po = new ParamObj(mem);
      ip = 0;
      inputIndex = 0;
    }

    public int memsize() {
      return mem.memsize();
    }

    public boolean stillRunning() {
      return running;
    }

    public Long resume() throws Exception {
      Long output = null;
      while (running && output == null) {
        long op = mem.get(ip);

        Log.out("pos " + ip + " - op " + op + " - next 4 codes " + Arrays.toString(mem.get(ip, 4)));

        po.setPos(ip);

        op %= 100;

        long p1, p2;
        int addr = 0;

        switch ((int) op) {
          case 1:
            p1 = po.get(1);
            p2 = po.get(2);
            addr = po.set(3, p1 + p2);
            Log.out("Add: " + p1 + " + " + p2 + " = " + mem.get(addr) + " into pos " + addr);
            ip += 4;
            break;
          case 2:
            p1 = po.get(1);
            p2 = po.get(2);
            addr = po.set(3, p1 * p2);
            Log.out("Mult: " + p1 + " * " + p2 + " = " + mem.get(addr) + " into pos " + addr);
            ip += 4;
            break;
          case 3:
            long input = inputs.get(inputIndex);
            addr = po.set(1, input);
            Log.out("Input #" + inputIndex + ": " + inputs + " = " + mem.get(addr) + " into pos "
                + addr);
            ip += 2;
            inputIndex++;
            break;
          case 4:
            output = po.get(1);
            Log.out(String.format("Output(pos %d) = %d \n", mem.get(ip + 1), output));
            ip += 2;
            break;
          case 5:
          case 6:
            boolean zeroCheck = po.get(1) != 0;
            if (op == 6)
              zeroCheck = !zeroCheck;
            Log.out("Jump-if-" + (op == 5 ? "nonzero" : "zero") + " test=" + po.get(1) + " to="
                + po.get(2));
            if (zeroCheck) {
              ip = (int) po.get(2);
            } else {
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
            addr = po.set(3, yesno);
            Log.out((op == 5 ? "Less-than" : "Equal-to") + " p1=" + p1 + " p2=" + p2 + " result="
                + mem.get(addr) + " into pos " + addr);
            ip += 4;
            break;
          case 9:
            p1 = po.get(1);
            po.adjustBase(p1);
            Log.out("Adjusting relBase by " + p1);
            ip += 2;
            break;
          case 99:
            running = false;
            break;
          default:
            throw new Exception("for pos " + ip + " -- unknown opcode value " + mem.get(ip)
                + " - next 4 codes " + Arrays.toString(mem.get(ip, 4)));
        }

      }
      return output;
    }

    private void addInput(Long newInput) {
      inputs.add(newInput);
    }

  }
}
