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
}
