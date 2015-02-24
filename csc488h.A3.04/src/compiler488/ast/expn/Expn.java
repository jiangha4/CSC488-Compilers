package compiler488.ast.expn;

import compiler488.ast.BaseAST;
import compiler488.ast.Printable;
import compiler488.ast.SourceCoord;

/**
 * A placeholder for all expressions.
 */
public abstract class Expn extends BaseAST implements Printable {
	public Expn(SourceCoord sourceCoord) {
		super(sourceCoord);
	}
}
