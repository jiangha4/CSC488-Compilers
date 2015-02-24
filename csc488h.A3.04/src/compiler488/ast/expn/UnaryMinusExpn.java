package compiler488.ast.expn;

import compiler488.ast.ASTVisitor;
import compiler488.ast.SourceCoord;

/**
 * Represents negation of an integer expression
 */
public class UnaryMinusExpn extends UnaryExpn {
    public UnaryMinusExpn(Expn operand, SourceCoord sourceCoord) {
        super(UnaryExpn.OP_MINUS, operand, sourceCoord);
    }

    @Override
	public void accept(ASTVisitor visitor) {
		visitor.visit(this);
		operand.accept(visitor);
	}
}
