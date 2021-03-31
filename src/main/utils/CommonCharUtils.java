package main.utils;

import java.util.ArrayList;
import java.util.List;

public class CommonCharUtils {

  //Retrieved from http://pi.math.cornell.edu/~mec/2003-2004/cryptography/subs/frequencies.html
  public static final char[] mostCommonCharacters = new char[]{'E','T','A','O','I','N','S','R','H','D','L','U','C','M','F','Y','W','G','P','B','V','K','X','Q','J','Z'};
  //The above array could have been generated using tess26.txt but I thought it would be more fun to tackle these tasks as if I had no idea what they would be decrypted into

  //I have added space as the first character here as an assumption that it is the most common character - every single word in english must have it before and/or after
  public static final char[] mostCommonCharactersWithSpace = new char[]{' ', 'E','T','A','O','I','N','S','R','H','D','L','U','C','M','F','Y','W','G','P','B','V','K','X','Q','J','Z'};


  public static List<Character> getMostCommonCharactersWithSpaceToList() {
    List<Character> characters = new ArrayList<>();

    for (char c : mostCommonCharactersWithSpace) {
      characters.add(c);
    }

    return characters;
  }

}