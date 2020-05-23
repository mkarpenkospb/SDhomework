package itmo.karpenko.cli;

import java.io.*;
import java.util.List;
import java.util.Scanner;

/**
 * реализацтя wc
 */
public class InnerWc implements Program {


    private List<String> args;

    @Override
    public void setArgs(List<String> args) {
        this.args = args;
    }

    static class TotalCounter {
        long totalBytes = 0;
        long totalLines = 0;
        long totalWordCount = 0;
    }

    @Override
    public void execute(InputStream inStream, PrintStream outStream)
            throws IOException {

        if (inStream != null) {
            innerRun(inStream, outStream, null, null);
        } else {
            TotalCounter counter = new TotalCounter();
            for (String file : args) {
                InputStream fileStream = new BufferedInputStream(new FileInputStream(file));
                innerRun(fileStream, outStream, file, counter);
                fileStream.close();
            }
            if (args.size() > 1) {
                outStream.append(String.valueOf(counter.totalLines)).
                        append(" ").append(String.valueOf(counter.totalWordCount)).
                        append(" ").append(String.valueOf(counter.totalBytes)).
                        append(" total\n");
            }
        }

    }


    void innerRun(InputStream inStream, PrintStream outStream, String filename,
                  TotalCounter coutner) {
        String line, suff = "";
        Scanner sc = new Scanner(inStream);
        int currentWordCount;
        long bytes = 0;
        long lines = 0;
        long wordCount = 0;
        if (filename != null) {
            suff = " " + filename;
        }
        while (sc.hasNextLine()) {
            line = sc.nextLine();
            lines++;
            if (line.trim().equals("")) {
                currentWordCount = 0;
            } else {
                currentWordCount = line.trim().split("\\w+").length;
            }
            wordCount += currentWordCount;
            if (currentWordCount == 0 && line.trim().length() > 0) {
                wordCount ++;
            }
            bytes += line.getBytes().length;
        }
        bytes += lines;
        outStream.append(String.valueOf(lines)).
                append(" ").append(String.valueOf(wordCount)).
                append(" ").append(String.valueOf(bytes)).append(suff).append("\n");

        if (coutner != null) {
            coutner.totalBytes += bytes;
            coutner.totalLines += lines;
            coutner.totalWordCount += wordCount;
        }

    }


}
