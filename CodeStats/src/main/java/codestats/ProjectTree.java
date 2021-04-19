package codestats;

import picocli.CommandLine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class ProjectTree {
  private final String head;
  private final Set<String> isIgnored;
  private final List<String> validFiles;
  private final String treeProject;

  public ProjectTree(String rootDirName, boolean gitignore) {
    // Если папка заканчивается на /, то сразу убираем этот /
    if (rootDirName.endsWith("/")) {
      head = rootDirName.substring(0, rootDirName.length() - 1);
    } else {
      head = rootDirName;
    }

    // Парсим, есть ли в папке .gitignore
    if (gitignore) {
      isIgnored = parseGitIgnore();
    } else {
      isIgnored = new HashSet<>();
    }

    // Пока получаем дерево, обходим и заполняем валидфайлс
    validFiles = new ArrayList<>();
    treeProject = getTreeProject();
  }

  public List<String> getValidFiles() {
    return validFiles;
  }

  public String getProjectTreeReport() {
    return treeProject;
  }

  /**
   * Если гитигнор есть, то мы его парсим
   * Ожидается что в гитигноре локальные пути
   * Так как мы используем все файлы с префиксом head
   * То в isIgnored добавляем с таким же префиксом
   **/
  private Set<String> parseGitIgnore() {
    Set<String> isIgnored = new HashSet<>();
    try {
      File file = new File(head + "/.gitignore");
      FileReader fr = new FileReader(file);
      BufferedReader reader = new BufferedReader(fr);
      String line = reader.readLine();
      while (line != null) {
        isIgnored.add(head + "/" + line);
        line = reader.readLine();
      }
    } catch (Exception ignored) {
    }
    return isIgnored;
  }

  /**
   * Проверка на валидность нашего файла
   */
  private boolean isValidFile(File file) {
    return file.exists() &&
            file.isFile() &&
            file.canRead() &&
            file.toString().matches(".*\\.(java$|hpp$|h$|H$|h++$|c$|cpp$|cxx$|cc$|c++$)") &&
            !isIgnored.contains(file.toString());
  }

  /**
   * Добавляем пустой префикс для красоты дерева
   */
  private static String getIndentString(int indent) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < indent; i++) {
      sb.append("    ");
    }
    return sb.toString();
  }

  /**
   * Получаем дерево проекта
   */
  private String getTreeProject() {
    int indent = 0;
    File folder = new File(head);

    return folder.getName() +
            "/\n" +
            enumerateFiles(folder, indent);
  }

  /**
   * Рекурсивный переход папок
   */
  private String enumerateFiles(File folder, int indent) {
    File[] files = folder.listFiles();
    if (files == null) {
      return null;
    }

    StringBuilder sb = new StringBuilder();

    Arrays.sort(files, Collections.reverseOrder());

    int counter = 0;

    for (File file : files) {
      if (file.isDirectory() || isValidFile(file)) {
        counter++;
      }
    }

    int now = 0;

    for (File file : files) {
      if (file.isDirectory() && !file.getName().startsWith(".")) {
        String childs = enumerateFiles(file, indent + 1);
        if (childs == null) {
          continue;
        }
        sb.append(getIndentString(indent));
        sb.append("└── ");
        sb.append(file.getName());
        sb.append("/\n");
        sb.append(childs);
        now++;
      } else if (isValidFile(file)) {
        sb.append(getIndentString(indent));
        if (now + 1 == counter) {
          sb.append("└── ");
        } else {
          sb.append("├── ");
        }
        sb.append(file.getName());
        validFiles.add(file.toString());
        sb.append("\n");
        now++;
      }
    }

    String res = sb.toString();
    if (res.equals("")) {
      return null;
    }
    return res;
  }
}
