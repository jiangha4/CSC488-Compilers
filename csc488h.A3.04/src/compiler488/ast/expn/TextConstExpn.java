package compiler488.ast.expn;

import compiler488.ast.ASTVisitor;
import compiler488.ast.Printable;
import compiler488.symbol.SymbolTable;
import compiler488.symbol.SymbolTable.SymbolType;
import compiler488.ast.SourceCoord;

/**
 * Represents a literal text constant.
 */
public class TextConstExpn extends ConstExpn implements Printable {
    /** The value of this literal. */
    private String value;

    public TextConstExpn(String value, SourceCoord sourceCoord) {
        super(sourceCoord);

        this.value = value;
    }

    public String getValue() {
        return value;
    }

    /**
     * Returns a description of the literal text constant.
     */
    @Override
    public String toString() {
        return "\"" + value + "\"";
    }

    @Override
	public void accept(ASTVisitor visitor) {
		visitor.visit(this);
	}

	@Override
	public SymbolType getExpnType(SymbolTable st) {
		// TODO: should we add Text type?
		return SymbolType.TEXT;
	}
}
