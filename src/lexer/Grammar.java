package lexer;

/**     Grammar.java
 * Class to represent part of the Grammar of the A2 Lexcon. Holds the ID and keyword.
 *
 * @author Alex Diep
 */
public class Grammar {

    /** ID in the A2 lexcon */
    private int id;
    /** Keyword for A2 lexcon */
    private String keyword;

    public Grammar(int id, String keyword)
    {
        this.id = id;
        this.keyword = keyword;

    }

    // -----------------------------------------------FUNCTIONS -------------------------------------------------------

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
