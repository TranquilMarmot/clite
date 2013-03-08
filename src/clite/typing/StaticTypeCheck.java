package clite.typing;
import java.util.Iterator;

import clite.syntax.Program;
import clite.syntax.Type;
import clite.syntax.declaration.Declaration;
import clite.syntax.declaration.Declarations;
import clite.syntax.expression.Binary;
import clite.syntax.expression.Expression;
import clite.syntax.expression.Unary;
import clite.syntax.expression.Variable;
import clite.syntax.function.Call;
import clite.syntax.function.Function;
import clite.syntax.function.Functions;
import clite.syntax.function.Return;
import clite.syntax.statement.Assignment;
import clite.syntax.statement.Block;
import clite.syntax.statement.Conditional;
import clite.syntax.statement.Loop;
import clite.syntax.statement.Skip;
import clite.syntax.statement.Statement;
import clite.syntax.value.Value;


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
		for (Declaration di : d.values())
			map.put(di.variable(), di.type());
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
	public static Type typeOf(Expression e, Functions funcs, TypeMap tm) {
		// value
		if (e instanceof Value) {
			return ((Value) e).type();
			
		// function call
		} else if(e instanceof Call) {
			Call c = (Call) e;
			Function f = funcs.get(c.identifier().toString());
			return f.type();
			
		// variable
		} else if (e instanceof Variable) {
			Variable v = (Variable) e;
			check(tm.containsKey(v), "Caught undefined variable in static type checker: " + v);
			return (Type) tm.get(v);
			
		// binary op
		} else if (e instanceof Binary) {
			Binary b = (Binary)e;
			// +, -, *, /
			if (b.operator().isArithmeticOp()){
				if(typeOf(b.term1(), funcs, tm) == Type.INT && typeOf(b.term1(), funcs, tm) == Type.INT)
					return Type.INT;
				else
					return Type.FLOAT;
			
			// &&, ||, <, >, <=, >=, ==, !=
			} else if (b.operator().isRelationalOp() || b.operator().isBooleanOp())
				return Type.BOOL;
			
			// unknown op
			else
				throw new IllegalArgumentException("Unknown binary op in static type checker, expression: " + e);
			
		// unary op
		} else if (e instanceof Unary) {
			Unary u = (Unary) e;
			// !
			if (u.operator().isNotOp())
				return Type.BOOL;
			// -
			else if (u.operator().isNegateOp())
				return typeOf(u.term, funcs, tm);
			
			// int cast
			else if (u.operator().isIntOp())
				return Type.INT;
			
			// float cast
			else if (u.operator().isFloatOp())
				return Type.FLOAT;
			
			// char cast
			else if (u.operator().isCharOp())
				return Type.CHAR;
		}
		throw new IllegalArgumentException("should never reach here");
	}
	
	/**
	 * validate a program's types
	 * @param p Program to validate
	 */
	public static void validate(Program p) {
		check(p.functions().containsKey("main"), "Error! Main function not found!");
		validate(p.functions(), typing(p.globals()));
	}
	
	/**
	 * Validates functions 
	 * @param functions Functions to validate
	 * @param tm TypeMap of global variables
	 */
	public static void validate(Functions functions, TypeMap tm){
		for(Function func : functions.values()){
			// add the function's parameters and local variables to the type map
			TypeMap newMap = new TypeMap();
			newMap.putAll(tm);
			newMap.putAll(typing(func.params()));
			newMap.putAll(typing(func.locals()));
			validate(func.body(), func, functions, newMap);
		}
	}
	
	/**
	 * Validates a block of statements, checking for return and function calls
	 * @param b Block to validate
	 * @param func Function that block belongs to, for checking return type
	 * @param functions Map of all functions
	 * @param tm Type map to check against
	 */
	public static void validate(Block b, Function func, Functions functions, TypeMap tm){
		// whether or not we've seen a return statement
		boolean hasReturn = false;
		Iterator<Statement> it = b.getMembers();
		while(it.hasNext()){
			Statement s = it.next();
			// special case for return statement
			if(s instanceof Return){
				// can only have one return statement
				check(!hasReturn, "Function " + func.id() + " has multiple return statements!");
				Return r = (Return)s;
				Type t = typeOf(r.result(), functions, tm);
				check(t == func.type(), "Return expression doesn't match function's return type! (got a " + t + ", expected a " + func.type() + ")");
				hasReturn = true;
				
			// special case for call statement
			} else if(s instanceof Call){
				check(!hasReturn, "Return must be last expression in function block (in function " + func.id() + "!");
				validate((Call) s, functions, tm);
				
			// else parse like regular statement (before all this function nonsense)
			} else {
				check(!hasReturn, "Return must be last expression in function block (in function " + func.id() + "!");
				validate(s, functions, tm);
			}
		}
		
		// make sure non-void functions have return types
		if(func.type() != Type.VOID && !func.id().equals("main")) // ignore main (we cool like dat)
			check(hasReturn, "Non-void function " + func.id() + " missing return statement!");
		
		// make sure void functions DON'T have return types
		else if(func.type() == Type.VOID)
			check(!hasReturn, "Void function " + func.id() + " has return statement when it shouldn't!");
	}
	
	/**
	 * Validates a function call
	 * @param c Call to validate
	 * @param funcs Function map
	 * @param tm Type map
	 */
	public static void validate(Call c, Functions funcs, TypeMap tm){
		// get the function being called
		Function f = funcs.get(c.identifier().toString());
		
		// go through iterator for function's parameters and the calls arguments
		Iterator<Declaration> funcIt = f.params().values().iterator();
		Iterator<Expression> callIt = c.arguments();
		while(funcIt.hasNext()){
			Declaration dec = funcIt.next();
			
			// make sure there's more arguments in the call
			check(callIt.hasNext(), "Incorrect number of arguments for function call!");
			Expression exp = callIt.next();
			
			// get the type of the expression and check if it's the same as the parameter type
			Type expType = typeOf(exp, funcs, tm);
			check(dec.type() == expType, "Wrong type in function call for " + dec.variable() + " (got a " + expType + ", expected a " + dec.type() + ")");
		}
		
		// given too many arguments
		check(!callIt.hasNext(), "Incorrect number of arguments for function call!");
	}

	/**
	 * validate an expression with a given type map
	 * @param e Expression to validate
	 * @param funcs Function map to validate with
	 * @param tm Type map to validate with
	 */
	public static void validate(Expression e, Functions funcs, TypeMap tm) {
		// value, so nothing to check
		if (e instanceof Value)
			return;
		
		// function call
		if(e instanceof Call){
			validate((Call)e, funcs, tm);
			return;
		}
		
		// variable, check if it's declared
		if (e instanceof Variable) {
			Variable v = (Variable) e;
			check(tm.containsKey(v), "Caught undeclared variable in static type check: " + v);
			return;
		}
		
		// binary op
		if (e instanceof Binary) {
			Binary b = (Binary) e;
			
			Type type1 = typeOf(b.term1(), funcs, tm);
			Type type2 = typeOf(b.term2(), funcs, tm);
			
			validate(b.term1(), funcs, tm);
			validate(b.term2(), funcs, tm);
			
			// +, -, *, /
			// Int -> Int   ->   Int | Float -> Float -> Float |
			// Int -> Float -> Float | Float -> Int   -> Float
			if (b.operator().isArithmeticOp()){
				check((type1 == Type.INT   && type2 == Type.INT)   ||
				      (type1 == Type.FLOAT && type2 == Type.FLOAT) ||
				      (type1 == Type.INT   && type2 == Type.FLOAT) ||
				      (type1 == Type.FLOAT && type2 == Type.INT),
				      "Type error for arithmetic op " + b.operator()  + "; got types " + type1 + " and " + type2);
				
			// <, >, <=, >=, ==, !=
			// Int  -> Int   -> Bool | Float -> Float -> Bool |
			// Int  -> Float -> Bool | Float -> Int   -> Bool |
			// Bool -> Bool  -> Bool | Char  -> Char  -> Bool
			} else if (b.operator().isRelationalOp()){
				check((type1 == Type.INT   && type2 == Type.INT)   ||
				      (type1 == Type.FLOAT && type2 == Type.FLOAT) ||
				      (type1 == Type.INT   && type2 == Type.FLOAT) ||
				      (type1 == Type.FLOAT && type2 == Type.INT)   ||
				      (type1 == Type.BOOL && type2 == Type.BOOL)   ||
				      (type1 == Type.CHAR && type2 == Type.CHAR),
				      "Type error for relational op " + b.operator()  + "; got types " + type1 + " and " + type2);
			}
			
			// &&, ||
			// Bool -> Bool -> Bool
			else if (b.operator().isBooleanOp())
				check(type1 == Type.BOOL && type2 == Type.BOOL,
				"Caught non-bool operand for " + b.operator() + " in static type checker; got types " + type1 + " and " + type2);
			
			else
				throw new IllegalArgumentException("should never reach here");
			return;
		}
		
		// unary op
		else if(e instanceof Unary){
			Unary u = (Unary) e;
			
			Type t = typeOf(u.term, funcs, tm);
			
			validate(u.term, funcs, tm);
			
			// !
			// Bool -> Bool
			if(u.operator().isNotOp()){
				check(t == Type.BOOL,
				     "Attempted not operation on non-bool (attempted on " + t + ")");
				
			// -
			// Int -> Int | Float -> Float
			} else if(u.operator().isNegateOp()){
				check(t == Type.FLOAT || t == Type.INT,
					 "Attempted negate operation on something other than a float or int (attempted on " + t + ")");
				
			// int cast
			// Float -> Int | Char -> Int
			} else if(u.operator().isIntOp()){
				check(t == Type.FLOAT || t == Type.CHAR,
					 "Attempted int cast from something other than a float or a char (attempted on " + t + ")");
			
			// float cast
			// Int -> Float
			} else if(u.operator().isFloatOp()){
				check(t == Type.INT,
					"Attempted float cast from something other than an int (attempted on " + t + ")");
				
			// char cast
			// Int -> Char
			} else if(u.operator().isCharOp()){
				check(t == Type.INT,
					 "Attempted char cast from something other than an int (attempted on " + t + ")");
			}
		} else{
			throw new IllegalArgumentException("should never reach here");
		}
	}

	/**
	 * validate a statement with a given type map
	 * @param s Statement to validate
	 * @param funcs Function map to validate with
	 * @param tm Type map to use for verification
	 */
	public static void validate(Statement s, Functions funcs, TypeMap tm) {
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
			check(tm.containsKey(a.target()), "Target not found in type map! (target: " + a.target() + ")");
			
			validate(a.source(), funcs, tm);
			
			Type targettype = (Type) tm.get(a.target());
			Type srctype = typeOf(a.source(), funcs, tm);
			
			if (targettype != srctype) {
				// assigning an int to a float is ok
				if (targettype == Type.FLOAT)
					check(srctype == Type.INT,
					     "Caught mixed mode assignment in static type checker from " + srctype + " to " + targettype + " (target: " + a.target() + ")");
				// assigning a char to an int is ok
				else if (targettype == Type.INT)
					check(srctype == Type.CHAR || srctype == Type.FLOAT,
					     "Caught mixed mode assignment in static type checker from " + srctype + " to " + targettype + " (target: " + a.target() + ")");
				else
					check(false,
					     "Caught mixed mode assignment in static type checker from " + srctype + " to " + targettype + " (target: " + a.target() + ")");
			}
			return;
			
		// block of statements
		} else if(s instanceof Block){
			Block b = (Block) s;
			
			// check every statement in block
			Iterator<Statement> members = b.getMembers();
			while(members.hasNext())
				validate(members.next(), funcs, tm);
			
		// while loop
		} else if(s instanceof Loop) {
			Loop l = (Loop) s;
			
			// validate test and body
			validate(l.test(), funcs, tm);
			validate(l.body(), funcs, tm);
			
		// if statement
		} else if(s instanceof Conditional){
			Conditional c = (Conditional) s;
			
			validate(c.test(), funcs, tm);
			validate(c.thenBranch(), funcs, tm);
			validate(c.elseBranch(), funcs, tm);

		// error!
		}else{
			throw new IllegalArgumentException("should never reach here " + s);
		}
	}
}

