package itmo.karpenko.cli;

import org.apache.commons.cli.ParseException;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class OuterProgram implements Program {

    private static final boolean windows = System.getProperty("os.name").toLowerCase().contains("windows");
    private String name;
    private String[] fullExec;


    private static String[] getFullExec(Command cmd) {
        StringBuilder res = new StringBuilder(cmd.getName());
        if (cmd.getArgs() != null) {
            for (String args : cmd.getArgs()) {
                res.append(" ").append(args);
            }
        }
        return res.toString().split(" ");
    }


    public void setUp(Command cmd) throws IOException, ParseException {
        name = cmd.getName();
        fullExec = getFullExec(cmd);
    }


    @Override
    public void setArgs(List<String> args) {
    }

    @Override
    public void execute(InputStream inStream, PrintStream outStream) throws IOException, ParseException {
        if (inStream != null) {
            throw new RuntimeException("Cannot build pipe with external program");
        } else {
            ProcessBuilder pBuild = new ProcessBuilder(windows ? "where" : "which", name);
            Path pathToTarget;
            try {
                Process process = pBuild.start();
                int errCode = process.waitFor();
                if (errCode == 0) {
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(process.getInputStream()));
                    pathToTarget = Paths.get(reader.readLine());
                    outStream.println("Programm at "  + pathToTarget);
                    Process pr2 = new ProcessBuilder(fullExec).start();
                    StringBuilder out = new StringBuilder();
                    try (BufferedReader read = new BufferedReader(
                            new InputStreamReader(pr2.getInputStream()))) {
                        String line;
                        while ((line = read.readLine()) != null) {
                            out.append(line);
                            out.append("\n");
                        }
                        outStream.println(out);
                    }

                } else {
                    outStream.println("Programm not found");
                }
            } catch (IOException | InterruptedException except) {
                outStream.println("Unexpected error");
            }
        }
    }
}
