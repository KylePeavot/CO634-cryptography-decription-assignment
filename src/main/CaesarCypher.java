package main;

import main.utils.CharUtils;

public class CaesarCypher {

  /*
  Shifts cypher text by a specified amount
 */
  public static String decryptCaesarCypher(String cypherText, int shift) {
    cypherText = cypherText.toUpperCase(); //enforce cypher text is upper case

    StringBuilder shiftedText = new StringBuilder();

    shift = shift % 26; //no point doing extra shifts

    for (char c : cypherText.toCharArray()) {
      c = CharUtils.shiftChar(c, shift);

      shiftedText.append(c);
    }

    return shiftedText.toString();
  }

}
