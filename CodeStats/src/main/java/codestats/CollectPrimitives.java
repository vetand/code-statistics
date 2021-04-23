package codestats;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

/**
 * Вспомогательный класс: группа типов
 */
class TypeGroup {
  private List<String> types;
  private String name;
  private int count;

  TypeGroup(List<String> types, String name) {
    this.types = types;
    this.name = name;
    count = 0;
  }

  public boolean contains(String word) {
    for (String type : types) {
      if (word.equals(type)) {
        return true;
      }
    }
    return false;
  }

  public void add() {
    count++;
  }

  public String getName() {
    return name;
  }

  public List<String> getTypes() {
    return types;
  }

  public int getCount() {
    return count;
  }
}

/**
 * Вспомогательный класс: холдер для групп типов
 */
class TypeGroups {
  private List<TypeGroup> groups = new ArrayList<>();

  TypeGroups(boolean java) {
    groups.add(new TypeGroup(
        Arrays.asList(java ? "boolean" : "bool"),
        "booleans"
    ));
    groups.add(new TypeGroup(
        Arrays.asList("int", "byte", "short", "long", "char"),
        "integers"
    ));
    groups.add(new TypeGroup(
        Arrays.asList("float", "double"),
        "floats"
    ));
//    groups.add(new TypeGroup(
//        Arrays.asList(java ? "String" : "string"),
//        "strings"
//    ));
  }

  public boolean contains(String word) {
    for (TypeGroup group : groups) {
      if (group.contains(word)) {
        return true;
      }
    }
    return false;
  }

  public TypeGroup groupOf(String word) {
    for (TypeGroup group : groups) {
      if (group.contains(word)) {
        return group;
      }
    }
    return null;
  }

  public void add(String word) {
    for (TypeGroup group : groups) {
      if (group.contains(word)) {
        group.add();
        break;
      }
    }
  }

  public List<TypeGroup> getGroups() {
    return new ArrayList<>(groups);
  }

  public List<String> allTypes() {
    List<String> types = new ArrayList<>();
    for (TypeGroup group : groups) {
      types.addAll(group.getTypes());
    }
    return types;
  }
}

/**
 * Считает количество объявлений примитивных типов:
 * - булевых примитивов (bool/boolean в зависимости от расширения файла (.java или др.));
 * - целочисленных примитивов (int, byte, short, long, long long (для C), char);
 * - вещественных примитивов (float, double).
 * Массивы [], указатели *, ссылки &, касты и др. над примитивными типами не включаются.
 * Множественные присваивания (`double a = 1, b = 2;`) считаются как одно.
 */
public class CollectPrimitives extends Statistics {
  private TypeGroups typeGroups;
  private boolean java;
  private static final String stopCharacters = "([;,=(){}*&?:+-\\[\\]])";

  public CollectPrimitives() {
    super("primitives");
  }

  private void reinitialize(String fileName) {
    java = fileName.endsWith(".java");
    typeGroups = new TypeGroups(java);
  }

  @Override
  public Report collect(String fileName) {
    try (Stream<String> lines = Files.lines(Paths.get(fileName), StandardCharsets.UTF_8)) {
      reinitialize(fileName);
      lines.forEachOrdered(this::parseLine);
    } catch (IOException e) {
      e.printStackTrace();
    }

    SimpleReport result = new SimpleReport();
    for (TypeGroup group : typeGroups.getGroups()) {
      result.addStat("Declarations of " + group.getName(), String.valueOf(group.getCount()));
    }
    return result;
  }

  private static String replaceStrings(String line) {
    line = line.replaceAll("\\\\\"", ".");
    return line.replaceAll("\"[^\"]*\"", "\"...\"");
  }

  private static String replaceLongLongs(String line) {
    return line.replaceAll("long long(?>![\\w])", "long");
  }

  private static String removeSpaceBeforeStopCharacters(String line) {
    return line.replaceAll("[\\s]+" + stopCharacters, "$1");
  }

  private static String addSpaceAfterStopCharacters(String line) {
    return line.replaceAll(stopCharacters, "$1 ");
  }

  private static boolean isStopCharacter(String word) {
    return word.length() == 1 && stopCharacters.contains(word);
  }

  private void parseLine(String line) {
    String cleanLine = replaceStrings(line);
    cleanLine = removeSpaceBeforeStopCharacters(cleanLine);
    cleanLine = addSpaceAfterStopCharacters(cleanLine);
    if (!java) {
      cleanLine = replaceLongLongs(cleanLine);
    }

    String[] words = cleanLine.split("[\\s]+");

    for (String word : words) {
      if (typeGroups.contains(word)) {
        typeGroups.add(word);
      }
    }
  }
}
