package parser;

import lexer.Token;
import lexer.Tokenizer;
import parser.pst.Node;
import parser.pst.ASTConverter;

import java.util.ArrayList;
import java.util.Stack;

public class Parser {

    private static Stack<Node> inputStack;
    private static ArrayList<Token> tokens;
    private static Node root;

    public static void main(String args[]) {
        Node astRoot = getASTRoot(true);
        printTree(astRoot, 0);
    }

    public static Node getASTRoot(boolean printPST) {
        ParseTable table = init();
        if (table == null) {
            System.out.println("The tokenizer really failed...");
            System.exit(1);
        }

        while(!inputStack.empty()) {
            String stackTop = inputStack.peek().getKeyword();
            String inputFront = tokens.get(0).getGrammar().getKeyword();
            //System.out.println("TOP = " + stackTop + "   FRONT = " + inputFront);

            if (stackTop.equals(inputFront) || stackTop.equals("$")) {
                m1();
            } else if (m2(stackTop)) {
                error("M2");
            } else {
                Rule rule = table.get(stackTop, inputFront);
                // M3 : empty cell == ERROR
                if (m3(rule)) {
                    error("M3");
                } else {
                    m4(rule);
                } // end else
            } // end if/elseif/else
        } // end while

        if (printPST)
            printTree(root, 0);

        return ASTConverter.convert(root);
    } // end main

    /**
     * Initializes the LL Parse table to read from.
     * @return ParseTable object containing the LL Parse table.
     */
    private static ParseTable init() {
        Tokenizer tokenizer = new Tokenizer("program.txt");

        if (tokenizer.isReady())
            tokens = tokenizer.tokenize();

        if (tokens == null || tokens.isEmpty())
            return null;

        // SETUP : start the parser
        inputStack = new Stack<Node>();
        root = new Node("Pgm");
        inputStack.push(root);

        return new ParseTable();
    }

    /**
     * Pops the node at the top of the stack and adds the value of the front of the input stream
     * only if it is a id, int, string, etc.
     */
    private static void m1() {
        Node poppedNode = inputStack.pop();
        Token poppedToken = tokens.remove(0);

        poppedNode.setValue(poppedToken.getValue());
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
            Node parent = inputStack.pop();

            String[] reversed = rule.getReversedRhsArray();
            for (String keyword : reversed) {
                Node child = new Node(keyword);
                parent.addChild(child);
                inputStack.push(child);
            }
        }
    }

    /**
     * Simple error function.
     * @param errorLoc String (usually m1-m4).
     */
    private static void error(String errorLoc) {
        System.out.println(errorLoc + " ERROR.");
        System.exit(1);
    }

    /**
     * Recursively prints the tree in a pre-order fashion, so that a nodes children are printed before the next sibling.
     * @param root  Root (or root @ that level) of the Tree
     * @param level The "level" or "depth" that the node is at
     */
    public static void printTree(Node root, int level) {
        if (root == null)
            return;

        String tabbing = getSpacing(level);
        System.out.println(tabbing + "(" + root + ")");

        level++;
        ArrayList<Node> children = root.getChildren();
        for (Node child : children) {
            printTree(child, level);
        }
    }

    /**
     * Helper of printTree.
     * Returns "level" number of spaces in a String to be prefixed to a child node.
     * @param level "level" or "depth" that the node is at
     * @return String containing "level" number of spaces.
     */
    private static String getSpacing(int level) {
        String str = "";
        for (int i = 0; i < level; i++)
            str = str + "  ";

        return str;
    }
} // end Parser
