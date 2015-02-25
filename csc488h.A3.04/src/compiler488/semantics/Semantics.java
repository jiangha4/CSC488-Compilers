package compiler488.semantics;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;

import compiler488.ast.*;
import compiler488.ast.decl.*;
import compiler488.ast.expn.*;
import compiler488.ast.stmt.*;
import compiler488.ast.type.*;
import compiler488.symbol.*;
import compiler488.symbol.SymbolTable.*;


/*
 * Implement semantic analysis for compiler 488
 * @author <B> Haohan Jiang (g3jiangh)
 *						 Maria Yancheva (c2yanche)
 *						 Timo Vink (c4vinkti)
 *						 Chandeep Singh (g2singh)
 *				 </B>
 */
public class Semantics implements ASTVisitor {
	/*
	 * Flag for tracing semantic analysis.
	 */
	private boolean trace = false;

	/*
	 * File sink for semantic analysis trace.
	 */
	private String traceFile = new String();
	public FileWriter tracer;
	public File f;

	/*
	 * Construct symbol table concurrently with semantic checking
	 */
	private SymbolTable symbolTable;

	/*
	 * Accummulate errors and raise an exception after all semantic rules have
	 */
	private SemanticErrorCollector errors;

	/*
	 * SemanticAnalyzer constructor
	 */
	public Semantics (boolean trace) {
		this.trace = trace;
		symbolTable = new SymbolTable();
		errors = new SemanticErrorCollector();
	}

	/*
	 * Called once by the parser at the end of compilation.
	 */
	public void Finalize() throws SemanticErrorException {
		if (errors.any()) {
			errors.raiseException();
		}
	}

	public SymbolTable getSymbolTable() {
		return symbolTable;
	}

	@Override
	public void visit(ArrayDeclPart arrayDeclPart) {
		if (this.trace) {
			System.out.println("Visiting ArrayDeclPart");
		}

		// Check first dimension
		Integer lowerBound = arrayDeclPart.getLowerBoundary1();
		Integer upperBound = arrayDeclPart.getUpperBoundary1();
		if (lowerBound > upperBound) {
			String msg = String.format(
				"Invalid bounds on first dimension of array '%s'. Lower bound '%d' must not be greater than upper bound '%d'.",
				arrayDeclPart.getName(), lowerBound, upperBound
			);
			errors.add(arrayDeclPart.getSourceCoord(), msg);
		};

		// Check second dimension if applicable
		if (arrayDeclPart.isTwoDimensional()) {
			lowerBound = arrayDeclPart.getLowerBoundary2();
			upperBound = arrayDeclPart.getUpperBoundary2();

			if (lowerBound > upperBound) {
				String msg = String.format(
					"Invalid bounds on second dimension of array '%s'. Lower bound '%d' must not be greater than upper bound '%d'.",
					arrayDeclPart.getName(), lowerBound, upperBound
				);
				errors.add(arrayDeclPart.getSourceCoord(), msg);
			}
		}
	}

	@Override
	public void visit(Declaration declaration) {
		if (this.trace) {
			System.out.println("Visiting Declaration");
		}
	}

	@Override
	public void visit(DeclarationPart declarationPart) {
		if (this.trace) {
			System.out.println("Visiting DeclarationPart");
		}
	}

	@Override
	public void visit(MultiDeclarations multiDeclarations) {
		if (this.trace) {
			System.out.println("Visiting MultiDeclarations");
		}

		// Add a symbol for every element in the declaration (into current scope)
		// (The value for the newly inserted elements is blank: in the project
		// language assignment cannot happen simultaneously with declaration)
		SymbolType declType = multiDeclarations.getType().toSymbolType();
		for (DeclarationPart elem : multiDeclarations.getParts()) {
			String elemName = elem.getName();
			SymbolKind elemKind = elem.getKind();

			// Check if identifier already exists in current scope
			SymbolTableEntry searchResult = symbolTable.search(elemName);
			if (searchResult != null) {
				// Detected a re-declaration in same scope
				SourceCoord originalDeclCoord = searchResult.getNode().getSourceCoord();
				String msg = String.format(
					"Redeclaration of '%s' not allowed in same scope. Original declaration at %s.",
					elemName, originalDeclCoord
				);
				errors.add(elem.getSourceCoord(), msg);
			}
			else {
				boolean success = symbolTable.insert(elemName, declType, elemKind, "", elem);
				if (success) {
					elem.setSTEntry(symbolTable.search(elemName));
				} else {
					throw new IllegalStateException("Insertion in symbol table failed.");
				}
			}
		}
	}

	@Override
	public void visit(RoutineDecl routineDecl) {
		if (this.trace) {
			System.out.println("Visiting RoutineDecl");
		}

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
				ReturnStmt rs = null;
				if (routineBody != null){
					for (Stmt routineStmt : routineBody) {
						rs = routineStmt.containsReturn();
						if (rs != null) {
							hasReturn = true;
							break;
						}
					}
				}
				if (!hasReturn) {
					errors.add(routineDecl.getSourceCoord(), "Function '" + routineName + "' must have at least one return statement.");
				}
			}

			// Check for existing declaration
			if (symbolTable.search(routineName) != null) {
				// Detected a re-declaration in same scope
				errors.add(routineDecl.getSourceCoord(), "Re-declaration of identifier " + routineName + " not allowed in same scope.");
			}
			else {
				boolean success = symbolTable.insert(routineName, routineType, routineKind, "", routineDecl);
				if (success) {
					routineDecl.setSTEntry(symbolTable.search(routineName));
				} else {
					errors.add(routineDecl.getSourceCoord(), "Unable to declare identifier " + routineName);
				}
			}

			// Begin new scope
			symbolTable.enterScope();

			// Mark as visited
			routineDecl.setVisited(true);
		} else {
			// Exit the scope
			symbolTable.exitScope();

			// Clear the flag
			routineDecl.setVisited(false);
		}

	}

	@Override
	public void visit(ScalarDecl scalarDecl) {
		if (this.trace) {
			System.out.println("Visiting ScalarDecl");
		}

		String declId = scalarDecl.getName();
		SymbolType declType = scalarDecl.getType().toSymbolType();

		// Check if identifier already exists in current scope
		if (symbolTable.search(declId) != null) {
			// Detected a re-declaration in same scope
			errors.add(scalarDecl.getSourceCoord(), "Re-declaration of identifier " + declId + " not allowed in same scope.");
		}
		else {
			boolean success = symbolTable.insert(declId, declType, SymbolKind.PARAMETER, "", scalarDecl);
			if (success) {
				scalarDecl.setSTEntry(symbolTable.search(declId));
			} else {
				errors.add(scalarDecl.getSourceCoord(), "Unable to declare identifier " + declId);
			}
		}
	}

	@Override
	public void visit(ScalarDeclPart scalarDeclPart) {
		if (this.trace) {
			System.out.println("Visiting ScalarDeclPart");
		}
	}

	@Override
	public void visit(AnonFuncExpn anonFuncExpn) {
		if (this.trace) {
			System.out.println("Visiting AnonFuncExpn");
			System.out.println("Type: " + anonFuncExpn.getExpnType(symbolTable));
		}
	}

	@Override
	public void visit(ArithExpn arithExpn) {
		if (this.trace) {
			System.out.println("Visiting ArithExpn");
			System.out.println("Type: " + arithExpn.getExpnType(symbolTable));
		}

		assertIsIntExpn(arithExpn);
	}

	@Override
	public void visit(BinaryExpn binaryExpn) {
		if (this.trace) {
			System.out.println("Visiting BinaryExpn");
			System.out.println("Type: " + binaryExpn.getExpnType(symbolTable));
		}
	}

	@Override
	public void visit(BoolConstExpn boolConstExpn) {
		if (this.trace) {
			System.out.println("Visiting BoolConstExpn");
			System.out.println("Type: " + boolConstExpn.getExpnType(symbolTable));
		}
	}

	@Override
	public void visit(BoolExpn boolExpn) {
		if (this.trace) {
			System.out.println("Visiting BoolExpn");
			System.out.println("Type: " + boolExpn.getExpnType(symbolTable));
		}

		assertIsBoolExpn(boolExpn);
	}

	@Override
	public void visit(CompareExpn compareExpn) {
		if (this.trace) {
			System.out.println("Visiting CompareExpn");
			System.out.println("Type: " + compareExpn.getExpnType(symbolTable));
		}

		assertIsBoolExpn(compareExpn);
	}

	@Override
	public void visit(ConstExpn constExpn) {
		if (this.trace) {
			System.out.println("Visiting ConstExpn");
			System.out.println("Type: " + constExpn.getExpnType(symbolTable));
		}
	}

	@Override
	public void visit(EqualsExpn equalsExpn) {
		if (this.trace) {
			System.out.println("Visiting EqualsExpn");
			System.out.println("Type: " + equalsExpn.getExpnType(symbolTable));
		}

		assertIsBoolExpn(equalsExpn);
	}

	@Override
	public void visit(Expn expn) {
		if (this.trace) {
			System.out.println("Visiting Expn");
			System.out.println("Type: " + expn.getExpnType(symbolTable));
		}
	}

	@Override
	public void visit(FunctionCallExpn functionCallExpn) {
		if (this.trace) {
			System.out.println("Visiting FunctionCallExpn");
			System.out.println("Type: " + functionCallExpn.getExpnType(symbolTable));
		}

		// S40: check that identifier has been declared as a function
		String functionName = functionCallExpn.getIdent();
		if (symbolTable.searchGlobal(functionName) == null) {
			errors.add(functionCallExpn.getSourceCoord(), "Function '" + functionName + "' cannot be used before it has been declared.");
		}
		else if (symbolTable.searchGlobal(functionName).getKind() != SymbolKind.FUNCTION) {
			errors.add(functionCallExpn.getSourceCoord(), "Identifier '" + functionName + "' cannot be used as a function because it has been declared as " + symbolTable.searchGlobal(functionName).getKind() + ".");
		}

		// S42/S43: check that the number of parameters declared in the function declaration is the same as the
			// number of arguments passed to the function call expression
		SymbolTableEntry declaredFunc = symbolTable.searchGlobal(functionName);
		RoutineDecl declaredFuncASTNode = (RoutineDecl)declaredFunc.getNode();
		int numArgs = functionCallExpn.getArguments().size();
		int numParams = declaredFuncASTNode.getParameters().size();
		if (numArgs != numParams) {
			errors.add(functionCallExpn.getSourceCoord(), "Function '" + functionName + "' is called with " + numArgs + " arguments, but requires " + numParams + " parameters.");
		}

		// S36: Check that type of argument expression matches type of corresponding formal parameter
		assertArgmtAndParamTypesMatch(functionCallExpn.getArguments(), declaredFuncASTNode.getParameters());
	}

	@Override
	public void visit(IdentExpn identExpn) {
		if (this.trace) {
			System.out.println("Visiting IdentExpn");
			System.out.println("Type: " + identExpn.getExpnType(symbolTable));
		}

		// S37: check that identifier has been declared as a scalar variable
		// S39: check that identifier has been declared as a parameter
		// S40: check that identifier has been declared as a function
		String identName = identExpn.getIdent();
		if (symbolTable.searchGlobal(identName) == null) {
			errors.add(identExpn.getSourceCoord(), "Identifier '" + identName + "' cannot be used before it has been declared.");
		}
		else if (symbolTable.searchGlobal(identName).getKind() != SymbolKind.VARIABLE &&
				 symbolTable.searchGlobal(identName).getKind() != SymbolKind.PARAMETER &&
				 symbolTable.searchGlobal(identName).getKind() != SymbolKind.FUNCTION ) {
			errors.add(identExpn.getSourceCoord(), "Identifier '" + identName + "' cannot be used in this context because it has been declared as " + symbolTable.searchGlobal(identName).getKind() + ".");
		}

	}

	@Override
	public void visit(IntConstExpn intConstExpn) {
		if (this.trace) {
			System.out.println("Visiting IntConstExpn");
			System.out.println("Type: " + intConstExpn.getExpnType(symbolTable));
		}
	}

	@Override
	public void visit(NotExpn notExpn) {
		if (this.trace) {
			System.out.println("Visiting NotExpn");
			System.out.println("Type: " + notExpn.getExpnType(symbolTable));
		}

		assertIsBoolExpn(notExpn);
	}

	@Override
	public void visit(SkipConstExpn skipConstExpn) {
		if (this.trace) {
			System.out.println("Visiting SkipConstExpn");
			System.out.println("Type: " + skipConstExpn.getExpnType(symbolTable));
		}
	}

	@Override
	public void visit(SubsExpn subsExpn) {
		if (this.trace) {
			System.out.println("Visiting SubsExpn");
			System.out.println("Type: " + subsExpn.getExpnType(symbolTable));
		}

		// S38: check that identifier has been declared as an array
		String arrayName = subsExpn.getVariable();
		if (symbolTable.searchGlobal(arrayName) == null) {
			errors.add(subsExpn.getSourceCoord(), "Array '" + arrayName + "' cannot be used before it has been declared.");
		}
		else if (symbolTable.searchGlobal(arrayName).getKind() != SymbolKind.ARRAY) {
			errors.add(subsExpn.getSourceCoord(), "Identifier '" + arrayName + "' cannot be used as an array because it has been declared as " + symbolTable.searchGlobal(arrayName).getKind() + ".");
		}

		// S31: check that subscripts are int expressions
		assertIsIntExpn(subsExpn.getSubscript1());
		if (subsExpn.isTwoDimensional()) {
			assertIsIntExpn(subsExpn.getSubscript2());
		}
	}

	@Override
	public void visit(TextConstExpn textConstExpn) {
		if (this.trace) {
			System.out.println("Visiting TextConstExpn");
			System.out.println("Type: " + textConstExpn.getExpnType(symbolTable));
		}
	}

	@Override
	public void visit(UnaryExpn unaryExpn) {
		if (this.trace) {
			System.out.println("Visiting UnaryExpn");
			System.out.println("Type: " + unaryExpn.getExpnType(symbolTable));
		}
	}

	@Override
	public void visit(UnaryMinusExpn unaryMinusExpn) {
		if (this.trace) {
			System.out.println("Visiting UnaryMinusExpn");
			System.out.println("Type: " + unaryMinusExpn.getExpnType(symbolTable));
		}

		assertIsIntExpn(unaryMinusExpn);
	}

	@Override
	public void visit(AssignStmt assignStmt) {
		if (this.trace) {
			System.out.println("Visiting AssignStmt");
		}

		// S34: Check that variable and expression in assignment are the same type
		SymbolType lType = assignStmt.getLval().getExpnType(symbolTable);
		SymbolType rType = assignStmt.getRval().getExpnType(symbolTable);
		if (lType != rType) {
			errors.add
				(assignStmt.getRval().getSourceCoord(),
				"LHS type (" + lType + ") in assignment differs from RHS type (" + rType + ")");
		}
	}

	@Override
	public void visit(ExitStmt exitStmt) {
		if (this.trace) {
			System.out.println("Visiting ExitStmt");
		}

		// Only do S30 check if "exit when"
		if (exitStmt.getExpn() != null) {
			assertIsBoolExpn(exitStmt.getExpn());
		}

		/**
		 * S50 Check that exit statement is directly inside a loop
		 *	Checks if symbols "loop" or "while" have been declared in the scope
		 *	If not, throws an error
		 */
		BaseAST currNode = exitStmt;
		boolean foundLoop = false;
		while(currNode != null)
		{
			if (currNode instanceof LoopStmt || currNode instanceof WhileDoStmt) {
				foundLoop = true;
				break;
			}
			if (currNode instanceof RoutineDecl || currNode instanceof AnonFuncExpn) {
				break;
			}
			currNode = currNode.getParentNode();
		}

		if (!foundLoop){
			errors.add(exitStmt.getSourceCoord(), "EXIT not contained in LOOP or WHILE statements");
		}
	}

	@Override
	public void visit(GetStmt getStmt) {
		if (this.trace) {
			System.out.println("Visiting GetStmt");
		}

		// S31: check that variables are integers
		for (Expn expn : getStmt.getInputs()) {
			assertIsIntExpn(expn);
		}
	}

	@Override
	public void visit(IfStmt ifStmt) {
		if (this.trace) {
			System.out.println("Visiting IfStmt");
		}

		assertIsBoolExpn(ifStmt.getCondition());
	}

	@Override
	public void visit(LoopingStmt loopingStmt) {
		if (this.trace) {
			System.out.println("Visiting LoopingStmt");
		}
	}

	@Override
	public void visit(LoopStmt loopStmt) {
		if (this.trace) {
			System.out.println("Visiting LoopStmt");
		}
	}

	@Override
	public void visit(ProcedureCallStmt procedureCallStmt) {
		if (this.trace) {
			System.out.println("Visiting ProcedureCallStmt");
		}

		// S41: check that identifier has been declared as a procedure
		String procName = procedureCallStmt.getName();
		if (symbolTable.searchGlobal(procName) == null) {
			errors.add(procedureCallStmt.getSourceCoord(), "Procedure '" + procName + "' cannot be used before it has been declared.");
		}
		else if (symbolTable.searchGlobal(procName).getKind() != SymbolKind.PROCEDURE) {
			errors.add(procedureCallStmt.getSourceCoord(), "Identifier '" + procName + "' cannot be used as a procedure because it has been declared as " + symbolTable.searchGlobal(procName).getKind() + ".");
		}

		// S42/S43: check that the number of parameters declared in the procedure declaration is the same as the
			// number of arguments passed to the procedure call statement
		SymbolTableEntry declaredProc = symbolTable.searchGlobal(procName);
		RoutineDecl declaredProcASTNode = (RoutineDecl)declaredProc.getNode();
		int numArgs = procedureCallStmt.getArguments().size();
		int numParams = declaredProcASTNode.getParameters().size();
		if (numArgs != numParams) {
			errors.add(procedureCallStmt.getSourceCoord(), "Procedure '" + procName + "' is called with " + numArgs + " arguments, but requires " + numParams + " parameters.");
		}

		// S36: Check that type of argument expression matches type of corresponding formal parameter
		assertArgmtAndParamTypesMatch(procedureCallStmt.getArguments(), declaredProcASTNode.getParameters());
	}

	@Override
	public void visit(Program program) {
		if (this.trace) {
			System.out.println("Visiting Program");
		}

		if (!program.isVisited()) {
			// Begin new scope
			symbolTable.enterScope();

			// Mark as visited
			program.setVisited(true);
		} else {
			// Exit the scope
			symbolTable.exitScope();

			// Clear the flag
			program.setVisited(false);
		}
	}

	@Override
	public void visit(PutStmt putStmt) {
		if (this.trace) {
			System.out.println("Visiting PutStmt");
		}

		// S31: Check that type of expression or variable is integer
		// Also added in check for text/skip
		for (Printable e : putStmt.getOutputs()) {
			SymbolType type = ((Expn)e).getExpnType(symbolTable);
			if (type != SymbolType.INTEGER &&
				type != SymbolType.TEXT &&
				type != SymbolType.SKIP) {
				errors.add(((Expn)e).getSourceCoord(), "Put statement can only contain integer expressions, text, or skips");
			}
		}
	}

	@Override
	public void visit(ReturnStmt returnStmt) {
		if (this.trace) {
			System.out.println("Visiting ReturnStmt");
		}

		// S51-52 Must check that return statements are in procedure
		// or function scope

		BaseAST currNode = returnStmt;
		RoutineDecl parentRoutine = null;
		while(currNode != null)
		{
			if (currNode instanceof RoutineDecl){
				parentRoutine = (RoutineDecl)currNode;
				break;
			}
			currNode = currNode.getParentNode();
		}

		if (parentRoutine == null){
			errors.add(returnStmt.getSourceCoord(), "Return statement is not in the scope of a function or procedure");
		}
		else if (parentRoutine.getType() != null) {
			// S35: Check that expression type matches the return type of enclosing function
			SymbolType returnStatementType = returnStmt.getValue().getExpnType(symbolTable);
			SymbolType routineType = parentRoutine.getType().toSymbolType();
			if ( routineType != returnStatementType ) {
				errors.add(
					returnStmt.getSourceCoord(),
					"Return statement type '" + returnStatementType + "' does not match function type '" + routineType + "'.");
			}
		}
	}

	@Override
	public void visit(Scope scope) {
		if (this.trace) {
			System.out.println("Visiting Scope");
		}
	}

	@Override
	public void visit(Stmt stmt) {
		if (this.trace) {
			System.out.println("Visiting Stmt");
		}
	}

	@Override
	public void visit(WhileDoStmt whileDoStmt) {
		if (this.trace) {
			System.out.println("Visiting WhileDoStmt");
		}

		assertIsBoolExpn(whileDoStmt.getExpn());
	}

	@Override
	public void visit(BooleanType booleanType) {
		if (this.trace) {
			System.out.println("Visiting BooleanType");
		}
	}

	@Override
	public void visit(IntegerType integerType) {
		if (this.trace) {
			System.out.println("Visiting IntegerType");
		}
	}

	@Override
	public void visit(Type type) {
		if (this.trace) {
			System.out.println("Visiting Type");
		}
	}

	// compare expn type to expectedType, and error if not the same
	private void checkExpnType(Expn expn, SymbolType expectedType) {
		SymbolType type = expn.getExpnType(symbolTable);
		if (type != expectedType) {
			String msg = String.format("%s expression expected, but is %s.", expectedType, type);
			errors.add(expn.getSourceCoord(), msg);
		}
	}

	// S30: check that type of expression is boolean
	private void assertIsBoolExpn(Expn expn) {
		checkExpnType(expn, SymbolType.BOOLEAN);
	}

	// S31: check that type of expression is integer
	private void assertIsIntExpn(Expn expn) {
		checkExpnType(expn, SymbolType.INTEGER);
	}

	// S36: Check that type of argument expression matches type of corresponding formal parameter
	private void assertArgmtAndParamTypesMatch(ASTList<Expn> argList, ASTList<ScalarDecl> paramList) {
		Iterator<Expn> argExpnIter = argList.iterator();
		Iterator<ScalarDecl> paramsIter	= paramList.iterator();
		int count = 1;
		while (argExpnIter.hasNext()) {
			Expn nextArg = argExpnIter.next();
			SymbolType aType = nextArg.getExpnType(symbolTable);
			SymbolType pType = paramsIter.next().getSTEntry().getType();
			if (aType != pType) {
				errors.add(
					nextArg.getSourceCoord(),
					"Arg expression " + count + "'s type (" + aType + ") does not match expected param type (" + pType + ")");
			}
			count++;
		}
	}
}
