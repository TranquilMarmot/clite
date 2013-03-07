package clite.function;

import clite.syntax.Type;
import clite.syntax.declaration.Declarations;
import clite.syntax.statement.Block;

public class Function {
	private Type type;
	private String id;
	private Declarations params, locals;
	private Block body;
	
	public Function(Type type, String id, Declarations params, Declarations locals, Block body){
		this.type = type;
		this.id = id;
		this.params = params;
		this.locals = locals;
		this.body = body;
	}
	
	public Type type(){ return type; }
	
	public String id(){ return id; }
	
	public Declarations params(){ return params; }
	
	public Declarations locals(){ return locals; }
	
	public Block body(){ return body;} 
}
