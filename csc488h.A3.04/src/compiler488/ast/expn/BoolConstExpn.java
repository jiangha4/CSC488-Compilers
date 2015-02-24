package compiler488.ast.expn;

import compiler488.ast.ASTVisitor;
import compiler488.ast.SourceCoord;

/**
 * Boolean literal constants.
 */
public class BoolConstExpn extends ConstExpn {
    /** The value of the constant */
    private boolean value;

    public BoolConstExpn(boolean value, SourceCoord sourceCoord) {
        super(sourceCoord);

        this.value = value;
    }

    public boolean getValue() {
        return value;
    }

    @Override
    public String toString () {
        return value ? "true" : "false";
    }

    @Override
	public void accept(ASTVisitor visitor) {
		visitor.visit(this);
	}
}
