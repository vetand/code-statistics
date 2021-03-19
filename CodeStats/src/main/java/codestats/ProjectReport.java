package codestats;

public class ProjectReport extends Report {
  public ProjectReport() {
    super();
  }

  public void addFileReport(Report fileReport) {
    fileReport.getStats().entrySet().forEach(entry -> {
      if (stats_.containsKey(entry.getKey())) {
        stats_.put(entry.getKey(), Integer.toString(
            Integer.parseInt(stats_.get(entry.getKey()))
                + Integer.parseInt(entry.getValue())));
      } else { // values should be ints in string format!
        stats_.put(entry.getKey(), entry.getValue());
      }
    });
  }
}
