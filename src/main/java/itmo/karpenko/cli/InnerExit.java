package itmo.karpenko.cli;

import org.apache.commons.cli.ParseException;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.List;

/**
 * реализация exit
 */

class InnerExit implements Program {

    private List<String> args;

    @Override
    public void setArgs(List<String> args) {
        this.args = args;
    }

    @Override
    public void execute(InputStream inStream, PrintStream outStream)
            throws IOException, ParseException {
        System.exit(0);
    }

}
