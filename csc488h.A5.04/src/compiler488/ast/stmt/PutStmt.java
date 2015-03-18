package compiler488.ast.stmt;

import compiler488.ast.*;
import compiler488.ast.expn.*;
import compiler488.symbol.*;
import compiler488.symbol.SymbolTable.*;


/**
 * The command to write data on the output device.
 */
public class PutStmt extends Stmt {
	/** The values to be printed. */
	private ASTList<Printable> outputs;

	public PutStmt(ASTList<Printable> outputs, SourceCoord sourceCoord) {
		super(sourceCoord);

		outputs.setParentNode(this);
		this.outputs = outputs;
	}

	public ASTList<Printable> getOutputs() {
		return outputs;
	}

	public int getNumChildrenOfType(SymbolTable st, SymbolType expnType) {
		int count = 0;
		for (Printable p : outputs) {
			if (((Expn)p).getExpnType(st) == expnType) {
				count++;
			}
		}

		return count;
	}

	@Override
	public void prettyPrint(PrettyPrinter p) {
		p.print("put ");
		outputs.prettyPrintCommas(p);
	}

	@Override
	public void accept(ASTVisitor visitor) {
		visitor.enterVisit(this);

		outputs.accept(visitor);

		visitor.exitVisit(this);
	}
}
