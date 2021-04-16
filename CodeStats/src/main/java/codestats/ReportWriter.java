package codestats;

public class ReportWriter {
  public void outputToConsole(ProjectReport report,
                              ProjectTree tree,
                              Mode mode) {
    TextLayout layout = new TextLayout();
    String printable = layout.toString(report, tree, mode);
    System.out.println(printable);
  }
}
