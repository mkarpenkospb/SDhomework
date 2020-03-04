package itmo.karpenko.cli;

import java.io.IOException;
import java.util.List;

public class InnerEcho implements Program {
    /**
     * Программа выводит свои аргументы
     * @param str
     * @return
     */
    public String execute(List<String> str) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < str.size(); i++) {
            if (i == str.size() - 1) {
                result.append(str.get(i) + "\n");
                continue;
            }
            result.append(str.get(i) + " ");
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
