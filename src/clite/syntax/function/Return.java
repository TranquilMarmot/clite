package clite.syntax.function;

import clite.syntax.expression.Expression;
import clite.syntax.expression.Variable;

/**
 * Return = Variable target; Expression result
 */
public class Return {
	/** Function being returned from (?) */
	private Variable target;
	
	/** Result to return */
	private Expression result;
	
	/**
	 * @param target Target to return from
	 * @param result Result to return
	 */
	public Return(Variable target, Expression result){
		this.target = target;
		this.result = result;
	}
	
	/** @return Target of return */
	public Variable target(){ return target; }
	
	/** @return Result being returned */
	public Expression result(){ return result; }
}
