package itmo.karpenko.cli;

import org.apache.commons.cli.ParseException;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;


/**
 *
 * Реализация программы pwd
 */
public class InnerPwd implements Program {

    List<String> args;
    @Override
    public void setArgs(List<String> args) {
        this.args = args;
    }

    @Override
    public void execute(InputStream inStream, PrintStream outStream)
            throws IOException, ParseException {
        Path currentDirectory = Paths.get("");
        outStream.append(currentDirectory.toAbsolutePath().toString());
    }
}
