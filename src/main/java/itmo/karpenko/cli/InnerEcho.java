package itmo.karpenko.cli;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class InnerEcho implements Programm {

    public String execute(List<String> str) {
        StringBuilder result = new StringBuilder();
        for (String arg: str) {
            result.append(arg + " ");
        }
        return result.toString();
    }

    @Override
    public String execute() throws IOException {
        return new String("\n");
    }

    @Override
    public String execute(String arg) throws IOException {
        return new String("\n");
    }


}
