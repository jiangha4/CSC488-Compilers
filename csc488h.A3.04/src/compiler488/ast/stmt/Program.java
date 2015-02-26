package compiler488.ast.stmt;

import compiler488.ast.ASTVisitor;
import compiler488.ast.PrettyPrinter;
import compiler488.ast.SourceCoord;


/**
 * Placeholder for the scope that is the entire program
 */
public class Program extends Scope {
	public Program(Scope scope, SourceCoord sourceCoord) {
		super(scope.getBody(), sourceCoord);
	}

	@Override
	public void prettyPrint(PrettyPrinter p) {
		super.prettyPrint(p);
		p.println("");
	}

	@Override
	public void accept(ASTVisitor visitor) {
		visitor.enterVisit(this);

		super.accept(visitor);

		visitor.exitVisit(this);
	}
}
