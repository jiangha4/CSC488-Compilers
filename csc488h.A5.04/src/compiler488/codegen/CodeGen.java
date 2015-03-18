package compiler488.codegen;

import java.io.*;
import java.util.*;
import compiler488.ast.*;
import compiler488.ast.decl.*;
import compiler488.ast.expn.*;
import compiler488.ast.stmt.*;
import compiler488.ast.type.*;
import compiler488.compiler.*;
import compiler488.runtime.*;

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

	/**
	 * Constructor to initialize code generation
	 */
	public CodeGen()
	{
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
		short counter = 1;
		List<Short> instructions = instrs.getInstructions();
		Machine.writeMemory((short)0, Machine.HALT);
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
		instrs.emitProgramActivationRecord(program);
	}

	@Override
	public void exitVisit(Program program)
	{
		instrs.emitCleanProgramActivationRecord(program);
		instrs.emitBranch();
	}

	@Override
	public void exitVisit(TextConstExpn textConstExpn)
	{
		instrs.emitPrintText(textConstExpn.getValue());
	}

	@Override
	public void exitVisit(SkipConstExpn skipConstExpn)
	{
		instrs.emitPrintSkip();
	}
}
