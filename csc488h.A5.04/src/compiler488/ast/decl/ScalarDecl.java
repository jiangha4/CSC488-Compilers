package compiler488.ast.decl;

import compiler488.ast.ASTVisitor;
import compiler488.ast.PrettyPrinter;
import compiler488.ast.SourceCoord;
import compiler488.ast.type.Type;

/**
 * Represents the declaration of a simple variable.
 */
public class ScalarDecl extends Declaration {
	public ScalarDecl(String name, Type type, SourceCoord sourceCoord) {
		super(name, type, sourceCoord);
	}

	@Override
	public void prettyPrint(PrettyPrinter p) {
		p.print(type + " " + name);
	}

	@Override
	public void accept(ASTVisitor visitor) {
		visitor.enterVisit(this);

		type.accept(visitor);

		visitor.exitVisit(this);
	}
}
