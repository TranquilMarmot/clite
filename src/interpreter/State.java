package interpreter;
import java.util.*;

import syntax.expression.Variable;
import syntax.value.Value;


@SuppressWarnings("serial")
/**
 * Defines the set of variables and their associated values
 * that are active during interpretation
 * 
 * Basically just a renamed HashMap with some specialized methods (onion())
 */
public class State extends HashMap<Variable, Value> {
	/**
	 * Create a new, empty state
	 */
	public State() {
		super();
	}

	/**
	 * Create a new state with a given variable and value
	 * @param variable Initial variable
	 * @param val Initial value of variable
	 */
	public State(Variable variable, Value val) {
		super();
		this.put(variable, val);
	}

	/**
	 * Put the given variable into the hash map
	 * and return the new state. Updates a variable
	 * if it's already in the map.
	 * @param variable Variable to add/update
	 * @param val Value for variable
	 * @return New state
	 */
	public State onion(Variable variable, Value val) {
		this.put(variable, val);
		return this;
	}

	/**
	 * Put the given state into this state and return the combined state
	 * @param other State to combine
	 * @return New state from two other states
	 */
	public State onion(State other) {
		for (Variable key : other.keySet())
			put(key, other.get(key));
		return this;
	}
	
	/**
	 * Print out all the variables in this state and their values
	 */
	public void display(){
		for(Variable v : this.keySet())
			System.out.println(v + ": " + this.get(v));
	}
}
