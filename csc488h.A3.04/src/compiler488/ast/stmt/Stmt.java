package compiler488.ast.stmt;

import compiler488.ast.BaseAST;
import compiler488.ast.SourceCoord;


/**
 * A placeholder for statements.
 */
public abstract class Stmt extends BaseAST {
	public Stmt(SourceCoord sourceCoord) {
		super(sourceCoord);
	}

	/**
	 * containsReturn - find whether a statement is or contains (within its body) a ReturnStmt
	 * By default, return the ReturnStmt object if of correct type; otherwise return null (no recursion).
	 * Specific subclasses of Stmt will override this method to provide required behaviour of searching the bodies of
	 * nested ordinary scopes, if statements and loops (while and loop).
	 *
	 * @return ReturnStmt : 'this' if self is a ReturnStmt; 'null' otherwise
	 */
	public ReturnStmt containsReturn() {
		if (this instanceof ReturnStmt) {
			return (ReturnStmt)this;
		}
		return null;
	}
}
