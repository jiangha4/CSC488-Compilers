package compiler488.ast.stmt;

import compiler488.ast.ASTList;
import compiler488.ast.ASTVisitor;
import compiler488.ast.PrettyPrinter;
import compiler488.ast.SourceCoord;
import compiler488.ast.expn.Expn;

/**
 * The command to read data into one or more variables.
 */
public class GetStmt extends Stmt {
    /** A list of locations to store the values read. */
    private ASTList<Expn> inputs;

    public GetStmt (ASTList<Expn> inputs, SourceCoord sourceCoord) {
        super(sourceCoord);

        inputs.setParentNode(this);
        this.inputs = inputs;
    }

    public ASTList<Expn> getInputs() {
        return inputs;
    }

    public void prettyPrint(PrettyPrinter p) {
        p.print("get ");
        inputs.prettyPrintCommas(p);
    }

    @Override
	public void accept(ASTVisitor visitor) {
		visitor.visit(this);
		inputs.accept(visitor);
	}
}
