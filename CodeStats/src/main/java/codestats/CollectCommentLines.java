package codestats;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

/**
 * Считает статистику по комментариям в C-like коде:
 * количество однострочных комментариев (C++ style),
 * а также многострочных (C style) и их суммарную длину.
 * Например, для однострочного кода с тремя C style комментариями суммарная длина 3.
 * Политика по переносам C++ style: игнорируются.
 */
public class CollectCommentLines extends Statistics {
  private int lineCommentCount = 0;  // count comment lines starting with //
  private int scopeCommentCount = 0;  // count /*...*/ scopes
  private int scopeCommentLenSum = 0;  // count total lines within /*...*/ scopes
  private int linesWithCommentCount = 0;  // count lines featuring any comment
  private int emptyLineCommentCount = 0;
  private boolean isScopeCommentOpen = false;
  private boolean isStringOpen = false;

  public CollectCommentLines() {
    super("comments");
  }

  public Report collect(String fileName) {
    try (Stream<String> lines = Files.lines(Paths.get(fileName), StandardCharsets.UTF_8)) {
      lines.forEachOrdered(this::parseLine);
    } catch (IOException e) {
      e.printStackTrace();
    }

    SimpleReport result = new SimpleReport();
    result.addStat("Total lines with comments", String.valueOf(linesWithCommentCount));
    result.addStat("Total single-line comments", String.valueOf(lineCommentCount));
    result.addStat("Empty single-line comments", String.valueOf(emptyLineCommentCount));
    result.addStat("Total multi-line comments", String.valueOf(scopeCommentCount));
    lineCommentCount = emptyLineCommentCount = scopeCommentCount = scopeCommentLenSum = linesWithCommentCount = 0;
    isScopeCommentOpen = isStringOpen = false;
    return result;
  }

  private void parseLine(String line) {
    String trimmedLine = line.trim();
    boolean lineFeaturesComment = false;
    if (isScopeCommentOpen) {
      scopeCommentLenSum++;
      lineFeaturesComment = true;
    }
    char prev = ' ';
    for (int i = 0; i < trimmedLine.length(); i++) {
      char cur = trimmedLine.charAt(i);
      if (isStringOpen) {
        // comment tokens inside strings mustn't count
        if (cur == '"' && prev != '\\') {
          isStringOpen = false;
        }
      } else if (!isScopeCommentOpen && cur == '"') {
        // string starts
        isStringOpen = true;
      } else if (!isScopeCommentOpen && prev == '/' && cur == '*') {
        // multiline comment starts
        isScopeCommentOpen = true;
        lineFeaturesComment = true;
        scopeCommentCount++;
        scopeCommentLenSum++;
      } else if (isScopeCommentOpen && prev == '*' && cur == '/') {
        // multiline comment ends
        isScopeCommentOpen = false;
      } else if (!isScopeCommentOpen && prev == '/' && cur == '/') {
        // single-line comment starts (and goes to the end of the line)
        lineFeaturesComment = true;
        lineCommentCount++;
        if (i + 1 == trimmedLine.length()) {
          emptyLineCommentCount++;
        }
        break;
      }
      prev = cur;
    }
    if (lineFeaturesComment) {
      linesWithCommentCount++;
    }
  }

  // некорректно работает на строке: /*abc "*/"
  private void parseLineRuleBase(String line) {
    // first remove strings ("") from line
    String filteredLine = line.replaceAll("\"", "");  // for further string replacements
    filteredLine = filteredLine.replaceAll("\"[^\"]*\"", "\"...\"");  // replacing strings with "..."

    // filter from previous scopes
    if (isScopeCommentOpen && filteredLine.contains("*/")) {
      scopeCommentLenSum++;
      filteredLine = filteredLine.substring(filteredLine.indexOf("*/") + 2);
      isScopeCommentOpen = false;
    }

    // now // and /*...*/ are surely outside strings and indicate comments
    int singleLineStart = filteredLine.indexOf("//");
    int scopeStart = filteredLine.indexOf("/*");
    if (singleLineStart == -1 && scopeStart == -1) {
      // no comments in this line
      return;
    }
    if (singleLineStart < scopeStart) {
      // a single line comment in the line
      lineCommentCount++;
      String trimmedLine = filteredLine.trim();
      if (singleLineStart + 2 == trimmedLine.length()) {
        emptyLineCommentCount++;
      }
    } else {
      // a scope starting in the line
      isScopeCommentOpen = true;
      scopeCommentCount++;
      scopeCommentLenSum++;
    }
  }
}
