package compiler488.ast.stmt;

import compiler488.ast.Assignable;
import compiler488.ast.ASTList;
import compiler488.ast.PrettyPrinter;

/**
 * The command to read data into one or more variables.
 */
public class GetStmt extends Stmt {
    /** A list of locations to store the values read. */
    private ASTList<Assignable> inputs;

    public GetStmt (ASTList<Assignable> inputs) {
        super();

        this.inputs = inputs;
    }

    public ASTList<Assignable> getInputs() {
        return inputs;
    }

    public void prettyPrint(PrettyPrinter p) {
        p.print("get ");
        inputs.prettyPrintCommas(p);
    }
}
