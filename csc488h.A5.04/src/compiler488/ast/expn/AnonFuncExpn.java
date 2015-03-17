package compiler488.ast.expn;

import compiler488.ast.*;
import compiler488.ast.stmt.Scope;
import compiler488.ast.stmt.Stmt;
import compiler488.semantics.Semantics;
import compiler488.symbol.*;
import compiler488.symbol.SymbolTable.SymbolType;

/**
 * Represents the parameters and instructions associated with a function or
 * procedure.
 */
public class AnonFuncExpn extends Expn implements ScopeCreator {
	/** Execute these statements. */
	private ASTList<Stmt> body;

	/** The expression whose value is yielded. */
	private Expn expn;

	/** The symbol table scope created by this expn **/
	private STScope stScope;

	public AnonFuncExpn(ASTList<Stmt> body, Expn expn, SourceCoord sourceCoord) {
		super(sourceCoord);

		body.setParentNode(this);
		this.body = body;
		expn.setParentNode(this);
		this.expn = expn;
	}

	public ASTList<Stmt> getBody() {
		return body;
	}

	public Expn getExpn() {
		return expn;
	}

	public void setSTScope(STScope scope) {
		stScope = scope;
	}

	public STScope getSTScope() {
		return stScope;
	}

	public void prettyPrint(PrettyPrinter p) {
		p.println("{");
		p.enterBlock();

		body.prettyPrintNewlines(p);

		p.print("yields ");
		expn.prettyPrint(p);
		p.newline();

		p.exitBlock();
		p.println("}");
	}

	@Override
	public void accept(ASTVisitor visitor) {
		visitor.enterVisit(this);

		body.accept(visitor);
		expn.accept(visitor);

		visitor.exitVisit(this);
	}

	/**
	 * getExpnType : recursively returns the type of the expression of the anon func
	 */
	@Override
	public SymbolType getExpnType(SymbolTable st) {
		if (this.expnType == null) {
			this.expnType = this.expn.getExpnType(st);
		}
		return this.expnType;
	}
}
