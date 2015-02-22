package compiler488.ast.decl;

import compiler488.ast.ASTVisitor;
import compiler488.symbol.SymbolTable.SymbolKind;

/**
 * Represents the declaration of a simple variable.
 */
public class ScalarDeclPart extends DeclarationPart {
    public ScalarDeclPart(String name) {
        super(name);
    }

    /**
     * Returns a string describing the name of the object being
     * declared.
     */
    @Override
    public String toString() {
        return name;
    }
    
    @Override
    public SymbolKind getKind() {
    	return SymbolKind.VARIABLE;
    }

    @Override
	public void accept(ASTVisitor visitor) {
		visitor.visit(this);
	}
}
