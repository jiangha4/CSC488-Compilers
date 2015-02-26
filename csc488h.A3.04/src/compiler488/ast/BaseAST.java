package compiler488.ast;

import compiler488.symbol.SymbolTableEntry;


/**
 * Base class implementation for the AST hierarchy.
 *
 * This is a convenient place to add common behaviours.
 *
 * @author Dave Wortman, Marsha Chechik, Danny House, Peter McCormick
 */
public abstract class BaseAST implements AST {
	/**
	 * Every AST node contains a source coordinate (in the source program)
	 * This is used for error reporting
	 */
	SourceCoord sourceCoord = null;

	/**
	 * Every AST node contains a link to a SymbolTable entry
	 */
	SymbolTableEntry stEntry = null;

	/**
	 * The parent of the current node
	 */
	BaseAST parentNode = null;

	/**
	 * Default constructor.
	 */
	public BaseAST(SourceCoord sourceCoord) {
		this.sourceCoord = sourceCoord;
	}

	/**
	 * @return SourceCoord : the source coordinates (line and col) into the source program
	 */
	public SourceCoord getSourceCoord() {
		return sourceCoord;
	}

	/**
	 *
	 * @param sourceCoord : the source coordinates (line and col) in the source program
	 */
	public void setSourceCoord(SourceCoord sourceCoord) {
		this.sourceCoord = sourceCoord;
	}

	/**
	 * getSTEntry - get the symbol table entry that this AST node links to
	 *
	 * @return SymbolTableEntry : the entry in the SymbolTable that this AST node links to
	 */
	public SymbolTableEntry getSTEntry() {
		return stEntry;
	}

	/**
	 *
	 * @param stEntry - an entry in the symbol table
	 */
	public void setSTEntry(SymbolTableEntry stEntry) {
		this.stEntry = stEntry;
	}

	/**
	 * Sets the parent node for the current node
	 * @param node : The parent node
	 */
	public void setParentNode(BaseAST node){
		this.parentNode = node;
	}

	/**
	 *
	 * @return : The parent of the current node
	 */
	public BaseAST getParentNode(){
		return this.parentNode;
	}

	/**
	 * A default pretty-printer implementation that uses <code>toString</code>.
	 *
	 * @param p the printer to use
	 */
	@Override
	public void prettyPrint(PrettyPrinter p) {
		p.print(toString());
	}

	/**
	 * All AST nodes are ASTVisitable, and must provide implementations of this method.
	 */
	@Override
	public abstract void accept(ASTVisitor visitor);
}
