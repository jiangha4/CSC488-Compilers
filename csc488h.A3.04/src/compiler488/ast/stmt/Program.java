package compiler488.ast.stmt;

import compiler488.ast.ASTVisitor;
import compiler488.ast.PrettyPrinter;
import compiler488.semantics.Semantics;

/**
 * Placeholder for the scope that is the entire program
 */
public class Program extends Scope {
    public Program(Scope scope) {
        super(scope.getBody());
    }

    @Override
    public void prettyPrint(PrettyPrinter p) {
        super.prettyPrint(p);
        p.println("");
    }
    
    @Override
	public void accept(ASTVisitor visitor) {
    	
    	visitor.visit(this);
		
    	// S00: start program scope
		((Semantics)visitor).getSymbolTable().enterScope();
		
		super.accept(visitor);
		
		// S01: end program scope
		((Semantics)visitor).getSymbolTable().exitScope();
	}
}
