package main.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class WordUtils {

  public static boolean doesTextContainRealWord(String textToCheck) {
    return doesTextContainRealWord(textToCheck, 5);
  }

  /*
    Checks a scrolling window of 4 to 6 characters (avg length of top 5000 chars is 6.32) throughout the first 100 characters of the text
    If 3 real words are found, the text has (probably) been decoded
    If less than 3 real words found, text (almost certainly) has not been decoded
   */
  public static boolean doesTextContainRealWord(String textToCheck, int wordsRequired) {
    int wordsFound = 0;
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

  public static boolean doesTextContainPartialWords(String textToCheck) {
    int wordsFound = 0;
    for (int i = 4; i <= 6; i++) { //the size of the char window to check
      for (int currentCharStart = 0; currentCharStart < 101; currentCharStart++) { //loop through the first 100 characters of the text to check (to save time)
        if (isStringPartOfRealWord(textToCheck.substring(currentCharStart, currentCharStart + i).toLowerCase())) {
          if (wordsFound++ > 5) {
            return true;
          }
        }
      }
    }

    return false;
  }

  public static boolean isStringPartOfRealWord(String stringToCheck) {
    //too many 2 letter words where being found and making the count a bit too incorrect
    for (String s : CommonWordsUtils.COMMON_WORDS_HALF.stream().filter(s -> s.length() > 2).collect(Collectors.toList())) {
      if (s.contains(stringToCheck)) {
        return true;
      }
    }

    return false;
  }

  /*
    Checks if given string exists in top 10000 most common words
    String must be single word
   */
  public static boolean isStringRealWord(String stringToCheck) {
    for (String s : CommonWordsUtils.COMMON_WORDS_HALF) {
      if (stringToCheck.equals(s)) {
        return true;
      }
    }

    return false;
  }


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

  public static List<String> splitCypherTextIntoSeparateStrings(String stringToSplit, int amountOfLists) {
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

  public static List<String> splitCypherTextColumnWise(String stringToSplit, int numOfColumns) {
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

  public static List<String> findCorrectWordsInString(String stringToSearch) {
    List<String> stringToSearchList = Arrays.asList(stringToSearch.split(" "));
    List<String> commonWordsFound = new ArrayList<>();
    for (String s : CommonWordsUtils.getCommonWordsGreaterThanLength(2)) {
      if (stringToSearchList.contains(s.toUpperCase())) {
        commonWordsFound.add(s);
      }
    }
    return commonWordsFound;
  }

}
