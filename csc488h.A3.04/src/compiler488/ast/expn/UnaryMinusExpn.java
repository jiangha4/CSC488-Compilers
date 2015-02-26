package compiler488.ast.expn;

import compiler488.ast.ASTVisitor;
import compiler488.ast.SourceCoord;
import compiler488.symbol.SymbolTable;
import compiler488.symbol.SymbolTable.SymbolType;


/**
 * Represents negation of an integer expression
 */
public class UnaryMinusExpn extends UnaryExpn {
	public UnaryMinusExpn(Expn operand, SourceCoord sourceCoord) {
		super(UnaryExpn.OP_MINUS, operand, sourceCoord);
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
			if (this.operand.getExpnType(st) == SymbolType.INTEGER) {
				this.expnType = SymbolType.INTEGER;
			}
			else {
				this.expnType = SymbolType.UNKNOWN;
			}
		}

		return this.expnType;
	}
}
