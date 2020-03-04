package itmo.karpenko.cli;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;

import static org.junit.Assert.*;

public class InnerEchoTest {
    private InnerEcho echo;

    @Before
    public void setUp() {
        echo = new InnerEcho();
    }

    @Test
    public void testEcho() throws IOException {
        assertEquals("asd 11 555\n",
                echo.execute(Arrays.asList("asd", "11", "555")));
        assertEquals("\n", echo.execute());
    }

}