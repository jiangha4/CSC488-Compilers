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

/** enterVisitor pattern for AST traversal: semantic checker is a enterVisitor who can enterVisit every node in the AST.
 *  @author  <B> Haohan Jiang (g3jiangh)
 *               Maria Yancheva (c2yanche)
 *               Timo Vink (c4vinkti)
 *               Chandeep Singh (g2singh)
 *           </B>
 */

public abstract class BaseASTVisitor implements ASTVisitor {
	public void enterVisit(BaseAST baseAST) {};
	public void exitVisit(BaseAST baseAST) {};

	/* Declarations */
	public void enterVisit(ArrayDeclPart arrayDeclPart) {};
	public void exitVisit(ArrayDeclPart arrayDeclPart) {};
	public void enterVisit(MultiDeclarations multiDeclarations) {};
	public void exitVisit(MultiDeclarations multiDeclarations) {};
	public void enterVisit(RoutineDecl routineDecl) {};
	public void exitVisit(RoutineDecl routineDecl) {};
	public void enterVisit(ScalarDecl scalarDecl) {};
	public void exitVisit(ScalarDecl scalarDecl) {};

	/* Expressions */
	public void enterVisit(AnonFuncExpn anonFuncExpn) {};
	public void exitVisit(AnonFuncExpn anonFuncExpn) {};
	public void enterVisit(ArithExpn arithExpn) {};
	public void exitVisit(ArithExpn arithExpn) {};
	public void enterVisit(BoolConstExpn boolConstExpn) {};
	public void exitVisit(BoolConstExpn boolConstExpn) {};
	public void enterVisit(BoolExpn boolExpn) {};
	public void exitVisitLHS(BoolExpn boolExpn) {};
	public void exitVisit(BoolExpn boolExpn) {};
	public void enterVisit(CompareExpn compareExpn) {};
	public void exitVisit(CompareExpn compareExpn) {};
	public void enterVisit(ConstExpn constExpn) {};
	public void exitVisit(ConstExpn constExpn) {};
	public void enterVisit(EqualsExpn equalsExpn) {};
	public void exitVisit(EqualsExpn equalsExpn) {};
	public void enterVisit(FunctionCallExpn functionCallExpn) {};
	public void exitVisit(FunctionCallExpn functionCallExpn) {};
	public void enterVisit(IdentExpn identExpn) {};
	public void exitVisit(IdentExpn identExpn) {};
	public void enterVisit(IntConstExpn intConstExpn) {};
	public void exitVisit(IntConstExpn intConstExpn) {};
	public void enterVisit(NotExpn notExpn) {};
	public void exitVisit(NotExpn notExpn) {};
	public void enterVisit(SkipConstExpn skipConstExpn) {};
	public void exitVisit(SkipConstExpn skipConstExpn) {};
	public void enterVisit(SubsExpn subsExpn) {};
	public void exitVisit(SubsExpn subsExpn) {};
	public void enterVisit(TextConstExpn textConstExpn) {};
	public void exitVisit(TextConstExpn textConstExpn) {};
	public void enterVisit(UnaryMinusExpn unaryMinusExpn) {};
	public void exitVisit(UnaryMinusExpn unaryMinusExpn) {};

	/* Statements */
	public void enterVisit(AssignStmt assignStmt) {};
	public void exitVisitLHS(AssignStmt assignStmt) {};
	public void exitVisit(AssignStmt assignStmt) {};
	public void enterVisit(ExitStmt exitStmt) {};
	public void exitVisit(ExitStmt exitStmt) {};
	public void enterVisit(GetStmt getStmt) {};
	public void exitVisit(GetStmt getStmt) {};
	public void enterVisit(IfStmt ifStmt) {};
	public void exitVisitCondition(IfStmt ifStmt) {};
	public void exitVisitWhenTrue(IfStmt ifStmt) {};
	public void exitVisit(IfStmt ifStmt) {};
	public void enterVisit(LoopStmt loopStmt) {};
	public void exitVisit(LoopStmt loopStmt) {};
	public void enterVisit(ProcedureCallStmt procedureCallStmt) {};
	public void exitVisit(ProcedureCallStmt procedureCallStmt) {};
	public void enterVisit(Program program) {};
	public void exitVisit(Program program) {};
	public void enterVisit(PutStmt putStmt) {};
	public void exitVisitPutExpn(Expn putStmtChild) {};
	public void exitVisit(PutStmt putStmt) {};
	public void enterVisit(ReturnStmt returnStmt) {};
	public void exitVisit(ReturnStmt returnStmt) {};
	public void enterVisit(Scope scope) {};
	public void exitVisit(Scope scope) {};
	public void enterVisit(WhileDoStmt whileDoStmt) {};
	public void exitVisit(WhileDoStmt whileDoStmt) {};
}
