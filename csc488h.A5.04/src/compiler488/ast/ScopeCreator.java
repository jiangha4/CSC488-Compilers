package compiler488.ast;

import compiler488.symbol.STScope;

/** Visitor pattern for AST traversal: every node in AST is ASTVisitable.
 *  @author  <B> Haohan Jiang (g3jiangh)
 *               Maria Yancheva (c2yanche)
 *               Timo Vink (c4vinkti)
 *               Chandeep Singh (g2singh)
 *           </B>
 */
public interface ScopeCreator {
	public void setSTScope(STScope scope);
	public STScope getSTScope();
}
