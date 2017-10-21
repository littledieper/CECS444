package lexer;

/**         Token.java
 *  Class to represent a Token in the A2 Lexcon.
 *
 *  @author Alex Diep
 */
public class Token {

    /** Line number the token is at */
    private int lineNum;
    /** Actual value of the token */
    private String value;
    /** Grammar that holds ID + keyword */
    private Grammar grammar;

    public Token(int lineNum, String value) {
        this.lineNum = lineNum;
        this.value = value;
        this.grammar = new Grammar(0, "");
    }

    // -----------------------------------------------FUNCTIONS -------------------------------------------------------
    @Override
    public String toString() {
        String val = getValue();
        String number = "";
        if (grammar.getId() == 3 || grammar.getId() == 4)
            number = " " + grammar.getKeyword() + "= " + val;
        else if (grammar.getKeyword().equals("string"))
            val = value.substring(1, value.length() - 1); // chop off the quotes

        return String.format("(Tok:%3d line=%3d str= \"%s\"%s)", grammar.getId(), lineNum, val, number);
    }

    // --------------------------------------------GETTERS/SETTERS ----------------------------------------------------
    public Grammar getGrammar() {
        return grammar;
    }

    public void setGrammar(Grammar grammar) {
        this.grammar = grammar;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getLineNum() {
        return lineNum;
    }

    public void setLineNum(int lineNum) {
        this.lineNum = lineNum;
    }
}
