package parser;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Rule {

    private int id;
    private String lhs;
    private String rhs;

    public Rule(int id, String lhs, String rhs) {
        this.id = id;
        this.lhs = lhs;
        this.rhs = rhs;
    }

    // ------------------- GETTERS / SETTERS -----------------------
    public boolean isEpsilonRule() {
        return rhs.equals("eps");
    }

    public String[] getReversedRhsArray() {
        List<String> list = Arrays.asList(rhs.trim().split(" "));
        Collections.reverse(list);

        return (String[]) list.toArray();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLhs() {
        return lhs;
    }

    public void setLhs(String lhs) {
        this.lhs = lhs;
    }

    public String getRhs() {
        return rhs;
    }

    public void setRhs(String rhs) {
        this.rhs = rhs;
    }
}
