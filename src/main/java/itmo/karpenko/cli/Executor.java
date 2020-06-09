package itmo.karpenko.cli;

import org.apache.commons.cli.ParseException;
import java.io.*;
import java.util.*;


/**
 * класс для формирования исполняемых команд
 * и их исполнения
 */
public class Executor {

    private final PrintStream outStream;
    public Executor(PrintStream outStream) {
        this.outStream = outStream;
    }
    public PrintStream getOutStream() {
        return outStream;
    }
    public static Map<String, Program> innerPrograms;
    static {
        innerPrograms = new HashMap<>();
        innerPrograms.put("cat", new InnerCat());
        innerPrograms.put("echo", new InnerEcho());
        innerPrograms.put("wc", new InnerWc());
        innerPrograms.put("pwd", new InnerPwd());
        innerPrograms.put("exit", new InnerExit());
    }

    /**
     *
     * @param tokens -- разобранные парсером токены косандной строки
     * @return -- список исполняемых програм
     * @throws IOException
     * @throws ParseException
     */
    public List<Program> getPipe(List<Token> tokens) throws IOException, ParseException {
        boolean correctFlag = true;
        List<Command> commandList = new ArrayList<>();
        List<String> fstAndArgs = new ArrayList<>();
        Command curCommand = new Command();
        /* собрать всё аргументы и имя функции до
        первого pipe или до конца строки
         */
        for (Token token : tokens) {
            if (token.toString().equals("|")) {
                break;
            } else {
                fstAndArgs.add(token.toString());
            }
        }
        /*
        Первая команда pipeline. Если длина собранного выше листа больше 1,
        то добавляем первой команде аргументы.
         */
        Command headCommand = new Command(fstAndArgs.get(0), null);
        if (fstAndArgs.size() > 1) {
            headCommand.setArgs(fstAndArgs.subList(1, fstAndArgs.size()));
        }
        commandList.add(headCommand);
        /*
        Если на этом набор токенов не закончился,
        собираем команду и её аргументы
         */
        if (tokens.size() > fstAndArgs.size()) {
            tokens = tokens.subList(fstAndArgs.size(), tokens.size());
            for (Token token : tokens) {
                if (token.toString().equals("|")) {
                    if (!correctFlag) {
                        outStream.println("Invalid pipe");
                        return null;
                    }
                    curCommand = new Command();
                    commandList.add(curCommand);
                    correctFlag = false;
                } else if(curCommand.getName() == null) {
                    curCommand.setName(token.toString());
                    correctFlag = true;
                } else {
                    curCommand.getArgs().add(token.toString());
                    correctFlag = true;
                }
            }
        }

        if (!correctFlag) {
            outStream.println("Invalid pipe");
            return null;
        }

        List<Program> programs = new ArrayList<>();
        Program currProgram;
        for(Command com: commandList) {
            if (innerPrograms.containsKey(com.getName())) {
                currProgram = innerPrograms.get(com.getName());
                currProgram.setArgs(com.getArgs());
                programs.add(currProgram);
            } else {
                OuterProgram outProgram = new OuterProgram();
                outProgram.setUp(com);
                if (commandList.size() > 1) {
                    throw new RuntimeException("Cannot build pipe with external program");
                }
                programs.add(outProgram);
            }
        }

        return programs;
    }


    void execute(List<Program> pipeLine) throws IOException, ParseException {
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out = new PipedOutputStream(in);
        pipeLine.get(0).execute(null, new PrintStream(out));
        if (pipeLine.size() > 1) {
            PipedInputStream in1;
            PipedOutputStream out1;
            for (Program prog: pipeLine.subList(1, pipeLine.size())) {
                in1 = new PipedInputStream();
                out1 = new PipedOutputStream(in1);
                out.close();
                prog.execute(in, new PrintStream(out1));
                in.close();
                in = in1;
                out = out1;
            }
        }
        out.close();
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String line;
        while ((line = br.readLine()) != null) {
            outStream.println(line);
        }
        br.close();
        in.close();
    }
}
