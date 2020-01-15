package com.goatwalker.misc;

import static java.util.Map.Entry.comparingByValue;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
// IMPORT LIBRARY PACKAGES NEEDED BY YOUR PROGRAM
// SOME CLASSES WITHIN A PACKAGE MAY BE RESTRICTED
// DEFINE ANY CLASS AND METHOD NEEDED
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;


// CLASS BEGINS, THIS CLASS IS REQUIRED
class Solution {



  public static void main(String[] args) {
    Solution s = new Solution();
    List<String> possibleFeatures = new ArrayList<String>();
    possibleFeatures.add("storage");
    possibleFeatures.add("battery");
    possibleFeatures.add("hover");
    possibleFeatures.add("kindle");
    possibleFeatures.add("alexa");
    possibleFeatures.add("waterproof");
    possibleFeatures.add("solar");
    List<String> featureRequests = new ArrayList<String>();;
    featureRequests.add("I wish my kindle had even more storage");
    featureRequests.add("battery life alexa");
    featureRequests.add("waterproof kindle alexa");
    featureRequests.add("waterproof Battery alexa");
    featureRequests.add("Waterproof waterproof");
    featureRequests.add("kindl hover ");
    featureRequests.add("kindle solar ");
    System.out.println(s.popularNFeatures(7, 4, possibleFeatures, 7, featureRequests));
  }



  // METHOD SIGNATURE BEGINS, THIS METHOD IS REQUIRED
  public ArrayList<String> popularNFeatures(int numFeatures, int topFeatures,
      List<String> possibleFeatures, int numFeatureRequests, List<String> featureRequests) {

    Map<String, Integer> featureCounts = new HashMap<String, Integer>();

    for (int featureIndex = 0; featureIndex < numFeatures; featureIndex++) {
      String feature = possibleFeatures.get(featureIndex);
      int occurences = 0;
      for (int requestIndex = 0; requestIndex < numFeatureRequests; requestIndex++) {
        String request = featureRequests.get(requestIndex).toLowerCase();
        if (request.indexOf(feature.toLowerCase()) != -1)
          occurences += 1;
      }
      featureCounts.put(feature, occurences);

    }
    
    

    System.out.println(featureCounts);
    
    

    Comparator<Entry<String, Integer>> valueComparator = new Comparator<Entry<String, Integer>>() {

      @Override
      public int compare(Entry<String, Integer> e1, Entry<String, Integer> e2) {
        Integer v1 = e1.getValue();
        Integer v2 = e2.getValue();
        int desc = -v1.compareTo(v2);
        if (desc == 0)
          desc = e1.getKey().compareToIgnoreCase(e2.getKey());
        return desc;
      }
    };


    LinkedHashMap<String, Integer> sorted =
        featureCounts.entrySet().stream().sorted(valueComparator).collect(Collectors
            .toMap(e -> e.getKey(), e -> e.getValue(), (e1, e2) -> e2, LinkedHashMap::new));

    System.out.println("sorted = " + sorted);
    
    
//    List<Entry<String, Integer>> listOfEntries = topNFeatures(topFeatures, featureCounts);
    
    
    
    System.out.println(listOfEntries);

    ArrayList<String> out = new ArrayList<String>();
    for (int top = 0; top < topFeatures; top++)
      out.add(sorted.);
    return out;

  }

  private List<Entry<String, Integer>> topNFeatures(int topFeatures, Map<String, Integer> featureCounts) {


    Comparator<Entry<String, Integer>> valueComparator = new Comparator<Entry<String, Integer>>() {

      @Override
      public int compare(Entry<String, Integer> e1, Entry<String, Integer> e2) {
        Integer v1 = e1.getValue();
        Integer v2 = e2.getValue();
        int desc = -v1.compareTo(v2);
        if (desc == 0)
          desc = e1.getKey().compareToIgnoreCase(e2.getKey());
        return desc;
      }
    };


    // Sort method needs a List, so let's first convert Map to a Set to a List in
    List<Entry<String, Integer>> listOfEntries =
        new ArrayList<Entry<String, Integer>>(featureCounts.entrySet());

    // sorting HashMap by values using comparator
    Collections.sort(listOfEntries, valueComparator);
    
    return listOfEntries;

  
  }



  private ArrayList<String> topNFeatures2(int topFeatures, Map<String, Integer> featureCounts) {
    LinkedList<Pair<Integer, LinkedList<String>>> topList =
        new LinkedList<Pair<Integer, LinkedList<String>>>();

    for (Map.Entry<String, Integer> featureCount : featureCounts.entrySet()) {
      System.out.println(featureCount);
      int count = featureCount.getValue();
      String feature = featureCount.getKey();
      int insert = 0;
      for (; insert < topList.size() && count < topList.get(insert).getFirst(); insert++);

      System.out.println("insert is " + insert);

      if (insert >= topList.size()) {
        LinkedList<String> list = new LinkedList<String>();
        list.add(feature);
        topList.add(new Pair<Integer, LinkedList<String>>(count, list));
        System.out.println("top 0 = " + topList);
      } else {
        if (count == topList.get(insert).getFirst()) {
          int i2 = 0;
          LinkedList<String> list = topList.get(insert).getSecond();
          for (; i2 < list.size() && feature.compareTo(list.get(i2)) < 0; i2++);
          if (i2 > list.size())
            list.add(feature);
          else
            list.add(i2, feature);
          System.out.println("top 1 = " + topList);
        } else {
          LinkedList<String> list = new LinkedList<String>();
          list.add(feature);
          topList.add(insert, new Pair<Integer, LinkedList<String>>(count, list));
          System.out.println("top 2 = " + topList);
        }
      }
    }


    System.out.println("hi");

    ArrayList<String> toptop = new ArrayList<String>();
    int jj = 0;
    while (jj < topFeatures) {
      Pair<Integer, LinkedList<String>> poll = topList.poll();
      System.out.println("poll = " + poll);
      while (jj < topFeatures) {
        String poll2 = poll.getSecond().poll();
        System.out.println("poll2 = " + poll2 + " toptop = " + toptop);
        if (poll2 != null) {
          toptop.add(poll2);
          jj++;
        } else
          break;
      }
    }
    System.out.println("toptop  = " + toptop);



    return toptop;

  }

  public class Pair<A, B> {
    private A first;
    private B second;

    public Pair(A first, B second) {
      super();
      this.first = first;
      this.second = second;
    }

    public int hashCode() {
      int hashFirst = first != null ? first.hashCode() : 0;
      int hashSecond = second != null ? second.hashCode() : 0;

      return (hashFirst + hashSecond) * hashSecond + hashFirst;
    }

    public boolean equals(Object other) {
      if (other instanceof Pair) {
        Pair<?, ?> otherPair = (Pair<?, ?>) other;
        return ((this.first == otherPair.first || (this.first != null && otherPair.first != null
            && this.first.equals(otherPair.first)))
            && (this.second == otherPair.second || (this.second != null && otherPair.second != null
                && this.second.equals(otherPair.second))));
      }

      return false;
    }

    public String toString() {
      return "(" + first + ", " + second + ")";
    }

    public A getFirst() {
      return first;
    }

    public void setFirst(A first) {
      this.first = first;
    }

    public B getSecond() {
      return second;
    }

    public void setSecond(B second) {
      this.second = second;
    }
  }


  // WRITE YOUR CODE HERE



  // METHOD SIGNATURE ENDS
}


