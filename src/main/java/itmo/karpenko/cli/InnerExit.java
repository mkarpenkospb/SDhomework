package itmo.karpenko.cli;

import java.io.IOException;
import java.util.List;

/**
 * реализация exit
 */

public class InnerExit implements Program {

    /**
     * Все функции завершают программу
     * @param files
     * @return
     * @throws IOException
     */
    @Override
    public String execute(List<String> files) throws IOException {

         return execute();
    }

    @Override
    public String execute() throws IOException {
        System.exit(0);
        return null;
    }

    @Override
    public String execute(String arg) throws IOException {
        return execute();
    }

}
