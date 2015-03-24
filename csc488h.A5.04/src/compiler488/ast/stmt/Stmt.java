package compiler488.ast.stmt;

import java.util.*;
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
	 * Return the current statement (as a list) if this is a ReturnStmt, or
	 * an empty list otherwise.
	 */
	public ArrayList<ReturnStmt> getReturnStmts() {
		ArrayList<ReturnStmt> returnStmts = new ArrayList<ReturnStmt>();
		if (this instanceof ReturnStmt) {
			returnStmts.add((ReturnStmt)this);
		}

		return returnStmts;
	}
}
