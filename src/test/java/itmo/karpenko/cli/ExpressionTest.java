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
    private Variables env;
    ByteArrayOutputStream buffer;
    private Executor executor;

    @Before
    public void setUp() {
        env = new Variables();
        buffer = new ByteArrayOutputStream();
        executor = new Executor(new PrintStream(buffer));
    }

    @After
    public void closeStreams() {
        executor.getOutStream().close();
    }

    @Test
    public void pipeTestEchoWc() throws IOException, InterruptedException, ParseException {
        String input = "echo 123| wc";
        List<Token> inputTokens = Parser.parseString(input, env);
        assertNotNull(inputTokens);
        List<Program> programs = executor.getPipe(inputTokens);
        assertNotNull(programs);
        executor.execute(programs);
        assertEquals("1 1 4\n", buffer.toString("utf8"));
    }


    @Test
    public void pipeCatWc1() throws IOException, InterruptedException, ParseException {
        String input = "cat file1.txt| wc";
        List<Token> inputTokens = Parser.parseString(input, env);
        assertNotNull(inputTokens);
        List<Program> programs = executor.getPipe(inputTokens);
        assertNotNull(programs);
        executor.execute(programs);
        assertEquals("3 6 18\n", buffer.toString("utf8"));
    }

    @Test
    public void pipeCatWc2() throws IOException, ParseException {
        String input = "cat file1.txt file2.txt| wc";
        List<Token> inputTokens = Parser.parseString(input, env);
        assertNotNull(inputTokens);
        List<Program> programs = executor.getPipe(inputTokens);
        assertNotNull(programs);
        executor.execute(programs);
        assertEquals("7 11 41\n", buffer.toString("utf8"));
    }

    @Test
    public void invalidPipe() throws IOException, ParseException {
        String input = "cat df | | sd|";
        List<Token> inputTokens = Parser.parseString(input, env);
        assertNotNull(inputTokens);
        List<Program> programs = executor.getPipe(inputTokens);
        assertNull(programs);
        assertEquals("Invalid pipe\n",  buffer.toString("utf8"));
    }

    @Test
    public void varsSub() throws IOException, InterruptedException, ParseException {
        String input = "a=file1.txt";
        List<Token> inputTokens = Parser.parseString(input, env);
        assertNull(inputTokens);
        input = "b=fil";
        inputTokens = Parser.parseString(input, env);
        assertNull(inputTokens);
        input = "c=e2.txt";
        inputTokens = Parser.parseString(input, env);
        assertNull(inputTokens);
        input = "cat $a $b$c| wc";
        inputTokens = Parser.parseString(input, env);
        assertNotNull(inputTokens);
        List<Program> programs = executor.getPipe(inputTokens);
        assertNotNull(programs);
        executor.execute(programs);
        assertEquals("7 11 41\n",  buffer.toString("utf8"));
    }

}