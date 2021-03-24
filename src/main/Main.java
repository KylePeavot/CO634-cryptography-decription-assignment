/**
 * @author Kyle Peavot (ksp5)
 */
package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Scanner;

public class Main {

  //Retrieved from http://pi.math.cornell.edu/~mec/2003-2004/cryptography/subs/frequencies.html
  private static final char[] mostCommonLetters = new char[]{'E','T','A','O','I','N','S','H','R','D','L','C','U','M','W','F','G','Y','P','B','V','K','J','X','Q','Z'};
  //The above array could have been generated using tess26.txt but I thought it would be more fun to tackle these tasks as if I had no idea what they would be decrypted into

  public static void main(String[] args) {
    exercise1();
    exercise2();
  }

  public static void exercise1() {
    try {
      File exercise1 = new File("./src/resources/cypherTextFiles/cexercise1.txt");
      Scanner myReader = new Scanner(exercise1);

      String cypherText = myReader.nextLine();

      //get most common character
      char mostCommonCharacter = mostCommonCharacter(cypherText);

      String shiftedText = "";
      boolean textDecrypted = false;

      for (char c : mostCommonLetters) {
        int currentShift = c - mostCommonCharacter;
        shiftedText = decryptCaesarCypher(cypherText, currentShift);

        if (doesTextContainRealWord(shiftedText)) {
          textDecrypted = true;
        }

        if (textDecrypted) {
          break;
        }
      }

      printOutcome(shiftedText, textDecrypted);

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void exercise2() {
    try {
      File exercise1 = new File("./src/resources/cypherTextFiles/cexercise2.txt");
      Scanner myReader = new Scanner(exercise1);

      String cypherText = myReader.nextLine();



    } catch (Exception e) {
      e.printStackTrace();
    }


  }

  public static void printOutcome(String decryptedText, boolean success) {
    System.out.println("Decryption was a " + (success ? "success!" : "failure..."));
    System.out.println(decryptedText.substring(0, 30));
  }

  /*
    Shifts cypher text by a specified amount
   */
  public static String decryptCaesarCypher(String cypherText, int shift) {
    cypherText = cypherText.toUpperCase(); //enforce cypher text is upper case

    StringBuilder shiftedText = new StringBuilder();

    shift = shift % 26; //no point doing extra shifts

    for (char c : cypherText.toCharArray()) {
      c += shift; //shift the character along the alphabet
      //if c has gone past Z, move it back 26 characters
      if (c < 'A') {
        c += 26;
      }
      shiftedText.append(c);
    }

    return shiftedText.toString();
  }
  
  public static void decryptVigenereCypher(String cypherText, String key) {



  }

  /*
    Checks a scrolling window of 4 to 6 characters (avg length of top 5000 chars is 6.32) throughout the first 100 characters of the text
    If 3 real words are found, the text has (probably) been decoded
    If less than 3 real words found, text (almost certainly) has not been decoded
   */
  public static boolean doesTextContainRealWord(String textToCheck) {
    int wordsFound = 0;
    for (int i = 4; i <= 6; i++) { //the amount of chars to check
      for (int currentCharStart = 0; currentCharStart < 101; currentCharStart++) {
        if (isStringRealWord(textToCheck.substring(currentCharStart, currentCharStart + i).toLowerCase())) {
          if (wordsFound++ > 5) {
            return true;
          }
        }
      }
    }
    return false;
  }

  /*
    Checks if given string exists in top 10000 most common words
    String must be single word
   */
  public static boolean isStringRealWord(String stringToCheck) {
    try {
      File commonWords = new File("./src/resources/statisticalHelpers/commonWords.txt");
      Scanner myReader = new Scanner(commonWords);

      while (myReader.hasNext()) {
        if (stringToCheck.equals(myReader.nextLine())) {
          return true;
        }
      }

      return false;
    } catch (FileNotFoundException e) {
      e.printStackTrace();
      return false;
    }
  }

  public static char mostCommonCharacter(String stringToAnalyse) {
    return frequencyAnalysis(stringToAnalyse).entrySet().stream()
        .sorted((o1, o2) -> {
          if (o1.getValue() < o2.getValue()) return 1;
          else if (o1.getValue() > o2.getValue()) return -1;
          else return 0;
        })
        .map(Entry::getKey)
        .findFirst().get();
  }

  public static HashMap<Character, Integer> frequencyAnalysis(String stringToAnalyse) {
    HashMap<Character, Integer> frequencyMap = new HashMap<>();
    for (char c : stringToAnalyse.toCharArray()) {
      if (frequencyMap.containsKey(c)) {
        frequencyMap.put(c, frequencyMap.get(c) + 1);
      } else {
        frequencyMap.put(c, 1);
      }
    }
    return frequencyMap;
  }

}
