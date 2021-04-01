package main.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class CommonWordsUtils {

  public static final List<String> COMMON_WORDS = getCommonWordsFromFile();
  public static final List<String> COMMON_WORDS_HALF = getHalfOfCommonWordsFromFile();

  /**
   * get full list of the common words
   * @return a list of the words in commonWords.txt
   */
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

  /**
   * get a smaller list of the common words for performance reasons
   * @return a list of the first 5000 words in commonWords.txt
   */
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

  /**
   * get common words bigger than a given length
   * @param length all words below this size will be filtered out
   * @return a list of common words longer than length
   */
  public static List<String> getCommonWordsGreaterThanLength(int length) {
    return COMMON_WORDS.stream().filter(s -> s.length() > length).collect(Collectors.toList());
  }
}
