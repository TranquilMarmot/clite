package clite.syntax.function;

import clite.syntax.expression.Expression;
import clite.syntax.expression.Variable;
import clite.syntax.statement.Statement;

/**
 * Return = Variable target; Expression result
 */
public class Return implements Statement {
	/** Result to return */
	private Expression result;
	
	/** Name of function this return statement belongs to */
	private Variable functionName;
	
	/**
	 * @param target Target to return from
	 * @param result Result to return
	 * @param ret 
	 */
	public Return(Variable functionName, Expression result){
		this.result = result;
		this.functionName = functionName;
	}
	
	/** @return Result being returned */
	public Expression result(){ return result; }
	
	/** @return Name of function this return statement belongs to */
	public Variable functionName(){ return functionName; }
	
	@Override
	public void display(int indent) {
		for(int i = 0; i < indent; i++)
			System.out.print("   ");
		System.out.println("| return ");
		result.display(indent + 1);
	}
}
