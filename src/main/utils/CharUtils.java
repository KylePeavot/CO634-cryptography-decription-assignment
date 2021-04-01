package main.utils;

import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class CharUtils {

  /**
   * shifts a character by a given amount
   * A, 2 -> C
   * Z, 1, -> A
   * @param characterToShift the character to shift
   * @param shift the amount to shift the character by
   * @return the shifted character
   */
  public static char shiftChar(char characterToShift, int shift) {
    characterToShift += shift;

    if (characterToShift < 'A') {
      characterToShift += 26;
    } else if (characterToShift > 'Z') {
      characterToShift -= 26;
    }

    return characterToShift;
  }

}
