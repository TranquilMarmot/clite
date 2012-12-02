package typing;
import syntax.Operator;
import syntax.Program;
import syntax.Type;
import syntax.expression.Expression;
import syntax.expression.Unary;
import syntax.expression.Variable;
import syntax.statement.Assignment;
import syntax.statement.Block;
import syntax.statement.Conditional;
import syntax.statement.Loop;
import syntax.statement.Skip;
import syntax.statement.Statement;


public class TypeTransformer {
	public static Program T(Program p, TypeMap tm) {
		Block body = (Block) T(p.body, tm);
		return new Program(p.declarations, body);
	}

	public static Statement T(Statement s, TypeMap tm) {
		// skip any skips
		if (s instanceof Skip)
			return s;
		
		// assignment statement
		if (s instanceof Assignment) {
			Assignment a = (Assignment) s;
			
			Variable target = a.target;
			Expression src = a.source;
			
			Type targettype = (Type) tm.get(a.target);
			Type srctype = StaticTypeCheck.typeOf(a.source, tm);
			
			System.out.println("t: " + targettype + " | " + "s: " + srctype);
			
			if (targettype == Type.FLOAT) {
				if (srctype == Type.INT) {
					src = new Unary(new Operator(Operator.FLOAT), src);
					srctype = Type.FLOAT;
				}
				
			} else if (targettype == Type.INT) {
				if (srctype == Type.CHAR) {
					src = new Unary(new Operator(Operator.INT), src);
					srctype = Type.INT;
				} else if(srctype == Type.FLOAT){
					src = new Unary(new Operator(Operator.INT), src);
					srctype = Type.INT;
				}
			}
			
			
			StaticTypeCheck.check(targettype == srctype, "bug in assignment to " + target);
			return new Assignment(target, src);
		}
		
		if (s instanceof Conditional) {
			Conditional c = (Conditional) s;
			
			Expression test = c.test;
			
			Statement tbr = T(c.thenbranch, tm);
			Statement ebr = T(c.elsebranch, tm);
			
			return new Conditional(test, tbr, ebr);
		}
		if (s instanceof Loop) {
			Loop l = (Loop) s;
			
			Expression test = l.test;
			Statement body = l.body;
			
			return new Loop(test, body);
		}
		if (s instanceof Block) {
			Block b = (Block) s;
			
			Block out = new Block();
			
			for (Statement stmt : b.members)
				out.members.add(T(stmt, tm));
			
			return out;
		}
		throw new IllegalArgumentException("should never reach here");
	}
} // class TypeTransformer

