package itmo.karpenko.cli;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.cli.*;


/**
 * Данный класс повторяет функцию grep
 * Поддерживаемые ключи: w, i, A
 */
public class InnerGrep implements Program {

    CommandLineParser cmdLinePosixParser = new PosixParser();
    Options posixOptions = new Options();

    String prefix = "";
    String p2M = "";
    String p2N = "";
    int nLines = 0;


    /**
     * констркутор инициализирует парсер
     * командной стоки соответствующими опциями
     */
    public InnerGrep () {
        Option optW = new Option("w", false, "word");
        optW.setArgs(0);
        optW.setOptionalArg(true);
        optW.setArgName("word ");
        posixOptions.addOption(optW);

        Option optI = new Option("i", false, "case insensitive");
        optI.setArgs(0);
        optI.setOptionalArg(true);
        optI.setArgName("case insensitive ");
        posixOptions.addOption(optI);

        Option optA = new Option("A", true, "lines after");
        optA.setArgs(1);
        optA.setOptionalArg(true);
        optA.setArgName("case insensitive ");
        posixOptions.addOption(optA);
    }



    @Override
    public String execute(List<String> args) throws IOException, ParseException {
        int cnt = 0;
        int delta = 0;
        Pattern pattern;
        String line;
        Matcher matcher;
        StringBuilder result = new StringBuilder();
        CommandLine commandLine = cmdLinePosixParser.parse(posixOptions,
                args.toArray(new String[0]));
        String toFind = commandLine.getArgs()[0];
        if (commandLine.hasOption("w")) {
            toFind = "\\b" + toFind + "\\b";
        }

        if (commandLine.hasOption("i")) {
            pattern = Pattern.compile(toFind, Pattern.CASE_INSENSITIVE);
        } else {
            pattern = Pattern.compile(toFind);
        }

        if (commandLine.hasOption("A")) {
            nLines = Integer.parseInt(commandLine.getOptionValues("A")[0]);
        }

        if (commandLine.getArgs().length > 2) {
            prefix = commandLine.getArgs()[1];
            p2M = ":";
            p2N = "-";
        }
        for (int i = 1; i < commandLine.getArgs().length; i++) {
            File file = new File(commandLine.getArgs()[i]);
            FileReader freader = new FileReader(file);
            BufferedReader fbuffer = new BufferedReader(freader);

            if (commandLine.hasOption("A")) {
                cnt = 0;
                while ((line = fbuffer.readLine()) != null) {
                    matcher = pattern.matcher(line);
                    delta ++;
                    if (matcher.find()) {
                        if (delta > 1 && result.length() > 0) {
                            result.append("--\n");
                        }
                        result.append(prefix).append(p2M).append(line).append("\n");
                        delta = 0;
                        while (cnt < nLines && (line = fbuffer.readLine()) != null) {
                            matcher = pattern.matcher(line);
                            if (matcher.find()) {
                                result.append(prefix).append(p2M).append(line).append("\n");
                                cnt = 0;
                            } else {
                                cnt ++;
                                result.append(prefix).append(p2N).append(line).append("\n");
                            }
                        }
                        cnt = 0;
                    }
                }


            } else {
                while ((line = fbuffer.readLine()) != null) {
                    matcher = pattern.matcher(line);
                    if (matcher.find()) {
                        result.append(prefix).append(p2M).append(line).append("\n");
                    }
                }
            }

            fbuffer.close();
            if (i < commandLine.getArgs().length -1) {
                prefix = commandLine.getArgs()[i + 1];
            }
        }

        return result.toString();
    }

    @Override
    public String execute() throws IOException {
        return null;
    }

    @Override
    public String execute(String arg) throws IOException {
        return null;
    }
}
