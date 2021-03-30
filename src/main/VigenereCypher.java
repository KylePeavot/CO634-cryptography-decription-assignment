package main;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import main.utils.CharUtils;
import main.utils.CommonCharUtils;
import main.utils.FrequencyUtils;
import main.utils.WordUtils;

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

  public static int workOutKeySizeForVigenere(String cypherText, int minKeySize, int maxKeySize) {
    double currentBestIOC = 0.0;
    int keySizeWithBestIOC = 0;

    for (int i = minKeySize; i <= maxKeySize; i++) {
      //Split the cypherText into separate strings by the current guess at the key size
      List<String> splitText = WordUtils.splitCypherTextIntoSeparateStrings(cypherText, i);
      double averageIndexOfCoincidence = 0.0;

      //calculate index of coincidence for each string and then average it
      for (String s : splitText) {
        averageIndexOfCoincidence += FrequencyUtils.calculateIndexOfCoincidence(s);
      }
      averageIndexOfCoincidence = averageIndexOfCoincidence / i;

      //If the new index of coincidence is closer to a "normal" index of coincidence, then replace the best IOC values with the new ones
      if (Math.abs(averageIndexOfCoincidence - FrequencyUtils.BEE_MOVIE_SCRIPT_IOC) < Math.abs(currentBestIOC - FrequencyUtils.BEE_MOVIE_SCRIPT_IOC)) {
        currentBestIOC = averageIndexOfCoincidence;
        keySizeWithBestIOC = i;
      }
    }
    return keySizeWithBestIOC;
  }
}
