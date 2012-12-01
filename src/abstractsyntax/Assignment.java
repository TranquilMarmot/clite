package abstractsyntax;

import abstractsyntax.expression.Expression;
import abstractsyntax.expression.Variable;
import abstractsyntax.statement.Statement;

/**
 * Assignment = Variable target; Expression source
 */
public class Assignment extends Statement {
	public Variable target;
	public Expression source;

	public Assignment(Variable target, Expression source) {
		this.target = target;
		this.source = source;
	}

}