package syntax.expression;

/**
 * Expression = Variable | Value | Binary | Unary
 */
public abstract class Expression {
	/**
	 * Prints out what this expression holds
	 * @param indent Level of indentation to print at
	 */
	public abstract void display(int indent);
}
