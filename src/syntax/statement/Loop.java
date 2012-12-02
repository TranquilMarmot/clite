package syntax.statement;

import syntax.expression.Expression;


/**
 * While Loop = Expression test; Statement body
 */
public class Loop extends Statement {
	/** Test for loop */
	private Expression test;
	/** Body of loop */
	private Statement body;

	/**
	 * Create new while loop
	 * @param test Test for loop
	 * @param body Statements executed each time through loop
	 */
	public Loop(Expression test, Statement body) {
		this.test = test;
		this.body = body;
	}
	
	/** @return Test performed each iteration of loop */
	public Expression test(){ return test; }
	/** @return Statements executed each iteration of loop */
	public Statement body(){ return body; }
	
	@Override
	public void display(int indent){
		for(int i = 0; i < indent; i++)
			System.out.print("   ");
		System.out.println("|While: ");
		
		test.display(indent);
		
		body.display(indent);
	}
}
