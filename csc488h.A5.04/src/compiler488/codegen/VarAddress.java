package compiler488.codegen;

/*
 * Information to find the location in memory where a given variable is stored.
 */
public class VarAddress
{
	private short lexicalLevel;
	private short orderNumber;

	public VarAddress(short lexicalLevel, short orderNumber)
	{
		this.lexicalLevel = lexicalLevel;
		this.orderNumber = orderNumber;
	}

	public short getLexicalLevel() {
		return lexicalLevel;
	}

	public short getOrderNumber() {
		return orderNumber;
	}
}
