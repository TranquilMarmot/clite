package syntax.value;

import syntax.Type;
import syntax.expression.Expression;

/**
 * Value = IntValue | BoolValue | CharValue | FloatValue
 */
public abstract class Value extends Expression {
	public Type type;
	protected boolean undef = true;

	/*
	 * NOTE:
	 * These all assert false to ensure that they're only called by classes that override them.
	 * So, for example, IntValue overrides the intValue() method
	 * (Note from Nate: I didn't make this, but it seems like pretty poor
	 * design from an OO standpoint, since even a BoolValue will have an IntValue method...)
	 */
	
	public int intValue() {
		assert false : "should never reach here";
		return 0;
	}

	public boolean boolValue() {
		assert false : "should never reach here";
		return false;
	}

	public char charValue() {
		assert false : "should never reach here";
		return ' ';
	}

	public float floatValue() {
		assert false : "should never reach here";
		return 0.0f;
	}

	public boolean isUndef() {
		return undef;
	}

	public Type type() {
		return type;
	}

	public static Value mkValue(Type type) {
		if (type == Type.INT)
			return new IntValue();
		if (type == Type.BOOL)
			return new BoolValue();
		if (type == Type.CHAR)
			return new CharValue();
		if (type == Type.FLOAT)
			return new FloatValue();
		throw new IllegalArgumentException("Illegal type in mkValue");
	}
}
