package compiler488.ast.stmt;

import compiler488.ast.Assignable;
import compiler488.ast.PrettyPrinter;
import compiler488.ast.expn.Expn;

/**
 * Holds the assignment of an expression to a variable.
 */
public class AssignStmt extends Stmt {
    /** The location being assigned to. */
    private Assignable lval;

    /** The value being assigned. */
    private Expn rval;

    public AssignStmt(Assignable lval, Expn rval) {
        super();

        this.lval = lval;
        this.rval = rval;
    }

    public Assignable getLval() {
        return lval;
    }

    public Expn getRval() {
        return rval;
    }

    @Override
    public void prettyPrint(PrettyPrinter p) {
        lval.prettyPrint(p);
        p.print(" <= ");
        rval.prettyPrint(p);
    }

}
