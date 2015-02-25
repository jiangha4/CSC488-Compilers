package compiler488.ast.expn;

import compiler488.ast.ASTVisitor;
import compiler488.ast.SourceCoord;
import compiler488.symbol.SymbolTable;
import compiler488.symbol.SymbolTable.SymbolType;


/**
 * Place holder for all binary expression where both operands must be integer
 * expressions.
 */
public class ArithExpn extends BinaryExpn {
    public final static String OP_PLUS 		= "+";
    public final static String OP_MINUS 	= "-";
    public final static String OP_TIMES 	= "*";
    public final static String OP_DIVIDE 	= "/";

    public ArithExpn(String opSymbol, Expn left, Expn right, SourceCoord sourceCoord) {
        super(opSymbol, left, right, sourceCoord);

        assert ((opSymbol == OP_PLUS) ||
                (opSymbol == OP_MINUS) ||
                (opSymbol == OP_TIMES) ||
                (opSymbol == OP_DIVIDE));
    }

    @Override
	public void accept(ASTVisitor visitor) {
		visitor.visit(this);
		left.accept(visitor);
		right.accept(visitor);
	}

  @Override
  public SymbolType getExpnType(SymbolTable st) {
    if (this.expnType == null) {
      if (this.left.getExpnType(st) == SymbolType.INTEGER &&
          this.right.getExpnType(st) == SymbolType.INTEGER) {
        this.expnType = SymbolType.INTEGER;
      } else {
        this.expnType = SymbolType.UNKNOWN;
      }
    }

    return this.expnType;
  }
}
