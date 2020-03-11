package itmo.karpenko.cli;

import org.apache.commons.cli.ParseException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;

import static org.junit.Assert.*;

public class InnerGrepTest {

    private InnerGrep grep;
    private ReaderImpl reader;
    private VariablesImpl env;


    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;


    @Before
    public void setUp() {
        grep = new InnerGrep();
        reader = new ReaderImpl();
        env = new VariablesImpl();
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }


    @After
    public void restoreStreams() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    @Test
    public void testGrep() throws InterruptedException, ParseException, IOException {
        String input = "grep ttt file1.txt";
        List<Token> inputTokens = Reader.parseString(input, env);
        assertNotNull(inputTokens);
        List<Command> commands = Expression.getPipe(inputTokens);
        assertNotNull(commands);
        Expression.execute(commands);
        assertEquals("strttt\n" +
                "strttt2\n" +
                "sjsjhj hdjhjs ttt ttt\n" +
                "ttt uiuiauf\n" +
                "tttghghg\n", outContent.toString());

    }

    @Test
    public void testGrepW() throws InterruptedException, ParseException, IOException {
        String input = "grep ttt -w file1.txt";
        List<Token> inputTokens = Reader.parseString(input, env);
        assertNotNull(inputTokens);
        List<Command> commands = Expression.getPipe(inputTokens);
        assertNotNull(commands);
        Expression.execute(commands);
        assertEquals("sjsjhj hdjhjs ttt ttt\n" +
                "ttt uiuiauf\n", outContent.toString());
    }

    @Test
    public void testGrepI() throws InterruptedException, ParseException, IOException {
        String input = "grep ttt -i file1.txt";
        List<Token> inputTokens = Reader.parseString(input, env);
        assertNotNull(inputTokens);
        List<Command> commands = Expression.getPipe(inputTokens);
        assertNotNull(commands);
        Expression.execute(commands);
        String test = outContent.toString();
        assertEquals("strttt\n" +
                "strttt2\n" +
                "sjsjhj hdjhjs ttt ttt\n" +
                "ttt uiuiauf\n" +
                "tttghghg\n" +
                "GHGHG TTT\n", outContent.toString());
    }

    @Test
    public void testGrepCombo() throws InterruptedException, ParseException, IOException {
        String input = "grep ttt -i -w -A 2 file1.txt";
        List<Token> inputTokens = Reader.parseString(input, env);
        assertNotNull(inputTokens);
        List<Command> commands = Expression.getPipe(inputTokens);
        assertNotNull(commands);
        Expression.execute(commands);
        String test = outContent.toString();
        assertEquals("sjsjhj hdjhjs ttt ttt\n" +
                "ttt uiuiauf\n" +
                "tttghghg\n" +
                "GHGHG TTT\n" +
                "sfs\n" +
                "sgssf\n", outContent.toString());
    }

    @Test
    public void testGrepRegexTwoFiles() throws InterruptedException, ParseException, IOException {
        String input = "grep 's..' file1.txt file2.txt";
        List<Token> inputTokens = Reader.parseString(input, env);
        assertNotNull(inputTokens);
        List<Command> commands = Expression.getPipe(inputTokens);
        assertNotNull(commands);
        Expression.execute(commands);
        String test = outContent.toString();
        assertEquals("file1.txt:str 1\n" +
                "file1.txt:str 2\n" +
                "file1.txt:str 3\n" +
                "file1.txt:strttt\n" +
                "file1.txt:wro gdsshsh\n" +
                "file1.txt:strttt2\n" +
                "file1.txt:sjsjhj hdjhjs ttt ttt\n" +
                "file1.txt:sfs\n" +
                "file1.txt:sgssf\n" +
                "file2.txt:str 11\n" +
                "file2.txt:str22\n" +
                "file2.txt:str33 nl\n", outContent.toString());
    }




}