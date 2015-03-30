package compiler488.ast.expn;

import compiler488.ast.ASTVisitor;
import compiler488.ast.Readable;
import compiler488.symbol.SymbolTable;
import compiler488.symbol.SymbolTable.SymbolType;
import compiler488.symbol.SymbolTableEntry;
import compiler488.ast.SourceCoord;


/**
 *	References to a scalar variable or function call without parameters.
 */
public class IdentExpn extends Expn implements Readable {
	/** Name of the identifier. */
	private String ident;
	
	/** Used for function calls without parameters: instruction location to patch later during code gen **/
	public short shouldPointToAfterBranch;

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
		visitor.enterVisit(this);
		visitor.exitVisit(this);
	}

	/**
	 * getExpnType : look up identifier in symbol table and if found return the symbol table entry type; 
	 * otherwise return UNKNOWN.
	 */
	@Override
	public SymbolType getExpnType(SymbolTable st) {
		if (this.expnType == null) {
			this.expnType = this.getSTETypeOrUnknown(st, this.ident);
		}
		return this.expnType;
	}
}
