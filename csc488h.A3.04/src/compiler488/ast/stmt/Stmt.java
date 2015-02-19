package compiler488.ast.stmt;

import compiler488.ast.ASTVisitable;
import compiler488.ast.ASTVisitor;
import compiler488.ast.BaseAST;

/**
 * A placeholder for statements.
 */
public abstract class Stmt extends BaseAST implements ASTVisitable {
	@Override
	public void accept(ASTVisitor visitor) {
		visitor.visit(this);
	}
}
