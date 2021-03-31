package main.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class FrequencyUtils {

  public static final String BEE_MOVIE_SCRIPT = getBeeMovieScript();
  public static final String BEE_MOVIE_SCRIPT_WITH_SPACES = getBeeMovieScriptWithSpaces();
  public static final double BEE_MOVIE_SCRIPT_IOC = calculateIndexOfCoincidence(BEE_MOVIE_SCRIPT);

  public static char mostCommonCharacter(String stringToAnalyse) {
    return charsOrderedByFrequency(stringToAnalyse).get(0);
  }

  public static List<Character> charsOrderedByFrequency(String stringToAnalyse) {
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

  public static double calculateIndexOfCoincidence(String text) {
    HashMap<Character, Integer> charsByFrequency = frequencyAnalysis(text);
    double indexOfCoincidence = 0.0;
    for (Integer frequencyOfCharacter : charsByFrequency.values()) {
      indexOfCoincidence += (frequencyOfCharacter * (frequencyOfCharacter - 1));
    }

    indexOfCoincidence = indexOfCoincidence / (text.length() * (text.length() - 1));

    return indexOfCoincidence;
  }

  public static String getBeeMovieScript() {
    try {
      return Files.readString(Path.of("./src/resources/statisticalHelpers/beeMovieScript.txt"));
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }

  public static String getBeeMovieScriptWithSpaces() {
    try {
      return Files.readString(Path.of("./src/resources/statisticalHelpers/beeMovieScriptWithSpaces.txt"));
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }
}
