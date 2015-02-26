package compiler488.ast.decl;

import compiler488.ast.ASTVisitable;
import compiler488.ast.ASTVisitor;
import compiler488.ast.BaseAST;
import compiler488.ast.SourceCoord;
import compiler488.symbol.SymbolTable.SymbolKind;

/**
 * The common features of declarations' parts.
 */
public abstract class DeclarationPart extends BaseAST implements ASTVisitable {
	/** The name of the thing being declared. */
	protected String name;

	public DeclarationPart(String name, SourceCoord sourceCoord) {
		super(sourceCoord);

		this.name = name;
	}

	public String getName() {
		return name;
	}

	public abstract SymbolKind getKind();

	@Override
	public void accept(ASTVisitor visitor) {
		visitor.enterVisit(this);
		visitor.exitVisit(this);
	}
}
