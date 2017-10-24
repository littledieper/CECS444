package parser;

import lexer.Token;
import lexer.Tokenizer;
import parser.pst.PSTNode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;

public class Parser {

    private static Stack<String> inputStack;
    private static ArrayList<Token> tokens;
    private static PSTNode root;

    public static void main(String args[]) {

        ParseTable table = init();
        if (table == null) {
            System.out.println("The tokenizer really failed...");
            System.exit(1);
        }

        while(!inputStack.empty()) {
            String stackTop = inputStack.peek();
            String inputFront = tokens.get(0).getGrammar().getKeyword();
            System.out.println("TOP = " + stackTop + "   FRONT = " + inputFront);
            System.out.println(Arrays.toString(inputStack.toArray()));

            if (m1(stackTop, inputFront)) {
                inputStack.pop();
                tokens.remove(0);
            } else if (m2(stackTop)) {
                error("M2");
            } else {
                Rule rule = table.get(stackTop, inputFront);
                System.out.println("Got rule " + rule.getLhs() + " = " + rule.getRhs());
                // M3 : empty cell == ERROR
                if (m3(rule)) {
                    error("M3");
                } else {
                    m4(rule);
                } // end else
            } // end if/elseif/else
        } // end while
    } // end main


    private static ParseTable init() {
        Tokenizer tokenizer = new Tokenizer("program.txt");

        if (tokenizer.isReady())
            tokens = tokenizer.tokenize();

        if (tokens.isEmpty())
            return null;

        // SETUP : start the parser
        inputStack = new Stack<>();
        inputStack.push("$");
        inputStack.push("Pgm");

        return new ParseTable();
    }

    /**
     * Returns if the top of the stack is the equal to the front, or if we've reached the end of the stack.
     * @param top       top of the symbol stack
     * @param front     front of input stream
     * @return      TRUE: when top is equal to the front, or when we've reached the bottom of the stack.
     */
    private static boolean m1(String top, String front) {
        return top.equals(front) || top.equals("$");
    }

    /**
     * Returns if the top of the stack is a temrinal symbol.
     * @param top       top of the symbol stack
     * @return      TRUE: when the top of the stack is a terminal symbol
     *              FALSE: when top is an empty string, or it is not terminal
     */
    private static boolean m2(String top) {
        return !top.isEmpty() && Character.isLowerCase(top.charAt(0));
    }

    /**
     * Returns if it's null...
     * @param rule  rule to test
     * @return      TRUE if null, FALSE if not.
     */
    private static boolean m3(Rule rule) {
        return rule == null;
    }

    /**
     * Pops the top of the stack if the rule is an epsilon rule or if there is a matching rule in the parse table.
     * Pushes the reversed of the RHS of the rule if it is a matching rule.
     * @param rule      rule to test
     */
    private static void m4(Rule rule) {
        // if the rule we get from the table is an epsilon rule, just pop it off the stack and move on
        if (rule.isEpsilonRule()) {
            inputStack.pop();
        } else {
            // M4: pop stack + reversed RHS onto stack
            inputStack.pop();

            String[] reversed = rule.getReversedRhsArray();
            for (String keyword : reversed)
                inputStack.push(keyword);
        }
    }

    private static void error(String errorLoc) {
        System.out.println(errorLoc + " ERROR.");
        System.exit(1);
    }
} // end Parser
