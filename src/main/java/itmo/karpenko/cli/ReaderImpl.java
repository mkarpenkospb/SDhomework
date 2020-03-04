package itmo.karpenko.cli;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.*;

public class ReaderImpl implements Reader {

    private boolean closed = true;
    private char whatUnclosed = 0;


    @Override
    public String readStream() throws IOException {
        BufferedReader reader =
                new BufferedReader(new InputStreamReader(System.in));
        StringBuilder allInput = new StringBuilder();
        while (true) {
            String fromInput = reader.readLine();
            allInput.append(fromInput).append("\n");
            if (quotesBalance(fromInput)) {
                break;
            }
            System.out.print("> ");
        }
        return allInput.toString().trim();
    }

    @Override
    public boolean quotesBalance(String fromInput) {
        CharacterIterator it = new StringCharacterIterator(fromInput);
        while (it.current() != CharacterIterator.DONE) {
            if (closed && (it.current() == '"' || it.current() == '\'')) {
                    whatUnclosed = it.current();
                    closed = false;
            } else if (!closed && it.current() == whatUnclosed) {
                    closed = true;
            }
            it.next();
        }
        return closed;
    }

}
