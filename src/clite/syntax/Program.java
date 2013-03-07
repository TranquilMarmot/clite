package clite.syntax;

import java.util.Iterator;

import clite.syntax.declaration.Declaration;
import clite.syntax.declaration.Declarations;
import clite.syntax.statement.Block;
import clite.syntax.statement.Statement;



/**
 * Program = Declarations decpart ; Block body
 */
public class Program {
	/** Declarations for all variables in program */
	private Declarations declarations;
	/** Body of program */
	private Block body;

	/**
	 * @param declarations Declarations to use for program
	 * @param body Body of program
	 */
	public Program(Declarations declarations, Block body) {
		this.declarations = declarations;
		this.body = body;
	}
	
	/** @return Program's declarations */
	public Declarations declarations(){ return declarations; }
	/** @return Body of program */
	public Block body(){ return body; }

	/**
	 * Prints out this program's declarations and body
	 */
	public void display() {
		//System.out.println("--- Abstract Syntax ---");
		System.out.println("Declarations:");
		for(Declaration d : declarations)
			System.out.println("\t" + d.variable() + " :: " + d.type());
		
		System.out.println("\nBody:");
		Iterator<Statement> members = body.getMembers();
		while(members.hasNext())
			members.next().display(1);
	}
}
