package compiler488.symbol;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import compiler488.ast.BaseAST;
import compiler488.ast.decl.ArrayDeclPart;
import compiler488.codegen.VarAddress;
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
	public short nextOrderNumber;

	final static String scopeSep = "=======================================================\n";
	final static String scopeIndent = "    ";

	public STScope () {
		this.parent = null;
		this.children = new ArrayList<STScope>();
		this.symbols = new HashMap<String,SymbolTableEntry>();
		this.lexicalLevel = 0;
		this.nextOrderNumber = 0;
	}

	public STScope getParent() {
		return parent;
	}

	public void setParent(STScope parent) {
		if (parent != null) {
			this.parent = parent;
			this.lexicalLevel = (short)(parent.getLexicalLevel() + 1);
		}
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
