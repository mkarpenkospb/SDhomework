package itmo.karpenko.cli;


import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Этот класс предназначен чтобы различать
 * слова без кавычек и с разным типом кавычек
 * Тип 0 - с двойными кавычками
 * Тип 1 - с одинарными кавычками
 * Тип 2 - без кавычек
 */
public class Token {
    String strValue;
    int begin;
    int end;
    int type;
    Token(String strValue, int begin, int end, int type) {
        this.strValue = strValue;
        this.begin = begin;
        this.end = end;
        this.type = type;
    }

    /**
     * Преобразует строку список токетов 3 типа
     * Регулярное - выражение -позиции начала слов
     * Выделяет | в отдельный токен
     * @param fromInput - часть входной строки без кавычек
     * @param d - позиция начала этой строки в исходной
     * @return -  список токенов второго типа
     */
    public static List<Token> spliType2(String fromInput, int d) {
        Pattern varAssigne1 =
                Pattern.compile("[^ \\n\\t]+");
        Matcher matcher1 = varAssigne1.matcher(fromInput);
        List<Token> result0 = new ArrayList<>();
        int beg = 0;
        int end = 0;
        while (matcher1.find())
        {
            beg = matcher1.start();
            end = matcher1.end();
            result0.add(new Token(fromInput.substring(beg, end),
                    beg + d, end + d - 1, 2));
        }

        Pattern varAssigne =
                Pattern.compile("\\|");
        List<Token> result1 = new ArrayList<>();
        String processed;
        int len = 0;
        int delta = 0;
        for (Token token: result0) {
            Matcher matcher2 = varAssigne.matcher(token.strValue);
            if (!token.strValue.contains("|") || token.strValue.length() == 1) {
                result1.add(token);
                continue;
            }
            processed = token.strValue;
            len = processed.length();
            while (matcher2.find()) {
                delta = len - processed.length();
                beg = matcher2.start();
                end = matcher2.end();
                if (beg > delta) {
                    result1.add(new Token(token.strValue.substring(delta, beg),
                            token.begin + delta, token.begin + beg - 1, 2));
                }
                result1.add(new Token(token.strValue.substring(beg, end),
                        token.begin + beg, token.begin + end - 1, 2));
                if(end < processed.length() /*&& processed.charAt(end) != '|'*/) {
                    processed = processed.substring(end - delta);
                }
            }

            if(end < processed.length() /*&& processed.charAt(end) != '|'*/) {
                result1.add(new Token(token.strValue.substring(end),
                        token.begin + end, token.end, 2));
            }
        }
        return result1;
    }
}
