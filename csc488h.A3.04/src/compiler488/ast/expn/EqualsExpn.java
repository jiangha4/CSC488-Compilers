package compiler488.ast.expn;

import compiler488.ast.ASTVisitor;
import compiler488.ast.SourceCoord;
import compiler488.symbol.SymbolTable;
import compiler488.symbol.SymbolTable.SymbolType;


/**
 * Place holder for all binary expression where both operands could be either
 * integer or boolean expressions. e.g. = and != comparisons
 */
public class EqualsExpn extends BinaryExpn {
		public final static String OP_EQUAL 	= "=";
		public final static String OP_NOT_EQUAL	= "!=";

		public EqualsExpn(String opSymbol, Expn left, Expn right, SourceCoord sourceCoord) {
			super(opSymbol, left, right, sourceCoord);

			assert ((opSymbol == OP_EQUAL) ||
							(opSymbol == OP_NOT_EQUAL));
		}

		@Override
	public void accept(ASTVisitor visitor) {
		visitor.enterVisit(this);

		left.accept(visitor);
		right.accept(visitor);

		visitor.exitVisit(this);
	}

	@Override
	public SymbolType getExpnType(SymbolTable st) {
		if (this.expnType == null) {
			if (this.left.getExpnType(st) == this.right.getExpnType(st)) {
				this.expnType = SymbolType.BOOLEAN;
			} else {
				this.expnType = SymbolType.UNKNOWN;
			}
		}

		return this.expnType;
	}
}
