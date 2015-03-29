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
import compiler488.symbol.*;
import compiler488.symbol.SymbolTable.*;
import compiler488.symbol.STScope.ScopeKind;

/*
 * Activation record.
 */
public class ActivationRecord
{
	/*
	 * Return the number of words we need to allocate for the local storage
	 * in the activation record.
	 */
	public static short getNumWordsToAllocateForVariables(STScope stScope) {
		return getMemSizeForKinds(stScope, SymbolKind.VARIABLE, SymbolKind.ARRAY);
	}

	/*
	 * Return the number of words we need to allocate for the local storage
	 * in the activation record.
	 */
	public static short getNumWordsToAllocateForParameters(STScope stScope) {
		return getMemSizeForKind(stScope, SymbolKind.PARAMETER);
	}

	/*
	 * Return the number of pops required to clean up all ofthis activation
	 * record except for the return address and return value if appropriate.
	 */
	public static short getNumWordsToPopForCleanUp(STScope stScope) {
		// Local storage
		short numRequired = getNumWordsToAllocateForVariables(stScope);
		numRequired += getNumWordsToAllocateForParameters(stScope);

		// Saved display
		numRequired += 1;

		return numRequired;
	}

	/*
	 * Get the offset within the record to the return address.
	 */
	public static short getOffsetToReturnAddress() {
		// Functions have a return value first
		return 1;
	}

	/*
	 * Get the offset within the record to the dynamic link addr.
	 */
	public static short getOffsetToDynamicLinkAddr() {
		return (short)(getOffsetToReturnAddress() + 1);
	}

	/*
	 * Get the offset within the record to the saved display value.
	 */
	public static short getOffsetToSavedDisplayValue() {
		return (short)(getOffsetToDynamicLinkAddr() + 1);
	}

	/*
	 * Get the offset within the record to the local parameter storage.
	 */
	public static short getOffsetToParameterStorage() {
		return (short)(getOffsetToSavedDisplayValue() + 1);
	}

	/*
	 * Get the offset within the record to the local variable storage.
	 */
	public static short getOffsetToVariableStorage(STScope stScope) {
		short offset = getOffsetToParameterStorage();
		offset += getNumWordsToAllocateForParameters(stScope);
		return offset;
	}

	private static short getMemSizeForKind(STScope scope, SymbolKind kind) {
		short numOfKind = 0;
		for (SymbolTableEntry ste : scope.getSymbols().values()) {
			if (ste.getKind() == kind) {
				if (ste.getKind() == SymbolKind.ARRAY) {
					numOfKind += ((ArrayDeclPart)ste.getNode()).getSize();
				} else {
					numOfKind += 1;
				}
			}
		}

		for (STScope child : scope.getChildren()) {
			if (child.getScopeKind() == ScopeKind.NORMAL) {
				numOfKind += getMemSizeForKind(child, kind);
			}
		}

		return numOfKind;
	}

	private static short getMemSizeForKinds(STScope scope, SymbolKind kind1, SymbolKind kind2) {
		return (short)(getMemSizeForKind(scope, kind1) + getMemSizeForKind(scope, kind2));
	}
}
