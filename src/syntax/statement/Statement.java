package syntax.statement;

/**
 * Statement = Skip | Block | Assignment | Conditional | Loop
 */
public abstract class Statement {
	/**
	 * Prints out what this statement holds
	 * @param indent Level of indentation to print at
	 */
	public abstract void display(int indent);
}
