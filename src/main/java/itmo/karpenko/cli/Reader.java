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
* чтения.
* */
public interface Reader {

    Pattern patternAssign =
            Pattern.compile("^[a-zA-Z_]+[A-Za-z0-9]*=([^ \"'|]+|\"[^\"]*\"|'[^']*'|)");

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
        Matcher m = patternAssign.matcher(fromInput);
        return m.matches();
    }
}
