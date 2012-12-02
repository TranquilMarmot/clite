package syntax.value;

import syntax.Type;

/**
 * Float literal value
 */
public class FloatValue extends Value {
	/** Float value */
	private float value = 0;

	/**
	 * Create new, undefined float value
	 */
	public FloatValue() {
		super();
		type = Type.FLOAT;
	}

	/**
	 * Create and define new float value
	 * @param v What to define value as
	 */
	public FloatValue(float v) {
		this();
		value = v;
		undefined = false;
	}

	@Override
	public float floatValue() {
		assert !undefined : "reference to undefined float value";
		return value;
	}

	@Override
	public String toString() {
		if (undefined)
			return "undef";
		return value + " (Float)";
	}
	
	@Override
	public void display(int indent){
		for(int i = 0; i < indent; i++)
			System.out.print("   ");
		System.out.println("| " + value + " (Float)");
	}
}
