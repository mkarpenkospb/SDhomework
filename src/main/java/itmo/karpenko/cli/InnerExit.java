package itmo.karpenko.cli;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author mkarpenko
 *
 * Этот класс не будет использоваться.
 * Функция данного класса -- команда Cat
 */

public class InnerExit implements Programm {

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
