package itmo.karpenko.cli;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
* Класс Reader читает входную строку
* Задача класса - понять,
* что строка закончилась или принять решение о продолжении
* чтения. Но затем этот класс стал обрабатывать строки.
* */
public interface Reader {
//    static String template = new String("\" \t'");
    static String typeQuotas = new String("\"'");
    /*
    * Читает входящую строку и собирает её
    * */
    String readStream() throws IOException;

    /**
    * Проверяет, что баланс скобок в строке
    * не нарушен. Возвращает false, если
    * нет возможность логически завершить строку
    * */
    boolean quotesBalance(String fromInput);

    static boolean isAssigment(String fromInput) {
        Pattern varAssigne =
                Pattern.compile("^[a-zA-Z_]+[A-Za-z0-9]*=([^ \"'|]+|\"[^\"]*\"|'[^']*'|)");
        Matcher m = varAssigne.matcher(fromInput);
        return m.matches();
    }


    /**
     * Функция уберает кавычки и разносит слова по
     * типам токенов, так же обрабатывает тип 2
     *
     * @see Token
     * @param fromInput - строка, которую ввел пользователь
     * @return -  набор токенов
     */

    static List<Token> getTokens(String fromInput) {
        int begin;
        int end;
        int currSearch = 0;
        List<Token> tokens = new ArrayList<>();
        String currToken;
        String tokensNoQuotas;
        char currQuote = 0;
        while (true) {
            if (fromInput.indexOf('\'', currSearch) == -1 &&
                    fromInput.indexOf('"', currSearch) == -1) {
                if (currSearch < fromInput.length()) {
                    tokensNoQuotas = fromInput.substring(currSearch);
                    tokens.addAll(Token.spliType2(tokensNoQuotas, currSearch));
                }
                break;
            }
            else if (fromInput.indexOf('\'', currSearch) == -1 ||
                    fromInput.indexOf('\"', currSearch) != -1 &&
                            fromInput.indexOf('"', currSearch) < fromInput.indexOf('\'', currSearch)
            ) {
                currQuote = '"';
            } else {

                currQuote = '\'';
            }
            begin = fromInput.indexOf(currQuote, currSearch);
            end = fromInput.indexOf(currQuote, begin + 1);
            currToken = fromInput.substring(begin + 1, end);
            // дублирование кода, плохо
            if (begin != currSearch) {
                tokensNoQuotas = fromInput.substring(currSearch, begin);
                tokens.addAll(Token.spliType2(tokensNoQuotas, currSearch));
            }

            tokens.add(new Token(currToken, begin + 1,
                    end - 1, typeQuotas.indexOf(currQuote)));
            currSearch = end + 1;
        }

        return tokens;



    };

    /**
     * функция подставляет переменные в $
     * @param tokens -
     * @param env
     * @return
     */
    static List<Token> parseTokens(List<Token> tokens, VariablesImpl env) {
        List<Token> result = new ArrayList<>();
        int beg = 0;
        int end = 0;
        String name, val, currentToken;
        for (Token token : tokens) {
            if (token.type == 1) {
                result.add(token);
                continue;
            }

            Pattern varAssigne =
                    Pattern.compile("\\$[a-zA-Z_]+[A-Za-z0-9]*\\b");
            Matcher matcher = varAssigne.matcher(token.strValue);
            currentToken = token.strValue;
            int d = 0;
            while (matcher.find()) {
                beg = matcher.start();
                end = matcher.end();
                name = token.strValue.substring(beg + 1, end);
                val = env.substituteVar(name);
                currentToken = currentToken.replace("$" + name, val);
            }
            token.strValue = currentToken;
            result.add(token);
        }
        return result;
    }


    /**
     *
     * @param fromInput строка, введенная пользователем
     * @param environment переменне
     * @return набор токенов, отличный по кавычкам, с подстваленными переменными
     * и выдеренными |
     */
    static List<Token> parseString(String fromInput, VariablesImpl environment) {
        List<Token> tokensFirstPass = getTokens(fromInput);
        List<Token> tokensSeckondPass = parseTokens(tokensFirstPass, environment);
        if (Reader.isAssigment(fromInput)) {
            environment.saveVar(tokensSeckondPass.get(0).strValue);
            return null;
        }
        return tokensSeckondPass;
    };

}
