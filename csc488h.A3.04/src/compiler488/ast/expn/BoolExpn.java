package compiler488.ast.expn;

import compiler488.ast.ASTVisitor;
import compiler488.ast.SourceCoord;


/**
 * Place holder for all binary expression where both operands must be boolean
 * expressions.
 */
public class BoolExpn extends BinaryExpn {
    public final static String OP_OR 	= "|";
    public final static String OP_AND	= "&";

    public BoolExpn(String opSymbol, Expn left, Expn right, SourceCoord sourceCoord) {
        super(opSymbol, left, right, sourceCoord);

        assert ((opSymbol == OP_OR) ||
                (opSymbol == OP_AND));
    }

    @Override
	public void accept(ASTVisitor visitor) {
		visitor.visit(this);
		left.accept(visitor);
		right.accept(visitor);
	}
}
