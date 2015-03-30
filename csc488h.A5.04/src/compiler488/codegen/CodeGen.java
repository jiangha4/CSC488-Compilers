package compiler488.codegen;

import java.io.*;
import java.util.*;

import compiler488.ast.*;
import compiler488.ast.decl.*;
import compiler488.ast.expn.*;
import compiler488.ast.stmt.*;
import compiler488.ast.type.*;
import compiler488.compiler.*;
import compiler488.codegen.ActivationRecord.*;
import compiler488.runtime.*;
import compiler488.symbol.*;
import compiler488.symbol.SymbolTable.*;
import compiler488.symbol.STScope.ScopeKind;

/**	  CodeGenerator.java
 *<pre>
 *  Code Generation Conventions
 *
 *  To simplify the course project, this code generator is
 *  designed to compile directly to pseudo machine memory
 *  which is available as the private array memory[]
 *
 *  It is assumed that the code generator places instructions
 *  in memory in locations
 *
 *	  memory[ 0 .. startMSP - 1 ]
 *
 *  The code generator may also place instructions and/or
 *  constants in high memory at locations (though this may
 *  not be necessary)
 *	  memory[ startMLP .. Machine.memorySize - 1 ]
 *
 *  During program exection the memory area
 *	  memory[ startMSP .. startMLP - 1 ]
 *  is used as a dynamic stack for storing activation records
 *  and temporaries used during expression evaluation.
 *  A hardware exception (stack overflow) occurs if the pointer
 *  for this stack reaches the memory limit register (mlp).
 *
 *  The code generator is responsible for setting the global
 *  variables:
 *	  startPC		 initial value for program counter
 *	  startMSP		initial value for msp
 *	  startMLP		initial value for mlp
 * </pre>
 * @author <B>
 *	  Haohan Jiang (g3jiangh)
 *	  Maria Yancheva (c2yanche)
 *	  Timo Vink (c4vinkti)
 *	  Chandeep Singh (g2singh)
 * </B>
 */
public class CodeGen extends BaseASTVisitor
{
	/** flag for tracing code generation */
	private boolean trace = Main.traceCodeGen ;

	/** helper to emit code **/
	private CodeGenHelper instrs;

	/** Symbol table from semantic analysis **/
	private SymbolTable symbolTable;

	/**
	 * Constructor to initialize code generation
	 */
	public CodeGen(SymbolTable st)
	{
		symbolTable = st;
		instrs = new CodeGenHelper();
	}

	/**
	 *  Write generated code to machine memory and set the PC, MSP, and MLP.
	 *
	 *  @throws MemoryAddressException  from Machine.writeMemory
	 *  @throws ExecutionException	  from Machine.writeMemory
	 */
	public void writeToMachine()
		throws MemoryAddressException, ExecutionException
	{
		// Trace emitted instruction
		if (trace) {
			instrs.printDebug();
		}

		// Write instructions to machine memory
		short counter = 0;
		List<Short> instructions = instrs.getInstructions();
		for (short instr : instructions) {
			Machine.writeMemory(counter, instr);
			counter++;
		}

		// Set initial values for registers
		Machine.setPC((short)1);
		Machine.setMSP((short)(counter + 1));
		Machine.setMLP((short)(Machine.memorySize - 1));
	}

	@Override
	public void enterVisit(Program program)
	{
		instrs.emitHalt();
		instrs.emitProgramActivationRecord(program.getSTScope());
	}

	@Override
	public void exitVisit(Program program)
	{
		instrs.emitProgramActivationRecordCleanUp();
		instrs.emitBranch();
		instrs.emitPop();
	}

	@Override
	public void exitVisitPutExpn(Expn putStmtChild)
	{
		if (putStmtChild instanceof TextConstExpn) {
			instrs.emitPrintText(((TextConstExpn)putStmtChild).getValue());
		} else if (putStmtChild instanceof SkipConstExpn) {
			instrs.emitPrintSkip();
		} else {
			instrs.emitPrintInt();
		}
	}

	@Override
	public void exitVisitGetExpn(Expn getStmtChild) {
		instrs.removeLastEmittedLoad();
		instrs.emitReadIntAndStore();
	}

	@Override
	public void exitVisitLHS(AssignStmt assignStmt)
	{
		instrs.removeLastEmittedLoad();
	}

	@Override
	public void exitVisit(AssignStmt assignStmt)
	{
		instrs.emitStore();
	}

	@Override
	public void exitVisitCondition(IfStmt ifStmt)
	{
		ifStmt.shouldPointToFalse = instrs.emitBranchIfFalseToUnknown();
	}

	@Override
	public void exitVisitWhenTrue(IfStmt ifStmt)
	{
		ifStmt.shouldPointToEnd = instrs.emitBranchToUnknown();
		instrs.patchForwardBranchToNextInstruction(ifStmt.shouldPointToFalse);
	}

	@Override
	public void exitVisit(IfStmt ifStmt)
	{
		instrs.patchForwardBranchToNextInstruction(ifStmt.shouldPointToEnd);
	}

	@Override
	public void enterVisit(ReturnStmt returnStmt) {
		// If there is a return value, prepare to put it in the 
		// "return value" memory location in the current activation record
		if (returnStmt.getValue() != null) {
			instrs.emitGetAddr(returnStmt.getContainingSTScope().getLexicalLevel(), 0);
		}
	}
	
	@Override
	public void exitVisit(ReturnStmt returnStmt)
	{
		if (returnStmt.getValue() != null) {
			// Store the actual return value (currently on top of stack) into the designated "return value" 
			// memory location in the activation record (currently second from the top of the stack)
			instrs.emitStore();
		}
		returnStmt.shouldPointToEnd = instrs.emitBranchToUnknown();
	}

	@Override
	public void exitVisit(ArithExpn arithExpn)
	{
		String operation = arithExpn.getOpSymbol();
		switch (operation) {
			case ArithExpn.OP_PLUS:
				instrs.emitAdd();
				break;
			case ArithExpn.OP_MINUS:
				instrs.emitSubtract();
				break;
			case ArithExpn.OP_TIMES:
				instrs.emitMultiply();
				break;
			case ArithExpn.OP_DIVIDE:
				instrs.emitDivide();
				break;

			default:
				String msg = "Unknown ArithExpn operation: " + operation;
				throw new UnsupportedOperationException(msg);
		}
	}

	@Override
	public void exitVisit(UnaryMinusExpn unaryMinusExpn)
	{
		instrs.emitNegateInt();
	}

	@Override
	public void exitVisitLHS(BoolExpn boolExpn)
	{
		if (boolExpn.getOpSymbol() == BoolExpn.OP_AND) {
			instrs.emitNot();
		}
	}

	@Override
	public void exitVisit(BoolExpn boolExpn)
	{
		String operation = boolExpn.getOpSymbol();
		switch (operation) {
			case BoolExpn.OP_OR:
				instrs.emitOr();
				break;
			case BoolExpn.OP_AND:
				instrs.emitNot();
				instrs.emitOr();
				instrs.emitNot();
				break;

			default:
				String msg = "Unknown BoolExpn operation: " + operation;
				throw new UnsupportedOperationException(msg);
		}
	}

	@Override
	public void exitVisit(NotExpn notExpn)
	{
		instrs.emitNot();
	}

	@Override
	public void exitVisit(IntConstExpn intConstExpn)
	{
		instrs.emitPushValue(intConstExpn.getValue());
	}

	@Override
	public void exitVisit(BoolConstExpn boolConstExpn)
	{
		instrs.emitPushBoolValue(boolConstExpn.getValue());
	}

	@Override
	public void enterVisit(IdentExpn identExpn) {
		// Get the symbol table entry
		STScope callerScope = identExpn.getContainingSTScope();
		String ident = identExpn.getIdent();
		SymbolTableEntry ste = symbolTable.searchGlobalFrom(ident, callerScope);
		
		if (ste.getKind() == SymbolKind.FUNCTION) {
			// Function call without parameters
			
			// Get the function's scope
			STScope calleeScope = ((RoutineDecl)ste.getNode()).getSTScope();
			
			// Emit function call setup
			identExpn.shouldPointToAfterBranch = instrs.emitRoutineCallSetup(callerScope, calleeScope);
		}
	}
	
	@Override
	public void exitVisit(IdentExpn identExpn) {
		// Get the symbol table entry
		STScope scope = identExpn.getContainingSTScope();
		String ident = identExpn.getIdent();
		SymbolTableEntry ste = symbolTable.searchGlobalFrom(ident, scope);
		
		if (ste.getKind() == SymbolKind.FUNCTION) {
			// Function call without parameters
			
			// Get the function's scope
			STScope calleeScope = ((RoutineDecl)ste.getNode()).getSTScope();
			
			// Emit function call branch
			instrs.emitFunctionCallBranch(calleeScope, identExpn.shouldPointToAfterBranch);
		}
		else {
			// Variable or parameter
			instrs.emitLoadVar(scope, ident);
		}
	}

	@Override
	public void enterVisit(LoopStmt loopStmt) {
		// body will be visited after this and we
		// need to save addr of the first stmt in body,
		// so save the addr of the next instruction.
		loopStmt.startOfLoop = instrs.getNextInstructionAddr();
	}

	@Override
	public void exitVisit(LoopStmt loopStmt) {
		instrs.emitPushValue(loopStmt.startOfLoop);
		instrs.emitBranch();

		// next instruction is end of loop, so patch everything that needs to point there
		instrs.patchForwardBranchToNextInstruction(loopStmt.shouldPointToEnd);
	}

	@Override
	public void exitVisit(ExitStmt exitStmt) {
		if (exitStmt.getExpn() == null) {
			// if exitStmt is unconditional exit, push True which is later
			// negated, so we always branch on the branch-false
			instrs.emitPushValue(Machine.MACHINE_TRUE);
		}

		// exit statement in form "exit when expr is true", but we only have
		// a branch-on-false, and unconditional branch. So we can just use
		// the branch-on-false after negating the expressing, to act as a
		// branch-on-true
		instrs.emitNot();

		// save addr, so we can patch to end of loop later
		short addr = instrs.emitBranchIfFalseToUnknown();

		LoopingStmt containingLoop = exitStmt.getContainingLoop();
		// shouldn't be null if it passed semantic analysis
		assert(containingLoop != null);

		// add addr to list of addrs to patch
		containingLoop.shouldPointToEnd.add(addr);
	}

	@Override
	public void exitVisit(EqualsExpn equalsExpn) {
		instrs.emitEquals();

		if (equalsExpn.getOpSymbol() == EqualsExpn.OP_NOT_EQUAL) {
			instrs.emitNot();
		}
	}

	@Override
	public void enterVisit(WhileDoStmt whileDoStmt) {
		// expn will be start to be evaluated in next instruction,
		// so save addr as start of loop
		whileDoStmt.startOfLoop = instrs.getNextInstructionAddr();
	}

	@Override
	public void enterVisitAfterWhileExpn(WhileDoStmt whileDoStmt) {
		// expn will have been evaluated at this point,
		// save branch-to address for later patching
		short addr = instrs.emitBranchIfFalseToUnknown();
		whileDoStmt.shouldPointToEnd.add(addr);
	}

	@Override
	public void exitVisit(WhileDoStmt whileDoStmt) {
		// branch to start of loop
		instrs.emitPushValue(whileDoStmt.startOfLoop);
		instrs.emitBranch();

		// next instruction is end of loop, so patch everything that needs to point there
		instrs.patchForwardBranchToNextInstruction(whileDoStmt.shouldPointToEnd);
	}
	
	@Override
	public void enterVisit(AnonFuncExpn anonFuncExpn) {
		// Branch past declaration
		anonFuncExpn.shouldPointToAfterDecl = instrs.emitBranchToUnknown();
		
		// Entrance code
		instrs.emitRoutineEntranceCode(anonFuncExpn.getSTScope());
	}
	
	@Override
	public void enterVisitYieldExpn(AnonFuncExpn anonFuncExpn) {
		// Before evaluating the return expression, place the designated "return value" 
		// address onto the stack, to prepare for moving the return value into it
		instrs.emitGetAddr(anonFuncExpn.getContainingSTScope().getLexicalLevel(), 0);
	}
	@Override
	public void exitVisit(AnonFuncExpn anonFuncExpn) {
		
		// At this point, the return value is at the top of the stack.
		// Move it to the "return value" memory location in the anon func's
		// activation record.
		instrs.emitStore();
		
		// Routine epilogue (immediately after routine body, so no branching needed)
		instrs.emitActivationRecordCleanUp();
		instrs.emitRestoreDisplay(anonFuncExpn);
		instrs.emitBranch();
		instrs.patchForwardBranchToNextInstruction(anonFuncExpn.shouldPointToAfterDecl);
		
		// Now, call the function that was just declared
		STScope callerScope = anonFuncExpn.getParentNode().getContainingSTScope();
		STScope calleeScope = anonFuncExpn.getSTScope();
		
		// Emit function call setup
		anonFuncExpn.shouldPointToAfterCall = instrs.emitRoutineCallSetup(callerScope, calleeScope);
		
		// Emit function call branch
		instrs.emitFunctionCallBranch(calleeScope, anonFuncExpn.shouldPointToAfterCall);
	}
	
	@Override 
	public void enterVisit(FunctionCallExpn functionCallExpn) {
		// Get the declaration's symbol table entry
		STScope callerScope = functionCallExpn.getContainingSTScope();
		String ident = functionCallExpn.getIdent();
		SymbolTableEntry ste = symbolTable.searchGlobalFrom(ident, callerScope);
		STScope calleeScope = ((RoutineDecl)ste.getNode()).getSTScope();

		// Emit function call setup
		functionCallExpn.shouldPointToAfterBranch = instrs.emitRoutineCallSetup(callerScope, calleeScope);
	}
	
	@Override
	public void exitVisit(FunctionCallExpn functionCallExpn) {
		// Get the declaration's symbol table entry
		STScope callerScope = functionCallExpn.getContainingSTScope();
		String ident = functionCallExpn.getIdent();
		SymbolTableEntry ste = symbolTable.searchGlobalFrom(ident, callerScope);
		STScope calleeScope = ((RoutineDecl)ste.getNode()).getSTScope();
		
		// Emit function call branch
		instrs.emitFunctionCallBranch(calleeScope, functionCallExpn.shouldPointToAfterBranch);
	}
	
	@Override
	public void enterVisit(ProcedureCallStmt callStmt) {
		// Get the declaration's symbol table entry
		STScope callerScope = callStmt.getContainingSTScope();
		String ident = callStmt.getName();
		SymbolTableEntry ste = symbolTable.searchGlobalFrom(ident, callerScope);
		STScope calleeScope = ((RoutineDecl)ste.getNode()).getSTScope();

		// Emit procedure call setup
		callStmt.shouldPointToAfterBranch = instrs.emitRoutineCallSetup(callerScope, calleeScope);
	}

	@Override
	public void exitVisit(ProcedureCallStmt callStmt) {
		// Get the declaration's symbol table entry
		STScope callerScope = callStmt.getContainingSTScope();
		String ident = callStmt.getName();
		SymbolTableEntry ste = symbolTable.searchGlobalFrom(ident, callerScope);
		STScope calleeScope = ((RoutineDecl)ste.getNode()).getSTScope();

		// Emit procedure call branch
		instrs.emitProcedureCallBranch(calleeScope, callStmt.shouldPointToAfterBranch);
	}

	@Override
	public void enterVisit(RoutineDecl routineDecl) {
		// Branch past declaration
		routineDecl.shouldPointToEnd = instrs.emitBranchToUnknown();

		// Entrance code
		instrs.emitRoutineEntranceCode(routineDecl.getSTScope());
	}

	@Override
	public void exitVisit(RoutineDecl routineDecl) {
		// Make return statements point here
		instrs.patchReturnStmtBranches(routineDecl);

		// Routine epilogue
		instrs.emitActivationRecordCleanUp();
		instrs.emitRestoreDisplay(routineDecl);
		instrs.emitBranch();
		instrs.patchForwardBranchToNextInstruction(routineDecl.shouldPointToEnd);
	}

	@Override
	public void exitVisit(CompareExpn compareExpn) {
		/*
		 * Note: LESS/GREATER and LESS_EQUAL/GREATER_EQUAL
		 * are differentiated by the order that the left and right
		 * operands are processed.
		 */
		String op = compareExpn.getOpSymbol();
		switch (op) {
			case CompareExpn.OP_LESS:
			case CompareExpn.OP_GREATER:
				instrs.emitLessThan();
				break;
			case CompareExpn.OP_LESS_EQUAL:
			case CompareExpn.OP_GREATER_EQUAL:
				instrs.emitLessThan();
				instrs.emitNot();
				break;
			default:
				String msg = "Unknown CompareExpn operation: " + op;
				throw new UnsupportedOperationException(msg);
		}
	}
}
