package itmo.karpenko.cli;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TestImpl {
    public static void main(String[] str) throws IOException {
        Reader reader = new ReaderImpl();
        VariablesImpl environment = new VariablesImpl();
//        boolean isBalansed = reader.quotesBalance("\"some\" String");
//        String inputStream = reader.readStream();
//        String testStr = "echo";
//        List <String> tokens = reader.getTokens(testStr);
//        System.out.println("Hello\nMasha");


/*   Variable matching */
//        String testAssign = "\"ads=36hj|\"";
//        boolean isAssign = Reader.isAssigment(testAssign);
//        System.out.println("Hello\nMasha");

/*   Variable save */
//        String testAssign = "ads=36hj";
        // вернет null если это было присвоение
//        List<Command> cmd = Reader.parseString(testAssign, environment);
//
//        System.out.println("Hello\nMasha");

/* Variable substitute */
//        String toTest = "sdgs \"dgga\" sdgsg dgs |jjkj\"$ads\"";
//        List<Command> cmd2 = Reader.parseString(toTest, environment);

/* Test new tokens*/
//        environment.env.put("asf", "asfaaa");
//        environment.env.put("a", "12345");
//        String toTest = "sdgs \"dgga\" $a sdgsg $c dgs |jjkj\"$asf\"";
//        List<Token> test = Reader.parseString(toTest, environment);
//        System.out.println("Hello\nMasha");
//        String toTest = "ghghg | hjhjh |jkjkj|jkjkjkj| |hjhf";
//        List<Token> test = Token.spliType2(toTest, 0);
//        System.out.println("Hello\nMasha");


        /* test commands*/
//        String toTest = "cat file1.txt";
//        List<Token> test = Reader.parseString(toTest, environment);
//        List<Command> ctest = Expression.getPipe(test);
//        System.out.println("sdg");

          List<String> args = new ArrayList<String>() {
              {
                  add("file1.txt");
                  add("file2.txt");
              }
            };
          Programm cat = new InnerCat();
          String result = cat.execute(args);
          System.out.print("shs");

//        String wcRes = Innerwc.countFileStat("file1.txt");
//        System.out.println(wcRes);
//
//        innerCat.readFile("file1.txt");
//
//        InnerEcho.echo(new String[]{"DFG", "hjhjh", "hjhjh"});
//
//        InnerPwd.pwd();
    }
}
;