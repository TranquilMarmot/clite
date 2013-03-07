package clite.syntax;


/**
 * Operator = BooleanOp | RelationalOp | ArithmeticOp | UnaryOp
 * BooleanOp = && | ||
 */
public class Operator {
	/** Possible operators */
	public final static String
	// BooleanOp = && | ||
	AND = "&&",
	OR = "||",
	
	// RelationalOp = < | <= | == | != | >= | >
	LT = "<",
	LE = "<=",
	EQ = "==",
	NE = "!=",
	GT = ">",
	GE = ">=",
	
	// ArithmeticOp = + | - | * | /
	PLUS = "+",
	MINUS = "-",
	TIMES = "*",
	DIV = "/",
	
	// UnaryOp = !
	NOT = "!",
	NEG = "-",
	
	// CastOp = int | float | char
	INT = "int",
	FLOAT = "float",
	CHAR = "char";
	
	/** Value of Operator */
	public String val;

	/**
	 * Create a new Operator
	 * @param s Value of operator (from static Strings in Operator class)
	 */
	public Operator(String s) { val = s; }

	@Override
	public String toString() { return val; }

	@Override
	public boolean equals(Object obj) { return val.equals(obj); }

	/**
	 * &&, ||
	 * @return Whether or not this is a boolean op
	 */
	public boolean isBooleanOp() {
		return val.equals(AND) ||
		       val.equals(OR);
	}

	/**
	 * <, <=, ==, !=, >=, >
	 * @return Whether or not this is a relational op
	 */
	public boolean isRelationalOp() {
		return val.equals(LT) ||
		       val.equals(LE) ||
		       val.equals(EQ) ||
		       val.equals(NE) ||
		       val.equals(GT) ||
		       val.equals(GE);
	}

	/**
	 * +, -, *, /
	 * @return Whether or not this is an arithmetic op
	 */
	public boolean isArithmeticOp() {
		return val.equals(PLUS)  ||
		       val.equals(MINUS) ||
		       val.equals(TIMES) ||
		       val.equals(DIV);
	}

	/**
	 * !
	 * @return Whether or not this is a not op
	 */
	public boolean isNotOp() {
		return val.equals(NOT);
	}

	/**
	 * -
	 * @return Whether or not this is a negate op
	 */
	public boolean isNegateOp() {
		return val.equals(NEG);
	}

	/**
	 * int cast
	 * @return Whether or not this is an int cast operator
	 */
	public boolean isIntOp() {
		return val.equals(INT);
	}

	/**
	 * float cast
	 * @return Whether or not this is a float cast operator
	 */
	public boolean isFloatOp() {
		return val.equals(FLOAT);
	}

	/**
	 * char cast
	 * @return Whether or not this is a char cast operator
	 */
	public boolean isCharOp() {
		return val.equals(CHAR);
	}
}
