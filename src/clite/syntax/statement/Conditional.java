package clite.syntax.statement;

import clite.syntax.expression.Expression;

/**
 *  Conditional = Expression test; Statement thenbranch, elsebranch
 */
public class Conditional implements Statement {
	/** Test to be performed */
	private Expression test;
	/** Then and Else statements */
	private Statement thenBranch, elseBranch;

	/**
	 * Create a conditional, without an else branch
	 * @param test Test to perform
	 * @param thenBranch Statement to execute if test succeeds
	 */
	public Conditional(Expression test, Statement thenBranch) {
		this(test, thenBranch, null);
	}

	/**
	 * Create a conditionl, with a then and else branch
	 * @param test Test to perform
	 * @param thenBranch Statement to execute if test succeeds
	 * @param elseBranch Statement to execute if test fails
	 */
	public Conditional(Expression test, Statement thenBranch, Statement elseBranch) {
		this.test = test;
		this.thenBranch = thenBranch;
		
		// create skip if there's no else branch
		if(elseBranch == null)
			elseBranch = new Skip();
		else
			this.elseBranch = elseBranch;
	}
	
	/** @return Test to be performed by conditional */
	public Expression test(){ return test; }
	/** @return Statement executed if test succeeds */
	public Statement thenBranch(){ return thenBranch; }
	/** @return Statement executed if test fails (will be a Skip if no else branch) */
	public Statement elseBranch(){ return elseBranch; }
	
	@Override
	public void display(int indent){
		for(int i = 0; i < indent; i++)
			System.out.print("   ");
		System.out.println("|Conditional: ");
		
		for(int i = 0; i < indent; i++)
			System.out.print("   ");
		System.out.println("|Test: ");
		
		test.display(indent + 1);
		
		for(int i = 0; i < indent; i++)
			System.out.print("   ");
		System.out.println("|Then: ");
		
		thenBranch.display(indent + 1);
		
		if(!(elseBranch instanceof Skip)){
			for(int i = 0; i < indent; i++)
				System.out.print("   ");
			System.out.println("|Else: ");
			
			elseBranch.display(indent + 1);
		}
	}
}
