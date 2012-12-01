package syntax;

import syntax.statement.Block;
import syntax.statement.Statement;


/**
 * Program = Declarations decpart ; Block body
 */
public class Program {
	public Declarations declarations;
	public Block body;

	public Program(Declarations decpart, Block body) {
		this.declarations = decpart;
		this.body = body;
	}

	public void display() {
		System.out.println("--- Abstract Syntax ---");
		System.out.println("Declarations:");
		for(Declaration d : declarations)
			System.out.println("\t" + d.var + " :: " + d.type);
		
		System.out.println("\nBody:");
		for(Statement s : body.members){
			s.display(1);
		}
	}
}
