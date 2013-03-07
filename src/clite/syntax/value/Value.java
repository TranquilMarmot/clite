package clite.syntax.value;

import clite.syntax.Type;
import clite.syntax.expression.Expression;

/**
 * Value = IntValue | BoolValue | CharValue | FloatValue
 */
public abstract class Value extends Expression {
	/** Type of this value */
	protected Type type;
	/** Whether or not this value is defined */
	protected boolean undefined;
	
	public Value(){
		undefined = true;
	}

	/*
	 * NOTE:
	 * These all assert false to ensure that they're only called by classes that override them.
	 * So, for example, IntValue overrides the intValue() method
	 * (Note from Nate: I didn't make this, but it seems like pretty poor
	 * design from an OO standpoint, since even a BoolValue will have an IntValue method...)
	 */
	
	/** @return Value of int literal */
	public int intValue() {
		assert false : "should never reach here";
		return 0;
	}

	/** @return Value of bool literal */
	public boolean boolValue() {
		assert false : "should never reach here";
		return false;
	}

	/** @return Value of char literal */
	public char charValue() {
		assert false : "should never reach here";
		return ' ';
	}

	/** @return Value of float literal */
	public float floatValue() {
		assert false : "should never reach here";
		return 0.0f;
	}

	/**
	 * @return Whether or not this value has been defined
	 */
	public boolean undefined() { return undefined; }

	/** @return Type of this value */
	public Type type() { return type; }

	/**
	 * Makes a new, undefined value of the given type
	 * @param type Type to make new value of
	 * @return Undefined value with given type
	 */
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
