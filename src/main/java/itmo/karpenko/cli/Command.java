package itmo.karpenko.cli;

import java.util.ArrayList;
import java.util.List;

/**
 * класс описывает команду для pipe line
 * если это первая команда, то inHead = true
 * по умолчанию одиночные команды такие
 */

public class Command {
    private String name = null;
    private List<String> args;

    public String getName() {
        return name;
    }

    public List<String> getArgs() {
        return args;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setArgs(List<String> args) {
        this.args = args;
    }

    Command() {
        args = new ArrayList<>();
    }

    Command(String name, List<String> args) {
        this.name = name;
        this.args = args;
    }
}
