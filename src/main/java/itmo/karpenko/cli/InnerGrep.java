package itmo.karpenko.cli;

import java.io.*;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.cli.*;

public class InnerGrep implements Program {

    static final String FILE_NOT_FOUND = "grep: %s: No such file or directory\n";
    static final String INVALID_A_ARGUMENT = "grep: %s: invalid context length argument\n";
    static final String INVALID_NUM_OF_ARGUMENTS = "grep: invalid number of arguments for option -- %s\n";
    private static final CommandLineParser cmdLinePosixParser = new PosixParser();
    private static final Options posixOptions = new Options();
    private Pattern pattern;

    private String sndPrefixForMatch = "";
    private String sndPrefixForLinesAfterMatch = "";
    private int nLines = 0;
    private String devider = "";

    private List<String> args;

    @Override
    public void setArgs(List<String> args) {
        this.args = args;
    }

    static {
        Option optW = new Option("w", false,
                "match strings that contain as a pattern the whole word");
        optW.setArgs(0);
        optW.setOptionalArg(true);
        posixOptions.addOption(optW);

        Option optI = new Option("i", false, "case insensitive");
        optI.setArgs(0);
        optI.setOptionalArg(true);
        posixOptions.addOption(optI);

        Option optA = new Option("A", true, "print NUM lines after match");
        optA.setArgs(1);
        optA.setOptionalArg(true);
        optA.setArgName("NUM");
        posixOptions.addOption(optA);
    }


    @Override
    public void execute(InputStream inStream, PrintStream outStream) throws IOException, ParseException {
        CommandLine commandLine = cmdLinePosixParser.parse(posixOptions, args.toArray(new String[0]));
        try {
            checkArguments(commandLine);
        } catch (IllegalArgumentException e) {
            outStream.append(e.getMessage());
            return;
        }

        String toFind = commandLine.getArgs()[0];
        String file;

        if (commandLine.hasOption("w")) {
            toFind = "\\b" + toFind + "\\b";
        }

        if (commandLine.hasOption("i")) {
            pattern = Pattern.compile(toFind, Pattern.CASE_INSENSITIVE);
        } else {
            pattern = Pattern.compile(toFind);
        }

        if (commandLine.hasOption("A")) {
            try {
                nLines = Integer.parseInt(commandLine.getOptionValues("A")[0]);
                if (nLines < 0) {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException e) {
                outStream.append(String.format(INVALID_A_ARGUMENT,
                        commandLine.getOptionValues("A")[0]));
                return;
            }
            devider = ConsoleColors.CYAN  + "--\n" + ConsoleColors.RESET;
        }

        // если нужно искать шаблон в нескольких файлах, форматируем вывод
        if (commandLine.getArgs().length > 2) {
            sndPrefixForMatch = ConsoleColors.CYAN + ":" + ConsoleColors.RESET;
            sndPrefixForLinesAfterMatch = ConsoleColors.CYAN  + "-" + ConsoleColors.RESET;
        }

        if (inStream != null) {
            innerRun(inStream, outStream, "");
        } else {
            for (int i = 1; i < commandLine.getArgs().length; i++) {
                file = commandLine.getArgs()[i];
                if (i > 1) {
                    outStream.append(devider);
                }
                if ((new File(file)).exists()) {
                    InputStream fileStream = new BufferedInputStream(new FileInputStream(file));
                    if (commandLine.getArgs().length == 2) {
                        file = "";
                    } else {
                        file = ConsoleColors.PURPLE + file + ConsoleColors.RESET;
                    }
                    innerRun(fileStream, outStream, file);
                    fileStream.close();
                } else {
                    outStream.append(String.format(FILE_NOT_FOUND, file));
                }
            }
        }
    }

    void innerRun(InputStream inStream, PrintStream ouStream, String prefix) {
        int cnt = 0;
        int delta = 0;
        Matcher matcher;
        String line;
        Scanner sc = new Scanner(inStream);
        boolean found = false;
        while (sc.hasNextLine()) {
            line = sc.nextLine();
            matcher = pattern.matcher(line);
            if (matcher.find()) {
                if (delta > 0 && cnt > 0) {
                    ouStream.append(devider);
                }
                ouStream.append(prefix).append(sndPrefixForMatch)
                        .append(formatLine(line, matcher)).append("\n");
                found = true;
                delta = 0;
                cnt = 0;
            } else if (cnt < nLines && found) {
                ouStream.append(prefix).append(sndPrefixForLinesAfterMatch)
                        .append(line).append("\n");
                cnt++;
            } else {
                delta ++;
                found = false;
            }
        }
    }

    String formatLine(String line, Matcher matcher) {
        StringBuilder result = new StringBuilder();
        int end = 0;
        do {
            if (matcher.start() >= end) {
                result.append(line, end, matcher.start());
                result.append(ConsoleColors.RED_BOLD)
                        .append(line, matcher.start(), matcher.end())
                        .append(ConsoleColors.RESET);
                end = matcher.end();
            }
        } while (matcher.find());
        result.append(line, end, line.length());
        return result.toString();
    }

    void checkArguments(CommandLine commandLine) throws IllegalArgumentException {
        if (commandLine.hasOption("A") &&
                (commandLine.getOptionValues("A") == null ||
                commandLine.getOptionValues("A").length != 1)) {
            throw new IllegalArgumentException(String.format(INVALID_NUM_OF_ARGUMENTS,
                    'A'));
        }

        if (commandLine.hasOption("w") && commandLine.getOptionValues("w") != null) {
            throw new IllegalArgumentException(String.format(INVALID_NUM_OF_ARGUMENTS,
                    'w'));
        }

        if (commandLine.hasOption("i") && commandLine.getOptionValues("i") != null) {
            throw new IllegalArgumentException(String.format(INVALID_NUM_OF_ARGUMENTS,
                    'i'));
        }
    }
}
