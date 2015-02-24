package compiler488.ast.expn;

import compiler488.ast.ASTVisitor;
import compiler488.ast.Readable;
import compiler488.symbol.SymbolTable;
import compiler488.symbol.SymbolTable.SymbolType;
import compiler488.symbol.SymbolTableEntry;
import compiler488.ast.SourceCoord;

/**
 *  References to a scalar variable or function call without parameters.
 */
public class IdentExpn extends Expn implements Readable {
    /** Name of the identifier. */
    private String ident;

    public IdentExpn(String ident, SourceCoord sourceCoord) {
        super(sourceCoord);

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
    	// (if within function scope) or to a scalar variable or to a function call
    	// S37: check that identifier has been declared as a scalar variable
    	// S39: check that identifier has been declared as a parameter
    	// S40: check that identifier has been declared as a function
		visitor.visit(this);
	}

	@Override
	public SymbolType getExpnType(SymbolTable st) {
		SymbolTableEntry ste = st.searchGlobal(this.ident);
		if (ste != null) {
			return ste.getType();
		} else {
			return SymbolType.UNKNOWN;
		}
	}
}
