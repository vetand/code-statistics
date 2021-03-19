package codestats;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class UnitTests {
  @Test
  public void testCollectCommentLines() {
    CollectCommentLines statMaker = new CollectCommentLines();
    // мб имя файла неправильное, можно поменять
    Report report = statMaker.collect("src/main/resources/Root/code-example.cpp");
    HashMap<String, String> result = report.getStats();
    assertEquals(result.size(), 3);
    assertEquals(result.get("Total comment lines"), "3");
    assertEquals(result.get("Total empty comment lines"), "1");
    assertEquals(result.get("Total filled comment lines"), "2");
  }
  @Test
  public void testProjectTreeFilesDIR() {
    ProjectTree tree = new ProjectTree("src/main/resources/Root");
    // мб имя файла неправильное, можно поменять
    List<String> files = tree.getValidFiles();
    Iterator<String> iter = files.iterator();
    assertEquals(files.size(), 2);
    // мб имя файла неправильное, можно поменять
    assertEquals(iter.next(), "src/main/resources/Root/code-example.cpp");
    assertEquals(iter.next(), "src/main/resources/Root/Subdir/another-example.java");
  }

  @Test
  public void testProjectTreeStructure() {
    ProjectTree tree = new ProjectTree("src/main/resources/Root");
    // мб имя файла неправильное, можно поменять
    String structure = tree.getProjectTreeReport();
    assertEquals(structure, "Root\n"
                                + "├── code-example.cpp\n"
                                + "└── Subdir\n"
                                + "    └── another-example.java\n");
  }

  @Test
  public void testSimpleTextLayout() {
    SimpleReport report = new SimpleReport();
    ProjectTree tree = new ProjectTree("src/main/resources/Root");
    TextLayout layout = new TextLayout();
    String result = layout.toString(report, tree);
    assertEquals(result, "code-example.cpp:\n"
                              + "    Total comment lines: 3\n"
                              + "    Total empty comment lines: 1\n"
                              + "    Total filled comment lines: 2\n");
  }
}