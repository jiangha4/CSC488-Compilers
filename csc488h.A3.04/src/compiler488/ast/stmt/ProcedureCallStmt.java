package compiler488.ast.stmt;

import compiler488.ast.ASTList;
import compiler488.ast.ASTVisitor;
import compiler488.ast.PrettyPrinter;
import compiler488.ast.SourceCoord;
import compiler488.ast.expn.Expn;

/**
 * Represents calling a procedure.
 */
public class ProcedureCallStmt extends Stmt {
    /** The name of the procedure being called. */
    private String name;

    /** The arguments passed to the procedure (if any.)
     *
     * <p>This value must be non-<code>null</code>. If the procedure takes no
     * parameters, represent that with an empty list here instead.</p>
     */
    private ASTList<Expn> arguments;

    public ProcedureCallStmt(String name, ASTList<Expn> arguments, SourceCoord sourceCoord) {
        super(sourceCoord);

        this.name = name;
        arguments.setParentNode(this);
        this.arguments = arguments;
    }

    public ProcedureCallStmt(String name, SourceCoord sourceCoord) {
        this(name, new ASTList<Expn>(), sourceCoord);
    }

    public String getName() {
        return name;
    }

    public ASTList<Expn> getArguments() {
        return arguments;
    }

    @Override
    public void prettyPrint(PrettyPrinter p) {
        p.print(name);

        if ((arguments != null) && (arguments.size() > 0)) {
            p.print("(");
            arguments.prettyPrintCommas(p);
            p.print(")");
        }
    }

    @Override
	public void accept(ASTVisitor visitor) {

    	// S41: check that identifier has been declared as a procedure
    	// S42/S43: check that the number of parameters declared in the procedure declaration is the same as the
    	// number of arguments passed to the procedure call statement
    	visitor.visit(this);

		if ((arguments != null) && (arguments.size() > 0)) {
			arguments.accept(visitor);
		}
	}
}
