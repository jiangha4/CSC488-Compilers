package compiler488.ast.expn;

import compiler488.ast.ASTVisitor;
import compiler488.ast.SourceCoord;
import compiler488.symbol.SymbolTable;
import compiler488.symbol.SymbolTable.SymbolType;


/**
 * Place holder for all ordered comparisons expression where both operands must
 * be integer expressions.  e.g. &lt; , &gt;  etc. comparisons
 */
public class CompareExpn extends BinaryExpn {
	public final static String OP_LESS 			= "<";
	public final static String OP_LESS_EQUAL 	= "<=";
	public final static String OP_GREATER 		= ">";
	public final static String OP_GREATER_EQUAL	= ">=";

	public CompareExpn(String opSymbol, Expn left, Expn right, SourceCoord sourceCoord) {
		super(opSymbol, left, right, sourceCoord);

		assert ((opSymbol == OP_LESS) ||
				(opSymbol == OP_LESS_EQUAL) ||
				(opSymbol == OP_GREATER) ||
				(opSymbol == OP_GREATER_EQUAL));
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
			if (this.left.getExpnType(st) == SymbolType.INTEGER &&
				this.right.getExpnType(st) == SymbolType.INTEGER) {
				this.expnType = SymbolType.BOOLEAN;
			} else {
				this.expnType = SymbolType.UNKNOWN;
			}
		}

		return this.expnType;
	}
}
