package compiler488.ast.expn;

import compiler488.ast.ASTVisitor;
import compiler488.ast.SourceCoord;
import compiler488.symbol.SymbolTable;
import compiler488.symbol.SymbolTable.SymbolType;


/**
 * Represents the boolean negation of an expression.
 */
public class NotExpn extends UnaryExpn {
	public NotExpn(Expn operand, SourceCoord sourceCoord) {
		super(UnaryExpn.OP_NOT, operand, sourceCoord);
	}

	@Override
	public void accept(ASTVisitor visitor) {
		visitor.enterVisit(this);

		operand.accept(visitor);

		visitor.exitVisit(this);
	}
	
	@Override
	public SymbolType getExpnType(SymbolTable st) {
		if (this.expnType == null) {
			if (this.operand.getExpnType(st) == SymbolType.BOOLEAN) {
				this.expnType = SymbolType.BOOLEAN;
			}
			else {
				this.expnType = SymbolType.UNKNOWN;
			}
		}

		return this.expnType;
	}
}
