package itmo.karpenko.cli;

import org.apache.commons.cli.ParseException;

import java.io.*;
import java.util.List;

/**
 * интерфейс для исполнения
 * внутренних программ
 */
public interface Program {

    void setArgs(List<String> args);

    void execute(InputStream inStream, PrintStream outStream)
            throws IOException, ParseException;

}
