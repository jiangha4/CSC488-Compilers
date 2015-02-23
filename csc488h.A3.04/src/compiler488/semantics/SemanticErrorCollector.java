package compiler488.semantics;

import java.util.ArrayList;
import java.util.List;

public class SemanticErrorCollector {

	List<String> errors;
	
	public SemanticErrorCollector() {
		errors = new ArrayList<String>();
	}
	
	public void add(String error) {
		errors.add(error);
	}
	
	public int getCount() {
		return errors.size();
	}
	
	public void raiseException() throws SemanticErrorException {
		StringBuilder sb = new StringBuilder();
		String sep = "\n";
		for (String s : errors) {
			sb.append(sep).append(s);
		}
		
		throw new SemanticErrorException(sb.toString());
	}
}
