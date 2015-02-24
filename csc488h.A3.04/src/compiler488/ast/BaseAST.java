package compiler488.ast;

import compiler488.ast.stmt.Scope;
import compiler488.symbol.SymbolTableEntry;

/**
 * Base class implementation for the AST hierarchy.
 *
 * This is a convenient place to add common behaviours.
 *
 * @author Dave Wortman, Marsha Chechik, Danny House, Peter McCormick
 */
public abstract class BaseAST implements AST {
	
	/* Every AST node contains a source coordinate (in the source program) 
	 * This is used for error reporting */
	SourceCoord sourceCoord = null;
	
	/* Every AST node contains a link to a SymbolTable entry */
	SymbolTableEntry stEntry = null;
	
	/* Keep track if the AST node has been visited */
	boolean isVisited = false;
	
	/* Keeps track of the control statement of the parent node and stored in the child node 
	 * This is going to be used to check rules S50-S53 
	 */
	controlStatement parentControlType = null;
	
	public enum controlStatement {
		LOOP, WHILE
	}
	
	/*
	 * The parent of the current node
	 */
	BaseAST parentNode = null;
	
    /**
     * Default constructor.
     *
     * <p>Add additional information to your AST tree nodes here.</p>
     */
	public BaseAST(SourceCoord sourceCoord) {
		this.sourceCoord = sourceCoord;
	}
	
	/**
	 * 
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
	 * 
	 * @param statement : sets the control type of the parent for the node
	 */
	public void setControlStatement(controlStatement statement){
		this.parentControlType = statement;
	}
	
	/**
	 * 
	 * @param node : Current AST node
	 * @return : the control type of the parent node
	 */
	public controlStatement getControlStatement(){
		return this.parentControlType;
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
