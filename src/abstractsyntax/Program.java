package abstractsyntax;

import abstractsyntax.statement.Block;
import abstractsyntax.statement.Statement;


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
		for(Declaration d : declarations){
			System.out.println(d.type + " : " + d.var);
		}
		
		for(Statement s : body.members){
			System.out.println(s.toString());
		}
	}
}
