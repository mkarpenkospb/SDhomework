package itmo.karpenko.cli;

import java.util.List;
import java.util.HashMap;


public interface Variables {

    void saveVar(String tokens);
    String substituteVar(String name);
}
