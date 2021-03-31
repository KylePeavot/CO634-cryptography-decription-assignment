package main;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;
import main.utils.WordUtils;

public class TranspositionCipher {

  public static String decrypt(String cipherText, String key) {
    //convert key to number
    List<Integer> keyAsIntArray = convertKeyToInteger(key);

    //Split cipher text into chunks
    List<String> separatedCipher = WordUtils.splitCypherTextColumnWise(cipherText, key.length());

    //TreeMaps are sorted by the key by default
    Map<Integer, List<Character>> cipherChunkByKey = new TreeMap<>();

    for (int i = 0; i < keyAsIntArray.size(); i++) {
      //turn each cipher chunk into a hashmap of itself and it's corresponding character in the key
      ArrayList<Character> cipherChunkAsChars = new ArrayList<>();
      for (Character c : separatedCipher.get(i).toCharArray()) {
        cipherChunkAsChars.add(c);
      }

      cipherChunkByKey.put(keyAsIntArray.get(i), cipherChunkAsChars);
    }

    //for max length in separatedCipher
    //for each char in key
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < cipherChunkByKey.get(0).size(); i++) { //max length in split text
      for (int j = 0; j < cipherChunkByKey.size(); j++) {//each list in split text
        sb.append(cipherChunkByKey.get(j).get(i)); //
      }
    }

    return sb.toString();

  }

  private static List<Integer> convertKeyToInteger(String key) {
    List<Integer> keyAsIntArray = new ArrayList<>(key.length());

    //convert all keys to numbers
    for (Character c : key.toUpperCase().toCharArray()) {
      keyAsIntArray.add(c - 'A');
    }

    //find lowest duplicate
    Integer lowestDuplicate = findLowestDuplicate(keyAsIntArray);

    while (lowestDuplicate != null) {
      boolean firstDuplicateFound = false;
      for (int i = 0; i < keyAsIntArray.size(); i++) {
        //if the current int is the lowest duplicate and the first duplicate hasn't been found
        if (keyAsIntArray.get(i).equals(lowestDuplicate) && !firstDuplicateFound) {
          //Don't update the first duplicate but mark the first duplicate as found
          firstDuplicateFound = true;
        } else if (keyAsIntArray.get(i) >= lowestDuplicate) {
          keyAsIntArray.set(i, keyAsIntArray.get(i) + 1);
        }
      }

      lowestDuplicate = findLowestDuplicate(keyAsIntArray);
    }

    return keyAsIntArray;
  }

  private static Integer findLowestDuplicate(List<Integer> integers) {
    Set<Integer> setToReturn = new HashSet<>();
    Set<Integer> setToNotReturn = new HashSet<>();

    for (Integer i: integers) { //for all the ints
      if (!setToNotReturn.add(i)) { //attempt to add the current int to the set, if it already exists in the set, Set.add returns false
        setToReturn.add(i); //if the int is already in the other set, add it to this set
      }
    }
    //return the lowest number in the set
    return setToReturn.stream().sorted().findFirst().orElse(null);
  }
}
