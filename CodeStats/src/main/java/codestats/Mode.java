package codestats;

import java.util.List;

public class Mode {
  final private String tag_;
  final List<Statistics> stats_;

  public Mode(String tag, List<Statistics> stats) {
    tag_ = tag;
    stats_ = stats;
  }

  public String getTag() {
    return tag_;
  }

  public List<Statistics> getStats() {
    return stats_;
  }
}
