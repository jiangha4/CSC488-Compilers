package compiler488.codegen;

import java.util.*;
import compiler488.ast.*;
import compiler488.ast.decl.*;
import compiler488.ast.expn.*;
import compiler488.ast.stmt.*;
import compiler488.ast.type.*;
import compiler488.compiler.Main;
import compiler488.runtime.Machine;;
import compiler488.symbol.STScope;

public class CodeGenHelper {
	/** Sizes of control blocks of activation records **/
	private static int FUNC_AR_CTRLBLOCK_SIZE = 4;
	private static int PROC_AR_CTRLBLOCK_SIZE = 3;

	/** flag for tracing code generation */
	private boolean trace = Main.traceCodeGen;

	/** emitted instructions **/
	private List<Short> instrs;

	public CodeGenHelper() {
		instrs = new ArrayList<Short>();
	}

	public List<Short> getInstructions() {
		return instrs;
	}

	public void printDebug()
	{
		System.out.println();
		System.out.println("=======================");
		System.out.println("    Instrs Emitted:    ");
		System.out.println("=======================");

		int i = 0;
		int numInstrs = instrs.size();
		while (i < numInstrs) {
			// Get operation name
			int opCode = instrs.get(i);
			String opName = Machine.instructionNames[opCode];

			// Get parameters
			int opLength = Math.max(1, Machine.instructionLength[opCode]);
			String params = "";
			for (int j = 1; j < opLength; j++) {
				Short param = instrs.get(i+j);
				String paramStr = (param == Machine.UNDEFINED) ? "UNDEFINED" : param.toString();
				params += "\t" + paramStr;
			}

			System.out.println(String.format("%05d", i+1) + "\t" + opName + params);
			i += opLength;
		}

		System.out.println("=======================");
		System.out.println();
	}

	public void emitPush(short value) {
		instrs.add(Machine.PUSH);
		instrs.add(value);
	}

	public void emitPush(int value) {
		emitPush((short)value);
	}

	public void emitPushN(int value, int numDuplicates) {
		if (numDuplicates < 0) {
			throw new IllegalArgumentException("numDuplicates must be >= 0");
		}

		switch (numDuplicates) {
			case 0:
				break;
			case 1:
				emitPush(value);
				break;
			case 2:
				emitPush(value);
				instrs.add(Machine.DUP);
				break;

			default:
				emitPush(value);
				emitPush(numDuplicates);
				instrs.add(Machine.DUPN);
		}
	}

	public void emitPop(int numToPop) {
		if (numToPop < 0) {
			throw new IllegalArgumentException("numToPop must be >= 0");
		}

		switch (numToPop) {
			case 0:
				break;
			case 1:
				instrs.add(Machine.POP);
				break;
			case 2:
				instrs.add(Machine.POP);
				instrs.add(Machine.POP);
				break;

			default:
				emitPush(numToPop);
				instrs.add(Machine.POPN);
		}
	}

	public void emitPop() {
		emitPop(1);
	}

	public void emitBranch() {
		instrs.add(Machine.BR);
	}

	public void emitProgramActivationRecord(Program program) {
		// Return address
		emitPush(0);

		// Space for control block (minus return address) and local vars
		int varMemSize = program.getSTScope().getVariableMemSize();
		int spaceRequired = (PROC_AR_CTRLBLOCK_SIZE - 1) + varMemSize;
		emitPushN(Machine.UNDEFINED, spaceRequired);
	}

	public void emitCleanProgramActivationRecord(Program program) {
		// Count how much memory needs to be cleaned up. This is all the local
		// storage, and the control block minus the return addr;
		int varMemSize = program.getSTScope().getVariableMemSize();
		int numToPop = varMemSize + (PROC_AR_CTRLBLOCK_SIZE - 1);

		// Pop this memory
		emitPop(numToPop);
	}
}
