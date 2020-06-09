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

public class InnerWcTest {
    private InnerWc wc;
    ByteArrayOutputStream buffer;

    @Before
    public void setUp() {
        wc = new InnerWc();
        buffer = new ByteArrayOutputStream();
    }

    @After
    public void closeStreams() throws IOException {
        buffer.close();
    }

    @Test
    public void TestWcFile1() throws IOException {
        wc.setArgs(Collections.singletonList("file1.txt"));
        wc.execute(null, new PrintStream(buffer));
        assertEquals("3 6 18 file1.txt\n", buffer.toString("utf8"));
    }

    @Test
    public void TestWcFile2() throws IOException {
        wc.setArgs(Collections.singletonList("file2.txt"));
        wc.execute(null, new PrintStream(buffer));
        assertEquals("4 5 23 file2.txt\n", buffer.toString("utf8"));
    }

    @Test
    public void TestWcTwoFiles() throws IOException {
        wc.setArgs(Arrays.asList("file1.txt", "file2.txt"));
        wc.execute(null, new PrintStream(buffer));
        assertEquals("3 6 18 file1.txt\n4 5 23 file2.txt\n7 11 41 total\n",
                buffer.toString("utf8"));
    }

}