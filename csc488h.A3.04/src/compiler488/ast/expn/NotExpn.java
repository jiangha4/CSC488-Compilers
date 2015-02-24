package compiler488.ast.expn;

import compiler488.ast.ASTVisitor;
import compiler488.ast.SourceCoord;


/**
 * Represents the boolean negation of an expression.
 */
public class NotExpn extends UnaryExpn {
    public NotExpn(Expn operand, SourceCoord sourceCoord) {
        super(UnaryExpn.OP_NOT, operand, sourceCoord);
    }

    @Override
	public void accept(ASTVisitor visitor) {
		visitor.visit(this);
		operand.accept(visitor);
	}
}
