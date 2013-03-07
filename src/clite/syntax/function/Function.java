package clite.syntax.function;

import clite.syntax.Type;
import clite.syntax.declaration.Declarations;
import clite.syntax.statement.Block;

/**
 * Function = Type t; String id; Declarations params, locals; Block body
 */
public class Function {
	/** Return type of function */
	private Type type;
	/** Name of function */
	private String id;
	/** Parameters and local values */
	private Declarations params, locals;
	/** Body of statements */
	private Block body;
	
	/**
	 * Create a new function
	 * @param type Return type of function
	 * @param id Name of function
	 * @param params Parameters of function
	 * @param locals Local variables
	 * @param body Body off statetments
	 */
	public Function(Type type, String id, Declarations params, Declarations locals, Block body){
		this.type = type;
		this.id = id;
		this.params = params;
		this.locals = locals;
		this.body = body;
	}
	
	/** @return Function's return type */
	public Type type(){ return type; }
	/** @return Function's name */
	public String id(){ return id; }
	/** @return Parameter list */
	public Declarations params(){ return params; }
	/** @return Local varaibles */
	public Declarations locals(){ return locals; }
	/** @return Body of statements */
	public Block body(){ return body;}

	public void display(int i) {
		// TODO
		System.out.println("TODO display function");
		
	} 
}
