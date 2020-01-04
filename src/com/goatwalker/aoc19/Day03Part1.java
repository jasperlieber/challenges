package com.goatwalker.aoc19;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import com.goatwalker.utils.Pair;

public class Day03Part1 {
  public static void main(String[] args) throws Exception {
    Day03Part1 game = new Day03Part1();
    game.doit();
  }

  private int curx, cury;

  private void doit() throws Exception {

    // WireSegs wireSegs = new WireSegs();

    String filename =
        "C:\\Users\\jasper\\Google Drive\\Fun\\2016AdventOfCode\\EclipseWorkspace\\2019-data\\"
            + "3p1.txt";

    boolean firstWire = true;

    WireSegs segs1 = null, segs2 = null;

    try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
      String line;

      while ((line = br.readLine()) != null) {
        curx = 0;
        cury = 0;
        if (firstWire) {
          segs1 = addSegs(line);
          firstWire = false;
        } else {
          segs2 = addSegs(line);
          Coord closest = findClosestCross(segs1, segs2);
          System.out.println("closest = " + closest + " with distance " + closest.dist());
        }

      }
    }
  }

  private Coord findClosestCross(WireSegs segs1, WireSegs segs2) throws Exception {
    Coord closest = null;
    for (WireSeg seg1 : segs1) {
      for (WireSeg seg2 : segs2) {
        System.out.println("Intersecting " + seg1 + " & " + seg2);
        Coord cross = seg1.intersect(seg2);
        if (cross != null && cross.nonZero() && cross.closer(closest)) {
          closest = cross;
        }
        System.out.println(
            "Cross of " + seg1 + " & " + seg2 + " is " + cross + ", closest now = " + closest);
      }
    }

    return closest;
  }

  private WireSegs addSegs(String line) throws Exception {
    WireSegs wireSegs = new WireSegs();

    String[] moves = line.split(",");
    System.out.printf("found %d dirs\n\n", moves.length);

    for (String move : moves) {
      char dir = move.charAt(0);
      int dist = Integer.parseInt(move.substring(1));

      Coord c1 = new Coord(curx, cury);
      Coord c2;

      switch (dir) {
        case 'R':
          c2 = new Coord(curx + dist, cury);
          break;
        case 'L':
          c2 = new Coord(curx - dist, cury);
          break;
        case 'U':
          c2 = new Coord(curx, cury + dist);
          break;
        case 'D':
          c2 = new Coord(curx, cury - dist);
          break;
        default:
          throw new Exception("unknown direction " + dir);
      }
      System.out.println(dir + " " + dist + ", cur = " + c2);

      wireSegs.add(new WireSeg(c1, c2));

      curx = c2.getFirst();
      cury = c2.getSecond();
    }

    System.out.println("wiresegs: " + wireSegs + "\n\n");

    return wireSegs;
  }

  private class Coord extends Pair<Integer, Integer> {

    public Coord(Integer first, Integer second) {
      super(first, second);
    }

    public int dist() {
      return Math.abs(getFirst()) + Math.abs(getSecond());
    }

    public boolean nonZero() {
      return getFirst() != 0 || getSecond() != 0;
    }

    public boolean closer(Coord coord) {
      if (coord == null)
        return true;
      return dist() < coord.dist();
    }

    public Coord(Coord cur) {
      super(cur.getFirst(), cur.getSecond());
    }

  }

  @SuppressWarnings("serial")
  private class WireSegs extends ArrayList<WireSeg> {
  }

  private class WireSeg extends Pair<Coord, Coord> {

    public WireSeg(Coord first, Coord second) {
      super(first, second);
    }

    public Coord intersect(WireSeg seg2) throws Exception {
      Coord cross = null;

      int x1 = this.getFirst().getFirst();
      int y1 = this.getFirst().getSecond();
      int x2 = this.getSecond().getFirst();
      int y2 = this.getSecond().getSecond();

      int x3 = seg2.getFirst().getFirst();
      int y3 = seg2.getFirst().getSecond();
      int x4 = seg2.getSecond().getFirst();
      int y4 = seg2.getSecond().getSecond();

      // see
      // https://en.wikipedia.org/wiki/Line%E2%80%93line_intersection#Given_two_points_on_each_line

      int denom = (x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4);
      if (denom == 0) {
        if (x1 == x2 && x1 == x3 || y1 == y2 && y1 == y3) {
          System.out.println("coincident segs: " + this + " & " + seg2);
        } else {
          System.out.println("parallel segs: " + this + " & " + seg2);
        }

        return null;
      }

      int tt = (x1 - x3) * (y3 - y4) - (y1 - y3) * (x3 - x4);
      int uu = -((x1 - x2) * (y1 - y3) - (y1 - y2) * (x1 - x3));

      float t = tt / (float) denom;
      float u = uu / (float) denom;

      if (0f <= t && t <= 1f && 0f <= u && u <= 1f) {
        float px = x1 + t * (x2 - x1);
        float py = y1 + t * (y2 - y1);

        cross = new Coord((int) px, (int) py);
        System.out.println("cross = " + cross + " t = " + t + " u = " + u + " denom = " + denom);

      } else {
        System.out.println("no cross, t = " + t + " u = " + u + " denom = " + denom);
      }

      return cross;
    }
  }
}
