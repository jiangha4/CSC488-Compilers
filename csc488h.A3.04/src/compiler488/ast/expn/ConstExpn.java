package compiler488.ast.expn;

import compiler488.ast.ASTVisitor;


/**
 * This is a place holder for literal constants.
 */
public abstract class ConstExpn extends Expn {
	@Override
	public void accept(ASTVisitor visitor) {
		visitor.visit(this);
	}
}
