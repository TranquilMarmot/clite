package typing;
import java.util.*;

import syntax.expression.Variable;
import syntax.value.Value;


@SuppressWarnings("serial")
public class State extends HashMap<Variable, Value> {
	// Defines the set of variables and their associated values
	// that are active during interpretation

	public State() {
		super();
	}

	public State(Variable key, Value val) {
		super();
		put(key, val);
	}

	public State onion(Variable key, Value val) {
		put(key, val);
		return this;
	}

	public State onion(State t) {
		for (Variable key : t.keySet())
			put(key, t.get(key));
		return this;
	}
	
	public void display(){
		for(Variable v : this.keySet()){
			Value val = this.get(v);
			
			System.out.println(v + ": " + val);
		}
	}
}
