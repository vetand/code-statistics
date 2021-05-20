package codestats;

import java.util.List;

public class Mode {
  final private String tag_;
  final List<Statistics> stats_;
  final Boolean showProjectTree;

  public Mode(String tag, List<Statistics> stats, Boolean showProjectTree) {
    tag_ = tag;
    stats_ = stats;
    this.showProjectTree = showProjectTree;
  }

  public String getTag() {
    return tag_;
  }

  public Boolean showTree() {
    return showProjectTree;
  }

  public Boolean base() {
    return tag_.equals("base");
  }

  public List<Statistics> getStats() {
    return stats_;
  }
}
