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
	
	/* Every AST node contains a link to a SymbolTable entry */
	SymbolTableEntry stEntry = null;
	
	/* Keep track if the AST node has been visited */
	boolean isVisited = false;
	
    /**
     * Default constructor.
     *
     * <p>Add additional information to your AST tree nodes here.</p>
     */
	public BaseAST() {
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
	 * @return boolean : true if visited, false otherwise
	 */
	public boolean isVisited() {
		return isVisited;
	}
	
	/**
	 * 
	 * @param isVisited : true if visited, false otherwise. This is set to true by the Semantics analyzer when it first visits the AST node.
	 */
	public void setVisited(boolean isVisited) {
		this.isVisited = isVisited;
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
