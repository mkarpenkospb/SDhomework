package itmo.karpenko.cli;

import java.io.IOException;
import java.util.List;

public interface Programm {
    String execute(List<String> args) throws IOException;
//    String ecexcuteInPipe(List<String> args);
    String execute() throws IOException;
    String execute(String arg) throws IOException;
}
