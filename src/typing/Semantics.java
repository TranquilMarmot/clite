package typing;
import abstractsyntax.Assignment;
import abstractsyntax.Declaration;
import abstractsyntax.Declarations;
import abstractsyntax.Operator;
import abstractsyntax.Program;
import abstractsyntax.Skip;
import abstractsyntax.expression.Binary;
import abstractsyntax.expression.Expression;
import abstractsyntax.expression.Unary;
import abstractsyntax.expression.Variable;
import abstractsyntax.statement.Block;
import abstractsyntax.statement.Conditional;
import abstractsyntax.statement.Loop;
import abstractsyntax.statement.Statement;
import abstractsyntax.value.BoolValue;
import abstractsyntax.value.CharValue;
import abstractsyntax.value.FloatValue;
import abstractsyntax.value.IntValue;
import abstractsyntax.value.Value;

/**
 * Following is the semantics class:
 * The meaning M of a Statement is a State
 * The meaning M of a Expression is a Value
 */
public class Semantics {
	State M(Program p) {
		return M(p.body, initialState(p.declarations));
	}

	State initialState(Declarations d) {
		State state = new State();
		//Value intUndef = new IntValue();
		for (Declaration decl : d)
			state.put(decl.var, Value.mkValue(decl.type));
		return state;
	}

	State M(Statement s, State state) {
		if (s instanceof Skip)
			return M((Skip) s, state);
		if (s instanceof Assignment)
			return M((Assignment) s, state);
		if (s instanceof Conditional)
			return M((Conditional) s, state);
		if (s instanceof Loop)
			return M((Loop) s, state);
		if (s instanceof Block)
			return M((Block) s, state);
		throw new IllegalArgumentException("should never reach here");
	}

	State M(Skip s, State state) {
		return state;
	}

	State M(Assignment a, State state) {
		return state.onion(a.target, M(a.source, state));
	}

	State M(Block b, State state) {
		for (Statement s : b.members)
			state = M(s, state);
		return state;
	}

	State M(Conditional c, State state) {
		if (M(c.test, state).boolValue())
			return M(c.thenbranch, state);
		else
			return M(c.elsebranch, state);
	}

	State M(Loop l, State state) {
		if (M(l.test, state).boolValue())
			return M(l, M(l.body, state));
		else
			return state;
	}

	Value applyBinary(Operator op, Value v1, Value v2) {
		StaticTypeCheck.check(!v1.isUndef() && !v2.isUndef(),
				"reference to undef value");
		if (op.val.equals(Operator.INT_PLUS))
			return new IntValue(v1.intValue() + v2.intValue());
		if (op.val.equals(Operator.INT_MINUS))
			return new IntValue(v1.intValue() - v2.intValue());
		if (op.val.equals(Operator.INT_TIMES))
			return new IntValue(v1.intValue() * v2.intValue());
		if (op.val.equals(Operator.INT_DIV))
			return new IntValue(v1.intValue() / v2.intValue());
		// TODO student exercise
		throw new IllegalArgumentException("should never reach here");
	}

	Value applyUnary(Operator op, Value v) {
		StaticTypeCheck.check(!v.isUndef(), "reference to undef value");
		if (op.val.equals(Operator.NOT))
			return new BoolValue(!v.boolValue());
		else if (op.val.equals(Operator.INT_NEG))
			return new IntValue(-v.intValue());
		else if (op.val.equals(Operator.FLOAT_NEG))
			return new FloatValue(-v.floatValue());
		else if (op.val.equals(Operator.I2F))
			return new FloatValue((float) (v.intValue()));
		else if (op.val.equals(Operator.F2I))
			return new IntValue((int) (v.floatValue()));
		else if (op.val.equals(Operator.C2I))
			return new IntValue((int) (v.charValue()));
		else if (op.val.equals(Operator.I2C))
			return new CharValue((char) (v.intValue()));
		throw new IllegalArgumentException("should never reach here");
	}

	Value M(Expression e, State state) {
		if (e instanceof Value)
			return (Value) e;
		if (e instanceof Variable)
			return (Value) (state.get(e));
		if (e instanceof Binary) {
			Binary b = (Binary) e;
			return applyBinary(b.op, M(b.term1, state), M(b.term2, state));
		}
		if (e instanceof Unary) {
			Unary u = (Unary) e;
			return applyUnary(u.op, M(u.term, state));
		}
		throw new IllegalArgumentException("should never reach here");
	}

	public static void main(String args[]) {
		/*
		Parser parser = new Parser(new Lexer(args[0]));
		Program prog = parser.program();
		// prog.display(); // TODO student exercise
		System.out.println("\nBegin type checking...");
		System.out.println("Type map:");
		TypeMap map = StaticTypeCheck.typing(prog.declarations);
		// map.display(); // TODO student exercise
		StaticTypeCheck.V(prog);
		Program out = TypeTransformer.T(prog, map);
		System.out.println("Output AST");
		// out.display(); // TODO student exercise
		Semantics semantics = new Semantics();
		State state = semantics.M(out);
		System.out.println("Final State");
		// state.display( ); // TODO student exercise
		 */
	}
}
