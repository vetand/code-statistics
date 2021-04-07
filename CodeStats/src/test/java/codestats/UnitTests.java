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
    List<String> files = tree.getValidFiles();
    Iterator<String> iter = files.iterator();
    assertEquals(files.size(), 2);
    assertEquals(iter.next(), "src/main/resources/Root/code-example.cpp");
    assertEquals(iter.next(), "src/main/resources/Root/Subdir/another-example.java");
  }

  @Test
  public void testProjectTreeStructure() {
    ProjectTree tree = new ProjectTree("src/main/resources/Root");
    String structure = tree.getProjectTreeReport();
    assertEquals(structure, "Root\n"
            + "├── code-example.cpp\n"
            + "└── Subdir\n"
            + "    └── another-example.java\n");
  }

  @Test
  public void testSimpleTextLayout() {
    ProjectReport PRreport = new ProjectReport();
    ProjectTree tree = new ProjectTree("src/main/resources/Root");
    TextLayout layout = new TextLayout();
    CollectCommentLines statMaker = new CollectCommentLines();
    Report report = statMaker.collect("src/main/resources/Root/Subdir/another-example.java");
    PRreport.addFileReport("src/main/resources/Root/Subdir/another-example.java", report);
    report = statMaker.collect("src/main/resources/Root/code-example.cpp");
    PRreport.addFileReport("src/main/resources/Root/code-example.cpp", report);
    String result = layout.PRtoString(PRreport, tree);
    assertEquals(result, "Root\n" +
            "├── code-example.cpp\n" +
            "└── Subdir\n" +
            "    └── another-example.java\n\n\n" +
            "\t\t\tREPORT\n" +
            "\t\t\t‾‾‾‾‾‾‾‾‾\n" +
            "src/main/resources/Root/Subdir/another-example.java:\n" +
            "\tTotal comment lines: 3\n" +
            "\tTotal empty comment lines: 1\n" +
            "\tTotal filled comment lines: 2\n\n" +
            "src/main/resources/Root/code-example.cpp:\n" +
            "\tTotal comment lines: 3\n" +
            "\tTotal empty comment lines: 1\n" +
            "\tTotal filled comment lines: 2\n\n");
  }
}
