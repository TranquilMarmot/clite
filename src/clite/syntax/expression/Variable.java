package clite.syntax.expression;


/**
 * Variable = String id
 */
public class Variable implements Expression {
	/** ID of variable */
	private String id;

	/**
	 * @param id ID of variable
	 */
	public Variable(String id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return id;
	}

	@Override
	public boolean equals(Object obj) {
		String s = ((Variable) obj).id;
		return id.equals(s); // case-sensitive identifiers
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}
	
	@Override
	public void display(int indent){
		for(int i = 0; i < indent; i++)
			System.out.print("   ");
		System.out.println("| " + id);
	}

}
