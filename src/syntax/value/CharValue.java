package syntax.value;

import syntax.Type;

public class CharValue extends Value {
	private char value = '\0';

	public CharValue() {
		type = Type.CHAR;
	}

	public CharValue(char v) {
		this();
		value = v;
		undef = false;
	}

	public char charValue() {
		assert !undef : "reference to undefined char value";
		return value;
	}

	public String toString() {
		if (undef)
			return "undef";
		return value + " (Char)";
	}

	public void display(int indent){
		for(int i = 0; i < indent; i++)
			System.out.print("   ");
		System.out.println("| " + value + " (Char)");
	}
}
