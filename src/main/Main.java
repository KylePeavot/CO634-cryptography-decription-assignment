/**
 * @author Kyle Peavot (ksp5)
 */
package main;

import java.io.File;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Scanner;
import main.utils.CommonCharUtils;
import main.utils.FrequencyUtils;
import main.utils.WordUtils;

public class Main {

  //TODO ensure all methods commented
  public static void main(String[] args) {
    Instant start = Instant.now();

//    exercise1();
//    exercise2();
//    exercise3();
//    exercise4();

    Instant end = Instant.now();

    System.out.println("This took " + Duration.between(start, end).toMillis() + " milliseconds to decrypt");
  }

  public static void exercise1() {
    try {
      File exercise1 = new File("./src/resources/cypherTextFiles/cexercise1.txt");
      Scanner myReader = new Scanner(exercise1);

      String cypherText = myReader.nextLine();

      //get most common character
      char mostCommonCharacter = FrequencyUtils.mostCommonCharacter(cypherText);

      String shiftedText = "";
      boolean textDecrypted = false;

      for (char c : CommonCharUtils.mostCommonCharacters) {
        int currentShift = c - mostCommonCharacter;
        shiftedText = CaesarCypher.decryptCaesarCypher(cypherText, currentShift);

        if (WordUtils.doesTextContainRealWord(shiftedText)) {
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
      File exercise2 = new File("./src/resources/cypherTextFiles/cexercise2.txt");
      Scanner myReader = new Scanner(exercise2);

      String cypherText = myReader.nextLine();

      String decryptedText = VigenereCypher.decryptVigenereCypher(cypherText, "TESSOFTHEDURBERVILLES");

      printOutcome(decryptedText, true);

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  //TODO tidy up
  public static void exercise3() {
    try {
      //get the cypher text
      File exercise2 = new File("./src/resources/cypherTextFiles/cexercise3.txt");
      Scanner myReader = new Scanner(exercise2);
      String cypherText = myReader.nextLine();
      myReader.close();

      int keySize = 6;

      //Separate the cypher text into separate strings
      List<String> separatedCypherText = WordUtils.splitCypherTextIntoSeparateStrings(cypherText, keySize);

      //find the most common character in each string. This will be used to determine what character was used to encode each string
      List<Character> mostCommonCharactersInCypherText = new ArrayList<>();
      for (String s : separatedCypherText) {
        mostCommonCharactersInCypherText.add(FrequencyUtils.mostCommonCharacter(s));
      }

      boolean isTextDecrypted = false;
      String attemptedDecryption = "";
      String keyGuess = "";

      //Stores the (index of) the letter that each of the mostCommonCharactersInCypherText is being guessed as
      int[] letterGuesses = new int[keySize];

      //
      int currentLetterGuessNumber = 0;
      //used to limit the amount of letters we check
      int currentMaxBase = 5;
      HashSet<String> previousGuesses = new HashSet<>();

      while (!isTextDecrypted && currentMaxBase <= 26) {
        keyGuess = VigenereCypher.createNewKeyGuess(mostCommonCharactersInCypherText, letterGuesses);

        //attempt decryption
        attemptedDecryption  = VigenereCypher.decryptVigenereCypher(cypherText, keyGuess);

        //add the common letter guess to set of previous guesses
        //converted to a string so that previousGuesses.contains() works correctly
        previousGuesses.add(Arrays.toString(letterGuesses).replaceAll("\\[|\\]|,|\\s", ""));

        if (WordUtils.doesTextContainRealWord(attemptedDecryption, 10)) { //if deciphered
          isTextDecrypted = true;
        } else if (WordUtils.doesTextContainPartialWords(attemptedDecryption)) { //if we're close to the solution
          for (int i = 0; i < letterGuesses.length; i++) {
            HashMap<Integer, Integer> amountOfWordsForEachLetter = new HashMap<>();
            for (int commonLetterIndex = 0; commonLetterIndex < CommonCharUtils.mostCommonCharacters.length; commonLetterIndex++) {
              //update this letter guess
              letterGuesses[i] = commonLetterIndex;

              //create a new key
              keyGuess = VigenereCypher.createNewKeyGuess(mostCommonCharactersInCypherText, letterGuesses);

              //attempt decryption
              attemptedDecryption = VigenereCypher.decryptVigenereCypher(cypherText, keyGuess);

              //add the attempted letter guesses to previousGuesses
              previousGuesses.add(Arrays.toString(letterGuesses).replaceAll("\\[|\\]|,|\\s", ""));

              //put the amount of words this guess decrypted into the set
              amountOfWordsForEachLetter.put(commonLetterIndex, WordUtils.getAmountOfPartialAndRealWords(attemptedDecryption));
            }

            Entry<Integer, Integer> highestAmountOfWords = amountOfWordsForEachLetter.entrySet().stream()
                .sorted((o1, o2) -> o2.getValue().compareTo(o1.getValue()))
                .findFirst()
                .get();

            //replace the current letter guess with the new best letter guess
            letterGuesses[i] = highestAmountOfWords.getKey();
          }

          keyGuess = VigenereCypher.createNewKeyGuess(mostCommonCharactersInCypherText, letterGuesses);
          attemptedDecryption = VigenereCypher.decryptVigenereCypher(cypherText, keyGuess);
          //if this text contains enough real words, then it is probably decrypted
          if (WordUtils.doesTextContainRealWord(attemptedDecryption)) {
            isTextDecrypted = true;
          }
        } else { //if not deciphered
            while (previousGuesses.contains(Arrays.toString(letterGuesses).replaceAll("\\[|\\]|,|\\s", ""))) {
              //increment the number used to retrieve the common letter guesses
              currentLetterGuessNumber++;
              //If it's the max number in a base
              if (currentLetterGuessNumber == Math.pow(currentMaxBase, keySize)) {
                //increase the base
                currentMaxBase++;
                //reset the common letter guess
                currentLetterGuessNumber = 0;
              }

              String newMostCommonLetterGuessesString = Integer.toString(currentLetterGuessNumber, currentMaxBase);

              for (int i = newMostCommonLetterGuessesString.length() - 1; i >= 0; i--) {
                int shift = letterGuesses.length - newMostCommonLetterGuessesString.length();
                letterGuesses[i + shift] = newMostCommonLetterGuessesString.toCharArray()[i] - '0';
              }
            }
          }
        }
      printOutcome(attemptedDecryption, isTextDecrypted);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void exercise4() {
    try {
      //get the cypher text
      File exercise4 = new File("./src/resources/cypherTextFiles/cexercise4.txt");
      Scanner myReader = new Scanner(exercise4);
      String cypherText = myReader.nextLine();
      myReader.close();

      int keySize = VigenereCypher.workOutKeySizeForVigenere(cypherText, 4, 6);

      //Separate the cypher text into separate strings
      List<String> separatedCypherText = WordUtils.splitCypherTextIntoSeparateStrings(cypherText, keySize);

      //find the most common character in each string. This will be used to determine what character was used to encode each string
      List<Character> mostCommonCharactersInCypherText = new ArrayList<>();
      for (String s : separatedCypherText) {
        mostCommonCharactersInCypherText.add(FrequencyUtils.mostCommonCharacter(s));
      }

      boolean isTextDecrypted = false;
      String attemptedDecryption = "";
      String keyGuess = "";

      //Stores the (index of) the letter that each of the mostCommonCharactersInCypherText is being guessed as
      int[] letterGuesses = new int[keySize];

      //
      int currentLetterGuessNumber = 0;
      //used to limit the amount of letters we check
      int currentMaxBase = 5;
      HashSet<String> previousGuesses = new HashSet<>();

      while (!isTextDecrypted && currentMaxBase <= 26) {
        keyGuess = VigenereCypher.createNewKeyGuess(mostCommonCharactersInCypherText, letterGuesses);

        //attempt decryption
        attemptedDecryption  = VigenereCypher.decryptVigenereCypher(cypherText, keyGuess);

        //add the common letter guess to set of previous guesses
        //converted to a string so that previousGuesses.contains() works correctly
        previousGuesses.add(Arrays.toString(letterGuesses).replaceAll("\\[|\\]|,|\\s", ""));

        if (WordUtils.doesTextContainRealWord(attemptedDecryption, 10)) { //if deciphered
          isTextDecrypted = true;
        } else if (WordUtils.doesTextContainPartialWords(attemptedDecryption)) { //if we're close to the solution
          for (int i = 0; i < letterGuesses.length; i++) {
            HashMap<Integer, Integer> amountOfWordsForEachLetter = new HashMap<>();
            for (int commonLetterIndex = 0; commonLetterIndex < CommonCharUtils.mostCommonCharacters.length; commonLetterIndex++) {
              //update this letter guess
              letterGuesses[i] = commonLetterIndex;

              //create a new key
              keyGuess = VigenereCypher.createNewKeyGuess(mostCommonCharactersInCypherText, letterGuesses);

              //attempt decryption
              attemptedDecryption = VigenereCypher.decryptVigenereCypher(cypherText, keyGuess);

              //add the attempted letter guesses to previousGuesses
              previousGuesses.add(Arrays.toString(letterGuesses).replaceAll("\\[|\\]|,|\\s", ""));

              //put the amount of words this guess decrypted into the set
              amountOfWordsForEachLetter.put(commonLetterIndex, WordUtils.getAmountOfPartialAndRealWords(attemptedDecryption));
            }

            Entry<Integer, Integer> highestAmountOfWords = amountOfWordsForEachLetter.entrySet().stream()
                .sorted((o1, o2) -> o2.getValue().compareTo(o1.getValue()))
                .findFirst()
                .get();

            //replace the current letter guess with the new best letter guess
            letterGuesses[i] = highestAmountOfWords.getKey();
          }

          keyGuess = VigenereCypher.createNewKeyGuess(mostCommonCharactersInCypherText, letterGuesses);
          attemptedDecryption = VigenereCypher.decryptVigenereCypher(cypherText, keyGuess);
          //if this text contains enough real words, then it is probably decrypted
          if (WordUtils.doesTextContainRealWord(attemptedDecryption)) {
            isTextDecrypted = true;
          }
        } else { //if not deciphered
            while (previousGuesses.contains(Arrays.toString(letterGuesses).replaceAll("\\[|\\]|,|\\s", ""))) {
              //increment the number used to retrieve the common letter guesses
              currentLetterGuessNumber++;
              //If it's the max number in a base
              if (currentLetterGuessNumber == Math.pow(currentMaxBase, keySize)) {
                //increase the base
                currentMaxBase++;
                //reset the common letter guess
                currentLetterGuessNumber = 0;
              }

              String newMostCommonLetterGuessesString = Integer.toString(currentLetterGuessNumber, currentMaxBase);

              for (int i = newMostCommonLetterGuessesString.length() - 1; i >= 0; i--) {
                int shift = letterGuesses.length - newMostCommonLetterGuessesString.length();
                letterGuesses[i + shift] = newMostCommonLetterGuessesString.toCharArray()[i] - '0';
              }
            }
          }
        }
      printOutcome(attemptedDecryption, isTextDecrypted);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void printOutcome(String decryptedText, boolean success) {
    System.out.println("Decryption was a " + (success ? "success!" : "failure..."));
    System.out.println(decryptedText.substring(0, 30));
  }
}
