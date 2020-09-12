package itmo.karpenko.cli;

import java.util.HashMap;

public class Variables {

    final private HashMap<String, String> env = new HashMap<>();

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
