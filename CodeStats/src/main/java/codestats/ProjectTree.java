package codestats;

import java.util.ArrayList;
import java.util.List;

public class ProjectTree {
  public ProjectTree(String rootDirName) {
  }

  // get all .c .cpp .java... files in directory
  public List<String> getValidFiles() {
    // офк стереть эту заглушку
    ArrayList<String> result = new ArrayList<String>();
    result.add("src/main/resources/Root/code-example.cpp");
    result.add("src/main/resources/Root/Subdir/another-example.java");
    return result;
  }

  public String getProjectTreeReport() {
    // офк стереть эту заглушку
    return "Root\n"
         + "├── code-example.cpp\n"
         + "└── Subdir\n"
         + "    └── another-example.java\n";
  }
}
