package compiler488.symbol;

import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
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
	/** Symbol Table  constructor
         *  Create and initialize a symbol table
	 */

	private class SymbolTableEntry {
		String id; //Name of the symbol
		String type; //Type of the symbol(Integer or Boolean)
		String kind; //Kind of the symbol(function, procedure...)
		BaseAST node; //AST node

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

	LinkedList<HashMap<String,SymbolTableEntry>> scopeList;

	public SymbolTable  (){

		// NOTE: putting everything in here for now
		//       do we need to split stuff to Initialize/Finalize?

		// Instantiate
		this.scopeList = new LinkedList<HashMap<String,SymbolTableEntry>>();

		// Add HashMap for global scope
		scopeList.addFirst(new HashMap<String,SymbolTableEntry>());

	}

	/**  Initialize - called once by semantic analysis
	 *                at the start of  compilation
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
	 *
	 * @param id : identifier (name of variable)
	 * @param type : 'integer' or 'boolean'
	 * @param kind : 'variable', 'procedure', or 'function'
	 * @param node : link to AST node
	 */
	public void insert(String id, String type, String kind, BaseAST node) {

		// Get current scope's hashmap
		HashMap<String,SymbolTableEntry> currentScope = scopeList.getFirst();

		// TODO: do we need to check before reassigning identifier?

		// add to symbol table
		SymbolTableEntry entry = new SymbolTableEntry(id, type, kind, node);
		currentScope.put(id, entry);

		return;
	}

	/**
	 *
	 * @param id : The symbol to search the symbol table for
	 * @return SymbolTableEntry if found, or null if not found
	 */
	public SymbolTableEntry search(String id){
		Iterator<HashMap<String, SymbolTableEntry>> scope = scopeList.iterator();
		while(scope.hasNext())
		{
			HashMap<String, SymbolTableEntry> currScope = scope.next();
			if (currScope.containsKey(id))
			{
				return currScope.get(id);
			}
		}

		return null;
	}

	public void enterScope() {
		// Add new HashMap to beginning of scopeList
		HashMap<String,SymbolTableEntry> newScope = new HashMap<String,SymbolTableEntry>();
		this.scopeList.addFirst(newScope);
	}

	public void exitScope() {
		// TODO: do we need checks for making sure there is at least one scope?
		this.scopeList.removeFirst();
	}

	public String toString() {
		String s = "";

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
