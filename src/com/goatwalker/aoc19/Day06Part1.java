package com.goatwalker.aoc19;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashSet;
import java.util.Set;

import com.goatwalker.utils.Tree;

//import com.goatwalker.aoc16.day08.InstructionDay08;
//import com.goatwalker.aoc16.day08.InstructionDay08.InstructionEnum;

public class Day06Part1
{

    public static void main(String[] args) throws Exception
    {
        Day06Part1 game = new Day06Part1();
        game.doit();
    }

    private void doit() throws Exception
    {
        String filename = "C:\\Users\\jasper\\Google Drive\\Fun\\2016AdventOfCode\\EclipseWorkspace\\2019-data\\"
//                 + "6p1-test.txt";
                + "6p1.txt";

        System.out.println(filename);

        // private Set<Tree<String>> trees = new HashSet<Tree<String>>();

        Orbits orbits = new Orbits();

        try (BufferedReader br = new BufferedReader(new FileReader(filename)))
        {
            String line;
            while ((line = br.readLine()) != null)
            {
                String[] bods = line.split("\\)");
                String b1 = bods[0];
                String b2 = bods[1];

                System.out.printf("Adding [%s ) %s]\n", b1, b2);
                // System.out.println(Arrays.toString(bods));

                // locate or create orbits for both bodies
                Tree<String> t1 = orbits.locate(b1);
                Tree<String> t2 = orbits.locate(b2);

                assert (t2.getTree(b1) == null); // no cycles
                assert (t2.getParent() == null); // can only add roots
                assert (t1.getTree(b2) == null); // a tree shouldn't already
                                                 // contain the second body

                t1.addSubTree(t2);

                orbits.remove(b2);

//                System.out.println(orbits.toString());
            }
        }

        assert (orbits.size() == 1);

        Tree<String> orbit = orbits.getOne();
        int orbitCount = 0;
        for (String body : orbit.locations())
        {
            orbitCount += orbit.numParents(body);
        }

        System.out.println(orbitCount);
    }

    class Orbits
    {
        private Set<Tree<String>> orbits = new HashSet<Tree<String>>();

        @Override
        public String toString()
        {
            String out = "orbits:";

            int jj = 0;
            for (Tree<String> orbit : orbits)
            {
                out = out.concat("\n" + (jj++) + ": " + orbit.printTree(3));
            }
            return out;
        }

        public Tree<String> getOne()
        {
            return orbits.iterator().next();
        }

        public int size()
        {
            return orbits.size();
        }

        public void remove(String b2)
        {
//            System.out.println("before remove of " + b2 + ", " + toString());
//            System.out.println("locateRoot(" + b2 + ") = " + locateRoot(b2));
//            orbits.forEach(orbit -> System.out
//                    .println(">" + orbit.getHead() + (orbit.getHead() == b2) + (b2.compareTo(orbit.getHead())==0)));
            boolean removed = orbits.removeIf(orbit -> (b2.compareTo(orbit.getHead()) == 0));
//            System.out.println("after remove of " + b2 + ", " + toString());

            assert (removed);
        }

        private Object locateRoot(String b2)
        {
            Tree<String> bodyOrbit = null;
            for (Tree<String> orbit : orbits)
            {
                if (orbit.getHead() == b2)
                {
                    assert (bodyOrbit == null);
                    bodyOrbit = orbit;
                }
            }
            return bodyOrbit;
        }

        public Tree<String> locate(String body)
        {
            // System.out.println("start of locate: orbits = " + orbits);
            // System.out.println("looking for " + body);
            Tree<String> bodyOrbit = null;
            for (Tree<String> orbit : orbits)
            {
                Tree<String> locatedOrbit = orbit.getTree(body);
                // System.out.println("locatedOrbit = " + locatedOrbit);
                if (locatedOrbit != null)
                {
                    // orbits should never contain dupes
                    assert (bodyOrbit == null);
                    bodyOrbit = locatedOrbit;
                }
            }
            if (bodyOrbit == null)
            {
                // not found - create one and add to orbits set
                bodyOrbit = new Tree<String>(body);
                orbits.add(bodyOrbit);
            }

            // System.out.println("end of locate: orbits = " + orbits);
            return bodyOrbit;
        }
    }
}