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
	private static int BLOCK_MARK_SIZE = 2;

	/* STScope, used to look up var offset */
	private STScope scope;

	/* Sizes (in words) of the amount of storage required for local storage
	 * of variables and parameters respectively */
	private short memSizeForVariables;
	private short memSizeForParameters;

	/*
	 * Constructor.
	 */
	public ActivationRecord(STScope stScope)
	{
		this.scope = stScope;
		this.memSizeForParameters = getMemSizeForKind(SymbolKind.PARAMETER);
		this.memSizeForVariables = getMemSizeForKinds(SymbolKind.VARIABLE, SymbolKind.ARRAY);
	}

	/*
	 * Return the lexical level of the associated scope.
	 */
	public short getLexicalLevel() {
		return scope.getLexicalLevel();
	}

	/*
	 * Return whether or not we need to allocate storage for the return value.
	 */
	public boolean hasReturnValue() {
		return scopeIsKind(ScopeKind.FUNCTION);
	}

	/*
	 * Return the number of words we need to allocate for the local storage
	 * in the activation record.
	 */
	public int getNumWordsToAllocateForBlockMark() {
		return BLOCK_MARK_SIZE;
	}

	/*
	 * Return the number of words we need to allocate for the local storage
	 * in the activation record.
	 */
	public int getNumWordsToAllocateForLocalStorage() {
		return memSizeForVariables + memSizeForParameters;
	}

	/*
	* Return the number of pops required to clean up all ofthis activation
	* record except for the return address and return value if appropriate.
	*/
	public int getNumWordsToPopForCleanUp() {
		// Local storage
		int numRequired = memSizeForVariables;
		numRequired += memSizeForParameters;

		// Block mark
		numRequired += BLOCK_MARK_SIZE;

		return numRequired;
	}

	/*
	 * Get the offset within the record to the return address.
	 */
	public short getOffsetToReturnAddress() {
		// Functions have a return value first
		short offset = 0;
		if (hasReturnValue()) {
			offset += 1;
		}

		return offset;
	}

	/*
	 * Get the offset within the record to the dynamic link addr.
	 */
	public short getOffsetToDynamicLinkAddr() {
		return (short)(getOffsetToReturnAddress() + 1);
	}

	/*
	 * Get the offset within the record to the saved display value.
	 */
	public short getOffsetToSavedDisplayValue() {
		return (short)(getOffsetToDynamicLinkAddr() + 1);
	}

	/*
	 * Get the offset within the record to the local parameter storage.
	 */
	public short getOffsetToParameterStorage() {
		return (short)(getOffsetToSavedDisplayValue() + 1);
	}

	/*
	 * Get the offset within the record to the local variable storage.
	 */
	public short getOffsetToVariableStorage() {
		return (short)(getOffsetToParameterStorage() + memSizeForParameters);
	}

	/*
	 * Return the amount of memory (in words) required for local storage of
	 * all variables in this scope.
	 */
	public int getMemSizeForVariables() {
		return memSizeForVariables;
	}

	/*
	 * Return the amount of memory (in words) required for local storage of
	 * all parameters for this function/procedure.
	 */
	public int getMemSizeForParameters() {
		return memSizeForParameters;
	}

	private boolean scopeIsKind(ScopeKind kind) {
		return kind == this.scope.getScopeKind();
	}

	private short getMemSizeForKind(SymbolKind kind) {
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

		return numOfKind;
	}

	private short getMemSizeForKinds(SymbolKind kind1, SymbolKind kind2) {
		return (short)(getMemSizeForKind(kind1) + getMemSizeForKind(kind2));
	}
}
