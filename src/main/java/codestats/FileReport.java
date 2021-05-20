package codestats;

public class FileReport extends Report {

  public FileReport() {
    super();
  }

  public void addStatReport(Report statReport) {
    statReport.getStats().entrySet().forEach(entry -> {
      stats.put(entry.getKey(), entry.getValue());
    });
  }
}
