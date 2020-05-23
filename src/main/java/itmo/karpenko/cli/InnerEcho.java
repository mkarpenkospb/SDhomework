package itmo.karpenko.cli;

import org.apache.commons.cli.ParseException;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.List;

public class InnerEcho implements Program {
    private List<String> args;

    @Override
    public void setArgs(List<String> args) {
        this.args = args;
    }

    @Override
    public void execute(InputStream inStream, PrintStream outStream)
            throws IOException, ParseException {
        if (args == null) {
            outStream.append("\n");
            return;
        }
        for (int i = 0; i < args.size(); i++) {
            if (i == args.size() - 1) {
                outStream.append(args.get(i)).append("\n");
                continue;
            }
            outStream.append(args.get(i)).append(" ");
        }
    }

}
