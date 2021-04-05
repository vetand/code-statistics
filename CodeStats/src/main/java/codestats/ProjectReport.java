package codestats;

import java.util.HashMap;

public class ProjectReport {

  HashMap<String, Report> fileStats_;

  public ProjectReport() {
    fileStats_ = new HashMap<String, Report>();
  }

  public HashMap<String, Report> getFileReports() {
    return fileStats_;
  }

  public void addFileReport(String fileName, Report fileReport) {
    if (fileStats_.containsKey(fileName)) {
      fileReport.getStats().entrySet().forEach(entry -> {
        if (fileStats_.get(fileName).getStats().containsKey(entry.getKey())) {
          fileStats_.get(fileName).getStats().put(entry.getKey(), Integer.toString(
                  Integer.parseInt(fileStats_.get(fileName).getStats().get(entry.getKey()))
                          + Integer.parseInt(entry.getValue())));
        } else { // values should be ints in string format!
          fileStats_.get(fileName).getStats().put(entry.getKey(), entry.getValue());
        }
      });
    } else {
      fileStats_.put(fileName, fileReport);
    }
  }
}
