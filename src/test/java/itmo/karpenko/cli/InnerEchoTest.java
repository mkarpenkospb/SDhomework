package itmo.karpenko.cli;

import org.apache.commons.cli.ParseException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Arrays;

import static org.junit.Assert.*;

public class InnerEchoTest {
    private InnerEcho echo;
    ByteArrayOutputStream buffer = new ByteArrayOutputStream();

    @Before
    public void setUp() {
        echo = new InnerEcho();
        buffer = new ByteArrayOutputStream();
    }

    @After
    public void closeStreams() throws IOException {
        buffer.close();
    }


    @Test
    public void testSimpleEcho() throws IOException, ParseException {
        echo.setArgs(Arrays.asList("asd", "11", "555"));
        echo.execute(null, new PrintStream(buffer));
        assertEquals("asd 11 555\n", buffer.toString("utf8"));
    }

    @Test
    public void testNewLine() throws IOException, ParseException {
        echo.setArgs(null);
        echo.execute(null, new PrintStream(buffer));
        assertEquals("\n", buffer.toString("utf8"));
    }

}