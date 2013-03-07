package clite.syntax.value;

import clite.syntax.Type;

/**
 * Boolean literal
 */
public class BoolValue extends Value {
	/** Boolean value */
	private boolean value = false;

	/**
	 * Create a new, undefined boolean literal
	 */
	public BoolValue() {
		super();
		type = Type.BOOL;
	}

	/**
	 * Create and assign a new bool value
	 * @param v What to assign value to
	 */
	public BoolValue(boolean v) {
		this();
		value = v;
		undefined = false;
	}

	@Override
	public boolean boolValue() {
		assert !undefined : "reference to undefined bool value";
		return value;
	}

	@Override
	public int intValue() {
		assert !undefined : "reference to undefined bool value";
		return value ? 1 : 0;
	}

	@Override
	public String toString() {
		if (undefined)
			return "undef";
		return value + " (Bool)";
	}
	
	@Override
	public void display(int indent){
		for(int i = 0; i < indent; i++)
			System.out.print("   ");
		System.out.println("| " + value + " (Bool)");
	}

}
