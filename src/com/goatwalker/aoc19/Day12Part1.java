package com.goatwalker.aoc19;

public class Day12Part1 {

  public static void main(String[] args) throws Exception {
    Day12Part1 game = new Day12Part1();
    game.doit();
  }

  // actual input
  // <x=-2, y=9, z=-5>
  // <x=16, y=19, z=9>
  // <x=0, y=3, z=6>
  // <x=11, y=0, z=11>

  int[][] moons = new int[][] {{-2, 9, -5}, {16, 19, 9}, {0, 3, 6}, {11, 0, 11},};

  // test input
  // <x=-1, y=0, z=2>
  // <x=2, y=-10, z=-7>
  // <x=4, y=-8, z=8>
  // <x=3, y=5, z=-1>
  // int[][] moons = new int[][] { { -1, 0, 2 }, { 2, -10, -7 }, { 4, -8, 8 },
  // { 3, 5, -1 }, };

  // test input 2
  // <x=-8, y=-10, z=0>
  // <x=5, y=5, z=10>
  // <x=2, y=-7, z=3>
  // <x=9, y=-8, z=-3>

  // int[][] moons = new int[][] { { -8,-10,0 }, { 5,5,10 }, { 2,-7,3 },
  // { 9,-8,-3 }, };

  int[][] vels = new int[][] {{0, 0, 0}, {0, 0, 0}, {0, 0, 0}, {0, 0, 0},};

  private void doit() throws Exception {
    int[][] pairs = new int[][] {{0, 1}, {0, 2}, {0, 3}, {1, 2}, {1, 3}, {2, 3}};
    int[] axises = new int[] {0, 1, 2};
    int time = 0;
    while (time < 1001) {
      display(time);
      // apply gravity
      for (int[] pair : pairs) {
        int m1 = pair[0], m2 = pair[1];
        for (int axis : axises) {
          int cmp = Integer.signum(moons[m2][axis] - moons[m1][axis]);
          vels[m1][axis] += cmp;
          vels[m2][axis] -= cmp;
        }
      }

      // apply velocity
      for (int moon = 0; moon < 4; moon++) {
        for (int axis : axises) {
          moons[moon][axis] += vels[moon][axis];
        }
      }

      time++;

    }
  }

  private void display(int time) {
    System.out.println("\nAfter " + time + " steps:");

    int[] pe = new int[] {0, 0, 0, 0};
    int[] ke = new int[] {0, 0, 0, 0};
    int[] te = new int[] {0, 0, 0, 0};
    int total = 0;

    for (int moon = 0; moon < 4; moon++) {
      System.out.printf("pos=<x=%2d, y=%2d, z=%2d>, vel=<x=%2d, y=%2d, z=%2d>\n", moons[moon][0],
          moons[moon][1], moons[moon][2], vels[moon][0], vels[moon][1], vels[moon][2]);
      pe[moon] = Math.abs(moons[moon][0]) + Math.abs(moons[moon][1]) + Math.abs(moons[moon][2]);
      ke[moon] = Math.abs(vels[moon][0]) + Math.abs(vels[moon][1]) + Math.abs(vels[moon][2]);
      te[moon] = pe[moon] * ke[moon];
      total += te[moon];
    }
    System.out.printf("Sum of total energy: %d + %d + %d + %d = %d\n", te[0], te[1], te[2], te[3],
        total);
  }
}
