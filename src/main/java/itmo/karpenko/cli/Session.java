package itmo.karpenko.cli;

import org.apache.commons.cli.ParseException;

import java.io.IOException;
import java.util.List;

/**
 * данный класс реализует диалог с пользователем
 */
public class Session {


    public static void main(String[] args) throws IOException, InterruptedException, ParseException {
        VariablesImpl environment = new VariablesImpl();
        Reader reader = new ReaderImpl();
        while (true) {
            String input = reader.readStream();
            List<Token> inputTokens = Reader.parseString(input, environment);
            if (inputTokens != null) {
//                List<Token> clearTokens = Reader.parseTokens(inputTokens, environment);
                List<Command> commands = Expression.getPipe(inputTokens);
                if (commands != null) {
                    Expression.execute(commands);
                }
            }
        }
    }
}
