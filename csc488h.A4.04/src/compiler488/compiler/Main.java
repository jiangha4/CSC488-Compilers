package compiler488.compiler;

import java.io.*;
import java.util.*;
import java.nio.charset.*;

import compiler488.parser.*;
import compiler488.ast.AST ;
import compiler488.ast.ASTVisitor;
import compiler488.ast.BasePrettyPrinter;
import compiler488.ast.stmt.Program;
import compiler488.semantics.SemanticErrorException;
import compiler488.semantics.Semantics;
import compiler488.symbol.SymbolTable;
import compiler488.codegen.CodeGen;
import compiler488.runtime.*;

public class Main {

  private Main() { }

  public static boolean supressExecution = false;
  public static boolean traceCodeGen = false;
  public static boolean traceExecution = false;
  public static PrintStream traceStream = null;
  public static boolean dumpCode = false;
  public static PrintStream dumpStream = null;
  private static String inputMachineCode;

  public static boolean isInteger(String s) {
	try {
		Short.parseShort(s);
	} catch(NumberFormatException e) {
		return false;
	}
	// only got here if we didn't return false
	return true;
  }

  public static void main(String argv[])
		throws MemoryAddressException, ExecutionException, FileNotFoundException, IOException
	{
		Machine.powerOn();

		String line;
		InputStream fis = new FileInputStream(argv[0]);
		InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
		BufferedReader br = new BufferedReader(isr);
		Short i = 0;
		while ((line = br.readLine()) != null) {
			line = line.trim();
			if (isInteger(line)) {
				Short val = Short.parseShort(line);
				Machine.writeMemory(i, val);
			} else {
				Short instruction = (short)Arrays.asList(Machine.instructionNames).indexOf(line);
				Machine.writeMemory(i, instruction);
			}
			i++;
		}

		Machine.setMSP((short)i);
		Machine.setPC((short)1);
		Machine.setMLP((short)(Machine.memorySize - 1));

		Machine.run();
	}
}
