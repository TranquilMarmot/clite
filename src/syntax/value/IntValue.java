package syntax.value;

import syntax.Type;

public class IntValue extends Value {
	private int value = 0;

	public IntValue() {
		type = Type.INT;
	}

	public IntValue(int v) {
		this();
		value = v;
		undef = false;
	}

	public int intValue() {
		assert !undef : "reference to undefined int value";
		return value;
	}

	public String toString() {
		if (undef)
			return "undef";
		return value + " (Int)";
	}

	public void display(int indent){
		for(int i = 0; i < indent; i++)
			System.out.print("   ");
		System.out.println("| " + value + " (Int)");
	}
}