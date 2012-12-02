package syntax.expression;

import syntax.Operator;

/**
 * Unary = Operator op Expression term
 */
public class Unary extends Expression {
	/** Operator to be applied to term */
	private Operator op;
	/** Term to apply operator to */
	public Expression term;

	/**
	 * @param o Operator to be applied to term
	 * @param e Term to apply operator to
	 */
	public Unary(Operator op, Expression term) {
		this.op = op;
		this.term = term;
	}
	
	/** @return Operator to be applied to term */
	public Operator operator(){ return op; }
	
	/** @return Term to apply operator to */
	public Expression term(){ return term; }
	
	@Override
	public void display(int indent){
		for(int i = 0; i < indent; i++)
			System.out.print("   ");
		System.out.println("|Unary: ");
		
		for(int i = 0; i < indent + 1; i++)
			System.out.print("   ");
		System.out.println("|" + op.val);
		
		term.display(indent + 1);
	}

}
