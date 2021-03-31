package main;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import main.utils.CommonWordsUtils;

public class SubstitutionCypher {

  public static String decrypt(String cypherText, HashMap<Character, Character> cypherCharToPlainChar) {
    var cypherTextChars = cypherText.toCharArray();

    StringBuilder decryptedText = new StringBuilder();

    //attempt decryption
    for (char c : cypherTextChars) {
      decryptedText.append(cypherCharToPlainChar.get(c));
    }

    return decryptedText.toString();
  }

  //  call to function that returns Map<Character, Character>
  //  function looks through common words and tries to find word that has n-1/n letters in the word
  //  return Map<Letter currently in word, letter to be replaced to make word>
  public static Entry<Character, Character> findCharactersToSwap(String attemptedDecryption, HashSet<Character> correctCharacters) {
    List<String> attemptedDecryptionAsList = Arrays.asList(attemptedDecryption.split(" "));

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

  public static Integer findSingleDifferentCharacter(String a, String b) {
    Integer indexToReturn = null;

    boolean isDifferentCharFound = false;

    var aCharArray = a.toCharArray();
    var bCharArray = b.toCharArray();
    for (int i = 0; i < a.length(); i++) {
      if (aCharArray[i] != bCharArray[i]) { //if chars are different
        if (!isDifferentCharFound) { //if a different char has not been found already
          isDifferentCharFound = true;
          indexToReturn = i;
        } else { //else chars are different but a different char already found
          indexToReturn = null;
        }
      }
    }

    return indexToReturn;
  }
}
