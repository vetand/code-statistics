package codestats;

import java.util.HashMap;

public class ProjectReport extends Report {

  private HashMap<String, Report> fileStats;

  public ProjectReport() {
    super();
    fileStats = new HashMap<String, Report>();
  }

  public HashMap<String, Report> getFileReports() {
    return fileStats;
  }

  public void addFileReport(String fileName, Report fileReport) {
    if (fileStats.containsKey(fileName)) {
      fileReport.getStats().entrySet().forEach(entry -> {
        if (fileStats.get(fileName).getStats().containsKey(entry.getKey())) {
          fileStats.get(fileName).getStats().put(entry.getKey(), Integer.toString(
                  Integer.parseInt(fileStats.get(fileName).getStats().get(entry.getKey()))
                          + Integer.parseInt(entry.getValue())));
        } else { // values should be ints in string format!
          fileStats.get(fileName).getStats().put(entry.getKey(), entry.getValue());
        }
      });
    } else {
      fileStats.put(fileName, fileReport);
    }

    fileReport.getStats().entrySet().forEach(entry -> {
      if (stats.containsKey(entry.getKey())) {
        stats.put(entry.getKey(), Integer.toString(
                Integer.parseInt(stats.get(entry.getKey()))
                        + Integer.parseInt(entry.getValue())));
      } else { // values should be ints in string format!
        stats.put(entry.getKey(), entry.getValue());
      }
    });
  }
}
