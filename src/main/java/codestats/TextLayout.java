package codestats;

import java.util.Map;
import java.util.List;
import java.util.Arrays;

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
    List<String> groups = Arrays.asList("comments", "occurrences", "Declarations");
    if (showTree) {
      res += tree.getProjectTreeReport() + "\n\n";
    }

    if (mode.base()) {
      res += "\t\t\tWHOLE PROJECT REPORT:\n\n";
      for (String group : groups) {
        boolean wasGroup = false;
        for (Map.Entry<String, String> stat : report.getSortedStats()) {
          if (stat.getKey().contains(group)) {
            res += (stat.getKey() + ": " + stat.getValue() + "\n");
            wasGroup = true;
          }
        }
        if (wasGroup) {
          res += "\n";
        }
      }
    } else {
      res += "\t\t\tREPORT FOR EACH FILE:\n\n";
      for (Map.Entry<String, Report> file : report.getFileReports().entrySet()) {
        res += (file.getKey() + ":\n");
        for (String group : groups) {
          boolean wasGroup = false;
          for (Map.Entry<String, String> stat : file.getValue().getSortedStats()) {
            if (stat.getKey().contains(group)) {
              res += ("\t" + stat.getKey() + ": " + stat.getValue() + "\n");
              wasGroup = true;
            }
          }
          if (wasGroup) {
            res += "\n";
          }
        }
        res += "\n";
      }
    }
    return res;
  }
}
