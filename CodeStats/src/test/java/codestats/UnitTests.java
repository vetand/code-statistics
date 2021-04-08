package codestats;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import java.io.File;
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
    assertEquals(result.size(), 4);
    assertEquals(result.get("Total lines with comments"), "3");
    assertEquals(result.get("Total single-line comments"), "3");
    assertEquals(result.get("Empty single-line comments"), "1");
    assertEquals(result.get("Total multi-line comments"), "0");

    report = statMaker.collect("src/main/resources/Root/code-example-with-scope-comments.cpp");
    result = report.getStats();
    assertEquals(result.size(), 4);
    assertEquals(result.get("Total lines with comments"), "16");
    assertEquals(result.get("Total single-line comments"), "5");
    assertEquals(result.get("Empty single-line comments"), "0");
    assertEquals(result.get("Total multi-line comments"), "8");
  }
  @Test
  public void testProjectTreeFilesDIR() {
    ProjectTree tree = new ProjectTree("src/main/resources/Root");
    List<String> files = tree.getValidFiles();
    Iterator<String> iter = files.iterator();
    assertEquals(files.size(), 3);
    assertEquals(iter.next(), new File("src/main/resources/Root/code-example-with-scope-comments.cpp").getPath());
    assertEquals(iter.next(), new File("src/main/resources/Root/code-example.cpp").getPath());
    assertEquals(iter.next(), new File("src/main/resources/Root/Subdir/another-example.java").getPath());
  }

  @Test
  public void testProjectTreeStructure() {
    ProjectTree tree = new ProjectTree("src/main/resources/Root");
    String structure = tree.getProjectTreeReport();
    assertEquals(structure, "Root/\n"
                                + "├── code-example-with-scope-comments.cpp\n"
                                + "├── code-example.cpp\n"
                                + "└── Subdir/\n"
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