package codestats;

// ЭТО ПОДОПЫТНЫЙ, НЕ ИСХОДНИК !!
public class CollectCommentLines extends Statistics {

  public CollectCommentLines() {
  }

  public Report collect(String fileName) {
    // офк стереть эту заглушку
    SimpleReport result = new SimpleReport();
    result.addStat("Total comment lines", "3");
    result.addStat("Total empty comment lines", "1");
    result.addStat("Total filled comment lines", "2");
    return result;
  }
}