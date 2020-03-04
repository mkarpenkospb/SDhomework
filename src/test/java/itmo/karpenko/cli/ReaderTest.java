package itmo.karpenko.cli;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNull;

public class ReaderTest {
    private ReaderImpl reader;

    @Before
    public void setUp() {
        reader = new ReaderImpl();
    }


    @Test
    public void testBalansed() {
        assertTrue(reader.quotesBalance("\"hjhjh\" hghgh ''  '' \"jkjkj'jkjkj\""));
        assertFalse(reader.quotesBalance("\"hjhjh\" hghgh ''  '' jkjkj'jkjkj\""));
    }


    @Test
    public void testAssigment() {
        assertTrue(Reader.isAssigment("a=7"));
        assertTrue(Reader.isAssigment("a=7hjhrr"));
        assertTrue(Reader.isAssigment("absd22=7hjhrr"));
        assertFalse(Reader.isAssigment("absd22=7hjhrr dd"));
        assertFalse(Reader.isAssigment("2nj=57"));
        assertFalse(Reader.isAssigment("2'n'j=57"));
    }

    @Test
    public void testTokens() {
        VariablesImpl env = new VariablesImpl();
        assertNull(Reader.parseString("a=7", env));
        assertNull(Reader.parseString("b=8", env));
        List<Token> inputTokens = Reader.parseString("\"echo\" $a$b", env);
        assertEquals(2, inputTokens.size());
        assertEquals("78", inputTokens.get(1).strValue);
        List<Token> inputTokens2 = Reader.parseString("\"echo\" $c$b", env);
        assertEquals("8", inputTokens2.get(1).strValue);
    }


}