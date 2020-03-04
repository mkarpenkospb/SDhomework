package itmo.karpenko.cli;

import java.io.IOException;
import java.util.List;

public class Session {


    public static void main(String[] args) throws IOException {
        VariablesImpl environment = new VariablesImpl();
        Reader reader = new ReaderImpl();
        while (true) {
            String input = reader.readStream();
            List<Token> inputTokens = Reader.parseString(input, environment);
            if (inputTokens != null) {
                List<Token> clearTokens = Reader.parseTokens(inputTokens, environment);
                List<Command> commands = Expression.getPipe(clearTokens);
                Expression.execute(commands);
            }
        }
    }
}
