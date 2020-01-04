package com.goatwalker.aoc19;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;

public class Day14Part1 {

  public static void main(String[] args) throws Exception {
    Day14Part1 game = new Day14Part1();
    game.doit();
  }

  boolean logging = true;
  Reactions reactions;
  Production surplus;

  private void doit() throws Exception {
    reactions = new Reactions();
    surplus = new Production();

    getInput();

    AmtChem goal = new AmtChem("1 FUEL");
    LinkedList<AmtChem> work = new LinkedList<AmtChem>();
    work.add(goal);

    int totalOre = doProduction(work);

    System.out.println("Requires " + totalOre + " ORE");
  }

  private int doProduction(LinkedList<AmtChem> work) {
    int totalOreCount = 0;
    while (!work.isEmpty()) {
      AmtChem subgoal = work.poll();
      String name = subgoal.name;
      int quant = subgoal.quant;

      myprintln("Goal: " + subgoal);

      int avail = surplus.getOrDefault(name, 0);
      if (avail > 0) {
        myprintln(" available for " + name + ": " + avail + " surplus");
        if (avail >= quant) {
          myprintln(" using " + quant + " surplus");
          surplus.put(name, avail - quant);
          continue;
        }
        myprintln(" reducing need by " + avail + " surplus");
        quant -= avail;
        avail = 0;
        surplus.put(name, 0);
      }

      if (name.compareTo("ORE") == 0) {
        myprintln(" mining " + quant + " ORE");
        totalOreCount += quant;
        continue;
      }

      Reaction reaction = reactions.getReaction(name);

      myprintln(" found reaction " + reaction);

      int q2 = reaction.getOutput().quant;
      int cnt = quant / q2;
      if (cnt * q2 < quant)
        cnt++;
      int extra = cnt * q2 - quant;
      if (extra > 0)
        surplus.put(name, avail + extra);

      myprintln(" adding " + cnt + " reactions making " + cnt * q2 + " amounts, " + extra
          + " extras, surplus now " + surplus.get(name));

      for (AmtChem input : reaction.getInputs()) {
        AmtChem amtChem = new AmtChem(cnt * input.quant + " " + input.name);
        myprintln(" adding " + amtChem);
        work.add(amtChem);
      }

    }
    return totalOreCount;
  }

  private void getInput() throws IOException, Exception, FileNotFoundException {
    String filename =
        "C:\\Users\\jasper\\Google Drive\\Fun\\2016AdventOfCode\\EclipseWorkspace\\2019-data\\"
            // + "14.txt";
            // + "14-test5.txt";
            + "14-test1.txt";

    try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
      String line;
      while ((line = br.readLine()) != null) {
        processLine(line);
        // System.out.println("read " + line);
      }
    }
  }

  private void processLine(String line) throws Exception {
    // myprintln(line);
    LinkedList<String> parts = new LinkedList<String>(Arrays.asList(line.split("\\s*,\\s*")));
    String[] lastOut = parts.pollLast().split("\\s*=>\\s*");
    assert (lastOut.length == 2);
    parts.add(lastOut[0]);
    // myprintln(parts.toString());

    AmtChem out = new AmtChem(lastOut[1]);
    List<AmtChem> ins = new ArrayList<AmtChem>();

    for (String part : parts)
      ins.add(new AmtChem(part));

    List<AmtChem> prev = reactions.put(out, ins);
    assert (prev == null);

  }

  public class AmtChem {
    int quant;
    String name;

    @Override
    public String toString() {
      return quant + " " + name;
    }

    public AmtChem(String qs) {
      String[] valKey = qs.split(" ");
      assert (valKey.length == 2);
      quant = Integer.valueOf(valKey[0]);
      name = valKey[1];
    }

  }

  public class Reactions extends HashMap<AmtChem, List<AmtChem>> {
    private static final long serialVersionUID = 1L;

    private HashMap<String, AmtChem> outKeys = new HashMap<String, AmtChem>();

    @Override
    public List<AmtChem> put(AmtChem key, List<AmtChem> value) {

      List<AmtChem> val = super.put(key, value);
      assert (val == null);

      AmtChem oldkey = outKeys.put(key.name, key);
      assert (oldkey == null);

      return null;
    }

    public String find(String name) {
      return get(outKeys.get(name)).toString() + " => " + outKeys.get(name);
    }

    public Reaction getReaction(String name) {
      return new Reaction(get(outKeys.get(name)), outKeys.get(name));
    }

  }

  public class Reaction extends Pair<List<AmtChem>, AmtChem> {
    public Reaction(List<AmtChem> inputs, AmtChem output) {
      super(inputs, output);
    }

    public AmtChem getOutput() {
      return getSecond();
    }

    public List<AmtChem> getInputs() {
      return getFirst();
    }

  }

  public class Production extends TreeMap<String, Integer> {
    private static final long serialVersionUID = 1L;

  }

  int cnt = 0;

  private void myprintln(String txt) {
    if (logging) {
      int line = Thread.currentThread().getStackTrace()[2].getLineNumber();
      System.out.println(cnt++ + " (" + line + "): " + txt);
    }

  }

}
