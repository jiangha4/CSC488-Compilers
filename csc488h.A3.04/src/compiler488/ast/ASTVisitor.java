package compiler488.ast;

import compiler488.ast.decl.ArrayDeclPart;
import compiler488.ast.decl.Declaration;
import compiler488.ast.decl.DeclarationPart;
import compiler488.ast.decl.MultiDeclarations;
import compiler488.ast.decl.RoutineDecl;
import compiler488.ast.decl.ScalarDecl;
import compiler488.ast.decl.ScalarDeclPart;
import compiler488.ast.expn.AnonFuncExpn;
import compiler488.ast.expn.ArithExpn;
import compiler488.ast.expn.BinaryExpn;
import compiler488.ast.expn.BoolConstExpn;
import compiler488.ast.expn.BoolExpn;
import compiler488.ast.expn.CompareExpn;
import compiler488.ast.expn.ConstExpn;
import compiler488.ast.expn.EqualsExpn;
import compiler488.ast.expn.Expn;
import compiler488.ast.expn.FunctionCallExpn;
import compiler488.ast.expn.IdentExpn;
import compiler488.ast.expn.IntConstExpn;
import compiler488.ast.expn.NotExpn;
import compiler488.ast.expn.SkipConstExpn;
import compiler488.ast.expn.SubsExpn;
import compiler488.ast.expn.TextConstExpn;
import compiler488.ast.expn.UnaryExpn;
import compiler488.ast.expn.UnaryMinusExpn;
import compiler488.ast.stmt.AssignStmt;
import compiler488.ast.stmt.ExitStmt;
import compiler488.ast.stmt.GetStmt;
import compiler488.ast.stmt.IfStmt;
import compiler488.ast.stmt.LoopStmt;
import compiler488.ast.stmt.LoopingStmt;
import compiler488.ast.stmt.ProcedureCallStmt;
import compiler488.ast.stmt.Program;
import compiler488.ast.stmt.PutStmt;
import compiler488.ast.stmt.ReturnStmt;
import compiler488.ast.stmt.Scope;
import compiler488.ast.stmt.Stmt;
import compiler488.ast.stmt.WhileDoStmt;
import compiler488.ast.type.BooleanType;
import compiler488.ast.type.IntegerType;
import compiler488.ast.type.Type;

/** Visitor pattern for AST traversal: semantic checker is a visitor who can visit every node in the AST. 
 *  @author  <B> Haohan Jiang (g3jiangh)
 *               Maria Yancheva (c2yanche)
 *               Timo Vink (c4vinkti)
 *               Chandeep Singh (g2singh)
 *           </B>
 */

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
