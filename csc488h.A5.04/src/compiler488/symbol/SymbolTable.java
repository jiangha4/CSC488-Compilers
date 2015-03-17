package compiler488.symbol;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import compiler488.ast.BaseAST;

/**
 * Symbol Table
 *
 *  @author  <B> Haohan Jiang (g3jiangh)
 *               Maria Yancheva (c2yanche)
 *               Timo Vink (c4vinkti)
 *               Chandeep Singh (g2singh)
 *           </B>
 */
public class SymbolTable {
	public STScope currentScope;
	STScope rootScope;

	/**
	 * Create and initialize a symbol table
	 */
	public SymbolTable() {
		this.currentScope = null;
		this.rootScope = null;
	}

	// Allowed values for 'type'
	public enum SymbolType {
		INTEGER, BOOLEAN, UNKNOWN, TEXT, SKIP
	}

	// Allowed values for 'kind'
	public enum SymbolKind {
		VARIABLE, ARRAY, FUNCTION, PROCEDURE, PARAMETER
	}

	/**
	 * insert - enter a new symbol table entry in current scope
	 *
	 * @param id : identifier (name of variable)
	 * @param type : 'integer', 'boolean', etc
	 * @param kind : 'variable', 'procedure', 'function', etc
	 * @param node : link to AST node
	 * @return boolean: true if successful, false otherwise
	 */
	public boolean insert(String id, SymbolType type, SymbolKind kind, BaseAST node) {
		// Make sure we have a current scope
		if (this.currentScope != null) {
			return insert(currentScope, id, type, kind, node);
		}
		return false;
	}

	/**
	 * insert - enter a new symbol table entry in designated scope
	 *
	 * @param scope : a hashmap representing the desired scope in the hierarchy of scopes
	 * @param id : identifier (name of variable)
	 * @param type : 'integer', 'boolean', etc
	 * @param kind : 'variable', 'procedure', 'function', etc
	 * @param node : link to AST node
	 * @return boolean: true if successful, false otherwise
	 */
	public boolean insert(STScope scope, String id, SymbolType type, SymbolKind kind, BaseAST node) {
		// Create a new entry and add to designated scope
		HashMap<String,SymbolTableEntry> symbols = scope.getSymbols();
		if (!symbols.containsKey(id)) {
			SymbolTableEntry entry = new SymbolTableEntry(id, type, kind, node);
			symbols.put(id, entry);
			return true;
		}
		return false;
	}

	/**
	 * delete - delete an existing symbol table entry from current scope
	 *
	 * @param id : identifier (name of variable)
	 * @return boolean: true if successful, false otherwise
	 */
	public boolean delete(String id) {
		// Make sure we have a current scope
		if (this.currentScope != null) {
			return delete(currentScope, id);
		}
		return false;
	}

	/**
	 * delete - delete an existing symbol table entry from designated scope
	 *
	 * @param scope : a hashmap representing the desired scope in the hierarchy of scopes
	 * @param id : identifier (name of variable)
	 * @return boolean: true if successful, false otherwise
	 */
	public boolean delete(STScope scope, String id) {
		// Delete existing entry from designated scope
		HashMap<String,SymbolTableEntry> symbols = scope.getSymbols();
		if (symbols.containsKey(id)) {
			symbols.remove(id);
			return true;
		}
		return false;
	}

	/**
	 * search - look up symbol table entry in current scope
	 * (e.g., use to check for re-declaration of identifier in same scope)
	 *
	 * @param id : the symbol (identifier) to search the symbol table for
	 * @return SymbolTableEntry if found, or null if not found
	 */
	public SymbolTableEntry search(String id) {
		if (this.currentScope != null) {
			HashMap<String, SymbolTableEntry> symbols = currentScope.getSymbols();
			if (symbols.containsKey(id)) {
				return symbols.get(id);
			}
		}
		return null;
	}

	/**
	 * searchGlobal - look up if symbol table entry is visible in current scope (i.e. defined in any scope so far)
	 * (e.g., use to check whether an identifier has been declared or not)
	 *
	 * @param id : the symbol (identifier) to search the symbol table for
	 * @return SymbolTableEntry if found, or null if not found
	 */
	public SymbolTableEntry searchGlobal(String id) {
		STScope scope = currentScope;
		while(scope != null) {
			HashMap<String, SymbolTableEntry> symbols = scope.getSymbols();
			if (symbols.containsKey(id)) {
				return symbols.get(id);
			}
			scope = scope.getParent();
		}

		return null;
	}

	/**
	 * enterScope - enter a new scope
	 *
	 * @return A reference to the newly created STScope
	 */
	public STScope enterScope() {
		// Add new scope as child of current scope
		STScope newScope = new STScope();
		newScope.setParent(currentScope);
		if (currentScope != null) {
			currentScope.addChild(newScope);
		}

		currentScope = newScope;
		if (rootScope == null) {
			rootScope = newScope;
		}

		return currentScope;
	}

	/**
	 * exitScope - exit current scope
	 */
	public void exitScope() {
		// Exit to parent scope
		if (this.currentScope != null) {
			this.currentScope = this.currentScope.getParent();
		}
	}

	/**
	 * toString - construct a text representation of the scope stack (most recent/current on top)
	 *
	 * @return String : a text representation of the scope stack
	 */
	@Override
	public String toString() {
		String s = "TRAVERSAL FROM CURRENT SCOPE -> GLOBAL SCOPE\n";
		STScope scope = currentScope;

		while (scope != null) {
			s += STScope.scopeSep;
			s += scope.toString();
			scope = scope.getParent();
		}

		s += STScope.scopeSep;
		s += "END OF LIST\n";

		return s;
	}

	public String fullTraversal() {
		String s = "FULL TRAVERSAL OF SYMBOL TABLE (ROOT TO LEAVES)\n";
		if (rootScope != null) {
			int depth = 0;
			s += rootScope.display(depth);
		}
		s += STScope.scopeSep + "END OF TRAVERSAL\n";

		return s;
	}
}
