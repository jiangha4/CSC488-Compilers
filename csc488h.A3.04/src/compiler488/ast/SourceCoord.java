package compiler488.ast;

public class SourceCoord {

	private int line;
	private int col;
	
	public SourceCoord(int line, int col) {
		// The "line" argument coming in from the parser is zero-indexed -> convert to one-indexed for user-friendly line numbers
		this.line = line + 1;
		
		// The "col" argument coming in from the parser is zero-indexed -> convert to one-indexed for user-friendly col numbers
		this.col = col + 1; 
	}
	
	/**
	 * 
	 * @return int : the line number in the source program
	 */
	public int getLine() {
		return line;
	}
	
	/**
	 * 
	 * @return int : the column number in the source program
	 */
	public int getCol() {
		return col;
	}
	
	/**
	 * 
	 * @param line : the line number in the source program
	 */
	public void setLine(int line) {
		this.line = line;
	}
	
	/**
	 * 
	 * @param col : the column number in the source program
	 */
	public void setCol(int col) {
		this.col = col;
	}
	
	@Override
	public String toString() {
		String s = "Line " + line + ", col " + col;
		return s;
	}
}
