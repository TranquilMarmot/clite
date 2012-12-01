package abstractsyntax.expression;

import abstractsyntax.Operator;

/**
 * Unary = Operator op; Expression term
 */
public class Unary extends Expression {
	public Operator op;
	public Expression term;

	public Unary(Operator o, Expression e) {
		op = o;
		term = e;
	} // unary

}
