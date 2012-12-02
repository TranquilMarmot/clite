package typing;
import syntax.Program;
import syntax.Type;
import syntax.declaration.Declaration;
import syntax.declaration.Declarations;
import syntax.expression.Binary;
import syntax.expression.Expression;
import syntax.expression.Unary;
import syntax.expression.Variable;
import syntax.statement.Assignment;
import syntax.statement.Block;
import syntax.statement.Conditional;
import syntax.statement.Loop;
import syntax.statement.Skip;
import syntax.statement.Statement;
import syntax.value.Value;

/**
 * Static type checking for Clite is defined by the functions 
 * V and the auxiliary functions typing and typeOf.  These
 * functions use the classes in the Abstract Syntax of Clite.
 */
public class StaticTypeCheck {

	/**
	 * Gets a type map from declarations
	 * @param d Declarations to build map from
	 * @return Type map from given declarations
	 */
	public static TypeMap typing(Declarations d) {
		TypeMap map = new TypeMap();
		for (Declaration di : d)
			map.put(di.var, di.type);
		return map;
	}

	/**
	 * Prints given message as error if test fails
	 * @param test If false, error, else nothing
	 * @param msg Error message
	 */
	public static void check(boolean test, String msg) {
		if (test)
			return;
		System.err.println(msg);
		System.exit(1);
	}
	
	/**
	 * Get the type of an expression from a type map
	 * @param e Expression to find type of
	 * @param tm Type map to use to find type
	 * @return Type of given expression 
	 */
	public static Type typeOf(Expression e, TypeMap tm) {
		// value
		if (e instanceof Value) {
			return ((Value) e).type();
			
		// variable
		} else if (e instanceof Variable) {
			Variable v = (Variable) e;
			check(tm.containsKey(v), "Caught undefined variable in static type checker: " + v);
			return (Type) tm.get(v);
			
		// binary op
		} else if (e instanceof Binary) {
			Binary b = (Binary)e;
			// +, -, *, /
			if (b.op.ArithmeticOp()){
				if(typeOf(b.term1, tm) == Type.INT && typeOf(b.term1, tm) == Type.INT)
					return Type.INT;
				else
					return Type.FLOAT;
			
			// &&, ||, <, >, <=, >=, ==, !=
			} else if (b.op.RelationalOp() || b.op.BooleanOp())
				return Type.BOOL;
			
			// unknown op
			else
				throw new IllegalArgumentException("Unknown binary op in static type checker, expression: " + e);
			
		// unary op
		} else if (e instanceof Unary) {
			Unary u = (Unary) e;
			// !
			if (u.op.NotOp())
				return Type.BOOL;
			// -
			else if (u.op.NegateOp())
				return typeOf(u.term, tm);
			
			// int cast
			else if (u.op.intOp())
				return Type.INT;
			
			// float cast
			else if (u.op.floatOp())
				return Type.FLOAT;
			
			// char cast
			else if (u.op.charOp())
				return Type.CHAR;
		}
		throw new IllegalArgumentException("should never reach here");
	}
	
	/**
	 * Verify a program's types
	 * @param p Program to verify
	 */
	public static void verify(Program p) {
		verify(p.declarations);
		verify(p.body, typing(p.declarations));
	}

	/**
	 * Verify a declaration's types
	 * @param d Declarations to verify
	 */
	public static void verify(Declarations d) {
		for (int i = 0; i < d.size() - 1; i++)
			for (int j = i + 1; j < d.size(); j++) {
				Declaration di = d.get(i);
				Declaration dj = d.get(j);
				check(!(di.var.equals(dj.var)), "Caught duplicate declaration in static type checking for: " + dj.var);
			}
	}

	/**
	 * Verify an expression with a given type map
	 * @param e Expression to verify
	 * @param tm Type map to verify with
	 */
	public static void verify(Expression e, TypeMap tm) {
		// value, so nothing to check
		if (e instanceof Value)
			return;
		
		// variable, check if it's declared
		if (e instanceof Variable) {
			Variable v = (Variable) e;
			check(tm.containsKey(v), "Caught undeclared variable in static type check: " + v);
			return;
		}
		
		// binary op
		if (e instanceof Binary) {
			Binary b = (Binary) e;
			
			Type type1 = typeOf(b.term1, tm);
			Type type2 = typeOf(b.term2, tm);
			
			verify(b.term1, tm);
			verify(b.term2, tm);
			
			// +, -, *, /
			// Int -> Int   ->   Int | Float -> Float -> Float |
			// Int -> Float -> Float | Float -> Int   -> Float
			if (b.op.ArithmeticOp()){
				check((type1 == Type.INT   && type2 == Type.INT)   ||
				      (type1 == Type.FLOAT && type2 == Type.FLOAT) ||
				      (type1 == Type.INT   && type2 == Type.FLOAT) ||
				      (type1 == Type.FLOAT && type2 == Type.INT),
				      "Type error for arithmetic op " + b.op  + "; got types " + type1 + " and " + type2);
				
			// <, >, <=, >=, ==, !=
			// Int  -> Int   -> Bool | Float -> Float -> Bool |
			// Int  -> Float -> Bool | Float -> Int   -> Bool |
			// Bool -> Bool  -> Bool | Char  -> Char  -> Bool
			} else if (b.op.RelationalOp()){
				check((type1 == Type.INT   && type2 == Type.INT)   ||
				      (type1 == Type.FLOAT && type2 == Type.FLOAT) ||
				      (type1 == Type.INT   && type2 == Type.FLOAT) ||
				      (type1 == Type.FLOAT && type2 == Type.INT)   ||
				      (type1 == Type.BOOL && type2 == Type.BOOL)   ||
				      (type1 == Type.CHAR && type2 == Type.CHAR),
				      "Type error for relational op " + b.op  + "; got types " + type1 + " and " + type2);
			}
			
			// &&, ||
			// Bool -> Bool -> Bool
			else if (b.op.BooleanOp())
				check(type1 == Type.BOOL && type2 == Type.BOOL,
				"Caught non-bool operand for " + b.op + " in static type checker; got types " + type1 + " and " + type2);
			
			else
				throw new IllegalArgumentException("should never reach here");
			return;
		}
		
		// unary op
		else if(e instanceof Unary){
			Unary u = (Unary) e;
			
			Type t = typeOf(u.term, tm);
			
			verify(u.term, tm);
			
			// !
			// Bool -> Bool
			if(u.op.NotOp()){
				check(t == Type.BOOL,
				     "Attempted not operation on non-bool (attempted on " + t + ")");
				
			// -
			// Int -> Int | Float -> Float
			} else if(u.op.NegateOp()){
				check(t == Type.FLOAT || t == Type.INT,
					 "Attempted negate operation on something other than a float or int (attempted on " + t + ")");
				
			// int cast
			// Float -> Int | Char -> Int
			} else if(u.op.intOp()){
				check(t == Type.FLOAT || t == Type.CHAR,
					 "Attempted int cast from something other than a float or a char (attempted on " + t + ")");
			
			// float cast
			// Int -> Float
			} else if(u.op.floatOp()){
				check(t == Type.INT,
					"Attempted float cast from something other than an int (attempted on " + t + ")");
				
			// char cast
			// Int -> Char
			} else if(u.op.charOp()){
				check(t == Type.INT,
					 "Attempted char cast from something other than an int (attempted on " + t + ")");
			}
		} else{
			throw new IllegalArgumentException("should never reach here");
		}
		
		// student exercise (done)
	}

	/**
	 * Verify a statement with a given type map
	 * @param s Statement to verify
	 * @param tm Type map to use for verification
	 */
	public static void verify(Statement s, TypeMap tm) {
		// make sure we're not given a null statement
		if (s == null)
			throw new IllegalArgumentException("AST error: null statement");
		
		// skip if it's a skip
		else if (s instanceof Skip)
			return;
		
		// assignment statement
		else if (s instanceof Assignment) {
			Assignment a = (Assignment) s;
			
			// make sure target exists
			check(tm.containsKey(a.target), "Target not found in type map! (target: " + a.target + ")");
			
			verify(a.source, tm);
			
			Type targettype = (Type) tm.get(a.target);
			Type srctype = typeOf(a.source, tm);
			
			if (targettype != srctype) {
				// assigning an int to a float is ok
				if (targettype == Type.FLOAT)
					check(srctype == Type.INT,
					     "Caught mixed mode assignment in static type checker from " + srctype + " to " + targettype + " (target: " + a.target + ")");
				// assigning a char to an int is ok
				else if (targettype == Type.INT)
					check(srctype == Type.CHAR || srctype == Type.FLOAT,
					     "Caught mixed mode assignment in static type checker from " + srctype + " to " + targettype + " (target: " + a.target + ")");
				else
					check(false,
					     "Caught mixed mode assignment in static type checker from " + srctype + " to " + targettype + " (target: " + a.target + ")");
			}
			return;
			
		// block of statements
		} else if(s instanceof Block){
			Block b = (Block) s;
			
			// check every statement in block
			for(Statement stat : b.members)
				verify(stat, tm);
			
		// while loop
		} else if(s instanceof Loop) {
			Loop l = (Loop) s;
			
			// verify test and body
			verify(l.test, tm);
			verify(l.body, tm);
			
		// if statement
		} else if(s instanceof Conditional){
			Conditional c = (Conditional) s;
			
			verify(c.test, tm);
			verify(c.thenbranch, tm);
			verify(c.elsebranch, tm);
		} else{
			throw new IllegalArgumentException("should never reach here " + s);
		}
		
		// student exercise (done)
	}
} // class StaticTypeCheck

