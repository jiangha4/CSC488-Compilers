package compiler488.ast.type;

import compiler488.ast.BaseAST;
import compiler488.ast.SourceCoord;
import compiler488.symbol.SymbolTable.SymbolType;


/**
 * A placeholder for types.
 */
public abstract class Type extends BaseAST {
	public Type(SourceCoord sourceCoord) {
		super(sourceCoord);
	}
	
	public abstract SymbolType toSymbolType();
}
