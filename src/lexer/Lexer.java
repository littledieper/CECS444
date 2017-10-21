package lexer;

import java.util.ArrayList;

/**         Lexer.java
 *  Main runnable class that displays all the tokens proccessed by the Tokenizer.
 *
 *  @author Alex Diep
 */
public class Lexer {
    public static void main(String args[]) {
        Tokenizer tokenizer = null;

        if (args.length > 0)
            tokenizer = new Tokenizer(args[0]);
        else
            tokenizer = new Tokenizer("program.txt");

        ArrayList<Token> tokens = null;
        if (tokenizer.isReady())
            tokens = tokenizer.tokenize();

        if (tokens != null && tokens.size() > 0) {
            for (Token t : tokens) {
                System.out.println(t);
            }
        } else
            System.out.println("No tokens");

        tokenizer.close();
    }
}
