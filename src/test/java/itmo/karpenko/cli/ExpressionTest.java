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
import static org.junit.Assert.assertNotNull;

public class ExpressionTest {
    private ReaderImpl reader;
    private VariablesImpl env;


    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;

    @Before
    public void setUp() {
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
    public void pipeTestEchoWc() throws IOException, InterruptedException, ParseException {
        String input = "echo 123| wc";
        List<Token> inputTokens = Reader.parseString(input, env);
        assertNotNull(inputTokens);
        List<Command> commands = Expression.getPipe(inputTokens);
        assertNotNull(commands);
        Expression.execute(commands);
        assertEquals("1 1 4\n", outContent.toString());
    }


    @Test
    public void pipeCatWc1() throws IOException, InterruptedException, ParseException {
        String input = "cat file1.txt| wc";
        List<Token> inputTokens = Reader.parseString(input, env);
        assertNotNull(inputTokens);
        List<Command> commands = Expression.getPipe(inputTokens);
        assertNotNull(commands);
        Expression.execute(commands);
        assertEquals("3 6 18\n", outContent.toString());
    }

    @Test
    public void pipeCatWc2() throws IOException, InterruptedException, ParseException {
        String input = "cat file1.txt file2.txt| wc";
        List<Token> inputTokens = Reader.parseString(input, env);
        assertNotNull(inputTokens);
        List<Command> commands = Expression.getPipe(inputTokens);
        assertNotNull(commands);
        Expression.execute(commands);
        assertEquals("7 11 41\n", outContent.toString());
    }

    @Test
    public void invalidPipe() {
        String input = "cat df | | sd|";
        List<Token> inputTokens = Reader.parseString(input, env);
        assertNotNull(inputTokens);
        List<Command> commands = Expression.getPipe(inputTokens);
        assertNull(commands);
        assertEquals("Invalid pipe\n", outContent.toString());
    }

    @Test
    public void varsSub() throws IOException, InterruptedException, ParseException {
        String input = "a=file1.txt";
        List<Token> inputTokens = Reader.parseString(input, env);
        assertNull(inputTokens);
        input = "b=fil";
        inputTokens = Reader.parseString(input, env);
        assertNull(inputTokens);
        input = "c=e2.txt";
        inputTokens = Reader.parseString(input, env);
        assertNull(inputTokens);
        input = "cat $a $b$c| wc";
        inputTokens = Reader.parseString(input, env);
        assertNotNull(inputTokens);
        List<Command> commands = Expression.getPipe(inputTokens);
        assertNotNull(commands);
        Expression.execute(commands);
        assertEquals("7 11 41\n", outContent.toString());
    }

}