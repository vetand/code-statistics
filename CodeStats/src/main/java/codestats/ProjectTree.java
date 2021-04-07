package codestats;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class ProjectTree {
  private final String head;

  public ProjectTree(String rootDirName) {
    head = rootDirName;
  }

  public List<String> getValidFiles() {
    try {
      return Files.walk(Paths.get(head))
              .filter(p -> isValidFile(p.toFile()))
              .map(Path::toString)
              .collect(Collectors.toList());
    } catch (IOException e) {
      System.out.println(e.toString());
    }
    return null;
  }

  public String getProjectTreeReport() {
    int indent = 0;
    File folder = new File(head);
    StringBuilder sb = new StringBuilder();

    sb.append(folder.getName());
    sb.append("/\n");

    getProjectTree(folder, indent, sb);

    return sb.toString();
  }

  private static boolean isValidFile(File file) {
    return file.exists() &&
            file.isFile() &&
            file.canRead() &&
            file.toString().matches(".*\\.(java$|hpp$|h$|H$|h++$|c$|cpp$|cxx$|cc$|c++$)");
  }

  private static void getProjectTree(File folder, int indent, StringBuilder sb) {
    File[] files = folder.listFiles();

    int counter = 0;

    for (File file : files) {
      if (file.isDirectory() || isValidFile(file)) {
        counter++;
      }
    }

    int now = 0;

    for (File file : files) {
      if (file.isDirectory()) {
        sb.append(getIndentString(indent));
        sb.append("└── ");
        sb.append(file.getName());
        sb.append("/\n");
        getProjectTree(file, indent + 1, sb);
        now++;
      } else if (isValidFile(file)) {
        sb.append(getIndentString(indent));
        if (now + 1 == counter) {
          sb.append("└── ");
        } else {
          sb.append("├── ");
        }
        sb.append(file.getName());
        sb.append("\n");
        now++;
      }
    }
  }

  private static String getIndentString(int indent) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < indent; i++) {
      sb.append("    ");
    }
    return sb.toString();
  }
}
