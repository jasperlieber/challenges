package com.goatwalker.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class regexHarness
{

    public static void main(String[] args)
    {
        // Console console = System.console();
        // if (console == null) {
        // System.err.println("No console.");
        // System.exit(1);
        // }
        //
        // Scanner sc = new Scanner(System.in);
        // System.out.println("Printing the file passed in:");
        // while(sc.hasNextLine()) System.out.println(sc.nextLine());
        // while (true) {

        Pattern pattern = Pattern.compile("rect (\\d+)x(\\d+)");

        Matcher matcher = pattern.matcher("rect 133x3");

        boolean found = false;
        // while (matcher.find())
        // {
        // System.out.format(
        // "I found the text" + " \"%s\" starting at "
        // + "index %d and ending at index %d.%n",
        // matcher.group(), matcher.start(), matcher.end());
        // found = true;
        // for (int jj = 0; jj <= matcher.groupCount(); jj++)
        // System.out.println("Group #" + jj + ": " + matcher.group(jj));
        // }
        // if (!found)
        // {
        // System.out.format("No match found.%n");
        // }

        // String testInput = "2 ZXNM, 1 PSVLS, 4 GRDNT, 26 GLZH, 3 VHJX, 16
        // BGPF, 1 LHVTN => 4 BTQL";
        // System.out.println(testInput);
        //
        // String regex = "((\\d+) (\\w+)){1}(, (\\d+) (\\w+)){6} => (\\d+)
        // (\\w+)";
        //
        // pattern = Pattern.compile(regex);
        // matcher = pattern.matcher(testInput);
        //
        // found = false;
        // while (matcher.find())
        // {
        // System.out.format(
        // "I found the text" + " \"%s\" starting at "
        // + "index %d and ending at index %d.%n",
        // matcher.group(), matcher.start(), matcher.end());
        // found = true;
        // for (int jj = 0; jj <= matcher.groupCount(); jj++)
        // System.out.println("Group #" + jj + ": " + matcher.group(jj));
        // }
        // if (!found)
        // {
        // System.out.format("No match found.%n");
        // }
        //

        String testInput = "1,2,3,4";
        System.out.println(testInput);

        String regex = "(\\d+)(,(\\d+))*";

        pattern = Pattern.compile(regex);
        matcher = pattern.matcher(testInput);

        if (matcher.find())
        {
            System.out.printf(
                    "Found \"%s\" starting at index %d and ending at index %d.%n",
                    matcher.group(), matcher.start(), matcher.end());
            for (int jj = 0; jj <= matcher.groupCount(); jj++)
                System.out.println("Group #" + jj + ": " + matcher.group(jj));
        }
        else
        {
            System.out.format("No match found.%n");
        }

    }
}
