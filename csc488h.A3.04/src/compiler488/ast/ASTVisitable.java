package compiler488.ast;

/** Visitor pattern for AST traversal: every node in AST is ASTVisitable.
 *  @author  <B> Haohan Jiang (g3jiangh)
 *               Maria Yancheva (c2yanche)
 *               Timo Vink (c4vinkti)
 *               Chandeep Singh (g2singh)
 *           </B>
 */
public interface ASTVisitable {
	public void accept(ASTVisitor visitor);
}
