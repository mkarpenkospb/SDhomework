package itmo.karpenko.cli;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * реализацтя wc
 */
public class InnerWc implements Program {

    /**
     * функция считает число строк, слов и байти в файле
     * @param files - список файлов
     * @return
     * @throws IOException
     */
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


    /**
     * Считает число строк, слов и байт во входной строке
     * @param arg - входная строка
     * @return
     * @throws IOException
     */
//    @Override
//    public String execute(String arg) throws IOException {
//        StringBuilder result = new StringBuilder();
//        List<String> file = Arrays.asList(arg.split("\n"));
//        long bytes = 0;
//        long lines = 0;
//        long wordCount = 0;
//        for(String line: file) {
//            lines ++;
//            wordCount += line.trim().split("\\s+").length;
//            bytes += line.getBytes().length;
//        }
//        bytes += lines;
//        result.append(lines + " " + wordCount + " " + bytes + "\n");
//        return result.toString();
//    }
    @Override
    public String execute(String arg) throws IOException {
        StringBuilder result = new StringBuilder();
        List<String> file = Arrays.asList(arg.split("\n"));
        Pattern linesFind =
                Pattern.compile("\n");
        Matcher matcher1 = linesFind.matcher(arg);
        long bytes = 0;
        long lines = 0;
        long wordCount = 0;
        while (matcher1.find())
        {
            lines ++;
        }

        for(String line: file) {
            wordCount += line.trim().split("\\s+").length;
            bytes += line.getBytes().length;
        }
        bytes += lines;
        result.append(lines + " " + wordCount + " " + bytes + "\n");
        return result.toString();
    }

//    public static String readBuffer(){
//
//    }

}
