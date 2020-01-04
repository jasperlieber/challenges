package com.goatwalker.aoc19;

import com.goatwalker.utils.LCM_long;

public class Day12Part2
{

    public static void main(String[] args) throws Exception
    {
        Day12Part2 game = new Day12Part2();
        game.doit();
    }

    // actual input
    // <x=-2, y=9, z=-5>
    // <x=16, y=19, z=9>
    // <x=0, y=3, z=6>
    // <x=11, y=0, z=11>

    // 320380285873116 is answer
    // 9223372036854775807 is max long

    long[][] moons = new long[][] { { -2, 9, -5 }, { 16, 19, 9 }, { 0, 3, 6 },
            { 11, 0, 11 }, };

    // test input
    // <x=-1, y=0, z=2>
    // <x=2, y=-10, z=-7>
    // <x=4, y=-8, z=8>
    // <x=3, y=5, z=-1>
    // long[][] moons = new long[][] { { -1, 0, 2 }, { 2, -10, -7 }, { 4, -8, 8
    // },
    // { 3, 5, -1 }, };

    // test input 2
    // <x=-8, y=-10, z=0>
    // <x=5, y=5, z=10>
    // <x=2, y=-7, z=3>
    // <x=9, y=-8, z=-3>
    // long[][] moons = new long[][] { { -8, -10, 0 }, { 5, 5, 10 }, { 2, -7, 3
    // },
    // { 9, -8, -3 }, };

    long[][] vels = new long[][] { { 0, 0, 0 }, { 0, 0, 0 }, { 0, 0, 0 },
            { 0, 0, 0 }, };

    private void doit() throws Exception
    {
        int[][] pairs = new int[][] { { 0, 1 }, { 0, 2 }, { 0, 3 }, { 1, 2 },
                { 1, 3 }, { 2, 3 } };
        int[] axises = new int[] { 0, 1, 2 };
        long time = 0;
        long reps[] = new long[3];
        for (int axis : axises)
            reps[axis] = 0;

        long[][] moons2 = new long[moons.length][3];
        for (int i = 0; i < moons.length; i++)
            for (int axis : axises)
                moons2[i][axis] = moons[i][axis];

        boolean allRepeated = false;
        while (!allRepeated)
        {
            // display(time);

            time++;

            for (int axis : axises)
            {
                // apply gravity
                for (int[] pair : pairs)
                {
                    int m1 = pair[0], m2 = pair[1];
                    long cmp = Long.signum(moons[m2][axis] - moons[m1][axis]);
                    vels[m1][axis] += cmp;
                    vels[m2][axis] -= cmp;
                }

                // apply velocity
                for (int moon = 0; moon < 4; moon++)
                {
                    moons[moon][axis] += vels[moon][axis];
                }

                allRepeated = true;

                for (int moon = 0; moon < 4; moon++)
                {
                    allRepeated &= moons[moon][axis] == moons2[moon][axis]
                            && vels[moon][axis] == 0;

                }
                if (allRepeated)
                {
                    if (reps[axis] == 0)
                    {
                        System.out.printf("axis %d repeats at time %d\n", axis,
                                time);
                        reps[axis] = time;
                    }
                }
            }
            allRepeated = true;
            for (int axis : axises)
                allRepeated &= reps[axis] != 0;
        }

        System.out.print("Axis reps: ");
        for (int axis : axises)
        {
            System.out.print(reps[axis] + " ");
        }
        System.out.println();

        System.out.println("Time til repeat: " + LCM_long.lcm(reps));
    }

}