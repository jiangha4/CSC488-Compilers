package compiler488.ast.stmt;

import compiler488.ast.ASTList;
import compiler488.ast.SourceCoord;
import compiler488.ast.expn.Expn;

/**
 * Represents the common parts of loops.
 */
public abstract class LoopingStmt extends Stmt {
    /** The control expression for the looping construct (if any.) */
    protected Expn expn = null;

    /** The body of the looping construct. */
    protected ASTList<Stmt> body;

    public LoopingStmt(Expn expn, ASTList<Stmt> body, SourceCoord sourceCoord) {
        super(sourceCoord);

        this.expn = expn;
        this.body = body;
    }

    public LoopingStmt(ASTList<Stmt> body, SourceCoord sourceCoord) {
        this(null, body, sourceCoord);
    }

    public Expn getExpn() {
        return expn;
    }

    public ASTList<Stmt> getBody() {
        return body;
    }
}
