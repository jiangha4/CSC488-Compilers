package compiler488.symbol;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map.Entry;

import compiler488.ast.BaseAST;


/** Symbol Table
 *  This almost empty class is a framework for implementing
 *  a Symbol Table class for the CSC488S compiler
 *
 *  Each implementation can change/modify/delete this class
 *  as they see fit.
 *
 *  @author  <B> Haohan Jiang (g3jiangh)
 *               Maria Yancheva (c2yanche)
 *               Timo Vink (c4vinkti)
 *               Chandeep Singh (g2singh)
 *           </B>
 */

public class SymbolTable {
	
	private class SymbolTableEntry {
		String id;    //Name of the symbol
		String type;  //Type of the symbol(integer, boolean, array, bound)
		String kind;  //Kind of the symbol(variable, constant, function, procedure...)
		BaseAST node; //AST node which this symbol represents

		public SymbolTableEntry(String id, String type, String kind, BaseAST node) {
			this.id = id;
			this.type = type;
			this.kind = kind;
			this.node = node;
		}

		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
		public String getKind() {
			return kind;
		}
		public void setKind(String kind) {
			this.kind = kind;
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
					+ kind + "]";
		}
	}

	/* Most recent scope at the beginning of the linked list (first), and oldest scope at the end of the list (last) */
	LinkedList<HashMap<String,SymbolTableEntry>> scopeList;

	
	/** Symbol Table  constructor
	 *  Create and initialize a symbol table
	 */
	public SymbolTable  (){

		// NOTE: putting everything in here for now
		//       do we need to split stuff to Initialize/Finalize?

		// Instantiate
		this.scopeList = new LinkedList<HashMap<String,SymbolTableEntry>>();
		
	}

	/**  Initialize - called once by semantic analysis
	 *                at the start of compilation
	 *                May be unnecessary if constructor
 	 *                does all required initialization
	 */
	public void Initialize() {

	   /**   Initialize the symbol table
	    *	Any additional symbol table initialization
	    *  GOES HERE
	    */

	}

	/**  Finalize - called once by Semantics at the end of compilation
	 *              May be unnecessary
	 */
	public void Finalize(){

	  /**  Additional finalization code for the
	   *  symbol table  class GOES HERE.
	   *
	   */
	}

	/** The rest of Symbol Table
	 *  Data structures, public and private functions
 	 *  to implement the Symbol Table
	 *  GO HERE.
	 */

	// TODO: enums for type/kind

	/**
	 * insert - enter a new symbol table entry in current scope
	 *
	 * @param id : identifier (name of variable)
	 * @param type : 'integer', 'boolean', etc
	 * @param kind : 'variable', 'procedure', 'function', etc
	 * @param node : link to AST node
	 * @return boolean: true if successful, false otherwise
	 */
	public boolean insert(String id, String type, String kind, BaseAST node) {

		// Get current scope's hashmap
		if (!scopeList.isEmpty()) {
			HashMap<String,SymbolTableEntry> currentScope = scopeList.getFirst();
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
	public boolean insert(HashMap<String,SymbolTableEntry> scope, String id, String type, String kind, BaseAST node) {
		
		// Create a new entry and add to designated scope
		if (scopeList.contains(scope) && !scope.containsKey(id)) {
			SymbolTableEntry entry = new SymbolTableEntry(id, type, kind, node);
			scope.put(id, entry);
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

		// Get current scope's hashmap
		if (!scopeList.isEmpty()) {
			HashMap<String,SymbolTableEntry> currentScope = scopeList.getFirst();
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
	public boolean delete(HashMap<String,SymbolTableEntry> scope, String id) {

		// Delete existing entry from designated scope
		if (scopeList.contains(scope) && scope.containsKey(id)) {
			scope.remove(id);
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
		if (!scopeList.isEmpty()) {
			HashMap<String, SymbolTableEntry> currentScope = scopeList.getFirst();
			if (currentScope.containsKey(id)) {
				return currentScope.get(id);
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
		Iterator<HashMap<String, SymbolTableEntry>> scope = scopeList.iterator();
		while(scope.hasNext()) {
			HashMap<String, SymbolTableEntry> nextScope = scope.next();
			if (nextScope.containsKey(id)) {
				return nextScope.get(id);
			}
		}

		return null;
	}

	/**
	 * enterScope - enter a new scope
	 * 
	 * @return HashMap<String,SymbolTableEntry> (a reference to the newly created current scope)
	 */
	public HashMap<String,SymbolTableEntry> enterScope() {
		
		// Add new HashMap to beginning of scopeList
		HashMap<String,SymbolTableEntry> newScope = new HashMap<String,SymbolTableEntry>();
		scopeList.addFirst(newScope);
		return newScope;
	}

	/**
	 * exitScope - exit current scope
	 * 
	 */
	public void exitScope() {
		
		// Remove the most recent scope
		if (!scopeList.isEmpty()) {
			scopeList.removeFirst();
		}
	}

	/**
	 * toString - construct a text representation of the scope stack (most recent/current on top)
	 * 
	 * @return String : a text representation of the scope stack
	 */
	public String toString() {
		String s = "TOP OF LIST (CURRENT SCOPE)\n";
		
		Iterator<HashMap<String, SymbolTableEntry>> scopeIter = scopeList.iterator();

		while (scopeIter.hasNext()) {
			s += "=======================================================\n";

			// get scope
			HashMap<String,SymbolTableEntry> scope = scopeIter.next();

			// Get all keys in scope
			for(Entry<String, SymbolTableEntry> id : scope.entrySet()) {
				s += id.getKey() + " = " + id.getValue() + "\n";
	        }

		}
		return s;
	}

	public static void main(String argv[]) {

		System.out.println("Hai.");

		SymbolTable st = new SymbolTable();

		st.enterScope();
		st.insert("abc", "Integer", "Variable", null);
		st.insert("othervar", "Boolean", "Variable", null);
		st.enterScope();
		st.insert("newscope", "Integer", "Variable", null);
//		st.exitScope();

		System.out.println(st.toString());

		st.exitScope();

		System.out.println("\n\nAfter exiting scope\n\n");

		System.out.println(st.toString());

		return;

	}

}
