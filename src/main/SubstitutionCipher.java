package main;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import main.utils.CommonWordsUtils;

public class SubstitutionCipher {

  /**
   * decrypts cipherText by mapping each character to the character in cipherCharToPlainChar
   * @param cipherText the text to decrypt
   * @param cipherCharToPlainChar the mapping for each encrypted character to it's decrypted counterpart
   * @return a decrypted string
   */
  public static String decrypt(String cipherText, HashMap<Character, Character> cipherCharToPlainChar) {
    var cipherTextChars = cipherText.toCharArray();

    StringBuilder decryptedText = new StringBuilder();

    //attempt decryption
    for (char c : cipherTextChars) {
      decryptedText.append(cipherCharToPlainChar.get(c));
    }

    return decryptedText.toString();
  }

  /**
   * searches through attemptedDecryption until a word is found that is one character off of being correct. Once found, return the character swap needed to make the word correct
   * @param attemptedDecryption the attempted decryption that has some words that are nearly correct
   * @param correctCharacters a set of characters that have already been swapped and are therefore correct
   * @return two characters to swap
   */
  public static Entry<Character, Character> findCharactersToSwap(String attemptedDecryption, HashSet<Character> correctCharacters) {
    List<String> attemptedDecryptionAsList = Arrays.asList(attemptedDecryption.split("\\|"));

    for (String commonWord : CommonWordsUtils.getCommonWordsGreaterThanLength(3)) {
      for (String partlyCorrectWord : attemptedDecryptionAsList) {
        if (commonWord.length() == partlyCorrectWord.length()) {
          if (!CommonWordsUtils.COMMON_WORDS.contains(partlyCorrectWord.toLowerCase())) { //if partly correct word isn't already a real word
            Integer singleDifferentCharacterIndex = findSingleDifferentCharacter(commonWord.toUpperCase(), partlyCorrectWord);

            if (singleDifferentCharacterIndex != null //if findSingleDifferentCharacter doesn't return null
                && !correctCharacters.contains(commonWord.toUpperCase().toCharArray()[singleDifferentCharacterIndex]) //and if the character hasn't already been swapped
                && !attemptedDecryptionAsList.contains(commonWord.toUpperCase())) { //and if the common word isn't already in the attempted decryption
              return Map.of(
                  partlyCorrectWord.toCharArray()[singleDifferentCharacterIndex],
                  commonWord.toUpperCase().toCharArray()[singleDifferentCharacterIndex]
              ).entrySet().iterator().next();
            }
          }
        }
      }
    }
    return null;
  }

  /**
   * compares correctWord and partialWord and returns the index of the character that is different
   * if more than one character different, return null
   * @param correctWord the correct word
   * @param partialWord the partial word
   * @return the index of the character that is different or null if more than one character is different
   */
  public static Integer findSingleDifferentCharacter(String correctWord, String partialWord) {
    Integer indexToReturn = null;

    boolean isDifferentCharFound = false;

    var correctWordCharArray = correctWord.toCharArray();
    var partialWordCharArray = partialWord.toCharArray();

    for (int i = 0; i < correctWord.length(); i++) {
      //if chars are different
      if (correctWordCharArray[i] != partialWordCharArray[i]) {
        //if correctWord different char has not been found already
        if (!isDifferentCharFound) {
          isDifferentCharFound = true;
          indexToReturn = i;
        //else chars are different but correctWord different char already found so set index to return to null
        } else {
          indexToReturn = null;
        }
      }
    }
    return indexToReturn;
  }
}
