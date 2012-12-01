package typing;
import abstractsyntax.Operator;
import abstractsyntax.Program;
import abstractsyntax.Type;
import abstractsyntax.expression.Binary;
import abstractsyntax.expression.Expression;
import abstractsyntax.expression.Unary;
import abstractsyntax.expression.Variable;
import abstractsyntax.value.BoolValue;
import abstractsyntax.value.CharValue;
import abstractsyntax.value.FloatValue;
import abstractsyntax.value.IntValue;
import abstractsyntax.value.Value;

/**
 * Following is the semantics class
 * for a dynamically typed language.
 * The meaning M of a Statement is a State.
 * The meaning M of a Expression is a Value.
 */
public class DynamicTyping extends Semantics {

	State M(Program p) {
		return M(p.body, new State());
	}

	Value applyBinary(Operator op, Value v1, Value v2) {
		StaticTypeCheck.check(v1.type() == v2.type(), "mismatched types");
		if (op.ArithmeticOp()) {
			if (v1.type() == Type.INT) {
				if (op.val.equals(Operator.PLUS))
					return new IntValue(v1.intValue() + v2.intValue());
				if (op.val.equals(Operator.MINUS))
					return new IntValue(v1.intValue() - v2.intValue());
				if (op.val.equals(Operator.TIMES))
					return new IntValue(v1.intValue() * v2.intValue());
				if (op.val.equals(Operator.DIV))
					return new IntValue(v1.intValue() / v2.intValue());
			}
			// TODO student exercise
		}
		// TODO student exercise
		throw new IllegalArgumentException("should never reach here");
	}

	Value applyUnary(Operator op, Value v) {
		if (op.val.equals(Operator.NOT))
			return new BoolValue(!v.boolValue());
		else if (op.val.equals(Operator.NEG))
			return new IntValue(-v.intValue());
		else if (op.val.equals(Operator.NEG))
			return new FloatValue(-v.floatValue());
		else if (op.val.equals(Operator.FLOAT))
			return new FloatValue((float) (v.intValue()));
		else if (op.val.equals(Operator.INT))
			return new IntValue((int) (v.floatValue()));
		else if (op.val.equals(Operator.INT))
			return new IntValue((int) (v.charValue()));
		else if (op.val.equals(Operator.CHAR))
			return new CharValue((char) (v.intValue()));
		throw new IllegalArgumentException("should never reach here");
	}

	Value M(Expression e, State sigma) {
		if (e instanceof Value)
			return (Value) e;
		if (e instanceof Variable) {
			StaticTypeCheck.check(sigma.containsKey(e),
					"reference to undefined variable");
			return (Value) (sigma.get(e));
		}
		if (e instanceof Binary) {
			Binary b = (Binary) e;
			return applyBinary(b.op, M(b.term1, sigma), M(b.term2, sigma));
		}
		if (e instanceof Unary) {
			Unary u = (Unary) e;
			return applyUnary(u.op, M(u.term, sigma));
		}
		throw new IllegalArgumentException("should never reach here");
	}

	public static void main(String args[]) {
		//Parser parser = new Parser(new Lexer(args[0]));
		//Program prog = parser.program();
		// prog.display(); // TODO student exercise
		//DynamicTyping dynamic = new DynamicTyping();
		//State state = dynamic.M(prog);
		//System.out.println("Final State");
		// state.display( ); // TODO student exercise
	}
}
