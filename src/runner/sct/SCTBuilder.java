package runner.sct;

import parser.pst.Node;

import java.util.ArrayList;

public class SCTBuilder {

    /**
     * Main function to call to build the scope tree from a given AST Node
     * @param astRoot   AST node to build scope tree of
     * @return          Scope tree root (relative to given AST Node)
     */
    public SCTNode buildScopeTree(Node astRoot) {
        SCTNode sctNode = new SCTNode();

        if (astRoot == null)
            return sctNode;

        return buildHelper(sctNode, astRoot);
    }

    /**
     * Recursive helper function to walk the tree and add declarations / create new sctNodes
     * @param sctNode   Scope tree currently being processed
     * @param astNode   AST Node currently being tested
     * @return  Current SCTNode or child if created
     */
    private SCTNode buildHelper(SCTNode sctNode, Node astNode) {
        if (astNode == null)
            return sctNode;

        if (isBlock(astNode)) {
            sctNode = handleBlock(sctNode, astNode);
        } else if (isDeclaration(astNode)) {
            handleDeclaration(sctNode, astNode);
        } else if (isUse(astNode)) {
            handleUse(sctNode, astNode);
        } else {
            ArrayList<Node> children = astNode.getChildren();
            for (int i = children.size() - 1; i >= 0; i--) {
                buildHelper(sctNode, children.get(i));
            }
        }
        return sctNode;
    }

    /**
     * Is the given AST node a use?
     * @param astNode   Node to test
     * @return  TRUE if the given node is a use, FALSE if not
     */
    protected boolean isUse(Node astNode) {
        return astNode != null &&
                astNode.getKeyword().equals("id") &&
                !astNode.getParent().getKeyword().equals("equal");
    }

    /**
     * Handles searching the scope tree on use.
     * @param sctNode   Scope tree to search (will search parents too)
     * @param astNode   Node to  search for in SCT
     */
    private void handleUse(SCTNode sctNode, Node astNode) {
        if (sctNode == null || astNode == null)
            return;

        sctNode.findEntry(astNode);
    }

    /**
     * Is the given node a block?
     * @param astNode   Node to test
     * @return  TRUE if the given node is a block, FALSE if not
     */
    protected boolean isBlock(Node astNode) {
        return astNode != null && astNode.getKeyword().equals("brace1");
    }

    /**
     * Handles the creation and bilinking of new SCT nodes.
     * @param sctNode   Scope tree node to currently test.
     * @param astNode   Block AST Node
     * @return  Returns the kid SCT node created
     */
    private SCTNode handleBlock(SCTNode sctNode, Node astNode) {
        SCTNode sctKid = new SCTNode(sctNode);
        // link the block node with the created SCT Node
        astNode.setSctNode(sctKid);

        // Process the astNode's children now....
        ArrayList<Node> children = astNode.getChildren();
        for (Node child : children) {
            buildHelper(sctKid, child);
        }

        return sctKid;
    }

    /**
     * Is the given AST node a declaration of a variable?
     * @param astNode   Node to test
     * @return  TRUE if it is a declaration, FALSE if not
     */
    protected boolean isDeclaration(Node astNode) {
        return astNode != null &&
                astNode.getKeyword().equals("equal") &&
                astNode.getChildren().get(1).getKeyword().equals("id");
    }

    /**
     * Handles creating new entries into the scope tree of the given astNode at the given level of the SCT node.
     * @param sctNode   SCT node to add the new declaration to
     * @param astNode   AST node to create a symtab entry of
     */
    private void handleDeclaration(SCTNode sctNode, Node astNode) {
        // This will be a decl, so grab the acc
        TableEntry entry = new TableEntry(sctNode, astNode);
        sctNode.addSymbol(entry);
    }
}
