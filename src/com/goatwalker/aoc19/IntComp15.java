package com.goatwalker.aoc19;

import java.util.Arrays;
import java.util.LinkedList;
import com.goatwalker.utils.GrowableLongArray;
import com.goatwalker.utils.Logger.Log;

public class IntComp15 {
  private GrowableLongArray mem = new GrowableLongArray();
  private boolean running = true;
  private ParamObj po = null;
  private LinkedList<Long> suppliedInputs = new LinkedList<Long>();
  private int ip;

  public IntComp15(long[] program) {
    mem.initialize(program);
    po = new ParamObj(mem);
    ip = 0;
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

          Long inputPoll = suppliedInputs.poll();
          if (inputPoll == null)
            throw new Exception("no input available");
          long input = inputPoll == null ? 0 : inputPoll;

          // System.out.println("----------input=" + input + " inputs=" +
          // suppliedInputs);

          addr = po.set(1, input);
          Log.out("Input: " + input + " = " + mem.get(addr) + " into pos " + addr);
          ip += 2;
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

  public void addInput(Long newInput) {
    suppliedInputs.add(newInput);
  }

  private class ParamObj {
    int pos;
    int relBase = 0;
    GrowableLongArray mem;

    public ParamObj(GrowableLongArray coremem) {
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

}
