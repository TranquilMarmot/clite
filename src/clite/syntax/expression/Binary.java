package clite.syntax.expression;

import clite.syntax.Operator;


/**
 * Binary = Expression term1 Operator op Expression term2
 */
public class Binary extends Expression {
	private Operator op;
	private Expression term1, term2;

	/**
	 * @param op Operator to use for binary expression
	 * @param term1 Left-hand side of expression
	 * @param term2 Right-hand side of expression
	 */
	public Binary(Operator op, Expression term1, Expression term2) {
		this.op = op;
		this.term1 = term1;
		this.term2 = term2;
	}
	
	/** @return Operator for binary expression */
	public Operator operator(){ return op; }
	/** @return Left-hand side of expression */
	public Expression term1(){ return term1; }
	/** @return Right-hand side of expression */
	public Expression term2(){ return term2; }
	
	@Override
	public void display(int indent){
		term1.display(indent + 1);
		
		for(int i = 0; i < indent + 1; i++)
			System.out.print("   ");
		System.out.println("| " + op.val);
		
		term2.display(indent + 1);
	}
}
