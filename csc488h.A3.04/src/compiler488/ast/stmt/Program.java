package compiler488.ast.stmt;

import compiler488.ast.ASTList;
import compiler488.ast.PrettyPrinter;

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
}
