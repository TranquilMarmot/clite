package typing;
import java.util.Iterator;

import syntax.Operator;
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
import syntax.value.BoolValue;
import syntax.value.CharValue;
import syntax.value.FloatValue;
import syntax.value.IntValue;
import syntax.value.Value;

/**
 * Following is the semantics class:
 * The meaning M of a Statement is a State
 * The meaning M of a Expression is a Value
 */
public class Interpreter {
	/**
	 * Interprets a binary statement	 
	 * Dynamically casts int to float if applying an op
	 * to a float and int
	 * 
	 * @param b Binary op to interpret
	 * @param state State to apply binary op to
	 * @return State after applying binary op
	 * @throws IllegalArgumentException If binary op has illegal types
	 */
	public static Value interpretBinary(Binary b, State state) throws IllegalArgumentException {
		Operator op = b.operator();
		Value v1 = interpret(b.term1(), state);
		Value v2 = interpret(b.term2(), state);
		
		//StaticTypeCheck.check(v1.type() == v2.type(), "mismatched types");
		StaticTypeCheck.check(!v1.undefined() || !v2.undefined(), "reference to undef value in binary op");
		
		/*   ARITHMETIC OP  --    +, -, *, /   */
		if (op.isArithmeticOp()) {
			// int (+, -, *, /) int
			if (v1.type() == Type.INT && v1.type() == Type.INT) {
				if (op.val.equals(Operator.PLUS))
					return new IntValue(v1.intValue() + v2.intValue());
				if (op.val.equals(Operator.MINUS))
					return new IntValue(v1.intValue() - v2.intValue());
				if (op.val.equals(Operator.TIMES))
					return new IntValue(v1.intValue() * v2.intValue());
				if (op.val.equals(Operator.DIV))
					return new IntValue(v1.intValue() / v2.intValue());
				
			// float (+, -, *,/) float 
			} else if(v1.type() == Type.FLOAT && v2.type() == Type.FLOAT) {
				if (op.val.equals(Operator.PLUS))
					return new FloatValue(v1.floatValue() + v2.floatValue());
				if (op.val.equals(Operator.MINUS))
					return new FloatValue(v1.floatValue() - v2.floatValue());
				if (op.val.equals(Operator.TIMES))
					return new FloatValue(v1.floatValue() * v2.floatValue());
				if (op.val.equals(Operator.DIV))
					return new FloatValue(v1.floatValue() / v2.floatValue());
				
			// if attempting op on int and float, cast int to float and do it again
			} else if((v1.type() == Type.INT && v2.type() == Type.FLOAT) ||
					  (v1.type() == Type.FLOAT && v2.type() == Type.INT)) {
				if(v1.type() == Type.INT)
					v1 = new FloatValue((float)v1.intValue());
				else if(v2.type() == Type.INT)
					v2 = new FloatValue((float)v2.intValue());
				return interpretBinary(new Binary(op, v1, v2), state);
			} else {
				throw new IllegalArgumentException("Attemped arithmetic op on a " + v1.type() + " and a " + v2.type() + ", not allowed (v1: " + v1 + " v2: " + v2 + ")");
			}
			
		/*   BOOLEAN OP --  &&, ||  */
		} else if (op.isBooleanOp()){
			// boolean op and only be performed on booleans
			if(!(v1.type() == Type.BOOL && v1.type() == Type.BOOL))
				throw new IllegalArgumentException("Attemped boolean op on " + v1.type() + ", not allowed");
			else {
				if(op.val.equals(Operator.AND))
					return new BoolValue(v1.boolValue() && v2.boolValue());
				else if(op.val.equals(Operator.OR))
					return new BoolValue(v1.boolValue() || v2.boolValue());
			}
			
		/*   RELATIONAL OP   --   <, >, <=, >=, ==, !=    */
		} else if(op.isRelationalOp()){
			// int (<, >, <=, >=, ==, !=) int
			if (v1.type() == Type.INT && v1.type() == Type.INT) {
				if(op.val.equals(Operator.LT))
					return new BoolValue(v1.intValue() < v2.intValue());
				else if(op.val.equals(Operator.GT))
					return new BoolValue(v1.intValue() > v2.intValue());
				else if(op.val.equals(Operator.LE))
					return new BoolValue(v1.intValue() <= v2.intValue());
				else if(op.val.equals(Operator.GE))
					return new BoolValue(v1.intValue() >= v2.intValue());
				else if(op.val.equals(Operator.EQ))
					return new BoolValue(v1.intValue() == v2.intValue());
				else if(op.val.equals(Operator.NE))
					return new BoolValue(v1.intValue() != v2.intValue());
				
			// float (<, >, <=, >=, ==, !=) float
			} else if(v1.type() == Type.FLOAT && v1.type() == Type.FLOAT){
				if(op.val.equals(Operator.LT))
					return new BoolValue(v1.floatValue() < v2.floatValue());
				else if(op.val.equals(Operator.GT))
					return new BoolValue(v1.floatValue() > v2.floatValue());
				else if(op.val.equals(Operator.LE))
					return new BoolValue(v1.floatValue() <= v2.floatValue());
				else if(op.val.equals(Operator.GE))
					return new BoolValue(v1.floatValue() >= v2.floatValue());
				else if(op.val.equals(Operator.EQ))
					return new BoolValue(v1.floatValue() == v2.floatValue());
				else if(op.val.equals(Operator.NE))
					return new BoolValue(v1.floatValue() != v2.floatValue());
				
			// if attempting op on int and float, cast int to float and do it again
			} else if((v1.type() == Type.INT && v2.type() == Type.FLOAT) ||
					  (v1.type() == Type.FLOAT && v2.type() == Type.INT)) {
				if(v1.type() == Type.INT)
					v1 = new FloatValue((float)v1.intValue());
				else if(v2.type() == Type.INT)
					v2 = new FloatValue((float)v2.intValue());
				return interpretBinary(new Binary(op, v1, v2), state);
				
			// chars
			} else if(v1.type() == Type.CHAR && v2.type() == Type.CHAR){
				if(op.val.equals(Operator.LT))
					return new BoolValue(v1.charValue() < v2.charValue());
				else if(op.val.equals(Operator.GT))
					return new BoolValue(v1.charValue() > v2.charValue());
				else if(op.val.equals(Operator.LE))
					return new BoolValue(v1.charValue() <= v2.charValue());
				else if(op.val.equals(Operator.GE))
					return new BoolValue(v1.charValue() >= v2.charValue());
				else if(op.val.equals(Operator.EQ))
					return new BoolValue(v1.charValue() == v2.charValue());
				else if(op.val.equals(Operator.NE))
					return new BoolValue(v1.charValue() != v2.charValue());
			}
			
			// bools can be compared with == and !=
			else if(v1.type() == Type.BOOL && v2.type() == Type.BOOL){
				if(op.val.equals(Operator.EQ))
					return new BoolValue(v1.boolValue() == v2.boolValue());
				else if(op.val.equals(Operator.NE))
					return new BoolValue(v1.boolValue() != v2.boolValue());
				else
					throw new IllegalArgumentException("Attempted illegal relational op " + op + " on two booleans (v1: " + v1 + " v2: " + v2 + ")");
			}
			
			else {
				throw new IllegalArgumentException("Attemped relational op on a " + v1.type() + " and a " + v2.type() + ", not allowed (v1: " + v1 + " v2: " + v2 + ")");
			}
		}
		throw new IllegalArgumentException("should never reach here (in DynamicTyping.applyBinary)");
	}

	/**
	 * Interprets a unary
	 * 
	 * @param u Unary to interpret
	 * @param state State to apply unary to
	 * @return State after applying unary
	 * @throws IllegalArgumentException If unary has invalid typse
	 */
	public static Value interpretUnary(Unary u, State state) throws IllegalArgumentException {
		Operator op = u.operator();
		Value v = interpret(u.term, state);
		StaticTypeCheck.check(!v.undefined(), "reference to undef value in unary op");
		
		// boolean not
		if (op.val.equals(Operator.NOT)){
			if(v.type() != Type.BOOL)
				throw new IllegalArgumentException("Can only apply ! operator to bool (attempted on " + v + ")");
			else
				return new BoolValue(!v.boolValue());
		}

		// negate
		else if(op.val.equals(Operator.NEG)){
			if(v.type() == Type.FLOAT)
				return new FloatValue(-v.floatValue());
			else if(v.type() == Type.INT)
				return new IntValue(-v.intValue());
			else
				throw new IllegalArgumentException("Can only apply - operator to int or float (attempted on " + v + ")");
		}
		
		// float cast
		else if (op.val.equals(Operator.FLOAT)){
			if(v.type() != Type.INT)
				throw new IllegalArgumentException("Can only cast int to float (tried to cast " + v + ")");
			else
				return new FloatValue((float)v.intValue());
		}
		
		// int cast
		else if (op.val.equals(Operator.INT)){
			if(v.type() == Type.FLOAT)
				return new IntValue((int)v.floatValue());
			else if(v.type() == Type.CHAR)
				return new IntValue((int)v.charValue());
			else
				throw new IllegalArgumentException("Can only cast float or char to int (tried to cast " + v + ")");
		}
		
		
		// char cast
		else if(op.val.equals(Operator.CHAR)){
			if(v.type() == Type.INT)
				return new CharValue((char)v.intValue());
			else
				throw new IllegalArgumentException("Can only cast int to char (tried to cast " + v + ")");
		}
		
		throw new IllegalArgumentException("should never reach here");
	}
	
	/**
	 * Get an initial state from a set of declarations
	 * @param d Declarations to create state from
	 * @return Initial state representing given declarations with uninitialized variables
	 */
	public static State initialState(Declarations d) {
		State state = new State();
		
		// allocate space for each variable in the declarations
		for (Declaration decl : d)
			state.put(decl.variable(), Value.mkValue(decl.type()));
		
		return state;
	}
	
	/**
	 * Interprets a program and returns the final state of the program
	 * @param p Program to interpret
	 * @return Final state of program
	 */
	public static State interpret(Program p) {
		return interpret(p.body(), initialState(p.declarations()));
	}

	/**
	 * Interprets a statement
	 * @param s Statement to interpret
	 * @param state State up to this point
	 * @return State after interpreting statement
	 */
	public static State interpret(Statement s, State state) {
		if (s instanceof Skip)
			return state;
		if (s instanceof Assignment)
			return interpret((Assignment) s, state);
		if (s instanceof Conditional)
			return interpret((Conditional) s, state);
		if (s instanceof Loop)
			return interpret((Loop) s, state);
		if (s instanceof Block)
			return interpret((Block) s, state);
		throw new IllegalArgumentException("should never reach here");
	}

	/**
	 * Interprets an assignment
	 * @param a Assignment to interpret
	 * @param state State up to this point
	 * @return State after interpreting assignment
	 */
	public static State interpret(Assignment a, State state) {
		return state.onion(a.target(), interpret(a.source(), state));
	}

	/**
	 * Interprets a block
	 * @param b Block to interpret
	 * @param state State up to this point
	 * @return State after interpreting block
	 */
	public static State interpret(Block b, State state) {
		Iterator<Statement> members = b.getMembers();
		while(members.hasNext())
			state = interpret(members.next(), state);
		
		return state;
	}

	/**
	 * Interpret a conditional statement
	 * @param c Conditional to interpret
	 * @param state State up to this point
	 * @return State after interpreting conditional
	 */
	public static State interpret(Conditional c, State state) {
		if (interpret(c.test(), state).boolValue())
			return interpret(c.thenBranch(), state);
		else
			return interpret(c.elseBranch(), state);
	}

	/**
	 * Interpret a while loop
	 * @param l Loop to interpret
	 * @param state State up to this point
	 * @return State after interpreting loop
	 */
	public static State interpret(Loop l, State state) {
		if (interpret(l.test(), state).boolValue())
			return interpret(l, interpret(l.body(), state));
		else
			return state;
	}

	/**
	 * Interpret an expression
	 * @param e Expression to interpret
	 * @param state State up to this point
	 * @return State after interpreting expression
	 */
	public static Value interpret(Expression e, State state) {
		if (e instanceof Value)
			return (Value) e;
		if (e instanceof Variable)
			return (Value) (state.get(e));
		if (e instanceof Binary) {
			Binary b = (Binary) e;
			return interpretBinary(b, state);
		}
		if (e instanceof Unary) {
			Unary u = (Unary) e;
			return interpretUnary(u, state);
		}
		throw new IllegalArgumentException("should never reach here");
	}
}
