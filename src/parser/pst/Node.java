package parser.pst;

import runner.sct.SCTNode;

import java.util.ArrayList;

public class Node {
    /** Linked SCTNode */
    private SCTNode sctNode;
    /** parent of this node */
    private Node parent;
    /** children of this node */
    private ArrayList<Node> children;
    /** keyword of the rule this node represents */
    private String keyword;
    /** value of the rule this node represents */
    private String value;
    /** hash of object */
    private int hashId;

    /**
     * Constructor
     *
     * Set's this Node's keyword and creates the empty list of children.
     * @param keyword
     */
    public Node(String keyword) {
        this(keyword, "");
    }

    /**
     * Constructor
     *
     * Sets this Node's keyword and value and creates the empty list of children.
     * @param keyword   keyword to set
     * @param value     value to set
     */
    public Node(String keyword, String value) {
        this.keyword = keyword;
        this.value = value;
        children = new ArrayList<Node>();
        this.hashId = System.identityHashCode(this);
    }

    public SCTNode getSctNode() {
        return sctNode;
    }

    public void setSctNode(SCTNode sctNode) {
        this.sctNode = sctNode;
    }

    /**
     * Returns the keyword of this Node.
     * @return this Node's keyword.
     */
    public String getKeyword() {
        return keyword;
    }

    /**
     * Returns the value of the Node
     * @return  value of the Node
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets the value of this node. We realistically only care if the associated 'keyword' of this node is an
     * int, float, string, or id, but we'll keep the value of other values as well.
     * @param value value to set.
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Returns this Node's parent.
     * @return parent of this Node.
     */
    public Node getParent() {
        return parent;
    }

    /**
     * Sets this Node's new parent.
     * @param parent new parent of this Node.
     */
    public void setParent(Node parent) {
        this.parent = parent;
    }

    /**
     * Returns this node's children. Note: this list may be empty.
     * @return list of this node's children.
     */
    public ArrayList<Node> getChildren() {
        return children;
    }

    /**
     * Adds a new child to this Node's list of children and sets this Node to the new child's parent
     * @param child new child to add.
     */
    public void addChild(Node child) {
        children.add(child);
        child.setParent(this);
    }

    /**
     * Adds the children in the given list to this Node's list of children
     * @param childrenToAdd new children to add to list.
     */
    public void addChildren(ArrayList<Node> childrenToAdd) {
        if (childrenToAdd == null)
            return;

        for (Node child : childrenToAdd)
            addChild(child);
    }

    /**
     * Clears all current children that this Node may have and replaces them with the given children, if any.
     *
     * @param childrenToAdd new children to add/replace the hold ones with.
     */
    public void replaceChildren(ArrayList<Node> childrenToAdd) {
        if (childrenToAdd == null)
            return;

        children.clear();
        for (Node child : childrenToAdd)
            addChild(child);
    }

    /**
     * Determines if the rule in this node is an epsilon rule.
     * @return TRUE: if the rule in this node is an eps rule.
     *         FALSE: otherwise
     */
    public boolean isEpsilonRule() {
        return Character.isUpperCase(this.keyword.charAt(0)) && children.isEmpty();
    }

    /**
     * Helper function to toString()
     *
     * Determines if the value that this node holds should be printed.
     * OK values are ints, floats, strings, and id's.
     * @return TRUE: if the value is one of the above types
     *         FALSE: otherwise
     */
    private boolean isValueOkayToPrint()
    {
        return !value.isEmpty() &&
                keyword.equals("id") || keyword.equals("int") ||
                keyword.equals("float") || keyword.equals("string");
    }

    @Override
    public String toString() {
        String str = "Node: " +
                "id='" + hashId + '\'' +
                ", keyword='" + keyword + '\'';

        if (isValueOkayToPrint()) {
            str = str + ", value='" + value + '\'';
        }

        str = str + "}";
        return str;
    }

}
