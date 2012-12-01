package abstractsyntax.statement;

import abstractsyntax.Skip;
import abstractsyntax.expression.Expression;

/**
 *  Conditional = Expression test; Statement thenbranch, elsebranch
 */
public class Conditional extends Statement {
	public Expression test;
	public Statement thenbranch;
	public Statement elsebranch;

	// elsebranch == null means "if... then"

	public Conditional(Expression test, Statement thenbranch) {
		this(test, thenbranch, null);
	}

	public Conditional(Expression test, Statement thenbranch, Statement elsebranch) {
		this.test = test;
		this.thenbranch = thenbranch;
		
		if(elsebranch == null)
			elsebranch = new Skip();
		else
			this.elsebranch = elsebranch;
	}
}
