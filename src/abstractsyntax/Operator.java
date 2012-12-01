package abstractsyntax;


/**
 * Operator = BooleanOp | RelationalOp | ArithmeticOp | UnaryOp
 * BooleanOp = && | ||
 */
public class Operator {
	public final static String
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
	CHAR = "char",
	
	/* Typed Operators */
	// RelationalOp = < | <= | == | != | >= | >
	INT_LT = "INT<",
	INT_LE = "INT<=",
	INT_EQ = "INT==",
	INT_NE = "INT!=",
	INT_GT = "INT>",
	INT_GE = "INT>=",
	
	// ArithmeticOp = + | - | * | /
	INT_PLUS = "INT+",
	INT_MINUS = "INT-",
	INT_TIMES = "INT*",
	INT_DIV = "INT/",
	
	// UnaryOp = !
	INT_NEG = "-",
	
	// RelationalOp = < | <= | == | != | >= | >
	FLOAT_LT = "FLOAT<",
	FLOAT_LE = "FLOAT<=",
	FLOAT_EQ = "FLOAT==",
	FLOAT_NE = "FLOAT!=",
	FLOAT_GT = "FLOAT>",
	FLOAT_GE = "FLOAT>=",
		
	// ArithmeticOp = + | - | * | /
	FLOAT_PLUS = "FLOAT+",
	FLOAT_MINUS = "FLOAT-",
	FLOAT_TIMES = "FLOAT*",
	FLOAT_DIV = "FLOAT/",
	
	// UnaryOp = !
	FLOAT_NEG = "-",
	
	// RelationalOp = < | <= | == | != | >= | >
	CHAR_LT = "CHAR<",
	CHAR_LE = "CHAR<=",
	CHAR_EQ = "CHAR==",
	CHAR_NE = "CHAR!=",
	CHAR_GT = "CHAR>",
	CHAR_GE = "CHAR>=",
	
	// RelationalOp = < | <= | == | != | >= | >
	BOOL_LT = "BOOL<",
	BOOL_LE = "BOOL<=",
	BOOL_EQ = "BOOL==",
	BOOL_NE = "BOOL!=",
	BOOL_GT = "BOOL>",
	BOOL_GE = "BOOL>=",
	
	// Type specific cast
	I2F = "I2F",
	F2I = "F2I",
	C2I = "C2I",
	I2C = "I2C";

	public String val;

	public Operator(String s) {
		val = s;
	}

	public String toString() {
		return val;
	}

	public boolean equals(Object obj) {
		return val.equals(obj);
	}

	public boolean BooleanOp() {
		return val.equals(AND) || val.equals(OR);
	}

	public boolean RelationalOp() {
		return val.equals(LT) || val.equals(LE) || val.equals(EQ)
				|| val.equals(NE) || val.equals(GT) || val.equals(GE);
	}

	public boolean ArithmeticOp() {
		return val.equals(PLUS) || val.equals(MINUS) || val.equals(TIMES)
				|| val.equals(DIV);
	}

	public boolean NotOp() {
		return val.equals(NOT);
	}

	public boolean NegateOp() {
		return val.equals(NEG);
	}

	public boolean intOp() {
		return val.equals(INT);
	}

	public boolean floatOp() {
		return val.equals(FLOAT);
	}

	public boolean charOp() {
		return val.equals(CHAR);
	}

	final static String intMap[][] = { 
		{ PLUS,  INT_PLUS },
		{ MINUS, INT_MINUS },
		{ TIMES, INT_TIMES },
		{ DIV,   INT_DIV },
		{ EQ,    INT_EQ },
		{ NE,    INT_NE },
		{ LT,    INT_LT },
		{ LE,    INT_LE },
		{ GT,    INT_GT },
		{ GE,    INT_GE },
		{ NEG,   INT_NEG },
		{ FLOAT, I2F },
		{ CHAR,  I2C }
	};

	final static String floatMap[][] = {
		{ PLUS,  FLOAT_PLUS },
		{ MINUS, FLOAT_MINUS },
		{ TIMES, FLOAT_TIMES },
		{ DIV,   FLOAT_DIV },
		{ EQ,    FLOAT_EQ },
		{ NE,    FLOAT_NE },
		{ LT,    FLOAT_LT },
		{ LE,    FLOAT_LE },
		{ GT,    FLOAT_GT },
		{ GE,    FLOAT_GE },
		{ NEG,   FLOAT_NEG },
		{ INT,   F2I }
	};

	final static String charMap[][] = {
		{ EQ,  CHAR_EQ },
		{ NE,  CHAR_NE },
		{ LT,  CHAR_LT },
		{ LE,  CHAR_LE },
		{ GT,  CHAR_GT },
		{ GE,  CHAR_GE },
		{ INT, C2I }
	};

	final static String boolMap[][] = {
		{ EQ, BOOL_EQ },
		{ NE, BOOL_NE },
		{ LT, BOOL_LT },
		{ LE, BOOL_LE },
		{ GT, BOOL_GT },
		{ GE, BOOL_GE }
	};

	final static private Operator map(String[][] tmap, String op) {
		for (int i = 0; i < tmap.length; i++)
			if (tmap[i][0].equals(op))
				return new Operator(tmap[i][1]);
		assert false : "should never reach here";
		return null;
	}

	final static public Operator intMap(String op) {
		return map(intMap, op);
	}

	final static public Operator floatMap(String op) {
		return map(floatMap, op);
	}

	final static public Operator charMap(String op) {
		return map(charMap, op);
	}

	final static public Operator boolMap(String op) {
		return map(boolMap, op);
	}

}
