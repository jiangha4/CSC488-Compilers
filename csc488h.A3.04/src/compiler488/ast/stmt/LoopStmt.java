package compiler488.ast.stmt;

import compiler488.ast.ASTList;
import compiler488.ast.ASTVisitor;
import compiler488.ast.PrettyPrinter;

/**
 * Represents a loop in which the exit condition is evaluated before each pass.
 */
public class LoopStmt extends LoopingStmt {
    public LoopStmt(ASTList<Stmt> body) {
        super(body);
    }

    /**
     * Pretty-print this AST node as a <code>loop</code> loop.
     */
    @Override
    public void prettyPrint(PrettyPrinter p) {
        p.println("loop");
        body.prettyPrintBlock(p);
        p.print("end");
    }

    @Override
	public void accept(ASTVisitor visitor) {
		visitor.visit(this); //Current node
		body.setListControlStatement(controlStatement.LOOP);
		body.accept(visitor); //Child node
	}
}
