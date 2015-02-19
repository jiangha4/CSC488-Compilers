package compiler488.ast;

import compiler488.ast.decl.*;
import compiler488.ast.expn.*;
import compiler488.ast.stmt.*;
import compiler488.ast.type.*;

public interface ASTVisitor {
	/* Declarations */
	public void visit(ArrayDeclPart arrayDeclPart);
	public void visit(Declaration declaration);
	public void visit(DeclarationPart declarationPart);
	public void visit(MultiDeclarations multiDeclarations);
	public void visit(RoutineDecl routineDecl);
	public void visit(ScalarDecl scalarDecl);
	public void visit(ScalarDeclPart scalarDeclPart);
	
	/* Expressions */
	public void visit(AnonFuncExpn anonFuncExpn);
	public void visit(ArithExpn arithExpn);
	public void visit(BinaryExpn binaryExpn);
	public void visit(BoolConstExpn boolConstExpn);
	public void visit(BoolExpn boolExpn);
	public void visit(CompareExpn compareExpn);
	public void visit(ConstExpn constExpn);
	public void visit(EqualsExpn equalsExpn);
	public void visit(Expn expn);
	public void visit(FunctionCallExpn functionCallExpn);
	public void visit(IdentExpn identExpn);
	public void visit(IntConstExpn intConstExpn);
	public void visit(NotExpn notExpn);
	public void visit(SkipConstExpn skipConstExpn);
	public void visit(SubsExpn subsExpn);
	public void visit(TextConstExpn textConstExpn);
	public void visit(UnaryExpn unaryExpn);
	public void visit(UnaryMinusExpn unaryMinusExpn);
	
	/* Statements */
	public void visit(AssignStmt assignStmt);
	public void visit(ExitStmt exitStmt);
	public void visit(GetStmt getStmt);
	public void visit(IfStmt ifStmt);
	public void visit(LoopingStmt loopingStmt);
	public void visit(LoopStmt loopStmt);
	public void visit(ProcedureCallStmt procedureCallStmt);
	public void visit(Program program);
	public void visit(PutStmt putStmt);
	public void visit(ReturnStmt returnStmt);
	public void visit(Scope scope);
	public void visit(Stmt stmt);
	public void visit(WhileDoStmt whileDoStmt);
	
	/* Type */
	public void visit(BooleanType booleanType);
	public void visit(IntegerType integerType);
	public void visit(Type type);
}
