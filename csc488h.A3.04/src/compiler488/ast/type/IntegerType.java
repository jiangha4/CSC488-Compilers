package compiler488.ast.type;

import compiler488.ast.ASTVisitor;
import compiler488.symbol.SymbolTable.SymbolType;


/**
 * Used to declare objects that yield integers.
 */
public class IntegerType extends Type {
    public String toString() {
        return "integer";
    }
    
    public SymbolType toSymbolType() {
    	return SymbolType.INTEGER;
    }
    
    @Override
	public void accept(ASTVisitor visitor) {
		visitor.visit(this);
	}

}
