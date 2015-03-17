package compiler488.ast.stmt;

import compiler488.ast.ASTList;
import compiler488.ast.ASTVisitor;
import compiler488.ast.PrettyPrinter;
import compiler488.ast.SourceCoord;


/**
 * Represents the declarations and instructions of a scope construct.
 */
public class Scope extends Stmt {
	/** Body of the scope, mixed list of declarations and statements. */
	protected ASTList<Stmt> body;

	public Scope(ASTList<Stmt> body, SourceCoord sourceCoord) {
		super(sourceCoord);
		if (body != null) {
			body.setParentNode(this);
		}
		this.body = body;
	}

	public Scope(SourceCoord sourceCoord) {
		this(null, sourceCoord);
	}

	public ASTList<Stmt> getBody() {
		return body;
	}

	/**
	 * containsReturn - the scope statement will recursively check each of its child statements for a return statement
	 */
	@Override
	public ReturnStmt containsReturn() {
		for (Stmt child : body) {
			ReturnStmt rs = child.containsReturn();
			if (rs != null) {
				return rs;
			}
		}

		return null;
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
		visitor.enterVisit(this);

		if (body != null && body.any()) {
			body.accept(visitor);
		}

		visitor.exitVisit(this);
	}
}
