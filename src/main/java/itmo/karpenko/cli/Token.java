package itmo.karpenko.cli;

public class Token {

    public enum TokenType {
        DOUBLE_QUOTED,
        SINGLE_QUOTED,
        LITERALS
    }

    private final int begin;
    private final int end;

    private String strValue;
    final private TokenType type;

    public int getBegin() {
        return begin;
    }

    public int getEnd() {
        return end;
    }

    public TokenType getType() {
        return type;
    }

    @Override
    public String toString() {
        return strValue;
    }

    public void setStrValue(String strValue) {
        this.strValue = strValue;
    }

    Token(String strValue, int begin, int end, TokenType type) {
        this.strValue = strValue;
        this.begin = begin;
        this.end = end;
        this.type = type;
    }
}
