package compiler488.symbol;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

	STScope currentScope;
	STScope firstScope;

	public class STScope {
		private STScope parent;
		private List<STScope> children;
		private HashMap<String,SymbolTableEntry> symbols;
		final static String scopeSep = "=======================================================\n";
		final static String scopeIndent = "    ";
		
		public STScope () {
			this.parent = null;
			this.children = new ArrayList<STScope>();
			this.symbols = new HashMap<String,SymbolTableEntry>();
		}
		public STScope getParent() {
			return parent;
		}
		public void setParent(STScope parent) {
			this.parent = parent;
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
		@Override
		public String toString() {
			return toString(0);
		}
		
		public String toString(int depth) {
			String s = "";
			
			// Get all keys in symbols
			for(Entry<String, SymbolTableEntry> id : this.symbols.entrySet()) {
				s += displayIndent(depth) + id.getKey() + " = " + id.getValue() + "\n";
	        }
			// If no symbols exist in this scope, display 'Empty'
			if (symbols.entrySet().size() == 0) {
				s += displayIndent(depth) + "Empty scope" + "\n";
			}
			return s;
		}
		
		public String displayIndent(int depth) {
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
		
	}
	
	/** Symbol Table  constructor
	 *  Create and initialize a symbol table
	 */
	public SymbolTable  (){

		// NOTE: putting everything in here for now
		//       do we need to split stuff to Initialize/Finalize?

		// Instantiate
		this.currentScope = null;
		this.firstScope = null;
		
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

	// Allowed values for 'type'
	public enum SymbolType {
		INTEGER, BOOLEAN, UNKNOWN
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
	public boolean insert(String id, SymbolType type, SymbolKind kind, String value, BaseAST node) {

		// Make sure we have a current scope
		if (this.currentScope != null) {
			return insert(currentScope, id, type, kind, value, node);
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
	public boolean insert(STScope scope, String id, SymbolType type, SymbolKind kind, String value, BaseAST node) {
		
		// Create a new entry and add to designated scope
		HashMap<String,SymbolTableEntry> symbols = scope.getSymbols();
		if (!symbols.containsKey(id)) {
			SymbolTableEntry entry = new SymbolTableEntry(id, type, kind, value, node);
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
	 * getValue - return the value of an identifier that exists in current scope
	 * 
	 * @param id : identifier (name of variable)
	 * @return String : if identifier exists in current scope return its value; return null otherwise
	 */
	public String getValue(String id) {
		SymbolTableEntry entry = searchGlobal(id);
		if (entry != null) {
			return entry.getValue();
		}
		return null;
	}
	
	/**
	 * setValue - set the value of an identifier that exists in current scope
	 * 
	 * @param id : identifier (name of variable)
	 * @param newValue : the new value to be assigned to the identifier
	 * @return boolean : true if successful, false otherwise
	 */
	public boolean setValue(String id, String newValue) {
		SymbolTableEntry entry = searchGlobal(id);
		if (entry != null) {
			entry.setValue(newValue);
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
	 * @return HashMap<String,SymbolTableEntry> (a reference to the newly created current scope)
	 */
	public HashMap<String,SymbolTableEntry> enterScope() {
		
		// Add new scope as child of current scope
		STScope newScope = new STScope();
		newScope.setParent(currentScope);
		if (currentScope != null) {
			currentScope.addChild(newScope);
		}
		
		currentScope = newScope;
		if (firstScope == null) {
			firstScope = newScope;
		}
		
		return currentScope.getSymbols();
	}

	/**
	 * exitScope - exit current scope
	 * 
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
		if (firstScope != null) {
			int depth = 0;
			s += firstScope.display(depth);
		}
		s += STScope.scopeSep + "END OF TRAVERSAL\n";
		return s;
	}

	public static void main(String argv[]) {

		System.out.println("Hai.");

		SymbolTable st = new SymbolTable();
		
		System.out.println(st);
		
		st.enterScope();
		st.insert("i", SymbolType.INTEGER, SymbolKind.VARIABLE, "1", null);
		st.insert("b", SymbolType.BOOLEAN, SymbolKind.VARIABLE, "Troooooo", null);
		
		System.out.println("After adding one scope with vars:\n" + st);
		System.out.println("Value of i: " + st.getValue("i"));
		System.out.println("Value of b: " + st.getValue("b"));
		System.out.println();
		
		st.enterScope();
		st.insert("i", SymbolType.INTEGER, SymbolKind.VARIABLE, "1 gajillion", null);

		System.out.println("Enter new scope and add var:\n" + st);
		System.out.println("Value of i: " + st.getValue("i"));
		System.out.println("Value of b: " + st.getValue("b"));
		System.out.println();
		
		st.delete("i");
		
		System.out.println("After deleting i:\n" + st);
		System.out.println("Value of i: " + st.getValue("i"));
		System.out.println("Value of b: " + st.getValue("b"));
		System.out.println();
		
		st.exitScope();
		
		System.out.println("After exiting scope:\n" + st);
		System.out.println("Value of i: " + st.getValue("i"));
		System.out.println("Value of b: " + st.getValue("b"));
		System.out.println();
		
		st.enterScope();
		st.insert("theNewGuy", SymbolType.INTEGER, SymbolKind.VARIABLE, "(H)", null);
		
		System.out.println("Enter new scope:\n" + st);
		
		st.exitScope();
		st.delete("b");
		System.out.println("Exit scope, delete b:\n" + st);
		System.out.println("Value of i: " + st.getValue("i"));
		System.out.println("Value of b: " + st.getValue("b"));
		System.out.println();
		
		System.out.println("Bai.");
		
		
		// Display entire symbol table (all retained scopes and identifiers, from root to leaves)
		System.out.println("\n\n\n" + st.fullTraversal());
		
		return;

	}

}
