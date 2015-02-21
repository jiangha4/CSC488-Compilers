package compiler488.symbol;

import compiler488.ast.BaseAST;
import compiler488.symbol.SymbolTable.SymbolKind;
import compiler488.symbol.SymbolTable.SymbolType;

public class SymbolTableEntry {
	String id;    //Name of the symbol
	SymbolType type;  //Type of the symbol(integer, boolean)
	SymbolKind kind;  //Kind of the symbol(variable, function, procedure, parameter)
	String value; //Value of the symbol (e.g. the actual value that an integer variable takes on in a given scope)
	BaseAST node; //AST node which this symbol represents

	public SymbolTableEntry(String id, SymbolType type, SymbolKind kind, BaseAST node) {
		this(id, type, kind, "", node);
	}
	
	public SymbolTableEntry(String id, SymbolType type, SymbolKind kind, String value, BaseAST node) {
		this.id = id;
		this.type = type;
		this.kind = kind;
		this.value = value;
		this.node = node;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public SymbolType getType() {
		return type;
	}
	public void setType(SymbolType type) {
		this.type = type;
	}
	public SymbolKind getKind() {
		return kind;
	}
	public void setKind(SymbolKind kind) {
		this.kind = kind;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public BaseAST getNode() {
		return node;
	}
	public void setNode(BaseAST node) {
		this.node = node;
	}

	@Override
	public String toString() {
		return "SymbolTableEntry [id=" + id + ", type=" + type + ", kind="
				+ kind + ", value=" + value + "]";
	}
}
