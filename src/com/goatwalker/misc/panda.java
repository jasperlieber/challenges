package com.goatwalker.misc;

public class panda {

  public static void main(String[] args) {

    int[] ducks = new int[] {136, 66, 140, 70, 156, 
        70, 168, 66, 64, 62, 164, 81, 146, 72, 152, 66};
    
    for (int i=0; i < ducks.length; i++) {
      System.out.print(Character.toString((char)(i%2 == 1 
          ? (ducks[i]+3) : ducks[i]/2)));
    }
  }

}
