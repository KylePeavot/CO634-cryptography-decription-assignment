/**
 * @author Kyle Peavot (ksp5)
 */
package main;

import java.io.File;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.stream.Collectors;
import main.utils.CommonCharUtils;
import main.utils.FrequencyUtils;
import main.utils.WordUtils;

public class Main {

  public static void main(String[] args) {
    Instant start = Instant.now();

    exercise1();
    exercise2();
    exercise3();
    exercise4();
    exercise5();
    exercise6(); //Warning: long due to brute force
    exercise7();

    Instant end = Instant.now();

    System.out.println("This took " + Duration.between(start, end).toMillis() + " milliseconds to decrypt");
  }

  /**
   * Decrypts text encoded with Caesar cipher in cexercise1.txt
   */
  public static void exercise1() {
    String cipherText = getExerciseCipherText(1);

    //get most common character in the cipher text
    char mostCommonCharacter = FrequencyUtils.mostCommonCharacter(cipherText);

    String shiftedText = "";
    boolean textDecrypted = false;

    //for each letter in the alphabet from most common to least
    for (char c : CommonCharUtils.mostCommonCharacters) {
      //calculate the current shift needed to shift the most common character in the cipher text to the current most common character
      int currentShift = c - mostCommonCharacter;
      shiftedText = CaesarCipher.decryptCaesarCipher(cipherText, currentShift);

      //if decrypted, then set flag
      if (WordUtils.doesTextContainRealWord(shiftedText)) {
        textDecrypted = true;
      }

      if (textDecrypted) {
        break;
      }
    }
    //print the outcome
    printOutcome(shiftedText, textDecrypted);
  }

  /**
   * Decrypts text encoded with Vigenere cipher in cexercise2.txt where the key used is 'TESSOFTHEDURBERVILLES'
   */
  public static void exercise2() {
    String cipherText = getExerciseCipherText(2);

    //simply attempt decryption with the given key
    String decryptedText = VigenereCipher.decryptVigenereCipher(cipherText, "TESSOFTHEDURBERVILLES");

    //assume true because if not successful, something has gone horribly wrong
    printOutcome(decryptedText, true);
  }

  /**
   * Decrypts text encoded with Vigenere cipher in cexercise3.txt where the key used is an arbitrary combination of 6 characters
   */
  public static void exercise3() {
    //get the cipher text
    String cipherText = getExerciseCipherText(3);

    int keySize = 6;

    //Separate the cipher text into separate strings
    List<String> separatedCipherText = WordUtils.splitCipherTextIntoSeparateStrings(cipherText, keySize);

    //find the most common character in each string. This will be used to determine what character was used to encode each string
    List<Character> mostCommonCharactersInCipherText = new ArrayList<>();
    for (String s : separatedCipherText) {
      mostCommonCharactersInCipherText.add(FrequencyUtils.mostCommonCharacter(s));
    }

    //the flag
    boolean isTextDecrypted = false;
    //the current attempt at decryption
    String attemptedDecryption = "";
    //the current guess as to what the key is
    String keyGuess = "";

    //Stores the (index of) the letter that each of the mostCommonCharactersInCipherText is being guessed as
    //ints initialise with a value of 0 in Java
    int[] letterGuesses = new int[keySize];

    //for each of the letter guesses
    for (int i = 0; i < letterGuesses.length; i++) {
      HashMap<Integer, Integer> amountOfWordsForEachLetter = new HashMap<>();
      //swap the letter guess with each letter in the alphabet
      for (int commonLetterIndex = 0; commonLetterIndex < CommonCharUtils.mostCommonCharacters.length; commonLetterIndex++) {
        //update this letter guess
        letterGuesses[i] = commonLetterIndex;

        //create a new key
        keyGuess = VigenereCipher.createNewKeyGuess(mostCommonCharactersInCipherText, letterGuesses);

        //attempt decryption
        attemptedDecryption = VigenereCipher.decryptVigenereCipher(cipherText, keyGuess);

        //put the amount of words this guess decrypted into the set
        amountOfWordsForEachLetter.put(commonLetterIndex, WordUtils.getAmountOfPartialAndRealWords(attemptedDecryption));
      }

      //find the character that resulted in the highest amount of words when text was decrypted
      Entry<Integer, Integer> highestAmountOfWords = amountOfWordsForEachLetter.entrySet().stream().sorted((o1, o2) -> o2.getValue().compareTo(o1.getValue())).findFirst().get();

      //replace the current letter guess with the new best letter guess
      letterGuesses[i] = highestAmountOfWords.getKey();
    }

    //finally, create a new key based on all the best letters
    keyGuess = VigenereCipher.createNewKeyGuess(mostCommonCharactersInCipherText, letterGuesses);
    //attempt decryption
    attemptedDecryption = VigenereCipher.decryptVigenereCipher(cipherText, keyGuess);
    //if this text contains enough real words, then it is probably decrypted
    if (WordUtils.doesTextContainRealWord(attemptedDecryption)) {
      isTextDecrypted = true;
    }

    printOutcome(attemptedDecryption, isTextDecrypted);
  }

  /**
   * Decrypts text encoded with Vigenere cipher in cexercise4.txt where the key used is an arbitrary combination of 4 to 6 characters
   */
  public static void exercise4() {
    String cipherText = getExerciseCipherText(4);

    int keySize = VigenereCipher.workOutKeySizeForVigenere(cipherText, 4, 6);

    //Separate the cipher text into separate strings
    List<String> separatedCipherText = WordUtils.splitCipherTextIntoSeparateStrings(cipherText, keySize);

    //find the most common character in each string. This will be used to determine what character was used to encode each string
    List<Character> mostCommonCharactersInCipherText = new ArrayList<>();
    for (String s : separatedCipherText) {
      mostCommonCharactersInCipherText.add(FrequencyUtils.mostCommonCharacter(s));
    }

    //the flag
    boolean isTextDecrypted = false;
    //the current attempt at decryption
    String attemptedDecryption = "";
    //the current guess as to what the key is
    String keyGuess = "";

    //Stores the (index of) the letter that each of the mostCommonCharactersInCipherText is being guessed as
    //ints initialise with a value of 0 in Java
    int[] letterGuesses = new int[keySize];

    //for each of the letter guesses
    for (int i = 0; i < letterGuesses.length; i++) {
      HashMap<Integer, Integer> amountOfWordsForEachLetter = new HashMap<>();
      //swap the letter guess with each letter in the alphabet
      for (int commonLetterIndex = 0; commonLetterIndex < CommonCharUtils.mostCommonCharacters.length; commonLetterIndex++) {
        //update this letter guess
        letterGuesses[i] = commonLetterIndex;

        //create a new key
        keyGuess = VigenereCipher.createNewKeyGuess(mostCommonCharactersInCipherText, letterGuesses);

        //attempt decryption
        attemptedDecryption = VigenereCipher.decryptVigenereCipher(cipherText, keyGuess);

        //put the amount of words this guess decrypted into the set
        amountOfWordsForEachLetter.put(commonLetterIndex, WordUtils.getAmountOfPartialAndRealWords(attemptedDecryption));
      }

      //find the character that resulted in the highest amount of words when text was decrypted
      Entry<Integer, Integer> highestAmountOfWords = amountOfWordsForEachLetter.entrySet().stream().sorted((o1, o2) -> o2.getValue().compareTo(o1.getValue())).findFirst().get();

      //replace the current letter guess with the new best letter guess
      letterGuesses[i] = highestAmountOfWords.getKey();
    }

    //finally, create a new key based on all the best letters
    keyGuess = VigenereCipher.createNewKeyGuess(mostCommonCharactersInCipherText, letterGuesses);
    //attempt decryption
    attemptedDecryption = VigenereCipher.decryptVigenereCipher(cipherText, keyGuess);
    //if this text contains enough real words, then it is probably decrypted
    if (WordUtils.doesTextContainRealWord(attemptedDecryption)) {
      isTextDecrypted = true;
    }

    printOutcome(attemptedDecryption, isTextDecrypted);
  }

  /**
   * Decrypts text encoded with transposition cipher in cexercise5.txt where the text is encoded by reading the columns from left to right.
   * The number of columns is either 4, 5, or 6
   */
  public static void exercise5() {
    String cipherText = getExerciseCipherText(5);

    String decryptedText = "";
    boolean isTextDecrypted = false;

    int keySize = 4;
    int maxKeySize = 6;

    while (!isTextDecrypted && keySize < maxKeySize) {
      String keyGuess = "";

      for (int i = 0; i < keySize; i++) {
        keyGuess += (char) ('A' + i);
      }

      decryptedText = TranspositionCipher.decrypt(cipherText, keyGuess);

      if (WordUtils.doesTextContainRealWord(decryptedText)) {
        isTextDecrypted = true;
      } else {
        keySize++;
      }
    }

    printOutcome(decryptedText, isTextDecrypted);

  }

  /**
   * Decrypts text encoded with transposition cipher in cexercise6.txt where the text is encoded with a 6 character long key and encoded normally
   */
  public static void exercise6() {
    String cipherText = getExerciseCipherText(6);

    String decryptedText = "";
    boolean isTextDecrypted = false;

    int keySize = 6;
    char[] bruteForceArray = new char[6];

    for (int i = 0; i < keySize; i++) {
      bruteForceArray[i] = (char) ('A' + i);
    }

    //CCABAF
    int maxCombinations = (int) Math.pow(keySize, keySize);
    for (int i = 9000; i < maxCombinations; i++) {
      StringBuilder keyGuess = new StringBuilder();
      List<Integer> newKey = new ArrayList<>();
      List<Integer> newNum = Integer.toString(i, 6).chars().mapToObj(value -> value - '0').collect(Collectors.toList());

      for (int j = 0; j < (keySize - newNum.size()); j++) {
        newKey.add(0);
      }

      newKey.addAll(newNum);

      for (Integer index : newKey) {
        keyGuess.append(bruteForceArray[index]);
      }

      decryptedText = TranspositionCipher.decrypt(cipherText, keyGuess.toString());

      if (WordUtils.doesTextContainRealWord(decryptedText, 10)) {
        isTextDecrypted = true;
        break;
      }
    }
    printOutcome(decryptedText, isTextDecrypted);

  }

  /**
   * Decrypts text encoded with a general substitution cipher in cexercise7.txt
   */
  public static void exercise7() {
    String cipherText = getExerciseCipherText(7);

    List<Character> charsInEnglishByFrequency = CommonCharUtils.getMostCommonCharactersWithSpaceToList();
    List<Character> charsInCipherTextByFrequency = FrequencyUtils.charsOrderedByFrequency(cipherText);

    HashMap<Character, Character> cipherCharToPlainChar = new HashMap<>();

    for (int i = 0; i < charsInCipherTextByFrequency.size(); i++) {
      cipherCharToPlainChar.put(charsInCipherTextByFrequency.get(i), charsInEnglishByFrequency.get(i));
    }

    String decryptedText = "";
    boolean isTextDecrypted = false;
    HashSet<Character> correctCharacters = new HashSet<>();

    while (!isTextDecrypted && correctCharacters.size() < 26) {
      //attempt decryption
      decryptedText = SubstitutionCipher.decrypt(cipherText, cipherCharToPlainChar);

      if (WordUtils.doesTextContainRealWord(decryptedText, 5)) {
        isTextDecrypted = true;
        break;
      }

      //get a map of the two letters to swap mappings for
      Entry<Character, Character> charsToSwap = SubstitutionCipher.findCharactersToSwap(decryptedText, correctCharacters);

      //find the char that maps to N
      char charToSwap = cipherCharToPlainChar.entrySet().stream().filter(entry -> entry.getValue() == charsToSwap.getKey()).findFirst().get().getKey();
      //find the char that maps to H
      char otherCharToSwap = cipherCharToPlainChar.entrySet().stream().filter(entry -> entry.getValue() == charsToSwap.getValue()).findFirst().get().getKey();

      cipherCharToPlainChar.put(charToSwap, charsToSwap.getValue());
      cipherCharToPlainChar.put(otherCharToSwap, charsToSwap.getKey());

      //add charsToSwap.getValue to a list to track all chars that are definitely correct
      correctCharacters.add(charsToSwap.getValue());
    }

    printOutcome(decryptedText, isTextDecrypted);
  }

  /**
   * prints the first 30 characters and the outcome of a decryption
   * @param decryptedText the decrypted text
   * @param success whether decryption was a success or not
   */
  public static void printOutcome(String decryptedText, boolean success) {
    System.out.println("Decryption was a " + (success ? "success!" : "failure..."));
    System.out.println(decryptedText.substring(0, Math.min(decryptedText.length(), 30)));
  }

  /**
   * gets a given exercises cipher text for decription
   * @param exerciseNumber the number of the exercise to retrieve the cipher text for
   * @return the cipher text for the given exerciseNumber
   */
  public static String getExerciseCipherText(int exerciseNumber) {
    try {
      File exercise = new File("./src/resources/cipherTextFiles/cexercise" + exerciseNumber + ".txt");
      Scanner myReader = new Scanner(exercise);

      String cipherText = myReader.nextLine();

      return cipherText;

    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }
}
