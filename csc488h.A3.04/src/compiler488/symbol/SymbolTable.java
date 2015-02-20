package compiler488.symbol;

import java.io.*;

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

/** First thoughts
 *  Needs to maintain mappings from AST nodes to identifiers
 *  - Need to have methods 
 *      - insert(string id, node) 
 *          - mappings the id to the AST node and needs to insert it into the hash table
 *      - search(string id) -> node
 *          - searches table for the identifier and returns the node if it exists
 *          - Thinking: This will be used to find global identifiers
 *      - searchScope(string id) -> node
 *          - searches the scope for the identifier and returns the node if it exists
 *          - Thinking: This will be used to find local identifiers
 *  - Need to support scope
 *      - I'm thinking of having an identifier that tracks scope level
 *          - Two methods that will increase and decrease scope level
 *              - increaseScope()
 *                  - Increment scope one more level
 *              - decreaseScope()
 *                  - I think this can just delete all identifers that were mapped to the current
 *                    scope level
 * - Need to create a hash table
 *   - Methods
 *      - Hashing function
 *          - hash based on identifier name, makes it easy to detect previously declared identifiers
 *      - How to deal with chaining? 
 *      - What should each entry include?
 *          - I'm thinking we should subclass the table entries
 *          - It should include pointers to the previous and next extries
 *          - Should include identifier name
 *          - Probably should include scope level - I think this will make deleting really easy
 *   - However, I'm (David) not sure on how to dynamically increase hash table size or 
 *     if it needs to be done dynamically or can we just allocate a chunk of memory and hope
 *     its enough?
 *
 *  - Need to create a print function to allow for debugging!  
 *      - Will want to print out the identifier, current scope level, the whole symbol table...  
 *
 *
 *
 *
 *
 *
 *
 *  Other idea: Stack of Hash Tables
 *
 *  Another idea for implementing the symbol table can be to have a hash table represent each scope
 *  and then a stack of hash tables.
 *
 *  The top of the stack represents the current scope. When entering a new scope, add a hash table
 *  to the top of stack. When exiting scope, pop off the top of the stack.
 * 
 *  To search for an identifier, start search in the top stack, and if nothing found then continue
 *  downwards in the stack until either identifier is found, or error.
 *
 *  Methods will work as follows:
 *
 *  insert(id, node) : insert into table at top of stack
 *  search(id) : as explained above
 *  searchScope: no longer needed
 *  increaseScope/decreaseScope will be add/pop tables from the stack
 *  
 *  Hash tables methods
 *
 *  Hash key, same as above (use identifier)
 *  Chaining: Shouldn't have chaining in this implementation
 *  
 *  Increasing/decreasing hash size: I believe Java's hash-table implementation should already be
 *  able to handle this efficiently enough, so let's not worry about it
 */

public class SymbolTable {
	/** Symbol Table  constructor
         *  Create and initialize a symbol table 
	 */
	public SymbolTable  (){
	
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

}
