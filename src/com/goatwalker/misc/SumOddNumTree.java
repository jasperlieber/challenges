package com.goatwalker.misc;

public class SumOddNumTree {

  public static void main(String[] args) {
    for (int jj = 1; jj < 6; jj++)
      System.out.println("sum = " + rowSumOddNumbers(jj));
  }

  public static int rowSumOddNumbers(int n) {
    int first = n * n - n + 1;
    int sum = n * (first + first + (n - 1) * 2) / 2;
    return sum;
  }

}
