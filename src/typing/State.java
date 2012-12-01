package typing;
import java.util.*;

import abstractsyntax.expression.Variable;
import abstractsyntax.value.Value;

@SuppressWarnings("serial")
public class State extends HashMap<Variable, Value> {
	// Defines the set of variables and their associated values
	// that are active during interpretation

	public State() {
	}

	public State(Variable key, Value val) {
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

}
