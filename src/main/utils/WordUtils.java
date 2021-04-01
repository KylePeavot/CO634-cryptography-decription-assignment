package main.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class WordUtils {

  /**
   * returns true if textToCheck contains enough real/complete words
   * @param textToCheck the text to check
   * @return true if textToCheck contains real words, false if not
   */
  public static boolean doesTextContainRealWord(String textToCheck) {
    return doesTextContainRealWord(textToCheck, 5);
  }

  /**
   * returns true if textToCheck contains wordsRequired amount of real/complete words
   * @param textToCheck the text to check
   * @param wordsRequired the amount of words required to return true
   * @return true if textToCheck contains real words, false if not
   */
  public static boolean doesTextContainRealWord(String textToCheck, int wordsRequired) {
    int wordsFound = 0;
    //Checks a scrolling window of 4 to 6 characters (avg length of top 5000 chars is 6.32) throughout the first 100 characters of the text
    //If enough real words are found, the text has (probably) been decoded
    for (int i = 4; i <= 6; i++) { //the size of the char window to check
      for (int currentCharStart = 0; currentCharStart < 101; currentCharStart++) { //loop through the first 100 characters of the text to check (to save time)
        if (isStringRealWord(textToCheck.substring(currentCharStart, currentCharStart + i).toLowerCase())) {
          if (wordsFound++ > wordsRequired) {
            return true;
          }
        }
      }
    }
    return false;
  }

  /**
   * returns true if textToCheck contains words that are nearly correct/are part of a real word
   * @param textToCheck the text to check
   * @return true if textToCheck contains partial real words
   */
  public static boolean doesTextContainPartialWords(String textToCheck) {
    int wordsFound = 0;
    //the size of the char window to check
    for (int i = 4; i <= 6; i++) {
      //loop through the first 100 characters of the text to check (to save time)
      for (int currentCharStart = 0; currentCharStart < 101; currentCharStart++) {
        if (isStringPartOfRealWord(textToCheck.substring(currentCharStart, currentCharStart + i).toLowerCase())) {
          if (wordsFound++ > 5) {
            return true;
          }
        }
      }
    }
    return false;
  }


  /**
   * checks if the given string is nearly equal to a real word
   * @param stringToCheck the string to check
   * @return true if the stringToCheck is part of a real word
   */
  public static boolean isStringPartOfRealWord(String stringToCheck) {
    //too many 2 letter words where being found and making the count a bit too incorrect
    for (String s : CommonWordsUtils.COMMON_WORDS_HALF.stream().filter(s -> s.length() > 2).collect(Collectors.toList())) {
      if (s.contains(stringToCheck)) {
        return true;
      }
    }

    return false;
  }

  /**
   * checks if the given string is exactly equal to a real word
   * @param stringToCheck the string to check
   * @return true if the stringToCheck is a real word
   */
  public static boolean isStringRealWord(String stringToCheck) {
    for (String s : CommonWordsUtils.COMMON_WORDS_HALF) {
      if (stringToCheck.equals(s)) {
        return true;
      }
    }

    return false;
  }

  /**
   * gets the amount of partial words in textToCheck
   * @param textToCheck the text to check
   * @return the amount of partial words in textToCheck
   */
  public static int getAmountOfPartialAndRealWords(String textToCheck) {
    int wordsFound = 0;
    for (int i = 3; i <= 6; i++) { //the size of the char window to check
      for (int currentCharStart = 0; currentCharStart < 101; currentCharStart++) { //loop through the first 100 characters of the text to check (to save time)
        String currentWordToCheck = textToCheck.substring(currentCharStart, currentCharStart + i).toLowerCase();
        if (WordUtils.isStringRealWord(currentWordToCheck)) {
          wordsFound++;
        } else if (WordUtils.isStringPartOfRealWord(currentWordToCheck)) {
          wordsFound++;
        }
      }
    }
    return wordsFound;
  }

  /**
   * separates stringToSplit into separate lists
   * letters added sequentially so for stringToSplit = ABCDEFGH and amountOfLists = 2 the resulting lists would be {{A, C, E, G}, {B, D, F, H}}
   * @param stringToSplit the string to split
   * @param amountOfLists the amount of lists to return
   * @return return stringToSplit in amountOfLists lists
   */
  public static List<String> splitCipherTextIntoSeparateStrings(String stringToSplit, int amountOfLists) {
    List<String> strings = new ArrayList<>();
    for (int i = 0; i < amountOfLists; i++) {
      strings.add("");
    }

    var charArrayToSplit = stringToSplit.toCharArray();

    //for all the characters in stringToSplit
    for (int currentCharIndex = 0; currentCharIndex < stringToSplit.length(); currentCharIndex++) {
      //add the current character to the correct list
      strings.set(
          currentCharIndex % amountOfLists,
          //get the current value in the list and append the new character to it
          strings.get(currentCharIndex % amountOfLists) + charArrayToSplit[currentCharIndex]
      );
    }

    return strings;
  }

  /**
   * splits a string into numOfColumns parts but is different from splitCipherTextIntoSeparateStrings as the text is split in chunks
   * if stringToSplit = "ABCDEFGH" and numOfColumns = 2 the resulting lists would be {{A, B, C, D}, {E, F, G, H}}
   * @param stringToSplit the string to split
   * @param numOfColumns the amount of columns in the substitution cipher
   * @return return stringToSplit in numOfColumns lists
   * @return
   */
  public static List<String> splitCipherTextColumnWise(String stringToSplit, int numOfColumns) {
    int sizeOfNewStrings = stringToSplit.length() / numOfColumns;

    List<String> strings = new ArrayList<>();
    for (int i = 0; i < numOfColumns; i++) {
      strings.add("");
    }

    var charArrayToSplit = stringToSplit.toCharArray();

    for (int i = 0; i < numOfColumns; i++) {
      for (int j = 0; j < sizeOfNewStrings; j++) {
        strings.set(i, strings.get(i) + charArrayToSplit[(i * sizeOfNewStrings) + j]);
      }
    }

    return strings;
  }
}
