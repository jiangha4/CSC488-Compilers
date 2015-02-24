package compiler488.ast.stmt;

import compiler488.ast.ASTList;
import compiler488.ast.ASTVisitor;
import compiler488.ast.PrettyPrinter;
import compiler488.ast.SourceCoord;
import compiler488.ast.expn.Expn;

/**
 * Represents a loop in which the exit condition is evaluated before each pass.
 */
public class WhileDoStmt extends LoopingStmt {
    public WhileDoStmt(Expn expn, ASTList<Stmt> body, SourceCoord sourceCoord) {
        super(expn, body, sourceCoord);
    }

    @Override
    public void prettyPrint(PrettyPrinter p) {
        p.print("while ");
        expn.prettyPrint(p);
        p.println(" do");

        body.prettyPrintBlock(p);
        p.print("end");
    }

    @Override
	public void accept(ASTVisitor visitor) {
		visitor.visit(this);
		expn.setControlStatement(controlStatement.WHILE);
		expn.accept(visitor);
		body.setListControlStatement(controlStatement.WHILE);
		body.accept(visitor);
	}
}
