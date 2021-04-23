package codestats;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

public abstract class Report {
  HashMap<String, String> stats;

  public Report() {
    stats = new HashMap<String, String>();
  }

  static private SortedSet<Map.Entry<String,String>> SortByValues(Map<String,String> map) {
    SortedSet<Map.Entry<String,String>> sorted = new TreeSet<>(
            (s1, s2) -> {
              int res = s1.getValue().compareTo(s2.getValue());
              if (s1.getKey().equals(s2.getKey())) {
                return res;
              } else {
                return res == 0 ? 1 : res;
              }
            });
    sorted.addAll(map.entrySet());
    return sorted;
  }

  public void addStat(String key, String value) {
    stats.put(key, value);
  }

  public HashMap<String, String> getStats() {
    return stats;
  }

  public SortedSet<Map.Entry<String,String>> getSortedStats() {
    return SortByValues(stats);
  }

  public int size() {
    return stats.size();
  }
}
