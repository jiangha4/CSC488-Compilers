package compiler488.ast.expn;

import compiler488.ast.ASTVisitor;
import compiler488.ast.Readable;

/**
 *  References to a scalar variable or function call without parameters.
 */
public class IdentExpn extends Expn implements Readable {
    /** Name of the identifier. */
    private String ident;

    public IdentExpn(String ident) {
        super();

        this.ident = ident;
    }

    public String getIdent() {
        return ident;
    }

    /**
     * Returns the name of the variable or function.
     */
    @Override
    public String toString() {
        return ident;
    }

    @Override
	public void accept(ASTVisitor visitor) {
    	
    	// NB: when an identifier is used as an expression, it could refer to either a parameter 
    	// (if within function scope) or to a scalar variable
    	// S37: check that identifier has been declared as a scalar variable
    	// S39: check that identifier has been declared as a parameter
		visitor.visit(this);
	}
}
