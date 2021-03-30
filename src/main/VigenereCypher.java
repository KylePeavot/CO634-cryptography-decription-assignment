package main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import main.utils.CharUtils;
import main.utils.CommonCharUtils;
import main.utils.FrequencyUtils;

public class VigenereCypher {

  public static String decryptVigenereCypher(String cypherText, String key) {
    int[] keyAlphabetNumber = Arrays.stream(key.chars().toArray())
        .map(charNumber -> charNumber - 'A').toArray();

    int keyPointer = 0;
    StringBuilder decryptedText = new StringBuilder();

    for(char c : cypherText.toCharArray()) {
      decryptedText.append(CharUtils.shiftChar(c, -keyAlphabetNumber[keyPointer]));

      keyPointer++;
      if(keyPointer > keyAlphabetNumber.length - 1) {
        keyPointer = 0;
      }
    }

    return decryptedText.toString();
  }

//  public static String


  public static String createNewKeyGuess(List<Character> mostCommonCharactersForStrings, int[] letterGuesses) {
    String newKeyGuess = "";
    for (int i = 0; i < mostCommonCharactersForStrings.size(); i++) {
      //guess what the cypher key is for each split string based on the distance between the most common character in the cypher text and the current guess at what it might be
      //e.g. if the most common char in a string is 'F', and we think that it is actually 'E', then the keyGuess for that letter would be 'B' as that is the letter required to shift the plaintext one up
      int lettersFromCommonLetter = (26 + (mostCommonCharactersForStrings.get(i) - CommonCharUtils.mostCommonCharacters[letterGuesses[i]])) % 26;
      newKeyGuess += CharUtils.shiftChar('A', lettersFromCommonLetter);
    }
    return newKeyGuess;
  }

  public static List<String> splitCypherTextIntoSeparateStrings(String stringToSplit, int amountOfLists) {
    var strings = new ArrayList<String>();
    strings.add(0, "");
    strings.add(1, "");
    strings.add(2, "");
    strings.add(3, "");
    strings.add(4 , "");
    strings.add(5, "");

    var charArrayToSplit = stringToSplit.toCharArray();

    for (int currentCharIndex = 0; currentCharIndex < stringToSplit.length(); currentCharIndex++) {
      strings.set(currentCharIndex % 6, strings.get(currentCharIndex % 6) + charArrayToSplit[currentCharIndex]);
    }

    return strings;
  }

  public static int workOutKeySize(String cypherText, int minKeySize, int maxKeySize) {
    HashMap<Character, Integer> charactersByFrequency =  FrequencyUtils.frequencyAnalysis(cypherText);

    for (int i = minKeySize; i < maxKeySize; i++) {
      //for each potential key size
      //split cypher text into List<String>
      //calculate index of coincidence for each string
      //calculate the average of all ioc
      //once done, find the ioc closest to 1.73


    }

    // if no key size is found
    System.out.println("No key size found");
    return -1;
  }
}
