package clite.interpreter;
import java.util.Iterator;

import clite.syntax.Operator;
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
import clite.syntax.value.BoolValue;
import clite.syntax.value.CharValue;
import clite.syntax.value.FloatValue;
import clite.syntax.value.IntValue;
import clite.syntax.value.Value;
import clite.typing.StaticTypeCheck;


/**
 * Following is the semantics class:
 * The meaning M of a Statement is a State
 * The meaning M of a Expression is a Value
 */
public class Interpreter {
	/**
	 * Interprets a program and returns the final state of the program
	 * @param p Program to interpret
	 * @return Final state of program
	 */
	public static State interpret(Program p) {
		return interpret(p.functions().get("main").body(), p.functions(), initialState(p.globals()));
	}
	
	/**
	 * Get an initial state from a set of declarations
	 * @param d Declarations to create state from
	 * @return Initial state representing given declarations with uninitialized variables
	 */
	public static State initialState(Declarations d) {
		State state = new State();
		
		// allocate space for each variable in the declarations
		for (Declaration decl : d.values())
			state.put(decl.variable(), Value.mkValue(decl.type()));
		
		return state;
	}

	/**
	 * Interprets a statement
	 * @param s Statement to interpret
	 * @param funcs Map of functions
	 * @param state State up to this point
	 * @return State after interpreting statement
	 */
	public static State interpret(Statement s, Functions funcs, State state) {
		if (s instanceof Skip)
			return state;
		if (s instanceof Assignment)
			return interpret((Assignment) s, funcs, state);
		if (s instanceof Conditional)
			return interpret((Conditional) s, funcs, state);
		if (s instanceof Loop)
			return interpret((Loop) s, funcs, state);
		if (s instanceof Block)
			return interpret((Block) s, funcs, state);
		if(s instanceof Call)
			return interpretCallStatement((Call)s, funcs, state);
		throw new IllegalArgumentException("should never reach here");
	}

	/**
	 * Interprets an assignment
	 * @param a Assignment to interpret
	 * @param funcs Map of functions
	 * @param state State up to this point
	 * @return State after interpreting assignment
	 */
	public static State interpret(Assignment a, Functions funcs, State state) {
		return state.onion(a.target(), interpret(a.source(), funcs, state));
	}

	/**
	 * Interprets a block
	 * @param b Block to interpret
	 * @param funcs Map of functions
	 * @param state State up to this point
	 * @return State after interpreting block
	 */
	public static State interpret(Block b, Functions funcs, State state) {
		Iterator<Statement> members = b.getMembers();
		while(members.hasNext()) 
			state = interpret(members.next(), funcs, state);
		
		return state;
	}

	/**
	 * Interpret a conditional statement
	 * @param c Conditional to interpret
	 * @param funcs Map of functions
	 * @param state State up to this point
	 * @return State after interpreting conditional
	 */
	public static State interpret(Conditional c, Functions funcs, State state) {
		if (interpret(c.test(), funcs, state).boolValue())
			return interpret(c.thenBranch(), funcs, state);
		else
			return interpret(c.elseBranch(), funcs, state);
	}

	/**
	 * Interpret a while loop
	 * @param l Loop to interpret
	 * @param funcs Map of functions
	 * @param state State up to this point
	 * @return State after interpreting loop
	 */
	public static State interpret(Loop l, Functions funcs, State state) {
		if (interpret(l.test(), funcs, state).boolValue())
			return interpret(l, funcs, interpret(l.body(), funcs, state));
		else
			return state;
	}
	
	/**
	 * Interpret an expression
	 * @param e Expression to interpret
	 * @param funcs Map of functions
	 * @param state State up to this point
	 * @return State after interpreting expression
	 */
	public static Value interpret(Expression e, Functions funcs, State state) {
		if (e instanceof Value)
			return (Value) e;
		if (e instanceof Variable)
			return (Value) (state.get(e));
		if (e instanceof Binary) 
			return interpret((Binary) e, funcs, state);
		if (e instanceof Unary) 
			return interpret((Unary) e, funcs, state);
		if(e instanceof Call)
			return interpretCallExpression((Call)e, funcs, state);
		throw new IllegalArgumentException("should never reach here");
	}
	
	/**
	 * Interprets a function call but ignores the return value
	 * @param c Function to call
	 * @param funcs Map of functions
	 * @param state State when function is called
	 * @return State after calling given function
	 */
	public static State interpretCallStatement(Call c, Functions funcs, State state){
		// grab the function from the call
		Function f = funcs.get(c.identifier().toString());
		
		// add function's locals to the state
		for (Declaration decl : f.locals().values())
			state.put(decl.variable(), Value.mkValue(decl.type()));

		// add parameters as their name defined by the function
		Iterator<Expression> argIt = c.arguments();
		Iterator<Declaration> funcIt = f.params().values().iterator(); 
		while(argIt.hasNext()){
			Expression exp = argIt.next();
			Declaration dec = funcIt.next();
			Value v = interpret(exp, funcs, state);
			state.put(dec.variable(), v);
		}
		
		// interpret every statement in the function's body
		Iterator<Statement> members = f.body().getMembers();
		while(members.hasNext()){
			Statement s = members.next();
			
			// if we hit the return statement, we can return the value of it's expression
			if(s instanceof Return){
				// remove locals and parameters from the state
				for (Declaration decl : f.locals().values())
					state.remove(decl.variable());
				for (Declaration decl : f.params().values())
					state.remove(decl.variable());
				return state;
			}// else, we interpret the statement
			else{
				state = interpret(s, funcs, state);
			}
		}
		
		// remove locals and parameters from the state
		for (Declaration decl : f.locals().values())
			state.remove(decl.variable());
		for (Declaration decl : f.params().values())
			state.remove(decl.variable());
		

		
		return state;
	}
	
	/**
	 * Interpret a function call to get a value
	 * @param c Call to interpret
	 * @param funcs Map of functions
	 * @param state State when function is called
	 * @return Value of function's return statement
	 */
	public static Value interpretCallExpression(Call c, Functions funcs, State state){
		// grab functions from call
		Function f = funcs.get(c.identifier().toString());
		
		// add function's locals to the state
		for (Declaration decl : f.locals().values())
			state.put(decl.variable(), Value.mkValue(decl.type()));

		// add parameters as their name defined by the function
		Iterator<Expression> argIt = c.arguments();
		Iterator<Declaration> funcIt = f.params().values().iterator(); 
		while(argIt.hasNext()){
			Expression exp = argIt.next();
			Declaration dec = funcIt.next();
			Value v = interpret(exp, funcs, state);
			state.put(dec.variable(), v);
		}
		
		// interpret every statement in the function's body
		Iterator<Statement> members = f.body().getMembers();
		while(members.hasNext()){
			Statement s = members.next();
			
			// if we hit the return statement, we can return the value of it's expression
			if(s instanceof Return){
				// interpret the return statement's expression
				Value v =  interpret(((Return)s).result(), funcs, state);
				
				// remove locals and parameters from the state
				for (Declaration decl : f.locals().values())
					state.remove(decl.variable());
				for (Declaration decl : f.params().values())
					state.remove(decl.variable());
				
				return v;
				
			
			} else if(hasReturn(s)){
				return interpretWithReturn(s, funcs, state);
			// else, we interpret the statement
			} else {
				state = interpret(s, funcs, state);
			}
		}
		throw new IllegalArgumentException("attemped to interpret function call with no return as expression");
	}
	
	public static Value interpretWithReturn(Statement s, Functions funcs, State state){
		if(s instanceof Skip){
			return null;
		}if (s instanceof Conditional){
			Conditional c = (Conditional)s;
			Statement chosen;
			
			if (interpret(c.test(), funcs, state).boolValue())
				chosen = c.thenBranch();
			else
				chosen = c.elseBranch();
			
			if(chosen instanceof Return)
				return interpret(((Return)s).result(), funcs, state);
			else
				return interpretWithReturn(chosen, funcs, state);
		}
		
		if (s instanceof Loop){
			Loop l = (Loop)s;
			if (interpret(l.test(), funcs, state).boolValue())
				return interpretWithReturn(l, funcs, interpret(l.body(), funcs, state));
		}
		
		if (s instanceof Block){
			Block b = (Block) s;
			Iterator<Statement> it = b.getMembers();
			while(it.hasNext()){
				Statement bs = it.next();
				if(bs instanceof Return)
					return interpret(((Return)s).result(), funcs, state);
				else 
					state = interpret(s, funcs, state);
			}
		}
		
		if(s instanceof Return)
			return interpret(((Return)s).result(), funcs, state);
		
		System.out.println(s.getClass().getCanonicalName());
		throw new IllegalArgumentException("tried to interpret statement with return when it didn't have a return");
	}
	
	public static boolean hasReturn(Statement s){
		if (s instanceof Skip || s instanceof Assignment)
			return false;
		if (s instanceof Conditional){
			Conditional c = (Conditional)s;
			return hasReturn(c.thenBranch()) || hasReturn(c.elseBranch());
		}if (s instanceof Loop){
			return hasReturn(((Loop)s).body());
		}if (s instanceof Block){
			Block b = (Block) s;
			Iterator<Statement> it = b.getMembers();
			while(it.hasNext())
				if(hasReturn(it.next()))
					return true;
			return false;
		}if(s instanceof Return)
			return true;
		throw new IllegalArgumentException("should never reach here");
	}
	
	/**
	 * Interprets a unary
	 * 
	 * @param u Unary to interpret
	 * @param funcs Map of functions
	 * @param state State to apply unary to
	 * @return State after applying unary
	 * @throws IllegalArgumentException If unary has invalid typse
	 */
	public static Value interpret(Unary u, Functions funcs, State state) throws IllegalArgumentException {
		Operator op = u.operator();
		Value v = interpret(u.term, funcs, state);
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
	 * Interprets a binary statement	 
	 * Dynamically casts int to float if applying an op
	 * 
	 * @param b Binary op to interpret
	 * @param state State to apply binary op to
	 * @param funcs Map of functions
	 * @return State after applying binary op
	 * @throws IllegalArgumentException If binary op has illegal types
	 */
	public static Value interpret(Binary b, Functions funcs, State state) throws IllegalArgumentException {
		Operator op = b.operator();
		// interpret the operator's terms
		Value v1 = interpret(b.term1(), funcs, state);
		Value v2 = interpret(b.term2(), funcs, state);
		
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
				return interpret(new Binary(op, v1, v2), funcs, state);
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
				return interpret(new Binary(op, v1, v2), funcs, state);
				
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
}
