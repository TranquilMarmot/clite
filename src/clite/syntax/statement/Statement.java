package clite.syntax.statement;

/**
 * Statement = Skip | Block | Assignment | Conditional | Loop
 */
public interface Statement {
	/**
	 * Prints out what this statement holds
	 * @param indent Level of indentation to print at
	 */
	public void display(int indent);
}
