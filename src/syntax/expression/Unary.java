package syntax.expression;

import syntax.Operator;

/**
 * Unary = Operator op; Expression term
 */
public class Unary extends Expression {
	public Operator op;
	public Expression term;

	public Unary(Operator o, Expression e) {
		op = o;
		term = e;
	}
	
	public void display(int indent){
		for(int i = 0; i < indent; i++)
			System.out.print("   ");
		System.out.println("|Unary: ");
		
		for(int i = 0; i < indent; i++)
			System.out.print("   ");
		System.out.println(op.val);
		
		term.display(indent + 1);
	}

}
