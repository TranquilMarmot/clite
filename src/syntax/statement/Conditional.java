package syntax.statement;

import syntax.Skip;
import syntax.expression.Expression;

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
		
		thenbranch.display(indent + 1);
		
		if(!(elsebranch instanceof Skip)){
			for(int i = 0; i < indent; i++)
				System.out.print("   ");
			System.out.println("|Else: ");
			
			elsebranch.display(indent + 1);
		}
	}
}
