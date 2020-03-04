package itmo.karpenko.cli;

import java.io.*;
import java.util.Arrays;
import java.util.List;

public class InnerWc implements Programm {

    @Override
    public String execute(List<String> files) throws IOException {
        StringBuilder result = new StringBuilder();
        long totalBytes = 0;
        long totalLines = 0;
        long totalWordCount = 0;
        for (String fileItem: files) {
            File file = new File(fileItem);
            FileReader freader = new FileReader(file);
            BufferedReader fbuffer = new BufferedReader(freader);
            long bytes = 0;
            long lines = 0;
            long wordCount = 0;
            String line;
            while((line=fbuffer.readLine())!=null) {
                lines ++;
                wordCount += line.trim().split("\\w+").length;
                bytes += line.getBytes().length;
            }
            freader.close();
            bytes += lines;
            result.append(lines + " " + wordCount + " " + bytes + "\n");
            totalBytes += bytes;
            totalLines += lines;
            totalWordCount += wordCount;
        }
        if(files.size() > 1) {
            result.append(totalLines + " " + totalWordCount + " " + totalBytes + " total\n");
        }
        return result.toString();
    }

    @Override
    public String execute() throws IOException {
        return null;
    }

    @Override
    public String execute(String arg) throws IOException {
        StringBuilder result = new StringBuilder();
        List<String> file = Arrays.asList(arg.split("\n"));
        long bytes = 0;
        long lines = 0;
        long wordCount = 0;
        for(String line: file) {
            lines ++;
            wordCount += line.trim().split("\\s+").length;
            bytes += line.getBytes().length;
        }
        bytes += lines - 1;
        result.append(lines + " " + wordCount + " " + bytes + "\n");
        return result.toString();
    }


//    public static String readBuffer(){
//
//    }

}
