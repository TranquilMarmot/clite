package abstractsyntax.expression;

import abstractsyntax.Operator;


/**
 * Binary = Operator op; Expression term1, term2
 */
public class Binary extends Expression {
	public Operator op;
	public Expression term1, term2;

	public Binary(Operator o, Expression l, Expression r) {
		op = o;
		term1 = l;
		term2 = r;
	} // binary

}
