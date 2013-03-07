package clite.syntax.value;

import clite.syntax.Type;

/**
 * An int literal
 */
public class IntValue extends Value {
	/** Value of int literal */
	private int value = 0;

	/**
	 * Create a new, undefined int value
	 */
	public IntValue() {
		super();
		type = Type.INT;
	}

	/**
	 * Create and define a new int value
	 * @param v What to define new value as
	 */
	public IntValue(int v) {
		this();
		value = v;
		undefined = false;
	}

	@Override
	public int intValue() {
		assert !undefined : "reference to undefined int value";
		return value;
	}

	@Override
	public String toString() {
		if (undefined)
			return "undef";
		return value + " (Int)";
	}

	@Override
	public void display(int indent){
		for(int i = 0; i < indent; i++)
			System.out.print("   ");
		System.out.println("| " + value + " (Int)");
	}
}