package itmo.karpenko.cli;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.*;

public class InnerCatTest {
    private InnerCat cat;
    ByteArrayOutputStream buffer;

    @Before
    public void setUp() {
        cat = new InnerCat();
        buffer = new ByteArrayOutputStream();
    }

    @After
    public void closeStreams() throws IOException {
        buffer.close();
    }

    @Test
    public void TestCatSimpleFile() throws IOException {
        cat.setArgs(Collections.singletonList("file1.txt"));
        cat.execute(null, new PrintStream(buffer));
        assertEquals("str 1\nstr 2\nstr 3\n",
                buffer.toString("utf8"));
    }

    @Test
    public void TestCatNewLines() throws IOException {
        cat.setArgs(Collections.singletonList("file2.txt"));
        cat.execute(null, new PrintStream(buffer));
        assertEquals("str 11\nstr22\nstr33 nl\n\n",
                buffer.toString("utf8"));
    }

    @Test
    public void TestCatTwoFiles() throws IOException {
        cat.setArgs(Arrays.asList("file1.txt", "file2.txt"));
        cat.execute(null, new PrintStream(buffer));
        assertEquals("str 1\nstr 2\nstr 3\nstr 11\nstr22\nstr33 nl\n\n",
                buffer.toString("utf8"));
    }
}