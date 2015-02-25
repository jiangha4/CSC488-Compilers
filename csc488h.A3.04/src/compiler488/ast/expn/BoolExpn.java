package compiler488.ast.expn;

import compiler488.ast.ASTVisitor;
import compiler488.ast.SourceCoord;
import compiler488.symbol.SymbolTable;
import compiler488.symbol.SymbolTable.SymbolType;


/**
 * Place holder for all binary expression where both operands must be boolean
 * expressions.
 */
public class BoolExpn extends BinaryExpn {
  public final static String OP_OR 	= "|";
  public final static String OP_AND	= "&";

  public BoolExpn(String opSymbol, Expn left, Expn right, SourceCoord sourceCoord) {
    super(opSymbol, left, right, sourceCoord);

    assert ((opSymbol == OP_OR) ||
            (opSymbol == OP_AND));
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
      if (this.left.getExpnType(st) == SymbolType.BOOLEAN &&
          this.right.getExpnType(st) == SymbolType.BOOLEAN) {
        this.expnType = SymbolType.BOOLEAN;
      } else {
        this.expnType = SymbolType.UNKNOWN;
      }
    }

    return this.expnType;
  }
}
