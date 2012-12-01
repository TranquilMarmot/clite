package syntax.expression;

import syntax.Operator;


/**
 * Binary = Operator op; Expression term1, term2
 */
public class Binary extends Expression {
	public Operator op;
	public Expression term1, term2;

	public Binary(Operator o, Expression l, Expression r) {
		op = o;
		term1 = l;
		term2 = r;
	} // binary
	
	public void display(int indent){
		term1.display(indent + 1);
		
		for(int i = 0; i < indent + 1; i++)
			System.out.print("   ");
		System.out.println("| " + op.val);
		
		term2.display(indent + 1);
	}

}
