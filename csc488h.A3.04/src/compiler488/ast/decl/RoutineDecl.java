package compiler488.ast.decl;

import compiler488.ast.ASTList;
import compiler488.ast.ASTVisitor;
import compiler488.ast.PrettyPrinter;
import compiler488.ast.SourceCoord;
import compiler488.ast.stmt.Scope;
import compiler488.ast.type.Type;

/**
 * Represents the declaration of a function or procedure.
 */
public class RoutineDecl extends Declaration {
    /** The formal parameters for this routine (if any.)
     *
     * <p>This value must be non-<code>null</code>. If absent, use an empty
     * list instead.</p>
     */
    private ASTList<ScalarDecl> parameters =  new ASTList<ScalarDecl>();

    /** The body of this routine (if any.) */
    private Scope body = null;

    /**
     * Construct a function with parameters, and a definition of the body.
     *   @param  name	      Name of the routine
     *   @param  type	      Type returned by the function
     *   @param  parameters   List of parameters to the routine
     *   @param  body	      Body scope for the routine
     */
    public RoutineDecl(String name, Type type, ASTList<ScalarDecl> parameters, Scope body, SourceCoord sourceCoord) {
        super(name, type, sourceCoord);

        this.parameters = parameters;
        this.body = body;
    }

    /**
     * Construct a function with no parameters, and a definition of the body.
     *   @param  name	      Name of the routine
     *   @param  type	      Type returned by the function
     *   @param  body	      Body scope for the routine
     */
    public RoutineDecl(String name, Type type, Scope body, SourceCoord sourceCoord) {
        this(name, type, new ASTList<ScalarDecl>(), body, sourceCoord);
    }

    /**
     * Construct a procedure with parameters, and a definition of the body.
     *   @param  name	      Name of the routine
     *   @param  parameters   List of parameters to the routine
     *   @param  body	      Body scope for the routine
     */
    public RoutineDecl(String name, ASTList<ScalarDecl> parameters, Scope body, SourceCoord sourceCoord) {
        this(name, null, parameters, body, sourceCoord);

        this.parameters = parameters;
        this.body = body;
    }

    /**
     * Construct a procedure with no parameters, and a definition of the body.
     *   @param  name	      Name of the routine
     *   @param  body	      Body scope for the routine
     */
    public RoutineDecl(String name, Scope body, SourceCoord sourceCoord) {
        this(name, null, new ASTList<ScalarDecl>(), body, sourceCoord);
    }

    public ASTList<ScalarDecl> getParameters() {
        return parameters;
    }

    public Scope getBody() {
        return body;
    }

    @Override
    public void prettyPrint(PrettyPrinter p) {
        if (type == null) {
            p.print("procedure ");
        } else {
            type.prettyPrint(p);
            p.print(" function ");
        }

        p.print(name);

        if (parameters.size() > 0) {
            p.print("(");
            parameters.prettyPrintCommas(p);
            p.print(")");
        }

        if (body != null) {
            p.print(" ");
            body.prettyPrint(p);
        }
    }
    
    @Override
	public void accept(ASTVisitor visitor) {
    	
    	// S11/S12: Declare function with/without parameters and with specified type
    	// S17/S18: Declare procedure with/without parameters
    	// S04/S08: Start function/procedure scope
		visitor.visit(this);
		
		if (type != null) {
			type.accept(visitor);
		}
		if (parameters.size() > 0) {
			parameters.setParentNode(this);
			parameters.setParentAttribute(attribute.METHOD);
			parameters.accept(visitor);
		}
		if (body != null) {
			body.getBody().setParentNode(this);
			body.getBody().setParentAttribute(attribute.METHOD);
			body.accept(visitor);
		}
		
		// S05/S09: End function/procedure scope
		visitor.visit(this);
	}
}
