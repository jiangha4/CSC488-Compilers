package compiler488.ast.stmt;

import compiler488.ast.ASTList;
import compiler488.ast.ASTVisitor;
import compiler488.ast.PrettyPrinter;
import compiler488.ast.SourceCoord;
import compiler488.ast.expn.Expn;


/**
 * Represents an if-then or an if-then-else construct.
 */
public class IfStmt extends Stmt {
	/** The condition that determines which branch to execute. */
	private Expn condition;

	/** Represents the statement to execute when the condition is true. */
	private ASTList<Stmt> whenTrue;

	/** Represents the statement to execute when the condition is false. */
	private ASTList<Stmt> whenFalse = null;

	public IfStmt(Expn condition, ASTList<Stmt> whenTrue, ASTList<Stmt> whenFalse, SourceCoord sourceCoord) {
		super(sourceCoord);

		condition.setParentNode(this);
		this.condition = condition;
		whenTrue.setParentNode(this);
		this.whenTrue = whenTrue;
		if (whenFalse != null) {
			whenFalse.setParentNode(this);
		}
		this.whenFalse = whenFalse;
	}

	public IfStmt(Expn condition, ASTList<Stmt> whenTrue, SourceCoord sourceCoord) {
		this(condition, whenTrue, null, sourceCoord);
	}

	public Expn getCondition() {
		return condition;
	}

	public ASTList<Stmt> getWhenTrue() {
		return whenTrue;
	}

	public ASTList<Stmt> getWhenFalse() {
		return whenFalse;
	}

	/**
	 * containsReturn - the if statement will recursively check each of its child statements for a return statement
	 */
	@Override
	public ReturnStmt containsReturn() {
		for (Stmt child : whenTrue) {
			ReturnStmt rs = child.containsReturn();
			if (rs != null) {
				return rs;
			}
		}

		for (Stmt child : whenFalse) {
			ReturnStmt rs = child.containsReturn();
			if (rs != null) {
				return rs;
			}
		}

		return null;
	}

	/**
	 * Print a description of the <strong>if-then-else</strong> construct. If the
	 * <strong>else</strong> part is empty, just print an <strong>if-then</strong> construct.
	 */
	@Override
	public void prettyPrint(PrettyPrinter p) {
		p.print("if ");
		condition.prettyPrint(p);
		p.println(" then");
		whenTrue.prettyPrintBlock(p);

		if (whenFalse != null) {
			p.println(" else");
			whenFalse.prettyPrintBlock(p);
		}

		p.print("end");
	}

	@Override
	public void accept(ASTVisitor visitor) {
		visitor.enterVisit(this);

		condition.accept(visitor);
		whenTrue.accept(visitor);
		if (whenFalse != null) {
			whenFalse.accept(visitor);
		}

		visitor.exitVisit(this);
	}
}
