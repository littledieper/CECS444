package parser;

public class RuleList {

    /** list of rules from A6 Grammar */
    private Rule[] rules;
    /** max number of rules allowed */
    private final int NUM_RULES = 52;

    /**
     * Default constructor that fills the list.
     */
    public RuleList() {
        fillList();
    }

    /**
     * Returns the grammar rule and its RHS from the A6 Grammar
     * @param ruleId    ruleId of rule to get
     * @return Rule with matching ruleId
     */
    public Rule get(int ruleId) {
        if (ruleId > NUM_RULES + 1 || ruleId < 1) {
            return null;
        }

        return rules[ruleId - 1];
    }

    /**
     * Fills the 2D array with rules given by the A6 Grammar.
     */
    private void fillList() {
        rules = new Rule[NUM_RULES];

        rules[0] = new Rule(1, "Pgm", "kprog BBlock");
        rules[1] = new Rule(2, "BBlock", "brace1 Vargroup Stmts brace2");
        rules[2] = new Rule(3, "Vargroup", "kwdvars PPvarlist");
        rules[3] = new Rule(4, "Vargroup", "eps");
        rules[4] = new Rule(5, "PPvarlist", "parens1 Varlist, parens2");
        rules[5] = new Rule(6, "Varlist", "Vardecl semi Varlist");
        rules[6] = new Rule(7, "Varlist", "eps");
        rules[7] = new Rule(8, "Vardecl", "Basekind Varid");
        rules[8] = new Rule(9, "Basekind", "kint");
        rules[9] = new Rule(10, "Basekind", "kfloat");
        rules[10] = new Rule(11, "Basekind", "kstring");
        rules[11] = new Rule(12, "Varid", "id");
        rules[12] = new Rule(13, "Stmts", "Stmt semi Stmts");
        rules[13] = new Rule(14, "Stmts", "eps");
        rules[14] = new Rule(15, "Stmt", "Stasgn");
        rules[15] = new Rule(16, "Stmt", "Stprint");
        rules[16] = new Rule(17, "Stmt", "Stwhile");
        rules[17] = new Rule(18, "Stasgn", "Varid equal Expr");
        rules[18] = new Rule(19, "Stprint", "kprint PPexprs");
        rules[19] = new Rule(20, "Stwhile", "kwhile PPexpr1 BBlock");
        rules[20] = new Rule(21, "PPexprs", "parens1 Exprlist parens2");
        rules[21] = new Rule(22, "PPexpr1", "parens1 Expr parens2");
        rules[22] = new Rule(23, "Exprlist","Expr Moreexprs");
        rules[23] = new Rule(24, "Moreexprs", "comma Exprlist");
        rules[24] = new Rule(25, "Moreexprs", "eps");
        rules[25] = new Rule(26, "S", "Oprel Rterm S");
        rules[26] = new Rule(27, "S", "eps");
        rules[27] = new Rule(28, "Expr", "Rterm S");
        rules[28] = new Rule(29, "R", "Opadd Term R");
        rules[29] = new Rule(30, "R", "eps");
        rules[30] = new Rule(31, "Rterm", "Term R");
        rules[31] = new Rule(32, "Q", "Opmul Fact Q");
        rules[32] = new Rule(33, "Q", "eps");
        rules[33] = new Rule(34, "Term", "Fact Q");
        rules[34] = new Rule(35, "Fact", "int");
        rules[35] = new Rule(36, "Fact", "float");
        rules[36] = new Rule(37, "Fact", "string");
        rules[37] = new Rule(38, "Fact", "Varid");
        rules[38] = new Rule(39, "Fact", "PPexpr1");
        rules[39] = new Rule(40, "Oprel", "opeq");
        rules[40] = new Rule(41, "Oprel", "opne");
        rules[41] = new Rule(42, "Oprel", "Lthan");
        rules[42] = new Rule(43, "Oprel", "ople");
        rules[43] = new Rule(44, "Oprel", "opge");
        rules[44] = new Rule(45, "Oprel", "Gthan");
        rules[45] = new Rule(46, "Lthan", "angle1");
        rules[46] = new Rule(47, "Gthan", "angle2");
        rules[47] = new Rule(48, "Opadd", "plus");
        rules[48] = new Rule(49, "Opadd", "minus");
        rules[49] = new Rule(50, "Opmul", "aster");
        rules[50] = new Rule(51, "Opmul", "slash");
        rules[51] = new Rule(52, "Opmul", "caret");
    }
}
