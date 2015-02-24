package compiler488.ast.expn;

import compiler488.ast.ASTVisitor;
import compiler488.ast.SourceCoord;


/**
 * Represents a literal integer constant.
 */
public class IntConstExpn extends ConstExpn {
    /**
     * The value of this literal.
     */
    private Integer value;

    public IntConstExpn(Integer value, SourceCoord sourceCoord) {
        super(sourceCoord);

        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    @Override
    public String toString () {
        return value.toString();
    }

    @Override
	public void accept(ASTVisitor visitor) {
		visitor.visit(this);
	}
}
