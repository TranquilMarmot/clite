package clite.syntax.function;

import clite.syntax.expression.Expression;
import clite.syntax.statement.Statement;

/**
 * Return = Variable target; Expression result
 */
public class Return extends Statement {
	/** Result to return */
	private Expression result;
	
	/**
	 * @param target Target to return from
	 * @param result Result to return
	 */
	public Return(Expression result){
		this.result = result;
	}
	
	/** @return Result being returned */
	public Expression result(){ return result; }
	
	@Override
	public void display(int indent) {
		for(int i = 0; i < indent; i++)
			System.out.println('\t');
		System.out.println("(Return)");
	}
}
