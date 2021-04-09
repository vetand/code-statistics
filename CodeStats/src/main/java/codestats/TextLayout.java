package codestats;

import java.util.Map;

public class TextLayout {
  public String PRtoString(ProjectReport report, ProjectTree tree) {
    String res = tree.getProjectTreeReport() + "\n\n" + "\t\t\tREPORT\n\t\t\t‾‾‾‾‾‾‾‾‾\n";
    for(Map.Entry<String, Report> file : report.getFileReports().entrySet()) {
      res += (file.getKey() + ":\n");
      for(Map.Entry<String, String> stat : file.getValue().getStats().entrySet()) {
        res += ("\t" + stat.getKey() + ": " + stat.getValue() + "\n");
      }
      res += "\n";
    }
    return res;
  }
}
