package codestats;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.io.File;
import java.math.BigInteger;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.util.List;
import java.util.concurrent.Callable;

@Command(name = "collect-stat", mixinStandardHelpOptions = true, version = "1.0",
    description = "Collect statistics in given file")
class CollectStatCommand implements Callable<Integer> {

  @Parameters(index = "0", description = "The file whose statistics to calculate")
  private String rootDirPath;

  @Option(names = {"-m", "--mode"}, description = "--full --base")
  private String modeName = "full";

  static ModeLibrary library = new ModeLibrary();
  static ReportWriter reporter = new ReportWriter();

  @Override
  public Integer call() throws Exception {
    Mode mode = library.getMode(modeName);
    ProjectTree tree = new ProjectTree(rootDirPath);
    List<String> fileNames = tree.getValidFiles();
    ProjectReport projectReport = new ProjectReport();
    fileNames.forEach(name -> {
      FileReport fileReport = new FileReport();
      mode.getStats().forEach(stat -> {
        Report statReport = stat.collect(name);
        fileReport.addStatReport(statReport);
      });
      projectReport.addFileReport(name, fileReport);
    });
    reporter.outputToConsole(projectReport, tree, mode);
    return 0;
  }

  public static void main(String... args) {
    int exitCode = new CommandLine(new CollectStatCommand()).execute(args);
    System.exit(exitCode);
  }
}
