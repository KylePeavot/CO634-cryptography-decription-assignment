package main.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class CommonWordsUtils {

  public static final List<String> commonWords = getCommonWordsFromFile();

  public static List<String> getCommonWordsFromFile() {
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
}
