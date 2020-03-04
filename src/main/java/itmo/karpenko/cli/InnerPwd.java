package itmo.karpenko.cli;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;


/**
 *
 * Реализация программы pwd
 */
public class InnerPwd implements Program {

    @Override
    public String execute(List<String> args) throws IOException {
        return execute();
    }

    @Override
    public String execute() {
        Path currentDirectory = Paths.get("");
        return  currentDirectory.toAbsolutePath().toString();
    }

    @Override
    public String execute(String arg) throws IOException {
        return execute() ;
    }


}
