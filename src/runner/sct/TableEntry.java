package runner.sct;

import parser.pst.Node;

import java.util.ArrayList;

public class TableEntry {

    /** SCTNode this entry is linked to */
    private SCTNode sctNode;
    /** ASTNode this entry is linked to */
    private Node astNode;
    /** Variable name */
    private String id;
    /** Type of variable */
    private String type;
    /** Value of variable */
    private String value;

    public TableEntry(SCTNode sctNode, Node astNode) {
        this.sctNode = sctNode;
        this.astNode = astNode;

        ArrayList<Node> children = astNode.getChildren();
        this.type = children.get(0).getKeyword();

        if (this.type.equals("id")) {
            this.value = sctNode.findEntry(astNode);
        } else {
            this.value = children.get(0).getValue();
        }

        this.id = children.get(1).getValue();
    }

    public SCTNode getSctNode() {
        return sctNode;
    }

    public void setSctNode(SCTNode sctNode) {
        this.sctNode = sctNode;
    }

    public Node getAstNode() {
        return astNode;
    }

    public void setAstNode(Node astNode) {
        this.astNode = astNode;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }


    @Override
    public String toString() {
        return "TableEntry{" +
                "id='" + id + '\'' +
                ", type='" + type + '\'' +
                ", value='" + value + '\'' +
                '}';
    }

    // used to have equals with id + type

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TableEntry that = (TableEntry) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        return type != null ? type.equals(that.type) : that.type == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (type != null ? type.hashCode() : 0);
        return result;
    }
}
