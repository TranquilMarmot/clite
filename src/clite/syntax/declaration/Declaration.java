package clite.syntax.declaration;

import clite.syntax.Type;
import clite.syntax.expression.Variable;

/**
 * Declaration = Variable v; Type t
 */
public class Declaration {
	/** Variable for declaration */
	private Variable var;
	
	/** Type of declaration */
	private Type type;

	/**
	 * @param var Variable for declaration
	 * @param type Type of declaration
	 */
	public Declaration(Variable var, Type type) {
		this.var = var;
		this.type = type;
	}
	
	/** @return Variable for declaration */
	public Variable variable(){ return var; }
	
	/** @return Type of declaration */
	public Type type() { return type; }
}
