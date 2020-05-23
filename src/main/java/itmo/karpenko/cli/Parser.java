package itmo.karpenko.cli;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {
    // регулярки для разбиения строки
    static private final Pattern patternSplit = Pattern.compile("[^ \\n\\t]+");
    static private final Pattern patternPipeline = Pattern.compile("\\|");
    static private final Pattern varAssign = Pattern.compile("\\$[a-zA-Z_]+[A-Za-z0-9]*\\b");

    static Token.TokenType quotesType(char ch) {
        if (ch == '"')
            return Token.TokenType.DOUBLE_QUOTED;
        return Token.TokenType.SINGLE_QUOTED;
    }

    public static List<Token> parseInputString(String fromInput, int d) {
        Matcher matcher1 = patternSplit.matcher(fromInput);
        List<Token> result0 = new ArrayList<>();
        int beg = 0;
        int end = 0;
        while (matcher1.find())
        {
            beg = matcher1.start();
            end = matcher1.end();
            result0.add(new Token(fromInput.substring(beg, end),
                    beg + d, end + d - 1, Token.TokenType.LITERALS));
        }

        List<Token> result1 = new ArrayList<>();
        String processed;
        int len;
        int delta;
        for (Token token: result0) {
            Matcher matcher2 = patternPipeline.matcher(token.toString());
            if (!token.toString().contains("|") || token.toString().length() == 1) {
                result1.add(token);
                continue;
            }
            processed = token.toString();
            len = processed.length();
            while (matcher2.find()) {
                delta = len - processed.length();
                beg = matcher2.start();
                end = matcher2.end();
                if (beg > delta) {
                    result1.add(new Token(token.toString().substring(delta, beg),
                            token.getBegin() + delta, token.getBegin() + beg - 1, Token.TokenType.LITERALS));
                }
                result1.add(new Token(token.toString().substring(beg, end),
                        token.getBegin() + beg, token.getBegin() + end - 1, Token.TokenType.LITERALS));
                if(end < processed.length()) {
                    processed = processed.substring(end - delta);
                }
            }
            if(end < processed.length()) {
                result1.add(new Token(token.toString().substring(end),
                        token.getBegin() + end, token.getEnd(), Token.TokenType.LITERALS));
            }
        }
        return result1;
    }


    static List<Token> getTokens(String fromInput) {
        int begin;
        int end;
        int currSearch = 0;
        List<Token> tokens = new ArrayList<>();
        String currToken;
        String tokensNoQuotas;
        char currQuote;
        while (true) {
            if (fromInput.indexOf('\'', currSearch) == -1 &&
                    fromInput.indexOf('"', currSearch) == -1) {
                if (currSearch < fromInput.length()) {
                    tokensNoQuotas = fromInput.substring(currSearch);
                    tokens.addAll(parseInputString(tokensNoQuotas, currSearch));
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
                tokens.addAll(parseInputString(tokensNoQuotas, currSearch));
            }
            tokens.add(new Token(currToken, begin + 1,
                    end - 1, quotesType(currQuote)));
            currSearch = end + 1;
        }
        return tokens;
    }

    /**
     * функция подставляет переменные в $
     * @param tokens -
     * @param env
     * @return
     */
    static List<Token> parseTokens(List<Token> tokens, Variables env) {
        List<Token> result = new ArrayList<>();
        int beg;
        int end;
        String name, val, currentToken;
        for (Token token : tokens) {
            if (token.getType() == Token.TokenType.SINGLE_QUOTED) {
                result.add(token);
                continue;
            }

            Matcher matcher = varAssign.matcher(token.toString());
            currentToken = token.toString();
            while (matcher.find()) {
                beg = matcher.start();
                end = matcher.end();
                name = token.toString().substring(beg + 1, end);
                val = env.substituteVar(name);
                currentToken = currentToken.replace("$" + name, val);
            }
            token.setStrValue(currentToken);
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
    static List<Token> parseString(String fromInput, Variables environment) {
        List<Token> tokensFirstPass = getTokens(fromInput);
        List<Token> tokensSecondPass = parseTokens(tokensFirstPass, environment);
        if (Reader.isAssigment(fromInput)) {
            environment.saveVar(tokensSecondPass.get(0).toString());
            return null;
        }
        return tokensSecondPass;
    }

}
