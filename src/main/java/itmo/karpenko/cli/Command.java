package itmo.karpenko.cli;

import java.util.List;

/**
 * класс описывает команду для pipe line
 * если это первая команда, то inHead = true
 * по умолчанию одиночные команды такие
 */

public class Command {
    public String name;
    public List<String> args;
    boolean inHead;
    Command(String name, List<String> args, boolean inHead) {
        this.name = name;
        this.args = args;
        this.inHead = inHead;
    }
}
