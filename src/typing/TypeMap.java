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
	public void display() {
		for(Variable v : this.keySet()){
			Type t = this.get(v);
			
			System.out.println("    " + v.toString() + " :: " + t);
		}
	}
}
