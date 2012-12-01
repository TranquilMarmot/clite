package syntax.expression;

/**
 * Expression = Variable | Value | Binary | Unary
 */
public abstract class Expression {
	public abstract void display(int indent);
}
