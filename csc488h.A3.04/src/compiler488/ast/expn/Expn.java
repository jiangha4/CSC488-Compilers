package compiler488.ast.expn;

import compiler488.ast.ASTVisitable;
import compiler488.ast.ASTVisitor;
import compiler488.ast.BaseAST;
import compiler488.ast.Printable;

/**
 * A placeholder for all expressions.
 */
public abstract class Expn extends BaseAST implements Printable, ASTVisitable {
	@Override
	public void accept(ASTVisitor visitor) {
		visitor.visit(this);
	}
}
