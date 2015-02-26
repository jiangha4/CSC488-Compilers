package compiler488.ast.expn;

import compiler488.ast.ASTVisitor;
import compiler488.ast.PrettyPrinter;
import compiler488.ast.Readable;
import compiler488.ast.SourceCoord;
import compiler488.symbol.SymbolTable;
import compiler488.symbol.SymbolTable.SymbolType;


/**
 * References to an array element variable
 */
public class SubsExpn extends Expn implements Readable {
	/** Name of the array variable. */
	private String variable;

	/** First subscript. */
	private Expn subscript1;

	/** Second subscript (if any.) */
	private Expn subscript2 = null;

	public SubsExpn(String variable, Expn subscript1, Expn subscript2, SourceCoord sourceCoord) {
		super(sourceCoord);

		this.variable = variable;
		subscript1.setParentNode(this);
		this.subscript1 = subscript1;
		if (subscript2 != null) {
			subscript2.setParentNode(this);
		}
		this.subscript2 = subscript2;
	}

	public SubsExpn(String variable, Expn subscript1, SourceCoord sourceCoord) {
		this(variable, subscript1, null, sourceCoord);
	}

	public String getVariable() {
		return variable;
	}

	public Expn getSubscript1() {
		return subscript1 ;
	}

	public Expn getSubscript2() {
		return subscript2;
	}

	public boolean isTwoDimensional() {
		return subscript2 != null;
	}

	public void prettyPrint(PrettyPrinter p) {
		p.print(variable + "[");

		subscript1.prettyPrint(p);

		if (subscript2 != null) {
			p.print(", ");
			subscript2.prettyPrint(p);
		}

		p.print("]");
	}

	@Override
	public void accept(ASTVisitor visitor) {
		visitor.enterVisit(this);

		subscript1.accept(visitor);
		if (subscript2 != null) {
			subscript2.accept(visitor);
		}

		visitor.exitVisit(this);
	}

	@Override
	public SymbolType getExpnType(SymbolTable st) {
		if (this.expnType == null) {
			this.expnType = this.getSTETypeOrUnknown(st, this.variable);
		}
		return this.expnType;
	}
}
