package runner.sct;

import parser.pst.Node;
import runner.Operations;

import java.util.ArrayList;
import java.util.HashSet;

public class SCTNode {

    /** Parent SCT Node     */
    private SCTNode parent;
    /** Children of this SCT Node */
    private ArrayList<SCTNode> children;
    /** Symbol table entries of this SCT Node */
    private HashSet<TableEntry> symbolTable;

    public SCTNode() {
        this.children = new ArrayList<>();
        this.symbolTable = new HashSet<>();
    }

    public SCTNode(SCTNode sctNode) {
        this();
        setParent(sctNode);
        sctNode.addChild(this);
    }

    public SCTNode getParent() {
        return parent;
    }

    public void setParent(SCTNode parent) {
        this.parent = parent;
    }

    public ArrayList<SCTNode> getChildren() {
        return children;
    }

    public void addChild(SCTNode node) {
        this.children.add(node);
    }

    public HashSet<TableEntry> getSymbolTable() {
        return symbolTable;
    }

    public void setSymbolTable(HashSet<TableEntry> symbolTable) {
        this.symbolTable = symbolTable;
    }

    /**
     * Adds new symbols to the SCTNode if they are not previously added
     * @param tableEntry    Entry to add
     */
    public void addSymbol(TableEntry tableEntry) {
        if (tableEntry == null)
            return;

        // straight up declaration w/ no operations
        if (tableEntry.getType().equals("int") ||
                tableEntry.getType().equals("float") ||
                tableEntry.getType().equals("string")) {
            if (!symbolTable.contains(tableEntry)) {
                symbolTable.add(tableEntry);
            }
        }

        Node nodeToTest = tableEntry.getAstNode().getChildren().get(0);
        if ((Operations.isOperation(nodeToTest) || nodeToTest.getKeyword().equals("id"))
                && findEntry(tableEntry.getAstNode().getChildren().get(1)).equals("")) {
            tableEntry.setValue(Operations.doOperation(tableEntry.getSctNode(), nodeToTest) + "");
            tableEntry.setType("float");
            symbolTable.add(tableEntry);
        }
    }

    /**
     * Finds and  updates a currently existing table entry in this SCTNode or a parent
     * @param astNode   AST Node to update
     * @param value     Value to update the node with
     * @return  TRUE on success, FALSE on failure (like when there is no declared value)
     */
    public boolean findAndUpdateEntry(Node astNode, String value) {
        if (astNode == null)
            return false;

        for (TableEntry entry : symbolTable) {
            // Find the matching value
            if (entry.getId().equals(astNode.getValue())) {
                entry.setValue(value);
                return true;
            }
        }

        if (this.parent != null)
            return this.parent.findAndUpdateEntry(astNode, value);
        else
            return false;
    }

    /**
     * Finds the value that belongs to that declaration
     * @param astNode   Node representing the declaration
     * @return  Value that the ASTNode should have
     */
    public String findEntry(Node astNode) {
        if (astNode == null)
            return "";

        // if they're passing in a number already, why find it...
        if (astNode.getKeyword().equals("int") || astNode.getKeyword().equals("float"))
            return astNode.getValue();

        for (TableEntry entry : symbolTable) {
            // Find the matching value
            if (entry.getId().equals(astNode.getValue())) {
                return entry.getValue();
            }
        }

        // if we can't find it here, try to find it in the parent
        if (parent != null)
            return this.parent.findEntry(astNode);
        else
            return "";
    }

    @Override
    public String toString() {
        String str = "SCTNode: " +
                "id= " + System.identityHashCode(this) +
                 //", children=" + children +
                ", symbolTable=" + symbolTable +
                '}';
        return str;
    }
}
