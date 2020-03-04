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

public class InnerCat implements Program {

    /**
     * Данная функция считывает входной файл, указанный в пути <tt>filename</tt>,
     * и выводит содержимое в стандартный поток вывода.
     *
     *
     * @param files имя файла
     * @throws IOException
     */
    @Override
    public String execute(List<String> files) throws IOException {
        StringBuilder result = new StringBuilder();
         for (String file: files) {
             BufferedReader in = new BufferedReader(new FileReader(file));
             String line;
             while((line = in.readLine()) != null)
             {
                 result.append(line);
                 result.append("\n");
             }
//             result.append("\n");
             in.close();
         }
         return result.toString();
    }

    @Override
    public String execute() throws IOException {
        return null;
    }

    /**
     * Считывает не файл, а переданную строку после pipe
     *
     * @param arg
     * @return
     * @throws IOException
     */
    @Override
    public String execute(String arg) throws IOException {
        List<String> lines = Arrays.asList(arg.split("\n"));
        StringBuilder result = new StringBuilder();
        for(String line: lines) {
            result.append(line);
        }
        result.append("\n");
        return result.toString();
    }

}
