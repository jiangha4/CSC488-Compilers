package compiler488.ast.stmt;

import compiler488.ast.ASTVisitor;
import compiler488.ast.BaseAST;
import compiler488.ast.PrettyPrinter;
import compiler488.ast.SourceCoord;
import compiler488.ast.decl.RoutineDecl;
import compiler488.ast.expn.AnonFuncExpn;
import compiler488.ast.expn.Expn;


/**
 * Represents the command to exit from a loop.
 */
public class ExitStmt extends Stmt {
	// Condition expression for <code>exit when</code> variation.
	private Expn expn = null;

	public ExitStmt(Expn expn, SourceCoord sourceCoord) {
		super(sourceCoord);

		if (expn != null) {
			expn.setParentNode(this);
		}
		this.expn = expn;
	}

	public ExitStmt(SourceCoord sourceCoord) {
		this(null, sourceCoord);
	}

	public Expn getExpn() {
		return expn;
	}
	
	/**
	 * Traverses the AST upwards and returns the first loop
	 * containing the exit statement. If none found, returns null
	 * @return containing LoopingStmt if found, null otherwise
	 */
	public LoopingStmt getContainingLoop() {
		LoopingStmt loop = null;
		
		BaseAST currNode = this;
		while(currNode != null && !(currNode instanceof RoutineDecl) && !(currNode instanceof AnonFuncExpn))
		{
			if (currNode instanceof LoopStmt || currNode instanceof WhileDoStmt) {
				loop = (LoopingStmt)currNode;
				break;
			}
			currNode = currNode.getParentNode();
		}
		
		return loop;
	}

	@Override
	public void prettyPrint(PrettyPrinter p) {
		p.print("exit");

		if (expn != null) {
			p.print(" when ");
			expn.prettyPrint(p);
		}
	}

	@Override
	public void accept(ASTVisitor visitor) {
		visitor.enterVisit(this);

		if (expn != null) {
			expn.accept(visitor);
		}

		visitor.exitVisit(this);
	}
}
