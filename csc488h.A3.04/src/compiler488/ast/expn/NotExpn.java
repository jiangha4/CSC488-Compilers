package compiler488.ast.expn;

import compiler488.ast.ASTVisitor;


/**
 * Represents the boolean negation of an expression.
 */
public class NotExpn extends UnaryExpn {
    public NotExpn(Expn operand) {
        super(UnaryExpn.OP_NOT, operand);
    }

    @Override
	public void accept(ASTVisitor visitor) {
		visitor.visit(this);
		operand.accept(visitor);
	}
}
