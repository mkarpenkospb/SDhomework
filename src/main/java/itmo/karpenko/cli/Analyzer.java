package itmo.karpenko.cli;

public interface Analyzer {

    String[] getTokens();

    String[] varLayer(String fromReader);

}
