package codestats;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Считает дупликации констант в файле.
 * На данный момент поддерживаются целочисленные константы.
 */
public class CollectConstants extends Statistics {
  private Map<Integer, Integer> constantCounter = new HashMap<>();
  private final Pattern integerPattern = Pattern.compile("(?<![\\w\\\\.])\\d+(?![\\w\\\\.])");  // 100
  private final Pattern exponentPattern = Pattern.compile("(?<![\\w\\\\.])(\\d+[eE]\\d+)(?![\\w\\\\.])");  // 10e3
  private final int N_CONSTANTS = 5;

  public CollectConstants() {
    super("constants");
  }

  @Override
  public Report collect(String fileName) {
    try (Stream<String> lines = Files.lines(Paths.get(fileName), StandardCharsets.UTF_8)) {
      constantCounter.clear();
      lines.forEachOrdered(this::parseLine);
    } catch (IOException e) {
      e.printStackTrace();
    }

    SimpleReport result = new SimpleReport();

    Map<Integer, Integer> sortedCount = sortCounter();
    for (Map.Entry<Integer, Integer> entry : sortedCount.entrySet()) {
      Integer key = entry.getKey(), count = entry.getValue();
      if (count <= 1) {
        continue;
      }
      result.addStat("Constant " + key, count + " occurrences");
    }

    result.addStat("Total number of constants", String.valueOf(constantCounter.size()));
    result.addStat("Number of duplicated constants", String.valueOf(countDuplicated()));
    return result;
  }

  private void parseLine(String line) {
    String filteredLine = replaceStrings(line);
    // System.out.println(filteredLine);

    Matcher integerMatcher = integerPattern.matcher(filteredLine);
    while (integerMatcher.find()) {
      int num = Integer.parseInt(integerMatcher.group());
      int count = constantCounter.getOrDefault(num, 0);
      constantCounter.put(num, count + 1);
    }
  }

  private static String replaceStrings(String line) {
    line = line.replaceAll("\\\\\"", ".");
    return line.replaceAll("\"[^\"]*\"", "\"...\"");
  }

  private Map<Integer, Integer> sortCounter() {
    // https://stackoverflow.com/a/23846961
    return constantCounter.entrySet().stream()
        .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
        .limit(N_CONSTANTS)
        .collect(Collectors.toMap(
            Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, HashMap::new));
  }

  private int countDuplicated() {
    int count = 0;
    for (int value : constantCounter.values()) {
      if (value > 1) {
        count++;
      }
    }
    return count;
  }
}
