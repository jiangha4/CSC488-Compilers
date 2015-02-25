package compiler488.ast.stmt;

import compiler488.ast.ASTVisitor;
import compiler488.ast.PrettyPrinter;
import compiler488.ast.SourceCoord;
import compiler488.ast.expn.Expn;

/**
 * Represents the command to exit from a loop.
 */
public class ExitStmt extends Stmt {
    /** Condition expression for <code>exit when</code> variation. */
    private Expn expn = null;

    public ExitStmt(Expn expn, SourceCoord sourceCoord) {
        super(sourceCoord);

        if (expn != null) {
          expn.setParentNode(this);
        }
        this.expn = expn;
    }

    public ExitStmt(SourceCoord sourceCoord) {
        this(null, sourceCoord);
    }

    public Expn getExpn() {
        return expn;
    }

    @Override
    public void prettyPrint(PrettyPrinter p) {
        p.print("exit");

        if (expn != null) {
            p.print(" when ");
            expn.prettyPrint(p);
        }
    }
    
    @Override
	public void accept(ASTVisitor visitor) {
		visitor.visit(this);
		if (expn != null) {
			expn.accept(visitor);
		}
	}

}
