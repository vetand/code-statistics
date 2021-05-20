package codestats;

public abstract class Statistics {
  private final String tag_;

  public Statistics() {
    tag_ = "Undefined";
  }

  public Statistics(String tag) {
    tag_ = tag;
  }

  public String getTag() {
    return tag_;
  }

  public abstract Report collect(String fileName);

}
