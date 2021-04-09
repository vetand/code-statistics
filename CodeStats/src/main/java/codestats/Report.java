package codestats;

import java.util.HashMap;

public abstract class Report {
  HashMap<String, String> stats_;

  public Report() {
    stats_ = new HashMap<String, String>();
  }

  public void addStat(String key, String value) {
    stats_.put(key, value);
  }

  public HashMap<String, String> getStats() {
    return stats_;
  }
}
