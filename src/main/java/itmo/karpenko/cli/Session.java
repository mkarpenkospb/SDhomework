package itmo.karpenko.cli;

import org.apache.commons.cli.ParseException;

import java.io.IOException;
import java.util.List;

/**
 * данный класс реализует диалог с пользователем
 */
public class Session {
    public static void main(String[] args) throws IOException, InterruptedException, ParseException {
        Variables environment = new Variables();
        Reader reader = new ReaderImpl();
        Executor executor = new Executor(System.out);
        while (true) {
            String input = reader.readStream();
            List<Token> inputTokens = Parser.parseString(input, environment);
            if (inputTokens != null) {
                List<Program> commands = executor.getPipe(inputTokens);
                if (commands != null) {
                    executor.execute(commands);
                }
            }
        }
    }
}
