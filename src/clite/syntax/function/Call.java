package clite.syntax.function;

import java.util.Iterator;
import java.util.Stack;

import clite.syntax.expression.Expression;
import clite.syntax.expression.Variable;
import clite.syntax.statement.Statement;

/**
 * Call = String name; Expressions args
 */
public class Call implements Statement, Expression {
	/** Function being called */
	private Variable identifier;
	
	/** Arguments to pass to function */
	private Stack<Expression> arguments;
	
	/**
	 * @param id Name of function being called
	 * @param arguments Arguments to call function with
	 */
	public Call(Variable id, Stack<Expression> arguments){
		this.identifier = id;
		this.arguments = arguments;
	}
	
	/** Name of function being called */
	public Variable identifier(){ return identifier; }
	
	/** Iterator to go through all arguments */
	public Iterator<Expression> arguments(){ return arguments.iterator(); }
	

	@Override
	public void display(int indent) {
		for(int i = 0; i < indent; i++)
			System.out.print("   ");
		System.out.println("| call " + identifier.toString() + "(");
		for(int i = 0; i < indent; i++)
			System.out.print("   ");
		System.out.println("| (");
		for(Expression e : arguments)
			e.display(indent + 1);
		for(int i = 0; i < indent; i++)
			System.out.print("   ");
		System.out.println("| )");
	}
}
