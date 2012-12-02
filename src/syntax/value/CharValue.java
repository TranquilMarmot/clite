package syntax.value;

import syntax.Type;

/**
 * Char literal value
 */
public class CharValue extends Value {
	/** Char value */
	private char value = '\0';

	/**
	 * Create new, undefined char value
	 */
	public CharValue() {
		super();
		type = Type.CHAR;
	}

	/**
	 * Create and define new char value
	 * @param v What to define value as
	 */
	public CharValue(char v) {
		this();
		value = v;
		undefined = false;
	}

	@Override
	public char charValue() {
		assert !undefined : "reference to undefined char value";
		return value;
	}

	@Override
	public String toString() {
		if (undefined)
			return "undef";
		return value + " (Char)";
	}

	@Override
	public void display(int indent){
		for(int i = 0; i < indent; i++)
			System.out.print("   ");
		System.out.println("| " + value + " (Char)");
	}
}
