package compiler488.symbol;

import compiler488.ast.BaseAST;
import compiler488.symbol.SymbolTable.SymbolKind;
import compiler488.symbol.SymbolTable.SymbolType;

public class SymbolTableEntry {
	String id;    //Name of the symbol
	SymbolType type;  //Type of the symbol(integer, boolean)
	SymbolKind kind;  //Kind of the symbol(variable, function, procedure, parameter)
	BaseAST node; //AST node which this symbol represents
	short orderNumber;

	public SymbolTableEntry(String id, SymbolType type, SymbolKind kind, BaseAST node, short orderNumber) {
		this.id = id;
		this.type = type;
		this.kind = kind;
		this.node = node;
		this.orderNumber = orderNumber;
	}

	public String getId() {
		return id;
	}
	public SymbolType getType() {
		return type;
	}
	public SymbolKind getKind() {
		return kind;
	}
	public BaseAST getNode() {
		return node;
	}

	@Override
	public String toString() {
		return "SymbolTableEntry [id=" + id + ", type=" + type + ", kind=" + kind + "]";
	}
}
