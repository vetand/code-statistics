package codestats;

import java.util.Map;
import java.util.HashMap;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Comparator;

public abstract class Report {
  HashMap<String, String> stats;

  public Report() {
    stats = new HashMap<String, String>();
  }

  static private SortedSet<Map.Entry<String,String>> SortByValues(Map<String,String> map) {
    SortedSet<Map.Entry<String,String>> sorted = new TreeSet<Map.Entry<String,String>>(
            new Comparator<Map.Entry<String,String>>() {
              @Override public int compare(Map.Entry<String,String> s1, Map.Entry<String,String> s2) {
                int res = s1.getValue().compareTo(s2.getValue());
                if (s1.getKey().equals(s2.getKey())) {
                  return res;
                } else {
                  return res == 0 ? 1 : res;
                }
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
