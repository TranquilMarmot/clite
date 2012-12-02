package typing;
import java.util.*;

import syntax.Type;
import syntax.expression.Variable;


@SuppressWarnings("serial")
/**
 * TypeMap is implemented as a Java HashMap.
 * Plus a 'display' method to facilitate experimentation.
 */
public class TypeMap extends HashMap<Variable, Type> {
	/** Displays all the values in this type map and their types */
	public void display() {
		for(Variable v : this.keySet())
			System.out.println("    " + v.toString() + " :: " + this.get(v));
	}
}
