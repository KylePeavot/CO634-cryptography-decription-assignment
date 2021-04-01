package main;

import main.utils.CharUtils;

public class CaesarCipher {

  /**
   * decrypts cipherText by shifting each character left the amount specified by shift
   * @param cipherText the text to decrypt
   * @param shift the amount each character has been shifted by
   * @return
   */
  public static String decryptCaesarCipher(String cipherText, int shift) {
    cipherText = cipherText.toUpperCase(); //enforce cipher text is upper case

    StringBuilder shiftedText = new StringBuilder();

    shift = shift % 26; //no point doing extra shifts

    for (char c : cipherText.toCharArray()) {
      c = CharUtils.shiftChar(c, shift);

      shiftedText.append(c);
    }

    return shiftedText.toString();
  }
}
