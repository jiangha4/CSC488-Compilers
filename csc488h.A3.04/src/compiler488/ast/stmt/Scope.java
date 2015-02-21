package compiler488.ast.stmt;

import compiler488.ast.ASTList;
import compiler488.ast.ASTVisitor;
import compiler488.ast.PrettyPrinter;

/**
 * Represents the declarations and instructions of a scope construct.
 */
public class Scope extends Stmt {
    /** Body of the scope, mixed list of declarations and statements. */
    protected ASTList<Stmt> body;

    public Scope() {
        super();

        this.body = null;
    }

    public Scope(ASTList<Stmt> body) {
        this();

        this.body = body;
    }

    public ASTList<Stmt> getBody() {
        return body;
    }

    @Override
    public void prettyPrint(PrettyPrinter p) {
        p.println("begin");
        if (body != null && body.size() > 0) {
            body.prettyPrintBlock(p);
        }
        p.print("end");
    }

    @Override
	public void accept(ASTVisitor visitor) {
		// S00/S06: begin scope
    	visitor.visit(this);
    	
    	// Process contained statements
		if (body != null && body.size() > 0) {
			body.accept(visitor);
		}
		
		// S01/S07: end scope
		visitor.visit(this);
	}
}
