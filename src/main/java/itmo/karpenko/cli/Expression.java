package itmo.karpenko.cli;


//import sun.security.x509.AttributeNameEnumeration;

import org.apache.commons.cli.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;


/**
 * класс для формирования исполняемых команд
 * и их исполнения
 */

public class Expression {
    /**
     * определяет ось
     */
    public static boolean windows = System.getProperty("os.name").toLowerCase().contains("windows");

    /**
     * все доступные реализованные команды
     */
    public static Map<String, Program> innerProgramms;
    static {
        innerProgramms = new HashMap<>();
        innerProgramms.put("cat", new InnerCat());
        innerProgramms.put("echo", new InnerEcho());
        innerProgramms.put("wc", new InnerWc());
        innerProgramms.put("pwd", new InnerPwd());
        innerProgramms.put("exit", new InnerExit());
        innerProgramms.put("grep", new InnerGrep());
    }

    /**
     * функция формирует pipeline,
     * null, если последовательно некорректная
     * @param tokens принимает набор токенов на вход
     * @return команды
     */
    static List<Command> getPipe(List<Token> tokens) {
        boolean correctFlag = false;
        List<Command> pipeLine = new ArrayList<>();
        List<String> fstAndArgs = new ArrayList<>();
        // собрать всё аргументы
        for (Token token : tokens) {
            if (token.strValue.equals("|")) {
                break;
            } else {
                fstAndArgs.add(token.strValue);
            }
        }

        Command headCommand = new Command(fstAndArgs.get(0), null, true);
        if (fstAndArgs.size() > 1) {
            headCommand.args = fstAndArgs.subList(1, fstAndArgs.size());
        }
        pipeLine.add(headCommand);
        if (tokens.size() > fstAndArgs.size()) {
            tokens = tokens.subList(fstAndArgs.size(), tokens.size());
            for (Token token : tokens) {
                if (token.strValue.equals("|") && !correctFlag) {
                    correctFlag = true;
                } else if (!token.strValue.equals("|") && correctFlag) {
                    pipeLine.add(new Command(token.strValue, null, false));
                    correctFlag = false;
                } else {
                    System.out.println("Invalid pipe");
                    return null;
                }
            }
            if (correctFlag) {
                System.out.println("Invalid pipe");
                return null;
            }

        }
        return pipeLine;
    }

    private static String[] getFullExec(Command cmd) {
        StringBuilder res = new StringBuilder(cmd.name);
        for (String args : cmd.args) {
            res.append(" ").append(args);
        }
        return res.toString().split(" ");
    }

    private static void runProgram(Command cmd) throws IOException, ParseException {
        if (innerProgramms.containsKey(cmd.name)) {
            System.out.print(innerProgramms.get(cmd.name).execute(cmd.args));
        } else {
            ProcessBuilder pBuild = new ProcessBuilder(windows ? "where" : "which", cmd.name);
            Path pathToTarget = null;
            try {
                Process process = pBuild.start();
                int errCode = process.waitFor();
                if (errCode == 0) {
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(process.getInputStream()));
                    pathToTarget = Paths.get(reader.readLine());
                    System.out.println("Programm at "  + pathToTarget);
//                    Runtime.getRuntime().exec(getFullExec(cmd));
                    Process pr2 = new ProcessBuilder(getFullExec(cmd)).start();
                    StringBuilder out = new StringBuilder();
                    try (BufferedReader read = new BufferedReader(
                            new InputStreamReader(pr2.getInputStream()))) {
                        String line = null;
                        while ((line = read.readLine()) != null) {
                            out.append(line);
                            out.append("\n");
                        }
                        System.out.println(out);
                    }

                } else {
                    System.out.println("Programm not found");
                }
            } catch (IOException | InterruptedException except) {
                System.out.println("Unexpected error");
            }

        }
    }

    /**
     * Функция, выполняющая команды
     * @param commands
     * @throws IOException
     * @throws InterruptedException
     */
    static void execute(List<Command> commands) throws IOException, InterruptedException, ParseException {
        if (commands.size() == 1) {
            runProgram(commands.get(0));
        } else {
           for (Command cmd: commands) {
                if(!innerProgramms.containsKey(cmd.name)) {
                    System.out.println("Cannot build pipeLine with external programms");
                    return;
                }
            }
            String nextArg = innerProgramms.get(commands.get(0).name).execute(commands.get(0).args);
            commands = commands.subList(1, commands.size());
            for (Command com: commands) {
                nextArg = innerProgramms.get(com.name).execute(nextArg);
            }
            System.out.print(nextArg);
        }
    }

}
