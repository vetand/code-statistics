package codestats;

public interface Layout {
  public String toString(ProjectReport report,
                         ProjectTree tree,
                         Mode mode,
                         boolean showTree);
}
