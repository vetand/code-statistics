package codestats;

public class ReportWriter {
  public void outputToConsole(Report report, ProjectTree tree) {
    TextLayout layout = new TextLayout();
    String printable = layout.toString(report, tree);
    System.out.println(printable);
  }
}
