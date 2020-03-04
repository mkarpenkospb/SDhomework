package itmo.karpenko.cli;


//import sun.security.x509.AttributeNameEnumeration;

import java.io.IOException;
import java.util.*;


/**
 * класс для формирования исполняемы команд
 */

public class Expression {
    public static Map<String, Programm> innerProgramms;
    static {
        innerProgramms = new HashMap<>();
        innerProgramms.put("cat", new InnerCat());
        innerProgramms.put("echo", new InnerEcho());
        innerProgramms.put("wc", new InnerWc());
        innerProgramms.put("pwd", new InnerPwd());
    }

//
//    static {
//        articleMapOne = new HashMap<>();
//        articleMapOne.put("ar01", "Intro to Map");
//        articleMapOne.put("ar02", "Some article");
//    }

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
                    // TODO кинуть исключение
                    return null;
                }
            }
            if (correctFlag) {
                return null;
            }

        }
        return pipeLine;
    }

    static void runProgram(String name, List<String> args) throws IOException {
        if (innerProgramms.containsKey(name)) {
            System.out.println(innerProgramms.get(name).execute(args));
        }
    }

    static void execute(List<Command> commands) throws IOException {
        if (commands.size() == 1) {
            runProgram(commands.get(0).name, commands.get(0).args);
        } else {
//            for
            String nextArg = innerProgramms.get(commands.get(0).name).execute(commands.get(0).args);
            commands = commands.subList(1, commands.size());
            for (Command com: commands) {
                nextArg = innerProgramms.get(com.name).execute(nextArg);
            }
            System.out.println(nextArg);
        }
    }

}
