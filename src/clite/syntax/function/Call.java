package clite.syntax.function;

import java.util.Iterator;
import java.util.Stack;

import clite.syntax.expression.Expression;
import clite.syntax.expression.Variable;
import clite.syntax.statement.Statement;

/**
 * Call = String name; Expressions args
 */
public class Call extends Statement {
	/** Function being called */
	private Variable identifier;
	
	/** Arguments to pass to function */
	private Stack<Expression> arguments;
	
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
			System.out.println('\t');
		System.out.println("(Call)");
	}
}
