package syntax.value;

import syntax.Type;

public class BoolValue extends Value {
	private boolean value = false;

	public BoolValue() {
		type = Type.BOOL;
	}

	public BoolValue(boolean v) {
		this();
		value = v;
		undef = false;
	}

	public boolean boolValue() {
		assert !undef : "reference to undefined bool value";
		return value;
	}

	public int intValue() {
		assert !undef : "reference to undefined bool value";
		return value ? 1 : 0;
	}

	public String toString() {
		if (undef)
			return "undef";
		return value + " (Bool)";
	}
	
	public void display(int indent){
		for(int i = 0; i < indent; i++)
			System.out.print("   ");
		System.out.println("| " + value + " (Bool)");
	}

}
