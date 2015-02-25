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
	 * By default, return true of the statement being searched is of the desired type; otherwise return false (no recursion). 
	 * Specific subclasses of Stmt will override this method to provide required behaviour of searching the bodies of 
	 * nested ordinary scopes, if statements and loops (while and loop).
	 * 
	 * @return boolean : true if self is a ReturnStmt; false otherwise
	 */
	public boolean containsReturn() {
		if (this instanceof ReturnStmt) {
			return true;
		}
		return false;
	}
}
