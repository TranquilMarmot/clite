package clite.typing;
import java.util.Iterator;

import clite.syntax.Operator;
import clite.syntax.Program;
import clite.syntax.Type;
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


/**
 * Transforms the types of any operators to match their expressions.
 * Calling transform on a program assumes that the given program
 * has already been through and passed StaticTypeChecker.validate()
 */
public class TypeTransformer {
	/**
	 * Transform a program's types
	 * @param p Program to transform
	 * @param tm Type map to use for transformation
	 * @return Transformed program
	 */
	public static Program transform(Program p) {
		// map of all global variables
		TypeMap globalMap = StaticTypeCheck.typing(p.globals());
		
		// functions list transformed functions get added to
		Functions funcs = p.functions();
		
		// transform every function's body
		for(Function f : p.functions().values()){
			// add function's params and locals to type map
			TypeMap newMap = new TypeMap();
			newMap.putAll(globalMap);
			newMap.putAll(StaticTypeCheck.typing(f.params()));
			newMap.putAll(StaticTypeCheck.typing(f.locals()));
			Block transformedBody = (Block) transform(f.body(), funcs, newMap);
			
			// add the function into the new functions map
			funcs.put(
					f.id(),
					new Function(
							f.type(),
							f.id(),
							f.params(),
							f.locals(),
							transformedBody
					)
			);
		}
		
		return new Program(p.globals(), funcs);
	}

	/**
	 * Transform a statement's types
	 * @param s Statement to transform
	 * @param tm Type map to use for transformation
	 * @return Transformed statement
	 */
	public static Statement transform(Statement s, Functions funcs, TypeMap tm) {
		// skip any skips
		if (s instanceof Skip ||
			s instanceof Function ||
			s instanceof Return ||
			s instanceof Call)
			return s;
		
		// assignment statement
		if (s instanceof Assignment) {
			Assignment a = (Assignment) s;
			
			Variable target = a.target();
			Expression src = a.source();
			
			Type targettype = (Type) tm.get(a.target());
			Type srctype = StaticTypeCheck.typeOf(a.source(), funcs, tm);
			
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
			
			Statement tbr = transform(c.thenBranch(), funcs, tm);
			Statement ebr = transform(c.elseBranch(), funcs, tm);
			
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
				out.addMember(transform(members.next(), funcs, tm));
			
			return out;
		}
		
		throw new IllegalArgumentException("should never reach here");
	}
}

