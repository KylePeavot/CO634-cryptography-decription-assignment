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
  public static final double BEE_MOVIE_SCRIPT_IOC = calculateIndexOfCoincidence(BEE_MOVIE_SCRIPT);

  /**
   * returns the most common character in a string
   * @param stringToAnalyse the string to search
   * @return the most common character in stringToAnalyse
   */
  public static char mostCommonCharacter(String stringToAnalyse) {
    return charsOrderedByFrequency(stringToAnalyse).get(0);
  }

  /**
   * get a list of characters ordered by the frequency the appear in stringToAnalyse
   * @param stringToAnalyse the string to search
   * @return list of characters ordered by frequency
   */
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

  /**
   * get a full map of all the characters in a string and how often they occur
   * @param stringToAnalyse the string to analyse
   * @return a hashmap of character to frequency
   */
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

  /**
   * calculates the index of coincidence for a given string
   * https://en.wikipedia.org/wiki/Index_of_coincidence
   * @param text the text to analyise
   * @return the index of coincidence for text
   */
  public static double calculateIndexOfCoincidence(String text) {
    HashMap<Character, Integer> charsByFrequency = frequencyAnalysis(text);
    double indexOfCoincidence = 0.0;
    for (Integer frequencyOfCharacter : charsByFrequency.values()) {
      indexOfCoincidence += (frequencyOfCharacter * (frequencyOfCharacter - 1));
    }

    indexOfCoincidence = indexOfCoincidence / (text.length() * (text.length() - 1));

    return indexOfCoincidence;
  }

  /**
   * get the entire script of Bee Movie in all caps, no punctuation, no whitespace for analytical reasons
   * I had to use some sort of text to calculate the index of coincidence for the english language
   * @return the Bee Movie script
   */
  public static String getBeeMovieScript() {
    try {
      return Files.readString(Path.of("./src/resources/statisticalHelpers/beeMovieScript.txt"));
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }
}
