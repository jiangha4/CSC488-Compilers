package compiler488.ast.decl;

import compiler488.ast.ASTList;
import compiler488.ast.ASTVisitor;
import compiler488.ast.PrettyPrinter;
import compiler488.ast.SourceCoord;
import compiler488.ast.type.Type;

/**
 * Holds the declaration of multiple elements.
 */
public class MultiDeclarations extends Declaration {
    /** The parts being declared */
    private ASTList<DeclarationPart> elements;

    public MultiDeclarations(Type type, ASTList<DeclarationPart> elements, SourceCoord sourceCoord) {
        super(null, type, sourceCoord);

        elements.setParentNode(this);
        this.elements = elements;
    }

    public ASTList<DeclarationPart> getParts() {
        return elements;
    }

    public void prettyPrint(PrettyPrinter p) {
        p.print(type + " ");
        elements.prettyPrintCommas(p);
    }

    @Override
	public void accept(ASTVisitor visitor) {
    	
    	// Handle the following rules:
    	// S10: Declare scalar variables
    	// S19: Declare array variables with specified lower and upper bounds
    	// S48: Declare array variables with specified upper bound
		// S47: Associate type with variables
    	visitor.visit(this);
		
		type.accept(visitor);
		elements.accept(visitor);
	}
}
