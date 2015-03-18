package compiler488.codegen;

import java.util.*;
import compiler488.compiler.Main;
import compiler488.runtime.Machine;

public class CodeGenCollector {
	/** flag for tracing code generation */
	private boolean trace = Main.traceCodeGen;

	/** emitted instructions **/
	private List<Short> instrs;

	public CodeGenCollector() {
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
				params += " " + instrs.get(i+j).toString();
			}
			i += opLength;

			System.out.println(String.format("%05d", i+1) + "\t" + opName + params);
		}

		System.out.println("=======================");
		System.out.println();
	}
}
