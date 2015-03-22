package compiler488.symbol;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import compiler488.ast.BaseAST;
import compiler488.ast.decl.ArrayDeclPart;
import compiler488.codegen.*;
import compiler488.runtime.*;
import compiler488.symbol.SymbolTable.SymbolKind;

/**
 * Symbol Table Scope
 *
 *  @author  <B> Haohan Jiang (g3jiangh)
 *               Maria Yancheva (c2yanche)
 *               Timo Vink (c4vinkti)
 *               Chandeep Singh (g2singh)
 *           </B>
 */
public class STScope {
	private STScope parent;
	private List<STScope> children;
	private HashMap<String,SymbolTableEntry> symbols;
	private short lexicalLevel;
	public short routineBodyAddress;
	public short nextOrderNumber;

	final static String scopeSep = "=======================================================\n";
	final static String scopeIndent = "    ";

	/* What kind of scope this is (program, function, ...) */
	public enum ScopeKind {
		PROGRAM,
		NORMAL,
		FUNCTION,
		PROCEDURE
	}
	private ScopeKind scopeKind;

	public STScope (ScopeKind kind, STScope parent) {
		this.parent = parent;
		this.children = new ArrayList<STScope>();
		this.symbols = new HashMap<String,SymbolTableEntry>();
		this.lexicalLevel = 0;
		this.scopeKind = kind;
		this.routineBodyAddress = Machine.UNDEFINED;

		if (kind == ScopeKind.NORMAL) {
			this.lexicalLevel = parent.lexicalLevel;
			this.nextOrderNumber = parent.nextOrderNumber;
		} else {
			this.lexicalLevel = (parent == null) ? (short)0 : (short)(parent.lexicalLevel + 1);
			this.nextOrderNumber = ActivationRecord.getOffsetToVariableStorage(this);
		}
	}

	public STScope getParent() {
		return parent;
	}

	public List<STScope> getChildren() {
		return children;
	}

	public void addChild(STScope child) {
		this.children.add(child);
	}

	public void setChildren(List<STScope> children) {
		this.children = children;
	}

	public void removeChildren() {
		this.children.clear();
	}

	public HashMap<String, SymbolTableEntry> getSymbols() {
		return symbols;
	}

	public void setSymbols(HashMap<String, SymbolTableEntry> symbols) {
		this.symbols = symbols;
	}

	public short getLexicalLevel() {
		return lexicalLevel;
	}

	public ScopeKind getScopeKind() {
		return scopeKind;
	}

	@Override
	public String toString() {
		return toString(0);
	}

	public String toString(int depth) {
		String s = "";

		// Get all keys in symbols
		for (Entry<String, SymbolTableEntry> id : this.symbols.entrySet()) {
			s += getIndent(depth) + id.getKey() + " = " + id.getValue() + "\n";
	}

		// If no symbols exist in this scope, display 'Empty'
		if (symbols.entrySet().size() == 0) {
			s += getIndent(depth) + "Empty scope" + "\n";
		}

		return s;
	}

	public String getIndent(int depth) {
		String s = "";
		for(int i = 0; i < depth; i++) {
			s += scopeIndent;
		}

		return s;
	}

	/**
	* display - recursively display the scope and all of its children (using indentation for child nodes)
	*
	* @param depth
	* @return
	*/
	public String display(int depth) {
		String s = "";
		s += scopeSep;
		s += toString(depth);
		for(STScope child : children) {
			s += child.display(depth + 1);
		}

		return s;
	}

	/*
	 *
	 */
	public VarAddress getVarAddress(String ident) {
		SymbolTableEntry ste = symbols.get(ident);
		if (ste != null) {
			return new VarAddress(lexicalLevel, ste.orderNumber);
		}
		else {
			return parent.getVarAddress(ident);
		}
	}
}
