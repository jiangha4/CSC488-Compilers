package compiler488.symbol;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import compiler488.ast.BaseAST;
import compiler488.ast.decl.ArrayDeclPart;
import compiler488.symbol.STScope.ScopeKind;


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
		HashMap<String,SymbolTableEntry> symbols = scope.getSymbols();
		if (!symbols.containsKey(id)) {
			// Create a new entry and add to designated scope
			SymbolTableEntry entry = new SymbolTableEntry(id, type, kind, node, scope.nextOrderNumber);
			symbols.put(id, entry);

			// Increment order number
			short size = 0;
			if (kind == SymbolKind.VARIABLE || kind == SymbolKind.PARAMETER) {
				size = 1;
			} else if (kind == SymbolKind.ARRAY) {
				size = (short)((ArrayDeclPart)node).getSize();
			}
			scope.nextOrderNumber += size;

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
	 * searchGlobal - searchGlobalFrom the current scope.
	 */
	public SymbolTableEntry searchGlobal(String id) {
		return searchGlobalFrom(id, currentScope);
	}

	/**
	 * searchGlobalFrom - look up if symbol table entry is visible in the given
	 * scope (i.e. it's in the given scope or a contained scope)
	 * Can be used to check whether an identifier has been declared or not.
	 *
	 * @param id : the symbol (identifier) to search the symbol table for
	 * @param scope : the scope to start the search from
	 * @return SymbolTableEntry if found, or null if not found
	 */
	public SymbolTableEntry searchGlobalFrom(String id, STScope scope) {
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
	public STScope enterScope(ScopeKind kind) {
		// Add new scope as child of current scope
		STScope newScope = new STScope(kind, currentScope);
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
			STScope parent = this.currentScope.getParent();
			if (parent != null) {
				parent.nextOrderNumber = this.currentScope.nextOrderNumber;
			}
			this.currentScope = parent;
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
