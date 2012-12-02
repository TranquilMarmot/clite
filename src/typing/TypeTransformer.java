package typing;
import java.util.Iterator;

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

/**
 * Transforms the types of any operators to match their expressions
 */
public class TypeTransformer {
	/**
	 * Transform a program's types
	 * @param p Program to transform
	 * @param tm Type map to use for transformation
	 * @return Transformed program
	 */
	public static Program transform(Program p, TypeMap tm) {
		Block body = (Block) transform(p.body(), tm);
		return new Program(p.declarations(), body);
	}

	/**
	 * Transform a statement's types
	 * @param s Statement to transform
	 * @param tm Type map to use for transformation
	 * @return Transformed statement
	 */
	public static Statement transform(Statement s, TypeMap tm) {
		// skip any skips
		if (s instanceof Skip)
			return s;
		
		// assignment statement
		if (s instanceof Assignment) {
			Assignment a = (Assignment) s;
			
			Variable target = a.target();
			Expression src = a.source();
			
			Type targettype = (Type) tm.get(a.target());
			Type srctype = StaticTypeCheck.typeOf(a.source(), tm);
			
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
		
		// conditional statement
		if (s instanceof Conditional) {
			Conditional c = (Conditional) s;
			
			Expression test = c.test();
			
			Statement tbr = transform(c.thenBranch(), tm);
			Statement ebr = transform(c.elseBranch(), tm);
			
			return new Conditional(test, tbr, ebr);
		}
		
		// while loop
		if (s instanceof Loop) {
			Loop l = (Loop) s;
			
			Expression test = l.test();
			Statement body = l.body();
			
			return new Loop(test, body);
		}
		
		// block of statments
		if (s instanceof Block) {
			Block b = (Block) s;
			
			Block out = new Block();
			
			Iterator<Statement> members = b.getMembers();
			while(members.hasNext())
				out.addMember(transform(members.next(), tm));
			
			return out;
		}
		throw new IllegalArgumentException("should never reach here");
	}
}

