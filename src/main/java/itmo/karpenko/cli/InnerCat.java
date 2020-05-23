package itmo.karpenko.cli;

import java.io.*;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author mkarpenko
 *
 * Функция данного класса -- команда Cat
 */

public class InnerCat implements Program {
    static String FILE_NOT_FOUND = "cat: %s:  No such file or directory\n";

    private List<String> args;

    @Override
    public void setArgs(List<String> args) {
        this.args = args;
    }

    @Override
    public void execute(InputStream inStream, PrintStream outStream)
            throws IOException {
        if (inStream != null) {
            innerRun(inStream, outStream);
        } else {
            for (String file: args) {
                if ((new File(file)).exists()) {
                    InputStream fileStream = new BufferedInputStream(new FileInputStream(file));
                    innerRun(fileStream, outStream);
                    fileStream.close();
                } else {
                    outStream.append(String.format(FILE_NOT_FOUND, file));
                }
            }
        }
    }

    void innerRun(InputStream inStream, PrintStream ouStream) {
        String line;
        Scanner sc = new Scanner(inStream);
        while(sc.hasNextLine()) {
            line = sc.nextLine();
            ouStream.append(line);
            ouStream.append("\n");
        }
    }
}
