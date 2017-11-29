package runner;

import parser.Parser;
import parser.pst.Node;

import static java.lang.Thread.sleep;

public class Main {

    public static void main(String args[]) {
        Node astRoot = Parser.getASTRoot(true);
        System.out.println("\n\n");
        Parser.printTree(astRoot, 0);

        try {
            sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("\n\n");
        ASTRunner runner = new ASTRunner(astRoot);
        runner.printScopeTree();
        System.out.println("\n\n");
        runner.run();
    }
}
