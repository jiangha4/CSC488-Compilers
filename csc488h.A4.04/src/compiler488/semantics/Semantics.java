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
	public void enterVisit(BaseAST baseAST) {
	}

	@Override
	public void exitVisit(BaseAST baseAST) {
	}

	@Override
	public void exitVisit(ArrayDeclPart arrayDeclPart) {
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
	public void exitVisit(MultiDeclarations multiDeclarations) {
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
	public void enterVisit(RoutineDecl routineDecl) {
		/**
		 * S11/S12: Declare function with/without parameters and with specified type
		 * S17/S18: Declare procedure with/without parameters
		 */

		// Record routine declaration in symbol table
		String routineName = routineDecl.getName();
		SymbolType routineType = null;
		SymbolKind routineKind = SymbolKind.PROCEDURE;

		if (routineDecl.isFunctionDecl()) {
			routineType = routineDecl.getType().toSymbolType();
			routineKind = SymbolKind.FUNCTION;

			// S53: check that a function body contains at least one return statement
			Scope routineScope = routineDecl.getBody();
			ReturnStmt returnStatement = routineScope.containsReturn();
			if (returnStatement == null) {
				String msg = String.format("Function '%s' must contain at least one return statement.", routineName);
				errors.add(routineDecl.getSourceCoord(), msg);
			}
		}

		// Check for existing declaration
		SymbolTableEntry searchResult = symbolTable.search(routineName);
		if (searchResult != null) {
			SourceCoord originalDeclCoord = searchResult.getNode().getSourceCoord();
			String msg = String.format(
				"Redeclaration of '%s' not allowed in same scope. Original declaration at %s.",
				routineName, originalDeclCoord
			);
			errors.add(routineDecl.getSourceCoord(), msg);
		} else {
			boolean success = symbolTable.insert(routineName, routineType, routineKind, "", routineDecl);
			if (success) {
				routineDecl.setSTEntry(symbolTable.search(routineName));
			} else {
				throw new IllegalStateException("Insertion in symbol table failed.");
			}
		}

		// Begin new scope
		symbolTable.enterScope();
	}

	@Override
	public void exitVisit(RoutineDecl routineDecl) {
		symbolTable.exitScope();
	}

	@Override
	public void exitVisit(ScalarDecl scalarDecl) {
		String declName = scalarDecl.getName();
		SymbolType declType = scalarDecl.getType().toSymbolType();

		// Check if identifier already exists in current scope
		SymbolTableEntry searchResult = symbolTable.search(declName);
		if (searchResult != null) {
			SourceCoord originalDeclCoord = searchResult.getNode().getSourceCoord();
			String msg = String.format(
				"Redeclaration of '%s' not allowed in same scope. Original declaration at %s.",
				declName, originalDeclCoord
			);
			errors.add(scalarDecl.getSourceCoord(), msg);
		} else {
			boolean success = symbolTable.insert(declName, declType, SymbolKind.PARAMETER, "", scalarDecl);
			if (success) {
				scalarDecl.setSTEntry(symbolTable.search(declName));
			} else {
				throw new IllegalStateException("Insertion in symbol table failed.");
			}
		}
	}

	@Override
	public void exitVisit(ArithExpn arithExpn) {
		assertIsIntExpn(arithExpn);
	}

	@Override
	public void exitVisit(BoolExpn boolExpn) {
		assertIsBoolExpn(boolExpn);
	}

	@Override
	public void exitVisit(CompareExpn compareExpn) {
		assertIsBoolExpn(compareExpn);
	}

	@Override
	public void exitVisit(EqualsExpn equalsExpn) {
		assertIsBoolExpn(equalsExpn);
	}

	@Override
	public void exitVisit(FunctionCallExpn functionCallExpn) {
		// S40: check that identifier has been declared
		String functionName = functionCallExpn.getIdent();
		SymbolTableEntry identEntry = symbolTable.searchGlobal(functionName);
		if (identEntry == null) {
			String msg = String.format("Unknown identifier '%s'.", functionName);
			errors.add(functionCallExpn.getSourceCoord(), msg);
			return;
		}

		// S40: check that identifier has been declared as a function
		SymbolKind identEntryKind = identEntry.getKind();
		BaseAST astNode = identEntry.getNode();
		SourceCoord originalDeclCoords = astNode.getSourceCoord();
		if (identEntryKind != SymbolKind.FUNCTION) {
			String msg = String.format(
				"Attempting to use '%s' as a function but it has been declared as a %s on %s.",
				functionName, identEntryKind, originalDeclCoords
			);
			errors.add(functionCallExpn.getSourceCoord(), msg);
			return;
		}

		// S42/S43: check that the number of parameters declared in the function declaration is the same as the
		// number of arguments passed to the function call expression
		int numArgs = functionCallExpn.getArguments().size();
		RoutineDecl declaredFuncASTNode = (RoutineDecl)astNode;
		int numParams = declaredFuncASTNode.getParameters().size();
		if (numArgs != numParams) {
			String msg = String.format(
				"Calling function '%s' with %d arguments, but declared with %d parameters on %s.",
				functionName, numArgs, numParams, originalDeclCoords
			);
			errors.add(functionCallExpn.getSourceCoord(), msg);
			return;
		}

		// S36: Check that type of argument expression matches type of corresponding formal parameter
		assertArgmtAndParamTypesMatch(functionCallExpn.getArguments(), declaredFuncASTNode.getParameters());
	}

	@Override
	public void exitVisit(IdentExpn identExpn) {
		// S37/S39/S40: check that identifier has been declared
		String identName = identExpn.getIdent();
		SymbolTableEntry identSymbol = symbolTable.searchGlobal(identName);
		if (identSymbol == null) {
			String msg = String.format("Unknown identifier '%s'.", identName);
			errors.add(identExpn.getSourceCoord(), msg);
			return;
		}

		// Check if symbol is of appropriate kind
		SymbolKind identKind = identSymbol.getKind();
		if (identKind != SymbolKind.VARIABLE &&
		    identKind != SymbolKind.PARAMETER &&
		    identKind != SymbolKind.FUNCTION ) {

			String msg = String.format(
				"Only variables, parameters and functions are valid here, but '%s' is a %s",
				identName, identKind
			);
			errors.add(identExpn.getSourceCoord(), msg);
			return;
		}

		// S42: check that the function has no parameters
		if (identKind == SymbolKind.FUNCTION) {
			RoutineDecl routineDecl = (RoutineDecl)identSymbol.getNode();
			SourceCoord originalDecl = routineDecl.getSourceCoord();
			Integer numParams = routineDecl.getParameters().size();
			if (numParams > 0) {
				String msg = String.format(
					"Calling function '%s' without arguments, but it was declared with %d parameters on %s.",
					identName, numParams, originalDecl
				);
				errors.add(identExpn.getSourceCoord(), msg);
			}
		}
	}

	@Override
	public void exitVisit(NotExpn notExpn) {
		assertIsBoolExpn(notExpn);
	}

	@Override
	public void exitVisit(SubsExpn subsExpn) {
		// S38: check that identifier has been declared
		String arrayName = subsExpn.getVariable();
		SymbolTableEntry searchResult = symbolTable.searchGlobal(arrayName);
		if (searchResult == null) {
			String msg = String.format("Unknown identifier '%s'.", arrayName);
			errors.add(subsExpn.getSourceCoord(), msg);
			return;
		}

		// S38: check that identifier has been declared as an array
		SymbolKind identKind = searchResult.getKind();
		if (identKind != SymbolKind.ARRAY) {
			SourceCoord originalDeclCoords = searchResult.getNode().getSourceCoord();
			String msg = String.format(
				"Attempting to use '%s' as an array but it has been declared as a %s on %s.",
				arrayName, identKind, originalDeclCoords
			);
			errors.add(subsExpn.getSourceCoord(), msg);
		}

		// S31: check that thesubscripts are int expressions
		assertIsIntExpn(subsExpn.getSubscript1());
		if (subsExpn.isTwoDimensional()) {
			assertIsIntExpn(subsExpn.getSubscript2());
		}
	}

	@Override
	public void exitVisit(UnaryMinusExpn unaryMinusExpn) {
		assertIsIntExpn(unaryMinusExpn);
	}

	@Override
	public void exitVisit(AssignStmt assignStmt) {
		// S34: Check that variable and expression in assignment are the same type
		SymbolType lType = assignStmt.getLval().getExpnType(symbolTable);
		SymbolType rType = assignStmt.getRval().getExpnType(symbolTable);
		if (lType != rType) {
			String msg = String.format(
				"Can't assign %s expression to %s variable.",
				rType, lType
			);
			errors.add(assignStmt.getRval().getSourceCoord(), msg);
		}
	}

	@Override
	public void exitVisit(ExitStmt exitStmt) {
		// S50: Check that exit statement is directly inside a loop
		BaseAST currNode = exitStmt;
		boolean foundLoop = false;
		while(currNode != null && !(currNode instanceof RoutineDecl) && !(currNode instanceof AnonFuncExpn))
		{
			if (currNode instanceof LoopStmt || currNode instanceof WhileDoStmt) {
				foundLoop = true;
				break;
			}
			currNode = currNode.getParentNode();
		}
		if (!foundLoop){
			errors.add(exitStmt.getSourceCoord(), "EXIT must be in a LOOP or WHILE statement.");
		}

		// S30: Check if "exit when" condition is a bool expn if applicable
		if (exitStmt.getExpn() != null) {
			assertIsBoolExpn(exitStmt.getExpn());
		}
	}

	@Override
	public void exitVisit(GetStmt getStmt) {
		// S31: check that variables are integer variables
		for (Expn expn : getStmt.getInputs()) {
			assertIsIntExpn(expn);
		}
	}

	@Override
	public void exitVisit(IfStmt ifStmt) {
		assertIsBoolExpn(ifStmt.getCondition());
	}

	@Override
	public void exitVisit(ProcedureCallStmt procedureCallStmt) {
		String procName = procedureCallStmt.getName();
		SymbolTableEntry identEntry = symbolTable.searchGlobal(procName);

		// S41: Check if identifier has been declared
		if (identEntry == null) {
			String msg = String.format("Unknown identifier '%s'.", procName);
			errors.add(procedureCallStmt.getSourceCoord(), msg);
			return;
		}

		// S41: Check if identifier has been declared as a procedure
		SymbolKind identEntryKind = identEntry.getKind();
		BaseAST astNode = identEntry.getNode();
		SourceCoord originalDeclCoords = astNode.getSourceCoord();
		if (identEntryKind != SymbolKind.PROCEDURE) {
			String msg = String.format(
				"Attempting to use '%s' as a procedure but it has been declared as a %s on %s.",
				procName, identEntryKind, originalDeclCoords
			);
			errors.add(procedureCallStmt.getSourceCoord(), msg);
			return;
		}

		// S42/S43: check that the number of parameters declared in the procedure declaration is the same as the
		// number of arguments passed to the procedure call statement
		int numArgs = procedureCallStmt.getArguments().size();
		RoutineDecl declaredProcASTNode = (RoutineDecl)astNode;
		int numParams = declaredProcASTNode.getParameters().size();
		if (numArgs != numParams) {
			String msg = String.format(
				"Calling procedure '%s' with %d arguments, but it has been declared with %d parameters on %s.",
				procName, numArgs, numParams, originalDeclCoords
			);
			errors.add(procedureCallStmt.getSourceCoord(), msg);
			return;
		}

		// S36: Check that type of argument expression matches type of corresponding formal parameter
		assertArgmtAndParamTypesMatch(procedureCallStmt.getArguments(), declaredProcASTNode.getParameters());
	}

	@Override
	public void enterVisit(Program program) {
		symbolTable.enterScope();
	}

	@Override
	public void exitVisit(Program program) {
		symbolTable.exitScope();
	}

	@Override
	public void exitVisit(PutStmt putStmt) {
		// S31: Check that type of expression or variable is integer
		// Also added in check for text/skip
		for (Printable printable : putStmt.getOutputs()) {
			Expn expn = (Expn)printable;
			SymbolType type = expn.getExpnType(symbolTable);
			if (type != SymbolType.INTEGER &&
				type != SymbolType.TEXT &&
				type != SymbolType.SKIP) {
				String msg = String.format("Can't 'put' %s expression. Can only 'put' integer expressions, text, and skips.", type);
				errors.add(expn.getSourceCoord(), msg);
			}
		}
	}

	@Override
	public void exitVisit(ReturnStmt returnStmt) {
		// S51-52: Ensure return is in a function or procedure
		BaseAST currNode = returnStmt;
		RoutineDecl parentRoutineDecl = null;
		while(currNode != null && !(currNode instanceof AnonFuncExpn))
		{
			if (currNode instanceof RoutineDecl) {
				parentRoutineDecl = (RoutineDecl)currNode;
				break;
			}
			currNode = currNode.getParentNode();
		}
		if (parentRoutineDecl == null){
			errors.add(returnStmt.getSourceCoord(), "Return statement is not in the scope of a function or procedure.");
			return;
		}

		// S35: Check that expression type matches the return type of enclosing function if applicable
		if (parentRoutineDecl.isFunctionDecl()) {
			Expn returnValue = returnStmt.getValue();
			SymbolType routineType = parentRoutineDecl.getType().toSymbolType();
			
			// Check that a return expression exists, then check its type
			if (returnValue != null) {
				SymbolType returnStatementType = returnValue.getExpnType(symbolTable);
				if (routineType != returnStatementType) {
					String msg = String.format(
						"Return statement expression type %s does not match function type %s.",
						returnStatementType, routineType
					);
					errors.add(returnStmt.getSourceCoord(), msg);
				}
			}
			else {
				String msg = String.format("Return statement is missing an expression; function '%s' must return %s.", parentRoutineDecl.getName(), routineType);
				errors.add(returnStmt.getSourceCoord(), msg);
			}
		}
		else {
			// The routine is a procedure -> ensure that there is no return expression
			Expn returnValue = returnStmt.getValue();
			if (returnValue != null) {
				String msg = String.format("Return statement contains an expression; procedure '%s' cannot return an expression.", parentRoutineDecl.getName());
				errors.add(returnStmt.getSourceCoord(), msg);
			}
		}
	}

	@Override
	public void exitVisit(WhileDoStmt whileDoStmt) {
		assertIsBoolExpn(whileDoStmt.getExpn());
	}

	// S30: check that type of expression is boolean
	private void assertIsBoolExpn(Expn expn) {
		checkExpnType(expn, SymbolType.BOOLEAN);
	}

	// S31: check that type of expression is integer
	private void assertIsIntExpn(Expn expn) {
		checkExpnType(expn, SymbolType.INTEGER);
	}

	// compare expn type to expectedType, and error if not the same
	private void checkExpnType(Expn expn, SymbolType expectedType) {
		SymbolType type = expn.getExpnType(symbolTable);
		if (type != expectedType) {
			String msg = String.format("%s expression expected, but is %s.", expectedType, type);
			errors.add(expn.getSourceCoord(), msg);
		}
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
				String msg = String.format(
					"Type mismatch for argument %d. %s expected, but %s found.",
					count, pType, aType
				);
				errors.add(nextArg.getSourceCoord(), msg);
			}
			count++;
		}
	}
}
