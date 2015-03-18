package compiler488.ast.expn;

import compiler488.ast.*;
import compiler488.symbol.*;
import compiler488.symbol.SymbolTable.SymbolType;
import compiler488.ast.SourceCoord;


/**
 * A placeholder for all expressions.
 */
public abstract class Expn extends BaseAST implements Printable {

	// Every Expn node has an associated type (defined as enum in SymbolTable.SymbolType)
	SymbolType expnType = null;

	/**
	 * getExpnType : if expnType is null, this method computes its value and stores it in the field;
	 * otherwise, it directly returns the field.
	 *
	 * NB: This method is implemented differently by each subclass of Expn.
	 *
	 * @param st : a reference to the current symbol table for the program
	 * @return SymbolType : the type for the expression; if the type is unknown or invalid, return UNKNOWN
	 */
	public abstract SymbolType getExpnType(SymbolTable st);

	public STScope getContainingSTScope() {
		BaseAST curNode = this;
		while (!(curNode instanceof ScopeCreator)) {
			curNode = curNode.getParentNode();
		}
		return ((ScopeCreator)curNode).getSTScope();
	}

	/**
	 * getSTETypeOrUnknown : for expressions representing identifiers (scalar vars, arrays, functions,
	 * procedures, params), look up the identifier name in the symbol table. If found, then return the
	 * symbol entry type as the expression type; otherwise return UNKNOWN.
	 *
	 * @param st : a reference to the current symbol table for the program
	 * @param ident : an identifier name to be looked up in the symbol table
	 * @param scope : the scope to start the search from in the symbol table
	 * @return SymbolType : the type for the expression; if not found in the symbol table, return UNKNOWN
	 */
	public SymbolType getSTETypeOrUnknown(SymbolTable st, String ident) {
		STScope scope = getContainingSTScope();
		SymbolTableEntry ste = st.searchGlobalFrom(ident, scope);
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
