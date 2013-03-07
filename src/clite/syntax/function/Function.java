package clite.syntax.function;

import clite.syntax.Type;
import clite.syntax.declaration.Declarations;
import clite.syntax.statement.Block;
import clite.syntax.statement.Statement;

/**
 * Function = Type t; String id; Declarations params, locals; Block body
 */
public class Function extends Statement {
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

	public void display(int indent) {
		for(int i = 0; i < indent; i++)
			System.out.print("   ");
		System.out.println(id + " :: " + type);
		indent++;
		for(int i = 0; i < indent; i++)
			System.out.print("   ");
		System.out.println("Parameters:");
		params.display(indent + 1);
		for(int i = 0; i < indent; i++)
			System.out.print("   ");
		System.out.println("Locals:");
		locals.display(indent + 1);
		for(int i = 0; i < indent; i++)
			System.out.print("   ");
		System.out.println("Body:");
		body.display(indent);
		System.out.println();
	} 
}
