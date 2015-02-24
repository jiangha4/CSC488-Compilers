package compiler488.ast.expn;

import compiler488.ast.ASTVisitor;
import compiler488.ast.Printable;
import compiler488.symbol.SymbolTable;
import compiler488.symbol.SymbolTable.SymbolType;
import compiler488.ast.SourceCoord;

/**
 * Represents the special literal constant associated with writing a new-line
 * character on the output device.
 */
public class SkipConstExpn extends ConstExpn implements Printable {
    public SkipConstExpn(SourceCoord sourceCoord) {
        super(sourceCoord);
    }

    @Override
    public String toString() {
        return "skip";
    }

    @Override
	public void accept(ASTVisitor visitor) {
		visitor.visit(this);
	}

	@Override
	public SymbolType getExpnType(SymbolTable st) {
		// TODO: check: will this mess anything up?
		return SymbolType.UNKNOWN;
	}
}
