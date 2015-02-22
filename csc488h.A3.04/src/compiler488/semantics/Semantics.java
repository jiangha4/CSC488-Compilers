package compiler488.semantics;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import compiler488.ast.ASTVisitor;
import compiler488.ast.decl.*;
import compiler488.ast.expn.*;
import compiler488.ast.stmt.*;
import compiler488.ast.type.*;
import compiler488.symbol.SymbolTable;
import compiler488.symbol.SymbolTable.SymbolKind;
import compiler488.symbol.SymbolTable.SymbolType;

/** Implement semantic analysis for compiler 488 
 *  @author  <B> Haohan Jiang (g3jiangh)
 *               Maria Yancheva (c2yanche)
 *               Timo Vink (c4vinkti)
 *               Chandeep Singh (g2singh)
 *           </B>
 */
public class Semantics implements ASTVisitor {
	
	/** flag for tracing semantic analysis */
	private boolean traceSemantics = false;
	
	/** file sink for semantic analysis trace */
	private String traceFile = new String();
	public FileWriter Tracer;
	public File f;
	
	/** Construct symbol table concurrently with semantic checking */
	private SymbolTable Symbol; 
	
	/** Accummulate errors and raise an exception after all semantic rules have */
    private SemanticErrorCollector errors;
    
    /** SemanticAnalyzer constructor */
	public Semantics (){
		Symbol = new SymbolTable();
		errors = new SemanticErrorCollector();
	}

	/**  semanticsInitialize - called once by the parser at the      */
	/*                        start of  compilation                 */
	void Initialize() {
	
	   /*   Initialize the symbol table             */
	
	   //Symbol.Initialize();
	   
	   /*********************************************/
	   /*  Additional initialization code for the   */
	   /*  semantic analysis module                 */
	   /*  GOES HERE                                */
	   /*********************************************/
	   
	}

	/**  semanticsFinalize - called by the parser once at the        */
	/*                      end of compilation                      */
	public void Finalize() throws SemanticErrorException {
	
		/*  Finalize the symbol table                 */
		// Symbol.Finalize();
  
		/*********************************************/
		/*  Additional finalization code for the      */
		/*  semantics analysis module                 */
		/*  GOES here.                                */
		/**********************************************/
	  
		if (errors.getCount() > 0) {
			errors.raiseException();
		}
	}
	
	/**
	 *  Perform one semantic analysis action
         *  @param  actionNumber  semantic analysis action number
         */
	void semanticAction( int actionNumber ) {

	if( traceSemantics ){
		if(traceFile.length() > 0 ){
	 		//output trace to the file represented by traceFile
	 		try{
	 			//open the file for writing and append to it
	 			Tracer = new FileWriter(traceFile, true);
	 				          
	 		    Tracer.write("Sematics: S" + actionNumber + "\n");
	 		    //always be sure to close the file
	 		    Tracer.close();
	 		}
	 		catch (IOException e) {
	 		  System.out.println(traceFile + 
				" could be opened/created.  It may be in use.");
	 	  	}
	 	}
	 	else{
	 		//output the trace to standard out.
	 		System.out.println("Sematics: S" + actionNumber );
	 	}
	 
	}
	                     
	   /*************************************************************/
	   /*  Code to implement each semantic action GOES HERE         */
	   /*  This stub semantic analyzer just prints the actionNumber */   
	   /*                                                           */
           /*  FEEL FREE TO ignore or replace this procedure            */
	   /*************************************************************/
	                     
	   System.out.println("Semantic Action: S" + actionNumber  );
	   return ;
	}

	
	// ADDITIONAL FUNCTIONS TO IMPLEMENT SEMANTIC ANALYSIS GO HERE

	public SymbolTable getSymbolTable() {
		return Symbol;
	}
	
	public SemanticErrorCollector getErrors() {
		return errors;
	}
	
	@Override
	public void visit(ArrayDeclPart arrayDeclPart) {
		System.out.println("Visiting ArrayDeclPart");
		
		if (!(arrayDeclPart.getLowerBoundary1() <= arrayDeclPart.getUpperBoundary1())) {
			errors.add("Array dimension 1: lower bound must be less than or equal to upper bound.");
		};
		
		if (arrayDeclPart.isTwoDimensional()) {
			if (!(arrayDeclPart.getLowerBoundary2() <= arrayDeclPart.getUpperBoundary2())) {
				errors.add("Array dimension 2: lower bound must be less than or equal to upper bound.");
			};
		}
	}

	@Override
	public void visit(Declaration declaration) {
		// TODO Auto-generated method stub
		System.out.println("Visiting Declaration");
	}

	@Override
	public void visit(DeclarationPart declarationPart) {
		// TODO Auto-generated method stub
		System.out.println("Visiting DeclarationPart");
	}

	@Override
	public void visit(MultiDeclarations multiDeclarations) {
		System.out.println("Visiting MultiDeclarations");
		
		// Add a symbol for every element in the declaration (into current scope)
		// (The value for the newly inserted elements is blank: in the project
		// language assignment cannot happen simultaneously with declaration)
		SymbolType declType = multiDeclarations.getType().toSymbolType();
		for (DeclarationPart nextElem : multiDeclarations.getParts()) {
			
			// Check if identifier already exists in current scope
			String elemId = nextElem.getName();
			if (Symbol.search(elemId) != null) {
				// Detected a re-declaration in same scope
				errors.add("Re-declaration of identifier " + elemId + " not allowed in same scope.");
			}
			else {
				boolean success = Symbol.insert(nextElem.getName(), declType, nextElem.getKind(), "", nextElem);
				if (success) {
					nextElem.setSTEntry(Symbol.search(elemId));
				} else {
					errors.add("Unable to declare identifier " + elemId);
				}
			}
		}
		
	}

	@Override
	public void visit(RoutineDecl routineDecl) {
		System.out.println("Visiting RoutineDecl");
		
		if (!routineDecl.isVisited()) {
			
			/**
			 * S11/S12: Declare function with/without parameters and with specified type
			 * S17/S18: Declare procedure with/without parameters
			 */
			// Record routine declaration in symbol table
			String routineName = routineDecl.getName();
			SymbolType routineType = null;
			SymbolKind routineKind = SymbolKind.PROCEDURE;
			
			// If the routine has a return value then it is a function; otherwise a procedure
			if (routineDecl.getType() != null) {
				routineType = routineDecl.getType().toSymbolType();
				routineKind = SymbolKind.FUNCTION;
			}
			
			// Check for existing declaration
			if (Symbol.search(routineName) != null) {
				// Detected a re-declaration in same scope
				errors.add("Re-declaration of identifier " + routineName + " not allowed in same scope.");
			}
			else {
				boolean success = Symbol.insert(routineName, routineType, routineKind, "", routineDecl);
				if (success) {
					routineDecl.setSTEntry(Symbol.search(routineName));
				} else {
					errors.add("Unable to declare identifier " + routineName);
				}
			}
			
			/**
			 * S04: Start function scope
			 * S08: Start procedure scope
			 */
			// Begin new function/procedure scope
			Symbol.enterScope();
			
			// Mark as visited
			routineDecl.setVisited(true);
		} else {
			/**
			 * S05: End function scope
			 * S09: End procedure scope
			 */
			Symbol.exitScope();
			
			// Clear flag
			routineDecl.setVisited(false);
		}
	}

	@Override
	public void visit(ScalarDecl scalarDecl) {
		// TODO Auto-generated method stub
		System.out.println("Visiting ScalarDecl");
	}

	@Override
	public void visit(ScalarDeclPart scalarDeclPart) {
		// TODO Auto-generated method stub
		System.out.println("Visiting ScalarDeclPart");
	}

	@Override
	public void visit(AnonFuncExpn anonFuncExpn) {
		// TODO Auto-generated method stub
		System.out.println("Visiting AnonFuncExpn");
	}

	@Override
	public void visit(ArithExpn arithExpn) {
		// TODO Auto-generated method stub
		System.out.println("Visiting ArithExpn");
	}

	@Override
	public void visit(BinaryExpn binaryExpn) {
		// TODO Auto-generated method stub
		System.out.println("Visiting BinaryExpn");
	}

	@Override
	public void visit(BoolConstExpn boolConstExpn) {
		// TODO Auto-generated method stub
		System.out.println("Visiting BoolConstExpn");
	}

	@Override
	public void visit(BoolExpn boolExpn) {
		// TODO Auto-generated method stub
		System.out.println("Visiting BoolExpn");
	}

	@Override
	public void visit(CompareExpn compareExpn) {
		// TODO Auto-generated method stub
		System.out.println("Visiting CompareExpn");
	}

	@Override
	public void visit(ConstExpn constExpn) {
		// TODO Auto-generated method stub
		System.out.println("Visiting ConstExpn");
	}

	@Override
	public void visit(EqualsExpn equalsExpn) {
		// TODO Auto-generated method stub
		System.out.println("Visiting EqualsExpn");
	}

	@Override
	public void visit(Expn expn) {
		// TODO Auto-generated method stub
		System.out.println("Visiting Expn");
	}

	@Override
	public void visit(FunctionCallExpn functionCallExpn) {
		// TODO Auto-generated method stub
		System.out.println("Visiting FunctionCallExpn");
	}

	@Override
	public void visit(IdentExpn identExpn) {
		// TODO Auto-generated method stub
		System.out.println("Visiting IdentExpn");
	}

	@Override
	public void visit(IntConstExpn intConstExpn) {
		// TODO Auto-generated method stub
		System.out.println("Visiting IntConstExpn");
	}

	@Override
	public void visit(NotExpn notExpn) {
		// TODO Auto-generated method stub
		System.out.println("Visiting NotExpn");
	}

	@Override
	public void visit(SkipConstExpn skipConstExpn) {
		// TODO Auto-generated method stub
		System.out.println("Visiting SkipConstExpn");
	}

	@Override
	public void visit(SubsExpn subsExpn) {
		// TODO Auto-generated method stub
		System.out.println("Visiting SubsExpn");
	}

	@Override
	public void visit(TextConstExpn textConstExpn) {
		// TODO Auto-generated method stub
		System.out.println("Visiting TextConstExpn");
	}

	@Override
	public void visit(UnaryExpn unaryExpn) {
		// TODO Auto-generated method stub
		System.out.println("Visiting UnaryExpn");
	}

	@Override
	public void visit(UnaryMinusExpn unaryMinusExpn) {
		// TODO Auto-generated method stub
		System.out.println("Visiting UnaryMinusExpn");
	}

	@Override
	public void visit(AssignStmt assignStmt) {
		// TODO Auto-generated method stub
		System.out.println("Visiting AssignStmt");
	}

	@Override
	public void visit(ExitStmt exitStmt) {
		// TODO Auto-generated method stub
		System.out.println("Visiting ExitStmt");
		
		/**S50 Check that exit statement is directly inside a loop
		*  Checks if symbols "loop" or "while" have been declared in the scope
		*  If not, throws an error
		*/
		// TODO check if exit statement is immediately contained in a loop
	}

	@Override
	public void visit(GetStmt getStmt) {
		// TODO Auto-generated method stub
		System.out.println("Visiting GetStmt");
	}

	@Override
	public void visit(IfStmt ifStmt) {
		// TODO Auto-generated method stub
		System.out.println("Visiting IfStmt");
	}

	@Override
	public void visit(LoopingStmt loopingStmt) {
		// TODO Auto-generated method stub
		System.out.println("Visiting LoopingStmt");
	}

	@Override
	public void visit(LoopStmt loopStmt) {
		// TODO Auto-generated method stub
		System.out.println("Visiting LoopStmt");
	}

	@Override
	public void visit(ProcedureCallStmt procedureCallStmt) {
		// TODO Auto-generated method stub
		System.out.println("Visiting ProcedureCallStmt");
	}

	@Override
	public void visit(Program program) {
		System.out.println("Visiting Program");
		
		if (!program.isVisited()) {
			// Begin new scope
			Symbol.enterScope();
			
			// Mark as visited
			program.setVisited(true);
		} else {
			Symbol.exitScope();
			
			// Clear the flag
			program.setVisited(false);
		}
	}

	@Override
	public void visit(PutStmt putStmt) {
		// TODO Auto-generated method stub
		System.out.println("Visiting PutStmt");
	}

	@Override
	public void visit(ReturnStmt returnStmt) {
		// TODO Auto-generated method stub
		System.out.println("Visiting ReturnStmt");
	}

	@Override
	public void visit(Scope scope) {
		System.out.println("Visiting Scope");
		
		if (!scope.isVisited()) {
			// Begin new scope
			Symbol.enterScope();
			
			// Mark as visited
			scope.setVisited(true);
		} else {
			Symbol.exitScope();
			
			// Clear the flag
			scope.setVisited(false);
		}
	}

	@Override
	public void visit(Stmt stmt) {
		// TODO Auto-generated method stub
		System.out.println("Visiting Stmt");
	}

	@Override
	public void visit(WhileDoStmt whileDoStmt) {
		// TODO Auto-generated method stub
		System.out.println("Visiting WhileDoStmt");
	}

	@Override
	public void visit(BooleanType booleanType) {
		// TODO Auto-generated method stub
		System.out.println("Visiting BooleanType");
	}

	@Override
	public void visit(IntegerType integerType) {
		// TODO Auto-generated method stub
		System.out.println("Visiting IntegerType");
	}

	@Override
	public void visit(Type type) {
		// TODO Auto-generated method stub
		System.out.println("Visiting Type");
	}

}
