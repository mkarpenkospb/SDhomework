package itmo.karpenko.cli;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;

import static org.junit.Assert.*;

public class InnerWcTest {
    private InnerWc wc;

    @Before
    public void setUp() {
        wc = new InnerWc();
    }

    @Test
    public void TestWc() throws IOException {
        assertEquals("3 6 18\n",
                wc.execute(Arrays.asList("file1.txt")));
        assertEquals("4 5 23\n",
                wc.execute(Arrays.asList("file2.txt")));
        assertEquals("3 6 18\n4 5 23\n7 11 41 total\n",
                wc.execute(Arrays.asList("file1.txt", "file2.txt")));
    }

}