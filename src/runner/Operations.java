package runner;

import parser.pst.Node;
import runner.sct.SCTNode;

import java.util.ArrayList;

public class Operations {

    /**
     * Is it an operation?
     * @param astNode Node to test
     * @return   TRUE if the given node is an operation, FALSE if not
     */
	public static boolean isOperation(Node astNode) {
	    if (astNode == null)
	        return false;

		String keyword = astNode.getKeyword();

		return keyword.equals("plus") ||
				keyword.equals("minus") ||
				keyword.equals("aster") ||
				keyword.equals("caret") ||
				keyword.equals("slash");		
	}

    /**
     * Is it an identifier / variable?
     * @param astNode   Node to test
     * @return  TRUE if it is an id / variable
     */
    private static boolean isIdentifier(Node astNode) {
	    return astNode.getKeyword().equals("id");
    }

    /**
     * Is the given node a number?
     * @param astNode AST node to test
     * @return  TRUE if it is a number (int or float), FALSE if not
     */
    private static boolean isNumber(Node astNode) {
	    String keyword = astNode.getKeyword();

	    return keyword.equals("int") ||
                keyword.equals("float");
    }

    /**
     * Does the mathematical operation from the given AST Node
     * @param sctNode   SCTNode to initially search
     * @param astNode   ASTNode to do operation on
     * @return  result of the operation
     */
	public static double doOperation(SCTNode sctNode, Node astNode) {
	    // Handle nulls
	    if (sctNode == null || astNode == null)
	        return 0;

	    // Return the number if the astNode is one.
	    if (isNumber(astNode))
	        return Double.parseDouble(astNode.getValue());

	    // Return the value from the identifier.
        if (isIdentifier(astNode)) {
            String value = sctNode.findEntry(astNode);
            if (!value.isEmpty())
                return Double.parseDouble(value);

        }

	    // If we've hit a parenthesis, make it so we're actually operating on the contents of the parenthesis
        if (astNode.getKeyword().equals("parens1"))
            astNode = astNode.getChildren().get(1);

        // If we've reached this point, and it's not an operation, well just return 0 so we don't break anything below
		if (!isOperation(astNode))
			return 0;

		String keyword = astNode.getKeyword();
		ArrayList<Node> children = astNode.getChildren();
        double ret = 0;

        switch(keyword) {
			case "plus":
			    for (Node child : children)
			        ret += doOperation(sctNode, child);

			    break;
            case "minus":
                for (Node child : children)
                    ret = doOperation(sctNode, child) - ret;

                break;
            case "aster" :
                ret = 1;
                for (Node child: children)
                    ret *= doOperation(sctNode, child);

                break;
            case "slash":
                ret = 1;
                for (Node child : children)
                    ret = doOperation(sctNode, child) / ret;

                break;
            case "caret":
                // child 0 is the exponent, child 1 is the base
                double base = doOperation(sctNode, children.get(1));
                double exponent = doOperation(sctNode, children.get(0));
                ret = Math.pow(base, exponent);
                break;
		}

		return ret;
	}

	
}
