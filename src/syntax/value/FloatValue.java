package syntax.value;

import syntax.Type;

public class FloatValue extends Value {
	private float value = 0;

	public FloatValue() {
		type = Type.FLOAT;
	}

	public FloatValue(float v) {
		this();
		value = v;
		undef = false;
	}

	public float floatValue() {
		assert !undef : "reference to undefined float value";
		return value;
	}

	public String toString() {
		if (undef)
			return "undef";
		return value + " (Float)";
	}
	
	public void display(int indent){
		for(int i = 0; i < indent; i++)
			System.out.print("   ");
		System.out.println("| " + value + " (Float)");
	}
}
