package compiler488.ast.stmt;

import java.util.*;
import compiler488.ast.*;
import compiler488.symbol.*;

/**
 * Represents the declarations and instructions of a scope construct.
 */
public class Scope extends Stmt implements ScopeCreator {
	/** Body of the scope, mixed list of declarations and statements. */
	protected ASTList<Stmt> body;

	/** The symbol table scope created by this expn **/
	private STScope stScope;

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

	public void setSTScope(STScope scope) {
		stScope = scope;
	}

	public STScope getSTScope() {
		return stScope;
	}

	/**
	 * Recursively check each of its child statements return statements,
	 * returning them as a list.
	 */
	@Override
	public ArrayList<ReturnStmt> getReturnStmts() {
		ArrayList<ReturnStmt> returnStmts = new ArrayList<ReturnStmt>();
		for (Stmt child : body) {
			returnStmts.addAll(child.getReturnStmts());
		}

		return returnStmts;
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
