package compiler488.ast.stmt;

import java.util.ArrayList;
import java.util.List;

import compiler488.ast.ASTList;
import compiler488.ast.SourceCoord;
import compiler488.ast.expn.Expn;


/**
 * Represents the common parts of loops.
 */
public abstract class LoopingStmt extends Stmt {
	/** The control expression for the looping construct (if any.) */
	protected Expn expn = null;

	/** The body of the looping construct. */
	protected ASTList<Stmt> body;
	
	/** Instruction locations to patch later during code gen **/
	public short startOfLoop;
	public ArrayList<Short> shouldPointToEnd;

	public LoopingStmt(Expn expn, ASTList<Stmt> body, SourceCoord sourceCoord) {
		super(sourceCoord);

		if (expn != null) {
			expn.setParentNode(this);
		}
		this.expn = expn;
		body.setParentNode(this);
		this.body = body;
		this.startOfLoop = 0;
		this.shouldPointToEnd = new ArrayList<Short>();
	}

	public LoopingStmt(ASTList<Stmt> body, SourceCoord sourceCoord) {
		this(null, body, sourceCoord);
	}

	public Expn getExpn() {
		return expn;
	}

	public ASTList<Stmt> getBody() {
		return body;
	}

	/**
	 * containsReturn - the looping statement will recursively check each of its child statements for a return statement
	 */
	@Override
	public ReturnStmt containsReturn() {
		for (Stmt child : body) {
			ReturnStmt rs = child.containsReturn();
			if (rs != null) {
				return rs;
			}
		}
		return null;
	}
}
