package compiler488.ast.expn;

import compiler488.ast.BaseAST;
import compiler488.ast.Printable;
import compiler488.symbol.SymbolTable;
import compiler488.symbol.SymbolTableEntry;
import compiler488.symbol.SymbolTable.SymbolType;
import compiler488.ast.SourceCoord;


/**
 * A placeholder for all expressions.
 */
public abstract class Expn extends BaseAST implements Printable {
	SymbolType expnType = null;

	public abstract SymbolType getExpnType(SymbolTable st);

	public SymbolType getSTETypeOrUnknown(SymbolTable st, String ident) {
		SymbolTableEntry ste = st.searchGlobal(ident);
		if (ste == null) {
			return SymbolType.UNKNOWN;
		} else {
			return ste.getType();
		}
	}

	public Expn(SourceCoord sourceCoord) {
		super(sourceCoord);
	}
}
