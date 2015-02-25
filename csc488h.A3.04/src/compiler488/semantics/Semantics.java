package compiler488.semantics;

import java.awt.geom.Area;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;

import compiler488.ast.ASTList;
import compiler488.ast.ASTVisitor;
import compiler488.ast.BaseAST;
import compiler488.ast.BaseAST.attribute;
import compiler488.ast.PrettyPrinter;
import compiler488.ast.Printable;
import compiler488.ast.decl.ArrayDeclPart;
import compiler488.ast.decl.Declaration;
import compiler488.ast.decl.DeclarationPart;
import compiler488.ast.decl.MultiDeclarations;
import compiler488.ast.decl.RoutineDecl;
import compiler488.ast.decl.ScalarDecl;
import compiler488.ast.decl.ScalarDeclPart;
import compiler488.ast.expn.AnonFuncExpn;
import compiler488.ast.expn.ArithExpn;
import compiler488.ast.expn.BinaryExpn;
import compiler488.ast.expn.BoolConstExpn;
import compiler488.ast.expn.BoolExpn;
import compiler488.ast.expn.CompareExpn;
import compiler488.ast.expn.ConstExpn;
import compiler488.ast.expn.EqualsExpn;
import compiler488.ast.expn.Expn;
import compiler488.ast.expn.FunctionCallExpn;
import compiler488.ast.expn.IdentExpn;
import compiler488.ast.expn.IntConstExpn;
import compiler488.ast.expn.NotExpn;
import compiler488.ast.expn.SkipConstExpn;
import compiler488.ast.expn.SubsExpn;
import compiler488.ast.expn.TextConstExpn;
import compiler488.ast.expn.UnaryExpn;
import compiler488.ast.expn.UnaryMinusExpn;
import compiler488.ast.stmt.AssignStmt;
import compiler488.ast.stmt.ExitStmt;
import compiler488.ast.stmt.GetStmt;
import compiler488.ast.stmt.IfStmt;
import compiler488.ast.stmt.LoopStmt;
import compiler488.ast.stmt.LoopingStmt;
import compiler488.ast.stmt.ProcedureCallStmt;
import compiler488.ast.stmt.Program;
import compiler488.ast.stmt.PutStmt;
import compiler488.ast.stmt.ReturnStmt;
import compiler488.ast.stmt.Scope;
import compiler488.ast.stmt.Stmt;
import compiler488.ast.stmt.WhileDoStmt;
import compiler488.ast.type.BooleanType;
import compiler488.ast.type.IntegerType;
import compiler488.ast.type.Type;
import compiler488.symbol.SymbolTable;
import compiler488.symbol.SymbolTable.SymbolKind;
import compiler488.symbol.SymbolTable.SymbolType;
import compiler488.symbol.SymbolTableEntry;

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
			errors.add(arrayDeclPart.getSourceCoord() + ": Array '" + arrayDeclPart.getName() + "', dimension 1: lower bound must be less than or equal to upper bound.");
		};
		
		if (arrayDeclPart.isTwoDimensional()) {
			if (!(arrayDeclPart.getLowerBoundary2() <= arrayDeclPart.getUpperBoundary2())) {
				errors.add(arrayDeclPart.getSourceCoord() + ": Array '" + arrayDeclPart.getName() + "', dimension 2: lower bound must be less than or equal to upper bound.");
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
				errors.add(nextElem.getSourceCoord() + ": Re-declaration of identifier " + elemId + " not allowed in same scope.");
			}
			else {
				boolean success = Symbol.insert(nextElem.getName(), declType, nextElem.getKind(), "", nextElem);
				if (success) {
					nextElem.setSTEntry(Symbol.search(elemId));
				} else {
					errors.add(nextElem.getSourceCoord() + ": Unable to declare identifier " + elemId);
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
				
				// S53: check that a function body contains at least one return statement
				ASTList<Stmt> routineBody = routineDecl.getBody().getBody();
				boolean hasReturn = false;
				if (routineBody != null){
					for (Stmt routineStmt : routineBody) {
						if (routineStmt.containsReturn()) {
							hasReturn = true;
							break;
						}
					}
				}
				if (!hasReturn) {
					errors.add(routineDecl.getSourceCoord() + ": Function '" + routineName + "' must have at least one return statement.");
				}
			}
			
			// Check for existing declaration
			if (Symbol.search(routineName) != null) {
				// Detected a re-declaration in same scope
				errors.add(routineDecl.getSourceCoord() + ": Re-declaration of identifier " + routineName + " not allowed in same scope.");
			}
			else {
				boolean success = Symbol.insert(routineName, routineType, routineKind, "", routineDecl);
				if (success) {
					routineDecl.setSTEntry(Symbol.search(routineName));
				} else {
					errors.add(routineDecl.getSourceCoord() + ": Unable to declare identifier " + routineName);
				}
			}
		
			// Begin new scope
			Symbol.enterScope();
			
			// Mark as visited
			routineDecl.setVisited(true);
		} else {
			// Exit the scope
			Symbol.exitScope();
			
			// Clear the flag
			routineDecl.setVisited(false);
		}
		
	}

	@Override
	public void visit(ScalarDecl scalarDecl) {
		System.out.println("Visiting ScalarDecl");
		
		String declId = scalarDecl.getName();
		SymbolType declType = scalarDecl.getType().toSymbolType();
		
		// Check if identifier already exists in current scope
		if (Symbol.search(declId) != null) {
			// Detected a re-declaration in same scope
			errors.add(scalarDecl.getSourceCoord() + ": Re-declaration of identifier " + declId + " not allowed in same scope.");
		}
		else {
			boolean success = Symbol.insert(declId, declType, SymbolKind.PARAMETER, "", scalarDecl);
			if (success) {
				scalarDecl.setSTEntry(Symbol.search(declId));
			} else {
				errors.add(scalarDecl.getSourceCoord() + ": Unable to declare identifier " + declId);
			}
		}
	
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
		System.out.println("Type: " + anonFuncExpn.getExpnType(Symbol));
	}

	@Override
	public void visit(ArithExpn arithExpn) {
		// TODO Auto-generated method stub
		System.out.println("Visiting ArithExpn");
		System.out.println("Type: " + arithExpn.getExpnType(Symbol));
		
		s31check(arithExpn);
	}

	@Override
	public void visit(BinaryExpn binaryExpn) {
		// TODO Auto-generated method stub
		System.out.println("Visiting BinaryExpn");
		System.out.println("Type: " + binaryExpn.getExpnType(Symbol));
	}

	@Override
	public void visit(BoolConstExpn boolConstExpn) {
		// TODO Auto-generated method stub
		System.out.println("Visiting BoolConstExpn");
		System.out.println("Type: " + boolConstExpn.getExpnType(Symbol));
	}

	@Override
	public void visit(BoolExpn boolExpn) {
		// TODO Auto-generated method stub
		System.out.println("Visiting BoolExpn");
		System.out.println("Type: " + boolExpn.getExpnType(Symbol));
		
		s30check(boolExpn);
	}

	@Override
	public void visit(CompareExpn compareExpn) {
		// TODO Auto-generated method stub
		System.out.println("Visiting CompareExpn");
		System.out.println("Type: " + compareExpn.getExpnType(Symbol));
		
		s31check(compareExpn);
	}

	@Override
	public void visit(ConstExpn constExpn) {
		// TODO Auto-generated method stub
		System.out.println("Visiting ConstExpn");
		System.out.println("Type: " + constExpn.getExpnType(Symbol));
	}

	@Override
	public void visit(EqualsExpn equalsExpn) {
		// TODO Auto-generated method stub
		System.out.println("Visiting EqualsExpn");
		System.out.println("Type: " + equalsExpn.getExpnType(Symbol));
		
		s31check(equalsExpn);
	}

	@Override
	public void visit(Expn expn) {
		// TODO Auto-generated method stub
		System.out.println("Visiting Expn");
		System.out.println("Type: " + expn.getExpnType(Symbol));
	}

	@Override
	public void visit(FunctionCallExpn functionCallExpn) {
		System.out.println("Visiting FunctionCallExpn");
		System.out.println("Type: " + functionCallExpn.getExpnType(Symbol));
		
		// S40: check that identifier has been declared as a function
		String functionName = functionCallExpn.getIdent();
		if (Symbol.searchGlobal(functionName) == null) {
			errors.add(functionCallExpn.getSourceCoord() + ": Function '" + functionName + "' cannot be used before it has been declared.");
		}
		else if (Symbol.searchGlobal(functionName).getKind() != SymbolKind.FUNCTION) {
			errors.add(functionCallExpn.getSourceCoord() + ": Identifier '" + functionName + "' cannot be used as a function because it has been declared as " + Symbol.searchGlobal(functionName).getKind() + ".");
		}
		
		// S42/S43: check that the number of parameters declared in the function declaration is the same as the 
    	// number of arguments passed to the function call expression
		SymbolTableEntry declaredFunc = Symbol.searchGlobal(functionName);
		RoutineDecl declaredFuncASTNode = (RoutineDecl)declaredFunc.getNode();
		int numArgs = functionCallExpn.getArguments().size();
		int numParams = declaredFuncASTNode.getParameters().size();
		if (numArgs != numParams) {
			errors.add(functionCallExpn.getSourceCoord() + ": Function '" + functionName + "' is called with " + numArgs + " arguments, but requires " + numParams + " parameters.");
		}
		
		// S36: Check that type of argument expression matches type of corresponding formal parameter
		s36check(functionCallExpn.getArguments(), declaredFuncASTNode.getParameters());
	}

	@Override
	public void visit(IdentExpn identExpn) {
		System.out.println("Visiting IdentExpn");
		System.out.println("Type: " + identExpn.getExpnType(Symbol));
		
		// S37: check that identifier has been declared as a scalar variable
		// S39: check that identifier has been declared as a parameter
		// S40: check that identifier has been declared as a function
		String identName = identExpn.getIdent();
		if (Symbol.searchGlobal(identName) == null) {
			errors.add(identExpn.getSourceCoord() + ": Identifier '" + identName + "' cannot be used before it has been declared.");
		}
		else if (Symbol.searchGlobal(identName).getKind() != SymbolKind.VARIABLE && 
				 Symbol.searchGlobal(identName).getKind() != SymbolKind.PARAMETER && 
				 Symbol.searchGlobal(identName).getKind() != SymbolKind.FUNCTION ) {
			errors.add(identExpn.getSourceCoord() + ": Identifier '" + identName + "' cannot be used in this context because it has been declared as " + Symbol.searchGlobal(identName).getKind() + ".");
		}
		
	}

	@Override
	public void visit(IntConstExpn intConstExpn) {
		// TODO Auto-generated method stub
		System.out.println("Visiting IntConstExpn");
		System.out.println("Type: " + intConstExpn.getExpnType(Symbol));
	}

	@Override
	public void visit(NotExpn notExpn) {
		// TODO Auto-generated method stub
		System.out.println("Visiting NotExpn");
		System.out.println("Type: " + notExpn.getExpnType(Symbol));
		
		s30check(notExpn);
	}

	@Override
	public void visit(SkipConstExpn skipConstExpn) {
		// TODO Auto-generated method stub
		System.out.println("Visiting SkipConstExpn");
		System.out.println("Type: " + skipConstExpn.getExpnType(Symbol));
	}

	@Override
	public void visit(SubsExpn subsExpn) {
		System.out.println("Visiting SubsExpn");
		System.out.println("Type: " + subsExpn.getExpnType(Symbol));
		
		// S38: check that identifier has been declared as an array
		String arrayName = subsExpn.getVariable();
		if (Symbol.searchGlobal(arrayName) == null) {
			errors.add(subsExpn.getSourceCoord() + ": Array '" + arrayName + "' cannot be used before it has been declared.");
		}
		else if (Symbol.searchGlobal(arrayName).getKind() != SymbolKind.ARRAY) {
			errors.add(subsExpn.getSourceCoord() + ": Identifier '" + arrayName + "' cannot be used as an array because it has been declared as " + Symbol.searchGlobal(arrayName).getKind() + ".");
		}
	}

	@Override
	public void visit(TextConstExpn textConstExpn) {
		// TODO Auto-generated method stub
		System.out.println("Visiting TextConstExpn");
		System.out.println("Type: " + textConstExpn.getExpnType(Symbol));
	}

	@Override
	public void visit(UnaryExpn unaryExpn) {
		// TODO Auto-generated method stub
		System.out.println("Visiting UnaryExpn");
		System.out.println("Type: " + unaryExpn.getExpnType(Symbol));
	}

	@Override
	public void visit(UnaryMinusExpn unaryMinusExpn) {
		// TODO Auto-generated method stub
		System.out.println("Visiting UnaryMinusExpn");
		System.out.println("Type: " + unaryMinusExpn.getExpnType(Symbol));
		
		s31check(unaryMinusExpn);
	}

	@Override
	public void visit(AssignStmt assignStmt) {
		// TODO Auto-generated method stub
		System.out.println("Visiting AssignStmt");
		
		// S34: Check that variable and expression in assignment are the same type
		SymbolType lType = assignStmt.getLval().getExpnType(Symbol);
		SymbolType rType = assignStmt.getRval().getExpnType(Symbol);
		if (lType != rType) {
			errors.add("LHS type (" + lType + ") in assignment differs from RHS type (" + rType + ")");
		}
	}

	@Override
	public void visit(ExitStmt exitStmt) {
		// TODO Auto-generated method stub
		System.out.println("Visiting ExitStmt");
		
		// Only do S30 check if "exit when"
		if (exitStmt.getExpn() != null) {
			s30check(exitStmt.getExpn());
		}
		
		/**S50 Check that exit statement is directly inside a loop
		*  Checks if symbols "loop" or "while" have been declared in the scope
		*  If not, throws an error
		*/
		
		BaseAST currNode = exitStmt;
		boolean foundLoop = false;
		while(currNode != null)
		{
			if ((currNode.getParentNode() instanceof LoopStmt) ||
				(currNode.getParentNode() instanceof WhileDoStmt)){
				foundLoop = true;
			}
			currNode = currNode.getParentNode();
		}
		
		if (!foundLoop){
			errors.add(exitStmt.getSourceCoord() + " EXIT not contained in LOOP or WHILE statements");
			
		}
	}

	@Override
	public void visit(GetStmt getStmt) {
		// TODO Auto-generated method stub
		System.out.println("Visiting GetStmt");
		
		// S31: check that variables are integers
		for (Expn expn : getStmt.getInputs()) {
			s31check(expn);
		}
	}

	@Override
	public void visit(IfStmt ifStmt) {
		// TODO Auto-generated method stub
		System.out.println("Visiting IfStmt");
		
		s30check(ifStmt.getCondition());
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
		System.out.println("Visiting ProcedureCallStmt");
		
		// S41: check that identifier has been declared as a procedure
		String procName = procedureCallStmt.getName();
		if (Symbol.searchGlobal(procName) == null) {
			errors.add(procedureCallStmt.getSourceCoord() + ": Procedure '" + procName + "' cannot be used before it has been declared.");
		}
		else if (Symbol.searchGlobal(procName).getKind() != SymbolKind.PROCEDURE) {
			errors.add(procedureCallStmt.getSourceCoord() + ": Identifier '" + procName + "' cannot be used as a procedure because it has been declared as " + Symbol.searchGlobal(procName).getKind() + ".");
		}
		
		// S42/S43: check that the number of parameters declared in the procedure declaration is the same as the 
    	// number of arguments passed to the procedure call statement
		SymbolTableEntry declaredProc = Symbol.searchGlobal(procName);
		RoutineDecl declaredProcASTNode = (RoutineDecl)declaredProc.getNode();
		int numArgs = procedureCallStmt.getArguments().size();
		int numParams = declaredProcASTNode.getParameters().size();
		if (numArgs != numParams) {
			errors.add(procedureCallStmt.getSourceCoord() + ": Procedure '" + procName + "' is called with " + numArgs + " arguments, but requires " + numParams + " parameters.");
		}
		
		// S36: Check that type of argument expression matches type of corresponding formal parameter
		s36check(procedureCallStmt.getArguments(), declaredProcASTNode.getParameters());
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
			// Exit the scope
			Symbol.exitScope();
			
			// Clear the flag
			program.setVisited(false);
		}
	}

	@Override
	public void visit(PutStmt putStmt) {
		// TODO Auto-generated method stub
		System.out.println("Visiting PutStmt");
		
		// S31: Check that type of expression or variable is integer
		// Also added in check for text/skip
		for (Printable e : putStmt.getOutputs()) {
			SymbolType type = ((Expn)e).getExpnType(Symbol);
			if (type != SymbolType.INTEGER &&
				type != SymbolType.TEXT &&
				type != SymbolType.SKIP) {
				errors.add("Put statement can only contain integer expressions, text, or skips");
			}
		}
	}

	@Override
	public void visit(ReturnStmt returnStmt) {
		// TODO Auto-generated method stub
		System.out.println("Visiting ReturnStmt");
		
		// S51-52 Must check that return statements are in procedure 
		// or function scope
		if (returnStmt.getParentAttribute() != attribute.METHOD)
		{
			errors.add(returnStmt.getSourceCoord() + " Return statement is not in the scope of a function or procedure");
		}
	}

	@Override
	public void visit(Scope scope) {
		System.out.println("Visiting Scope");
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
		
		s30check(whileDoStmt.getExpn());
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
	
	// compare expn type to expectedType, and error if not the same
	private void checkExpnType(Expn expn, SymbolType expectedType) {
		SymbolType type = expn.getExpnType(Symbol);
		if (type != expectedType) {
			errors.add("Expression is not of type " + expectedType);
		}
	}
	
	// S30: check that type of expression is boolean
	private void s30check(Expn expn) {
		checkExpnType(expn, SymbolType.BOOLEAN);
	}
	
	// S31: check that type of expression is integer
	private void s31check(Expn expn) {
		checkExpnType(expn, SymbolType.INTEGER);
	}
	
	// S36: Check that type of argument expression matches type of corresponding formal parameter
	private void s36check(ASTList<Expn> argList, ASTList<ScalarDecl> paramList) {
		Iterator<Expn> argExpnIter = argList.iterator();
		Iterator<ScalarDecl> paramsIter  = paramList.iterator();
		int count = 1;
		while (argExpnIter.hasNext()) {
			SymbolType aType = argExpnIter.next().getExpnType(Symbol);
			SymbolType pType = paramsIter.next().getSTEntry().getType();
			if ( aType != pType ) {
				errors.add("Arg expression " + count + "'s type (" + aType + ") does not match expected param type (" + pType + ")");
			}
			count++;
		}
	}
	
	

}
