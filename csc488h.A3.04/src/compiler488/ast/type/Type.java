package compiler488.ast.type;

import compiler488.ast.ASTVisitable;
import compiler488.ast.ASTVisitor;
import compiler488.ast.BaseAST;

/**
 * A placeholder for types.
 */
public abstract class Type extends BaseAST implements ASTVisitable {
	@Override
	public void accept(ASTVisitor visitor) {
		visitor.visit(this);
	}
}
