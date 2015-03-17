package compiler488.semantics;

import compiler488.ast.SourceCoord;
import java.util.ArrayList;
import java.util.List;

public class SemanticErrorCollector {

	List<String> errors;

	public SemanticErrorCollector() {
		errors = new ArrayList<String>();
	}

	public void add(SourceCoord location, String error) {
		errors.add(location + ": " + error);
	}

	public boolean any() {
		return errors.size() > 0;
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
