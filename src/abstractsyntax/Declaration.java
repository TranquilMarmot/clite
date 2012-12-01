package abstractsyntax;

import abstractsyntax.expression.Variable;

/**
 * Declaration = Variable v; Type t
 */
public class Declaration {
	public Variable var;
	public Type type;

	public Declaration(Variable var, Type type) {
		this.var = var;
		this.type = type;
	} // declaration */

}
