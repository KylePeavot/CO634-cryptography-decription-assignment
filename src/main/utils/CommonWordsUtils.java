package main.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class CommonWordsUtils {

  public static final List<String> COMMON_WORDS = getCommonWordsFromFile();
  public static final List<String> COMMON_WORDS_HALF = getHalfOfCommonWordsFromFile();

  public static List<String> getHalfOfCommonWordsFromFile() {
    try {
      return Files.readAllLines(Path.of("./src/resources/statisticalHelpers/commonWords.txt")).stream()
          .filter(s -> s.length() > 1)
          .collect(Collectors.toList())
          .subList(0, 5000);
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }
  public static List<String> getCommonWordsFromFile() {
    try {
      return Files.readAllLines(Path.of("./src/resources/statisticalHelpers/commonWords.txt")).stream()
          .filter(s -> s.length() > 1)
          .collect(Collectors.toList());
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }

  public static List<String> getCommonWordsGreaterThanLength(int length) {
    return COMMON_WORDS.stream().filter(s -> s.length() > length).collect(Collectors.toList());
  }
}
