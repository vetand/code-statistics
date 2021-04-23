package codestats;

import java.util.Map;

public class TextLayout implements Layout {
  public String toString(ProjectReport report,
                         ProjectTree tree,
                         Mode mode,
                         boolean showTree) {
    String res = "";
    if (report.size() == 0) {
      res += "Project is empty.\n";
      return res;
    }
    if (showTree) {
      res += tree.getProjectTreeReport() + "\n\n";
    }

    if (mode.base()) {
      res += "\t\t\tWHOLE PROJECT REPORT:\n\n";
      for (Map.Entry<String, String> stat : report.getSortedStats()) {
        res += (stat.getKey() + ": " + stat.getValue() + "\n");
      }
    } else {
      res += "\t\t\tREPORT FOR EACH FILE:\n\n";
      for (Map.Entry<String, Report> file : report.getFileReports().entrySet()) {
        res += (file.getKey() + ":\n");
        for (Map.Entry<String, String> stat : file.getValue().getSortedStats()) {
          res += ("\t" + stat.getKey() + ": " + stat.getValue() + "\n");
        }
        res += "\n";
      }
    }
    return res;
  }
}
