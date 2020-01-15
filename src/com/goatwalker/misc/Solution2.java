package com.goatwalker.misc;

import java.util.ArrayList;
import java.util.List;

public class Solution2 {

  public static void main(String[] args) {

    int[] test = new int[] {1, 0, 0, 0, 0, 1, 0, 0};

    Solution2 s = new Solution2();
    System.out.println(s.cellCompete(test, 1));


    test = new int[] {1, 1, 1, 0, 1, 1, 1, 1};
    System.out.println(s.cellCompete(test, 2));

  }

  // METHOD SIGNATURE BEGINS, THIS METHOD IS REQUIRED
  public List<Integer> cellCompete(int[] states, int days) {

    // sanity check the days input and state vector length
    if (days < 0 || states.length != 8)
      return null;

    // put the int array into an Integer List
    List<Integer> nextState = new ArrayList<Integer>();
    for (int state : states)
      nextState.add(state);

    // cellCompeteOnce does one iteration -- call it "days" times
    for (int jj = 0; jj < days; jj++) {
      nextState = cellCompeteOnce(nextState);
    }

    return nextState;
  }



  public List<Integer> cellCompeteOnce(List<Integer> states) {
    List<Integer> nextState = new ArrayList<Integer>();
    for (int i = 0; i < 8; i++) {
      // handle the first and last cells specially -- per
      // instructions, assume neighbors off the list are inactive.
      // So for edge cases, the next state of edges
      // is the existing state of the neighbor.
      if (i == 0) {
        nextState.add(states.get(1));
      } else if (i == 7) {
        nextState.add(states.get(6));
      } else {
        // If both neighbors active or inactive, become inactive
        // Otherwise become active.
        nextState.add(states.get(i - 1) == states.get(i + 1) ? 0 : 1);
      }
    }

    return nextState;
  }
  // METHOD SIGNATURE ENDS

}
