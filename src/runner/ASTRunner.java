package runner;

import parser.pst.Node;
import runner.sct.SCTBuilder;
import runner.sct.SCTNode;

import java.util.ArrayList;

public class ASTRunner extends SCTBuilder {

    /** Root of the AST Tree*/
    private Node astRoot;
    /** Root of the Scope tree */
    private SCTNode sctRoot;

    /**
     *  Creates an ASTRunner for an AST tree and builds a scope tree from the given AST tree.
     * @param astRoot Root of the AST to run
     */
    public ASTRunner(Node astRoot) {
        this.astRoot = astRoot;
        sctRoot = buildScopeTree(astRoot);
    }

    /**
     * Main function to call to run the AST.
     */
    public void run() {
        run(sctRoot, astRoot);
    }

    /**
     * Helper recursive function to run the AST
     * @param sctRoot   Scope tree node (at a given AST level)
     * @param astRoot   AST node that we're currently processing
     */
    private void run(SCTNode sctRoot, Node astRoot) {
        if (isBlock(astRoot))
            sctRoot = astRoot.getSctNode();

        if (isDeclaration(astRoot)) {
            // this also could be a redeclaration, so just in case lets find the entry and update it
            double value = Operations.doOperation(sctRoot, astRoot.getChildren().get(0));
            sctRoot.findAndUpdateEntry(astRoot.getChildren().get(1), value + "");
        } else if (isUse(astRoot)) {
            String value = sctRoot.findEntry(astRoot);
            sctRoot.findAndUpdateEntry(astRoot, value);
        } else {
            if (isPrint(astRoot))
                handlePrint(sctRoot, astRoot);
            if (isWhile(astRoot)) {
                handleWhile(sctRoot, astRoot);
                return; // don't handle the kids
            }
        }

        // Now handle children...
        ArrayList<Node> children = astRoot.getChildren();
        //for (Node child : children) {
        //    run(sctRoot, child);
        for (int i = children.size() - 1; i >= 0; --i)
            run(sctRoot, children.get(i));
    }

    /**
     *  Is the given AST Node the print function?
     * @param astNode   Node to test
     * @return      TRUE if it is a print astNode, FALSE if not.
     */
    private boolean isPrint(Node astNode) {
        if (astNode == null)
            return false;

        return astNode.getKeyword().equals("kprint");
    }

    /**
     * Handles the print function and
     * @param sctNode   Scope node to search for variables (if any)
     * @param astRoot   AST node with print function
     */
    private void handlePrint(SCTNode sctNode, Node astRoot) {
        // there's a parens1, so get the parenthesis's children
        ArrayList<Node> children = astRoot.getChildren().get(0).getChildren();
        Node nodeToPrint = children.get(1);
        System.out.print(nodeToPrint.getValue().replaceAll("^\"|\"$", ""));

        // there may be some arguments to handle, where we'll need to search the scope tree for the declaration....
        printArgs(sctNode, nodeToPrint);
        System.out.println(""); // print out the next line
    }

    /**
     * Helper recursive function to process any arguments the scope tree may have.
     * @param sctNode Scope tree to immediately search
     * @param astNode Child node (argument) of scope tree
     */
    private void printArgs(SCTNode sctNode, Node astNode) {
        // make sure the astNode has children first...
        ArrayList<Node> children = astNode.getChildren();
        if (children == null || children.isEmpty() || !children.get(0).getKeyword().equals("comma"))
            return;

        Node nodeToOperate = children.get(0).getChildren().get(0);
        if (nodeToOperate.getKeyword().equals("string"))
            System.out.print(nodeToOperate.getValue().replaceAll("^\"|\"$", ""));
        else
            System.out.print(Operations.doOperation(sctNode, nodeToOperate));

        // there might be more arguments, so we'll print those
        printArgs(sctNode, nodeToOperate);
    }

    /**
     * Is the given astNode a while function?
     * @param astNode   Node to test
     * @return  TRUE if the given node is a while node, FALSE if not.
     */
    private boolean isWhile(Node astNode) {
        if (astNode == null)
            return false;

        return astNode.getKeyword().equals("kwhile");
    }

    /**
     * Handles and runs the while loop node.
     * @param sctNode   Scope tree to search for any declarations
     * @param astNode   AST while node
     */
    private void handleWhile(SCTNode sctNode, Node astNode) {
        Node condition = astNode.getChildren().get(1);
        Node body = astNode.getChildren().get(0);

        while (evaluate(sctNode, condition)) {
            run(sctNode, body);
        }
    }

    /**
     * Evaluates operators
     * @param sctNode   Scope tree node to look from
     * @param astNode   AST operator node
     * @return      TRUE if the operator node results to true
     *              FALSE if the operator node results to false
     */
    private boolean evaluate(SCTNode sctNode, Node astNode) {
        // there's going to be a parenthesis, so actually grab the child we want to operate on
        String left = sctNode.findEntry(astNode.getChildren().get(1));
        Node condition = astNode.getChildren().get(1).getChildren().get(0);
        String right = sctNode.findEntry(condition.getChildren().get(0));

        double leftArg = Double.parseDouble(left); // get the numbers
        double rightArg = Double.parseDouble(right);

        String operator = condition.getKeyword();
        switch(operator) {
            case "opeq":
                return leftArg == rightArg;
            case "opne":
                return leftArg != rightArg;
            case "ople":
                return leftArg <= rightArg;
            case "opge":
                return leftArg >= rightArg;
            case "angle1":
                return leftArg < rightArg;
            case "angle2":
                return leftArg > rightArg;
            default:
                return false;
        }
    }


    // =========================== PRINTING TREE FUNCTIONS ======================================

    /**
     * Recursively prints the tree in a pre-order fashion, so that a nodes children are printed before the next sibling.
     */
    public void printScopeTree() {
        printTree(sctRoot, 0);
    }

    /**
     * Recursively prints the tree in a pre-order fashion, so that a nodes children are printed before the next sibling.
     * @param root  Root (or root @ that level) of the Tree
     * @param level The "level" or "depth" that the node is at
     */
    private void printTree(SCTNode root, int level) {
        if (root == null)
            return;

        String tabbing = getSpacing(level);
        System.out.println(tabbing + "(" + root + ")");

        level++;
        ArrayList<SCTNode> children = root.getChildren();
        for (SCTNode child : children) {
            printTree(child, level);
        }
    }

    /**
     * Helper of printTree.
     * Returns "level" number of spaces in a String to be prefixed to a child node.
     * @param level "level" or "depth" that the node is at
     * @return String containing "level" number of spaces.
     */
    private String getSpacing(int level) {
        String str = "";
        for (int i = 0; i < level; i++)
            str = str + "  ";

        return str;
    }
}
