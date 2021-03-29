package main.utils;

import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class CharUtils {

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
