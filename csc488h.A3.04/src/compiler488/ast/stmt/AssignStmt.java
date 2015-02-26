package compiler488.ast.stmt;

import compiler488.ast.ASTVisitor;
import compiler488.ast.PrettyPrinter;
import compiler488.ast.SourceCoord;
import compiler488.ast.expn.Expn;

/**
 * Holds the assignment of an expression to a variable.
 */
public class AssignStmt extends Stmt {
    /** The location being assigned to. */
    private Expn lval;

    /** The value being assigned. */
    private Expn rval;

    public AssignStmt(Expn lval, Expn rval, SourceCoord sourceCoord) {
        super(sourceCoord);

        lval.setParentNode(this);
        this.lval = lval;
        rval.setParentNode(this);
        this.rval = rval;
    }

    public Expn getLval() {
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

    @Override
	public void accept(ASTVisitor visitor) {
		lval.accept(visitor);
		rval.accept(visitor);
        visitor.visit(this);
	}
}
