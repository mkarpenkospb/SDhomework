package itmo.karpenko.cli;

import java.io.IOException;
import java.util.List;

/**
 * универсальный интерфейс для исполнения
 * внутренних программ
 */
public interface Program {
    /**
     * одиночное исполнение команды
     * @param args
     * @return
     * @throws IOException
     */
    String execute(List<String> args) throws IOException;

    /**
     * без аргументов
     * @return
     * @throws IOException
     */
    String execute() throws IOException;

    /**
     * для pipeline
     * @param arg
     * @return
     * @throws IOException
     */
    String execute(String arg) throws IOException;
}
