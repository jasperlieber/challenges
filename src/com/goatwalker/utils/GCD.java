package com.goatwalker.utils;


public class GCD {


  /**
   * generalizedGCD() calculates the greatest common divisor of 
   * the <num> numbers if the array <arr>
   * 
   * @param num - an integer representing the number of positive integers
   * @param arr - an array of positive integers
   * @return - an integer representing the GCD of the given integers, or 0 if 
   * error in input
   */
  public int generalizedGCD(int num, int[] arr) {

    if (num < 1)
      return 0; // <num> must be 2 or more
    if (arr.length < num)
      return 0; // supplied array is shorter than GCD num request


    // check that the <num> integers in <arr> are positive
    for (int jj = 0; jj < num; jj++)
      if (arr[jj] < 1)
        return 0; // supplied integers must be positive");

    if (num == 1)
      return arr[0];

    int gcd = gcd2(arr[0], arr[1]);

    for (int jj = 2; jj < num; jj++)
      gcd = gcd2(gcd, arr[jj]);

    return gcd;

  }

  /**
   * Calculate the GCD of 2 positive integers, using Euclid's algorithm
   * 
   * @param a - first integer
   * @param b - second integer
   * @return the GCD of the two integers
   */
  private int gcd2(int a, int b) {

    if (b == 0) {
      return a;
    } else {
      return (gcd2(b, a % b));
    }
  }

  /**
   * Some test cases
   * 
   * @param args
   * @throws GCDException
   */
  public static void main(String[] args) {
    int[] is = new int[] {2, 4, 6, 8};

    GCD g = new GCD();
    System.out.println(g.generalizedGCD(is.length, is));

    is = new int[] {2, 3, 4, 5, 6};
    System.out.println(g.generalizedGCD(is.length, is));

    is = new int[] {2, -3, 4, 5, 6};

    System.out.println(g.generalizedGCD(5, is));

  }


}
