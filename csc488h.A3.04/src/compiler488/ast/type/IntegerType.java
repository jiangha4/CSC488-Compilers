package compiler488.ast.type;

import compiler488.ast.ASTVisitor;
import compiler488.ast.SourceCoord;
import compiler488.symbol.SymbolTable.SymbolType;


/**
 * Used to declare objects that yield integers.
 */
public class IntegerType extends Type {
	
    public IntegerType(SourceCoord sourceCoord) {
		super(sourceCoord);
	}

	public String toString() {
        return "integer";
    }
    
    @Override
    public SymbolType toSymbolType() {
    	return SymbolType.INTEGER;
    }
    
    @Override
	public void accept(ASTVisitor visitor) {
		visitor.visit(this);
	}

}
