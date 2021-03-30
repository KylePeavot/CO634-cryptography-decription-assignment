package main.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class FrequencyUtils {

  public static final String beeMovieScript = getBeeMovieScript();

  public static char mostCommonCharacter(String stringToAnalyse) {
    return charactersByFrequency(stringToAnalyse).get(0);
  }

  public static List<Character> charactersByFrequency(String stringToAnalyse) {
    return frequencyAnalysis(stringToAnalyse).entrySet().stream()
        .sorted((o1, o2) -> {
          if (o1.getValue() < o2.getValue()) return 1;
          else if (o1.getValue() > o2.getValue()) return -1;
          else return 0;
        })
        .map(Entry::getKey)
        .collect(Collectors.toList());
  }

  public static HashMap<Character, Integer> frequencyAnalysis(String stringToAnalyse) {
    HashMap<Character, Integer> frequencyMap = new HashMap<>();
    for (char c : stringToAnalyse.toCharArray()) {
      if (frequencyMap.containsKey(c)) {
        frequencyMap.put(c, frequencyMap.get(c) + 1);
      } else {
        frequencyMap.put(c, 1);
      }
    }
    return frequencyMap;
  }

  public static String getBeeMovieScript() {
    try {
      return Files.readString(Path.of("./src/resources/statisticalHelpers/beeMovieScript.txt"));
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }
}
