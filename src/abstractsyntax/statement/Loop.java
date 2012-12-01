package abstractsyntax.statement;

import abstractsyntax.expression.Expression;


/**
 * Loop = Expression test; Statement body
 */
public class Loop extends Statement {
	public Expression test;
	public Statement body;

	public Loop(Expression test, Statement body) {
		this.test = test;
		this.body = body;
	}

}
