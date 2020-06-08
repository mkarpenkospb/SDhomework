package itmo.karpenko.cli;

import org.apache.commons.cli.ParseException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class InnerGrepTest {

    private InnerGrep grep;
    private ByteArrayOutputStream buffer;
    private Variables env;
    private Executor executor;


    @Before
    public void setUp() {
        grep = new InnerGrep();
        buffer = new ByteArrayOutputStream();
        executor = new Executor(new PrintStream(buffer));
        env = new Variables();
    }


    @After
    public void closeStreams() throws IOException {
        buffer.close();
    }

    @Test
    public void testGrep() throws ParseException, IOException {
        grep.setArgs(Arrays.asList("ttt", "file3.txt"));
        grep.execute(null, new PrintStream(buffer));
        String test = buffer.toString("utf8");
        assertEquals("strttt\n" +
                "strttt2\n" +
                "sjsjhj hdjhjs ttt ttt\n" +
                "ttt uiuiauf\n" +
                "tttghghg\n", buffer.toString("utf8")
                .replaceAll("\u001B\\[[;\\d]*m", ""));
    }

    @Test
    public void testGrepW() throws ParseException, IOException {
        grep.setArgs(Arrays.asList("ttt", "file3.txt", "-w"));
        grep.execute(null, new PrintStream(buffer));
        assertEquals("sjsjhj hdjhjs ttt ttt\n" +
                "ttt uiuiauf\n",  buffer.toString("utf8")
                .replaceAll("\u001B\\[[;\\d]*m", ""));
    }

    @Test
    public void testGrepI() throws ParseException, IOException {
        grep.setArgs(Arrays.asList("ttt", "file3.txt", "-i"));
        grep.execute(null, new PrintStream(buffer));
        assertEquals("strttt\n" +
                "strttt2\n" +
                "sjsjhj hdjhjs ttt ttt\n" +
                "ttt uiuiauf\n" +
                "tttghghg\n" +
                "GHGHG TTT\n", buffer.toString("utf8")
                .replaceAll("\u001B\\[[;\\d]*m", ""));
    }

    @Test
    public void testGrepCombo() throws ParseException, IOException {
        grep.setArgs(Arrays.asList("ttt", "file3.txt", "-i", "-A", "2", "-w"));
        grep.execute(null, new PrintStream(buffer));
        assertEquals("sjsjhj hdjhjs ttt ttt\n" +
                "ttt uiuiauf\n" +
                "tttghghg\n" +
                "GHGHG TTT\n" +
                "sfs\n" +
                "sgssf\n", buffer.toString("utf8")
                .replaceAll("\u001B\\[[;\\d]*m", ""));
    }

    @Test
    public void testGrepRegexTwoFiles() throws InterruptedException, ParseException, IOException {
        grep.setArgs(Arrays.asList("s..", "file3.txt", "file4.txt"));
        grep.execute(null, new PrintStream(buffer));
        assertEquals("file3.txt:str 1\n" +
                "file3.txt:str 2\n" +
                "file3.txt:str 3\n" +
                "file3.txt:strttt\n" +
                "file3.txt:wro gdsshsh\n" +
                "file3.txt:strttt2\n" +
                "file3.txt:sjsjhj hdjhjs ttt ttt\n" +
                "file3.txt:sfs\n" +
                "file3.txt:sgssf\n" +
                "file4.txt:str 11\n" +
                "file4.txt:str22\n" +
                "file4.txt:str33 nl\n", buffer.toString("utf8")
                .replaceAll("\u001B\\[[;\\d]*m", ""));
    }

    @Test
    public void pipeTest() throws ParseException, IOException {
        String input = " cat file3.txt file4.txt | grep 's..' -A 1";
        List<Token> inputTokens = Parser.parseString(input, env);
        assertNotNull(inputTokens);
        List<Program> programs = executor.getPipe(inputTokens);
        assertNotNull(programs);
        executor.execute(programs);
        assertEquals("str 1\n"+
                "str 2\n"+
                "str 3\n"+
                "strttt\n"+
                "wro gdsshsh\n"+
                "strttt2\n"+
                "dddkag\n"+
                "--\n"+
                "sjsjhj hdjhjs ttt ttt\n"+
                "ttt uiuiauf\n"+
                "--\n"+
                "sfs\n"+
                "sgssf\n"+
                "str 11\n"+
                "str22\n" +
                "str33 nl\n\n"
                , buffer.toString("utf8")
                .replaceAll("\u001B\\[[;\\d]*m", ""));
    }
}