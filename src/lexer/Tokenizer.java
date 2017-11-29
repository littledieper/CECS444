package lexer;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;

/**         Tokenizer.java
 *  Class that holds the main token processing logic. Reads a file containing code and creates tokens of each
 *  of the keywords using the A2 Lexcon.
 *
 *  @author Alex Diep
 */
public class Tokenizer {

    /** Platform specific end of line character */
    private static final char eol = System.lineSeparator().charAt(0);
    /** HashMap to hold lexcon id + definitions. */
    private static final HashMap<String, Grammar> lexcon = populateLexcon();
    /** BufferedReader to parse the file */
    private BufferedReader file;
    /** Counts number of lines */
    private int lineCount;
    /** Holds created tokens */
    private ArrayList<Token> tokens;
    /** String added to in order to create tokens */
    private String token;

    /**
     * Constructor -- creates a tokenizer to read from a specific file.
     * @param fileName File to read from.
     */
    public Tokenizer(String fileName) {
        tokens = new ArrayList<Token>();
        token = "";
        lineCount = 1;

        try {
            file = new BufferedReader(new FileReader(fileName));
        } catch (FileNotFoundException e) {
            System.out.println("FILE NOT FOUND");

            Token t = new Token(lineCount, "");
            t.setGrammar(new Grammar(99, "error"));
            tokens.add(t);
        }
    }

    // TOKENIZER FUNCTIONS -------------------------------------------------------------------------------------------

    /**
     * Returns if the Tokenizer is ready to produce tokens.
     * Usually, this means that if the BufferedReader failed to open the file.
     *
     * @return TRUE if the Tokenizer is ready to create some tokens, FALSE if not.
     */
    public boolean isReady() {
        boolean ready = false;
        try {
            ready = file != null && file.ready();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ready;
    }

    /**
     * Processes all the tokens in the given file.
     * @return ArrayList of Strings containing potential tokens.
     */
    public ArrayList<Token> tokenize() {
        int value = 0;
        try {
            while ((value = file.read()) != -1) {
                char input = (char) value;

                if (isPunctuation(input))
                    handlePunctuation(input);
                else if (Character.isLetter(input))
                    handleKeyword(input);
                else if (Character.isDigit(input))
                    handleDigit(input);
            }

            // make the eof token
            Token t= new Token(lineCount, "");
            t.setGrammar(new Grammar(0, "eol"));
            tokens.add(t);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return tokens;
    }

    /**
     * Closes the Tokenizer (mainly the buffered reader)
     */
    public void close() {
        if (file != null) {
            tokens.clear();
            try {
                file.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Adds the cached string into the tokenStrings to be returned to the Lexer.
     */
    private void createToken() {
        if (token.length() != 0) {
            Token t = new Token(lineCount, token);
            addGrammarToToken(t);

            tokens.add(t);
            token = "";
        }
    }

    /**
     * Adds the grammar to the token, with some processing.
     * @param token
     */
    private void addGrammarToToken(Token token) {
        String tokenValue = token.getValue();

        // we have to manage special cases for strings, numbers, and ids
        if (tokenValue.charAt(0) == '"') {
            // we've found a string
            token.setGrammar(lexcon.get("string"));
        } else if (tokenValue.equals("int") || tokenValue.equals("float") || tokenValue.equals("string")) {
            // these are keywords for int/float/string, so they should get the keyword grammar
            token.setGrammar(lexcon.get("k" + tokenValue));
        } else if (tokenValue.length() == 1 && tokenValue.charAt(0) == '-') {
            token.setGrammar(lexcon.get("-"));
        } else if (tokenValue.matches("[-.0-9]+")) {
            // found int/float digits
            if (tokenValue.contains(".")) // if it has a dot, it's a float
                token.setGrammar(lexcon.get("float"));
            else
                token.setGrammar(lexcon.get("int"));
        } else {
            // if we've gotten this far, it's a symbol, actual keyword, or an ID, so just get it.
            Grammar grammar = lexcon.get(tokenValue);
            if (grammar == null)
                token.setGrammar(lexcon.get("id"));
            else
                token.setGrammar(grammar);
        }
    }

    /* ===============================================================================================================
                                                TOKEN HANDLER FUNCTIONS
    Handler functions are broken up into punctuation, digits, and keywords and from there are broken up into states
    like how DFA are.

    Generally, the order of importance is (1) handle terminating cases, (2) handle "next stage" cases,
    (3) handle special cases. I sometimes will break this order though...
    =================================================================================================================*/

    // ---------------------------------------------- PUNCTUATION -----------------------------------------------------

    /**
     * Checks to see if the given char is a punctuation.
     *
     * @param c character to test
     * @return TRUE if punctuation, FALSE if not.
     */
    private static boolean isPunctuation(char c) {
        return Pattern.matches("\\p{Punct}", Character.toString(c)) || c == eol;
    }

    /**
     *  Handles all punctuation and creates them into tokens.
     * @param input
     * @throws IOException
     */
    private void handlePunctuation(char input) throws IOException {
        if (input == eol) {
            lineCount++;
        } else if (Pattern.matches("[;,\\[\\](){}:.*^]", Character.toString(input))) {
            // Handle characters that terminate here
            token += input;
            createToken();
        } else if (Pattern.matches("[!><=]", Character.toString(input))) {
            // Handle characters that probably have stuff after it
            token += input;
            handlePunctuation2((char) file.read());
        } else if (input == '-') {
            // either self, oparrow, or integer sign
            token += input;

            char value = (char) file.peek();
            if (Character.isWhitespace(value)) {
                // its itself
                createToken();
            } else if (Character.isDigit(value)) {
                // we're handling negative numbers
                handleDigit((char) file.read());
            } else {
                // it has stuff after it
                handlePunctuation2((char) file.read());
            }
        } else if (input == '+') {
            // handle either + token or integer sign
            char value = (char) file.peek();
            if (Character.isDigit(value)) { // if there's a digit value after, its an int/float
                // we don't need to consume it for positive integers
                handleDigit((char) file.read());
            } else { // it's its own token
                token += input;
                createToken();
            }
        } else if (input == '"') {
            // it's a string
            token += input;

            // read until we find and consume another quote or we reach EOF
            int value = file.peek();
            while (value != -1 && ((char) value != '"')) {
                token += (char) file.read();
                value = file.peek();
            }
            // the loop won't consume the quote, which we want, so do it...
            token += (char) file.read();

            createToken();
        } else if (input == '/') {
            // it's a comment or divide
            if ((char) file.peek() == '/') {
                file.read(); // found a comment. consume it, but don't make it into a token

                // read until we reach the end of the line
                int value = file.peek();
                while (value != -1) {
                    char c = (char) value;
                    if (c != eol) {
                        file.read();
                        value = file.peek();
                    } else {
                        value = -1;
                    }
                } // end while
            } // end if
            else {
                token += input; // it's not a comment, so its just a divide
                createToken(); // and it's also it's own token
            }
        } else if (input == '_') {
            // we found an id
            getIdentifier(input);
            createToken();
        }
    } // end handlePunctuation()

    private void handlePunctuation2(char input) {
        // we've come from !<>=, so available matches are ><=
        if (Pattern.matches("[<>=]", Character.toString(input))) {
            token += input;
        }

        createToken();
    }

    //  ------------------------------------------------ DIGITS --------------------------------------------------------

    /**
     *  Handles all forms of digits and terminates on itself.
     * @param input
     * @throws IOException
     */
    private void handleDigit(char input) throws IOException {
        token += input;

        boolean reading = true;
        while (reading) {
            int next = file.peek();
            if (next != -1 && (Character.isDigit((char) next) || (char) next == '.'))
                token += (char) file.read();
            else
                reading = false;
        }

        createToken();
    }

    // ---------------------------------------------- KEYWORDS ---------------------------------------------------------

    /**
     * Mainly used in one of the handleKeyword functions, but it will grab all numbers/digits/underscores until
     * we hit the next whitespace.
     *
     * @param input
     * @throws IOException
     */
    private void getIdentifier(char input) throws IOException{
        token += input;

        int value = file.peek();
        while (value != -1) {
            char c = (char) value;

            if (Character.isWhitespace(c) || !(Character.isLetterOrDigit(c) || c == '_'))
                break;

            token += (char) file.read();
            value = file.peek();

        }
    }

    /**
     * Initial starter for handling potential keywords.
     * Keywords that terminate here:
     *
     * @param input first character of potential token
     * @throws IOException
     */
    private void handleKeyword(char input) throws IOException {
        if (Pattern.matches("[ceifmnprsw]", Character.toString(input))) {
            token += input;
            handleKeyword2((char) file.read());
        } else
            getIdentifier(input);

        createToken();
    }

    /**
     * Level 2 of finite automata for handling potential keywords.
     * Keywords that terminate here: if
     *
     * @param input second character of potential token
     * @throws IOException
     */
    private void handleKeyword2(char input) throws IOException {
        if (input == 'f' && Character.isWhitespace((char) file.peek())) {
            // terminating if
            token += input;
        } else if (Pattern.matches("[acelnrth]", Character.toString(input))) {
            token += input;
            handleKeyword3((char) file.read());
        } else
            getIdentifier(input);
    }

    /**
     * Level 3 of finite automata for handling potential keywords.
     * Keywords that terminate here: int, new, fcn
     * @param input
     * @throws IOException
     */
    private void handleKeyword3(char input) throws IOException {
        if (Pattern.matches("[nw]", Character.toString(input))) {
            token += input;
        } else if (Pattern.matches("[aoirsp]", Character.toString(input))) {
            token += input;
            handleKeyword4((char) file.read());
        } else if (input == 't') {
            token += input;
            // handle terminating int, but continuing return
            if ((char) file.peek() == 'u')
                handleKeyword4((char) file.read());
        } else
            getIdentifier(input);
    }

    /**
     * Level 4 of finite automata for handling potential keywords.
     * Keywords that terminate here: prog
     * @param input
     * @throws IOException
     */
    private void handleKeyword4(char input) throws IOException {
        if (Pattern.matches("[g]", Character.toString(input))) {
            token += input;
        } else if (Pattern.matches("[alisneu]", Character.toString(input))) {
            token += input;
            // we have to handle main/else here.
            if (!Character.isWhitespace((char) file.peek()))
                handleKeyword5((char) file.read());
        } else
            getIdentifier(input);
    }

    /**
     * Level 5 of finite automata for handling potential keywords.
     * Keywords that terminate here: string, elseif, while, input, class, float, print, return
     * @param input
     * @throws IOException
     */
    private void handleKeyword5(char input) throws IOException {
        if (Pattern.matches("[est]", Character.toString(input))) {
            token += input;
        } else if (Pattern.matches("[inr]", Character.toString(input))) {
            // we'll just handle the case of string, elseif, and return here.
            token += input;

            char value = (char) file.peek();
            if (Pattern.matches("[gfn]", Character.toString(value))) {
                token += (char) file.read();
            }
        } else
            getIdentifier(input);
    }

    private static HashMap<String, Grammar> populateLexcon()
    {
        HashMap<String, Grammar> lexcon = new HashMap<String, Grammar>();

        // Declarations
        lexcon.put("id", new Grammar(2, "id"));
        lexcon.put("int", new Grammar(3, "int"));
        lexcon.put("float", new Grammar(4, "float"));
        lexcon.put("string", new Grammar(5, "string"));

        // Unpaired delimiters
        lexcon.put(",", new Grammar(6, "comma"));
        lexcon.put(";", new Grammar(7, "semi"));

        // Keywords
        lexcon.put("prog", new Grammar(10, "kprog"));
        lexcon.put("main", new Grammar(11, "kmain"));
        lexcon.put("fcn", new Grammar(12, "kfcn"));
        lexcon.put("class", new Grammar(13, "kclass"));
        lexcon.put("kfloat", new Grammar(15, "kfloat"));
        lexcon.put("kint", new Grammar(16, "kint"));
        lexcon.put("kstring", new Grammar(17, "kstring"));
        lexcon.put("if", new Grammar(18, "kif"));
        lexcon.put("elseif", new Grammar(19, "kelseif"));
        lexcon.put("else", new Grammar(20, "kelse"));
        lexcon.put("while", new Grammar(21, "kwhile"));
        lexcon.put("input", new Grammar(22, "kinput"));
        lexcon.put("print", new Grammar(23, "kprint"));
        lexcon.put("new", new Grammar(24, "knew"));
        lexcon.put("return", new Grammar(25, "kreturn"));

        // Paired delimiters
        lexcon.put("<", new Grammar(31, "angle1"));
        lexcon.put(">", new Grammar(32, "angle2"));
        lexcon.put("{", new Grammar(33, "brace1"));
        lexcon.put("}", new Grammar(34, "brace2"));
        lexcon.put("[", new Grammar(35, "bracket1"));
        lexcon.put("]", new Grammar(36, "bracket2"));
        lexcon.put("(", new Grammar(37, "parens1"));
        lexcon.put(")", new Grammar(38, "parens2"));

        // Other punctuation
        lexcon.put("*", new Grammar(41, "aster"));
        lexcon.put("^", new Grammar(42, "caret"));
        lexcon.put(":", new Grammar(43, "colon"));
        lexcon.put(".", new Grammar(44, "dot"));
        lexcon.put("=", new Grammar(45, "equal"));
        lexcon.put("-", new Grammar(46, "minus"));
        lexcon.put("+", new Grammar(47, "plus"));
        lexcon.put("/", new Grammar(48, "slash"));

        // Multi-char ops
        lexcon.put("->", new Grammar(51, "oparrow"));
        lexcon.put("==", new Grammar(52, "opeq"));
        lexcon.put("!=", new Grammar(53, "opne"));
        lexcon.put("<=", new Grammar(54, "ople"));
        lexcon.put(">=", new Grammar(55, "opge"));
        lexcon.put("<<", new Grammar(56, "opshl"));
        lexcon.put(">>", new Grammar(57, "opshr"));

        // Misc
        lexcon.put("", new Grammar(99, "error")); // error
        lexcon.put(" ", new Grammar(0, "eol")); // end of input

        return lexcon;
    }
}
