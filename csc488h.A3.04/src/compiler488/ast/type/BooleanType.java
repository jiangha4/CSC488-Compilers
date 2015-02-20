package compiler488.ast.type;

import compiler488.ast.ASTVisitor;


/**
 * The type of things that may be true or false.
 */
public class BooleanType extends Type {
    @Override
    public String toString() {
        return "boolean";
    }

    @Override
	public void accept(ASTVisitor visitor) {
		visitor.visit(this);
	}
}
