package itmo.karpenko.cli;

import java.util.HashMap;
import java.util.List;

public class VariablesImpl {

    public HashMap<String, String> env = new HashMap<String, String>();

    public void saveVar(String token) {
        int eqPosition = token.indexOf('=');
        String name = token.substring(0, eqPosition);
        String val = token.substring(eqPosition + 1);
        env.put(name, val);
    }

    public String substituteVar(String name) {
        return env.getOrDefault(name, "");
    }
}
