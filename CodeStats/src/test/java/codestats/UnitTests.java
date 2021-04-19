package codestats;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.io.File;
import java.util.HashMap;
import java.util.SortedMap;
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
    ProjectTree tree = new ProjectTree("src/main/resources/Root", true);
    List<String> files = tree.getValidFiles();
    Iterator<String> iter = files.iterator();
    assertEquals(files.size(), 3);
    assertEquals(iter.next(), new File("src/main/resources/Root/code-example.cpp").getPath());
    assertEquals(iter.next(), new File("src/main/resources/Root/code-example-with-scope-comments.cpp").getPath());
    assertEquals(iter.next(), new File("src/main/resources/Root/Subdir/another-example.java").getPath());
  }

  @Test
  public void testProjectTreeStructure() {
    ProjectTree tree = new ProjectTree("src/main/resources/Root", true);
    String structure = tree.getProjectTreeReport();
    assertEquals(structure, "Root/\n"
                                + "├── code-example.cpp\n"
                                + "├── code-example-with-scope-comments.cpp\n"
                                + "└── Subdir/\n"
                                + "    └── another-example.java\n");
  }

  @Test
  public void testSimpleTextLayoutFull() {
    ProjectReport PRreport = new ProjectReport();
    ProjectTree tree = new ProjectTree("src/main/resources/Root", true);
    TextLayout layout = new TextLayout();
    CollectCommentLines statMaker = new CollectCommentLines();
    Report report = statMaker.collect("src/main/resources/Root/Subdir/another-example.java");
    PRreport.addFileReport("src/main/resources/Root/Subdir/another-example.java", report);
    report = statMaker.collect("src/main/resources/Root/code-example.cpp");
    PRreport.addFileReport("src/main/resources/Root/code-example.cpp", report);
    String result = layout.toString(PRreport, tree, new ModeLibrary().getMode("full"));
    assertEquals(result, "Root/\n"
        + "├── code-example-with-scope-comments.cpp\n"
        + "├── code-example.cpp\n"
        + "└── Subdir/\n"
        + "    └── another-example.java\n\n\n"
        + "\t\t\tREPORT FOR EACH FILE:\n\n"
        + "src/main/resources/Root/Subdir/another-example.java:\n"
        + "\tEmpty single-line comments: 0\n"
        + "\tTotal multi-line comments: 0\n"
        + "\tTotal single-line comments: 2\n"
        + "\tTotal lines with comments: 2\n"
        + "\n"
        + "src/main/resources/Root/code-example.cpp:\n"
        + "\tTotal multi-line comments: 0\n"
        + "\tEmpty single-line comments: 1\n"
        + "\tTotal single-line comments: 3\n"
        + "\tTotal lines with comments: 3\n\n");
  }

  @Test
  public void testSimpleTextLayoutSummaryOnly() {
    ProjectReport PRreport = new ProjectReport();
    ProjectTree tree = new ProjectTree("src/main/resources/Root", true);
    TextLayout layout = new TextLayout();
    CollectCommentLines statMaker = new CollectCommentLines();
    Report report = statMaker.collect("src/main/resources/Root/Subdir/another-example.java");
    PRreport.addFileReport("src/main/resources/Root/Subdir/another-example.java", report);
    report = statMaker.collect("src/main/resources/Root/code-example.cpp");
    PRreport.addFileReport("src/main/resources/Root/code-example.cpp", report);
    String result = layout.toString(PRreport, tree, new ModeLibrary().getMode("base"));
    assertEquals(result,"\t\t\tWHOLE PROJECT REPORT:\n\n"
        + "Total multi-line comments: 0\n" +
            "Empty single-line comments: 1\n" +
            "Total single-line comments: 5\n" +
            "Total lines with comments: 5\n");
  }

  @Test
  public void testCollectConstants() {
    CollectConstants statMaker = new CollectConstants();
    Report report = statMaker.collect("src/main/resources/Root/code-example.cpp");
    HashMap<String, String> result = report.getStats();
    assertEquals(result.size(), 4);
    assertEquals(result.get("Constant 100"), "4 occurrences");
    assertEquals(result.get("Constant 200"), "2 occurrences");
    assertEquals(result.get("Total number of constants"), "3");
    assertEquals(result.get("Number of duplicated constants"), "2");
  }
}
