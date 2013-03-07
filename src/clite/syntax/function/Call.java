package clite.syntax.function;

import java.util.ArrayList;
import java.util.Iterator;

import clite.syntax.expression.Expression;

/**
 * Call = String name; Expressions args
 */
public class Call extends Expression {
	/** Function being called */
	private String identifier;
	
	/** Arguments to pass to function */
	private ArrayList<Expression> arguments;
	
	public Call(String id, ArrayList<Expression> arguments){
		this.identifier = id;
		this.arguments = arguments;
	}
	
	/** Name of function being called */
	public String identifier(){ return identifier; }
	
	/** Iterator to go through all arguments */
	public Iterator<Expression> arguments(){ return arguments.iterator(); }
	

	@Override
	public void display(int indent) {
		System.out.println("TODO call display");
	}

}
