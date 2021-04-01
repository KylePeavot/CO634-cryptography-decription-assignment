package main;

import java.util.Arrays;
import java.util.List;
import main.utils.CharUtils;
import main.utils.CommonCharUtils;
import main.utils.FrequencyUtils;
import main.utils.WordUtils;

public class VigenereCipher {

  /**
   * decrypts text that has been encrypted with vigenere cipher
   * @param cipherText the text to decipher
   * @param key the key used to decipher
   * @return the decrypted text
   */
  public static String decryptVigenereCipher(String cipherText, String key) {
    int[] keyAlphabetNumber = Arrays.stream(key.chars().toArray())
        .map(charNumber -> charNumber - 'A').toArray();

    int keyPointer = 0;
    StringBuilder decryptedText = new StringBuilder();

    for(char c : cipherText.toCharArray()) {
      decryptedText.append(CharUtils.shiftChar(c, -keyAlphabetNumber[keyPointer]));

      keyPointer++;
      if(keyPointer > keyAlphabetNumber.length - 1) {
        keyPointer = 0;
      }
    }

    return decryptedText.toString();
  }

  /**
   * generates a key based on the difference between the most common character in a string and the character that it is guessed to be
   * @param mostCommonCharactersForStrings The most common character in each of the separated strings from the cipher text
   * @param letterGuesses The current guesses at what each of the characters in mostCommonCharactersForStrings is when decrypted
   * @return a string representation of the key required to translate each character in mostCommonCharactersForStrings to letterGuesses
   */
  public static String createNewKeyGuess(List<Character> mostCommonCharactersForStrings, int[] letterGuesses) {
    String newKeyGuess = "";
    for (int i = 0; i < mostCommonCharactersForStrings.size(); i++) {
      //guess what the cipher key is for each split string based on the distance between the most common character in the cipher text and the current guess at what it might be
      //e.g. if the most common char in a string is 'F', and we think that it is actually 'E', then the keyGuess for that letter would be 'B' as that is the letter required to shift the plaintext one up
      int lettersFromCommonLetter = (26 + (mostCommonCharactersForStrings.get(i) - CommonCharUtils.mostCommonCharacters[letterGuesses[i]])) % 26;
      newKeyGuess += CharUtils.shiftChar('A', lettersFromCommonLetter);
    }
    return newKeyGuess;
  }

  /**
   * works out the key size for a given vigenere cipher
   * @param cipherText the text to analyse
   * @param minKeySize the minimum key size
   * @param maxKeySize the maximum key size
   * @return the key size for a given vigenere cipher
   */
  public static int workOutKeySizeForVigenere(String cipherText, int minKeySize, int maxKeySize) {
    double currentBestIOC = 0.0;
    int keySizeWithBestIOC = 0;

    for (int i = minKeySize; i <= maxKeySize; i++) {
      //Split the cipherText into separate strings by the current guess at the key size
      List<String> splitText = WordUtils.splitCipherTextIntoSeparateStrings(cipherText, i);
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
