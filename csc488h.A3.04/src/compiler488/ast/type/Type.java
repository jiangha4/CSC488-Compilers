package compiler488.ast.type;

import compiler488.ast.BaseAST;
import compiler488.symbol.SymbolTable.SymbolType;

/**
 * A placeholder for types.
 */
public abstract class Type extends BaseAST {
	public abstract SymbolType toSymbolType();
}
