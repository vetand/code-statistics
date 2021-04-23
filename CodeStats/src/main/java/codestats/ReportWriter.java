package codestats;

public class ReportWriter {
  public void outputToConsole(ProjectReport report,
                              ProjectTree tree,
                              Mode mode, boolean showTree) {
    TextLayout layout = new TextLayout();
    String printable = layout.toString(report, tree, mode, showTree);
    System.out.println(printable);
  }
}
