package main;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import main.utils.WordUtils;

public class TranspositionCipher {

  /**
   * decrypts cipherText for a transposition cipher given a certain key
   * @param cipherText the text to decrypt
   * @param key the key to use for decryption
   * @return the decrypted text
   */
  public static String decrypt(String cipherText, String key) {
    //convert key to number
    List<Integer> keyAsIntArray = convertKeyToInteger(key);

    //Split cipher text into chunks
    List<String> separatedCipher = WordUtils.splitCipherTextColumnWise(cipherText, key.length());

    //TreeMaps are sorted by the key by default
    Map<Integer, List<Character>> cipherChunkByKey = new TreeMap<>();

    for (int i = 0; i < keyAsIntArray.size(); i++) {
      //turn each cipher chunk into a hashmap where the key is a character in the current key guess and the value is the cipher chunK
      ArrayList<Character> cipherChunkAsChars = new ArrayList<>();
      for (Character c : separatedCipher.get(i).toCharArray()) {
        cipherChunkAsChars.add(c);
      }

      cipherChunkByKey.put(keyAsIntArray.get(i), cipherChunkAsChars);
    }

    //for max length in separatedCipher
    //for each char in key
    StringBuilder sb = new StringBuilder();
    //max length in split text
    for (int i = 0; i < cipherChunkByKey.values().iterator().next().size(); i++) {
      //each list in split text
      for (List<Character> characters : cipherChunkByKey.values()) {
        sb.append(characters.get(i));
      }
    }

    return sb.toString();
  }

  /**
   * convert each letter in the key to it's int counterpart
   * A -> 0, B -> 1, etc.
   * @param key the key to convert
   * @return a list of integers representing the key
   */
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
        //for all other numbers greater than the lowest duplicate, increment them
        } else if (keyAsIntArray.get(i) >= lowestDuplicate) {
          keyAsIntArray.set(i, keyAsIntArray.get(i) + 1);
        }
      }

      lowestDuplicate = findLowestDuplicate(keyAsIntArray);
    }

    return keyAsIntArray;
  }

  /**
   * finds the lowest duplicate number in a list of integers
   * @param integers the list to search
   * @return the lowest duplicate
   */
  private static Integer findLowestDuplicate(List<Integer> integers) {
    Set<Integer> setToReturn = new HashSet<>();
    Set<Integer> setToNotReturn = new HashSet<>();

    //for all the ints
    for (Integer i: integers) {
      //attempt to add the current int to the set, if it already exists in the set, Set.add returns false
      if (!setToNotReturn.add(i)) {
        //if the int is already in the other set, add it to this set
        setToReturn.add(i);
      }
    }
    //return the lowest number in the set
    return setToReturn.stream().sorted().findFirst().orElse(null);
  }
}
