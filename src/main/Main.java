package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {

  public static void main(String[] args) {


  }

  public void caesarCypher(String cypherText) {


    
  }

  /*
    Checks if given string exists in top 10000 most common words
    String must be single word
   */
  public static boolean isStringRealWord(String stringToCheck) {
    try {
      File commonWords = new File("./src/resources/statisticalHelpers/commonWords.txt");
      Scanner myReader = new Scanner(commonWords);
      return myReader.findAll(stringToCheck.trim())
        .anyMatch(matchResult -> true);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
      return false;
    }
  }


}
