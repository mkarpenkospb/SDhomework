package itmo.karpenko.cli;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;

import static org.junit.Assert.*;

public class InnerCatTest {
    private InnerCat cat;

    @Before
    public void setUp() {
        cat = new InnerCat();
    }

    @Test
    public void TestCat() throws IOException {
        String res = cat.execute(Arrays.asList("file1.txt"));
        assertEquals("str 1\nstr 2\nstr 3\n",
                cat.execute(Arrays.asList("file1.txt")));
        assertEquals("str 11\nstr22\nstr33 nl\n\n",
                cat.execute(Arrays.asList("file2.txt")));
        assertEquals("str 1\nstr 2\nstr 3\nstr 11\nstr22\nstr33 nl\n\n",
                cat.execute(Arrays.asList("file1.txt", "file2.txt")));
    }

}