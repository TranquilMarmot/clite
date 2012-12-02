package syntax.statement;

import syntax.expression.Expression;
import syntax.expression.Variable;

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
	
	@Override
	public void display(int indent){
		for(int i = 0; i < indent; i++)
			System.out.print("   ");
		System.out.println("|" + target.toString() + " = ");
		
		source.display(indent + 1);
	}
}