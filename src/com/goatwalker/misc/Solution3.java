package com.goatwalker.misc;

import java.util.ArrayList;
import java.util.List;

public class Solution3 {


  // METHOD SIGNATURE BEGINS, THIS METHOD IS REQUIRED
  // If both neighbors active or inactive, become inactive
  // Otherwise become active
  public List<Integer> cellCompete(int[] states, int days) {
    List<Integer> nextState = new ArrayList<Integer>();
    for (int i = 0; i < 8; i++) {
      if (i == 0) {
        // assume left is inactive - so if right inactive, become inactive
        // or if right active, become active
        nextState.add(states[1]);
      } else if (i == 7) {
        nextState.add(states[6]);
      } else {
        nextState.add(states[i - 1] == states[i + 1] ? 0 : 1);
      }
    }
    return nextState;
  }
  // METHOD SIGNATURE ENDS
  
  public static void main(String[] args) {

    int[] test = new int[] {0, 0, 0, 0, 0, 0, 0, 0};

    Solution s = new Solution();
    System.out.println(s.cellCompete(test, 1));

  }


}
