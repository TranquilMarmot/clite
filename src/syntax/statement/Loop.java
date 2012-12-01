package syntax.statement;

import syntax.expression.Expression;


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
	
	public void display(int indent){
		for(int i = 0; i < indent; i++)
			System.out.print("   ");
		System.out.println("|While: ");
		
		test.display(indent);
		
		body.display(indent);
	}
}
