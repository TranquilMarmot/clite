package clite.syntax.statement;

import clite.syntax.expression.Expression;
import clite.syntax.expression.Variable;

/**
 * Assignment = Variable target; Expression source
 */
public class Assignment implements Statement {
	/** Target for assignment */
	private Variable target;
	/** What to assign */
	private Expression source;

	/**
	 * @param target Target for assignment
	 * @param source What to assign to target
	 */
	public Assignment(Variable target, Expression source) {
		this.target = target;
		this.source = source;
	}
	
	/** @return Target for assignment */
	public Variable target(){ return target; }
	
	/** @return WHat's going to be assigned to the target */
	public Expression source(){ return source; }
	
	@Override
	public void display(int indent){
		for(int i = 0; i < indent; i++)
			System.out.print("   ");
		System.out.println("|" + target.toString() + " = ");
		
		source.display(indent + 1);
	}
}