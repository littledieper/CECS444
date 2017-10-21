package lexer;

import java.io.IOException;
import java.io.Reader;

/**         BufferedReader.java
 * BufferedReader implementation to allow for peeking at the next char of fileinput.
 *
 * @author Alex Diep
 */
public class BufferedReader extends java.io.BufferedReader{

    /** Buffersize used for .mark() */
    private final int readAhead = 1000;

    /**
     * Creates a new BufferedReader with a given Reader instance.
     * @param r  java.io.Reader
     */
    public BufferedReader(Reader r)
    {
        super(r);
    }

    /**
     * Reads the next character and returns back to the previous location.
     * @return next character
     * @throws IOException
     */
    public int peek() throws IOException {
        mark(readAhead);
        int input = read();
        reset();

        return input;
    }
}
