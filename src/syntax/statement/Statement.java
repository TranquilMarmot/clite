package syntax.statement;

/**
 * Statement = Skip | Block | Assignment | Conditional | Loop
 */
public abstract class Statement {
	public abstract void display(int indent);
}
