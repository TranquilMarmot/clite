package clite.syntax;

import java.util.Iterator;

import clite.function.Function;
import clite.function.Functions;
import clite.syntax.declaration.Declaration;
import clite.syntax.declaration.Declarations;



/**
 * Program = Declarations globals; Functions functions
 */
public class Program {
	/** Declarations for all variables in program */
	private Declarations globals;
	/** Functions in program */
	private Functions functions;

	/**
	 * @param globals Declarations to use for program
	 * @param body Body of program
	 */
	public Program(Declarations globals, Functions functions) {
		this.globals = globals;
		this.functions = functions;
	}
	
	/** @return Program's declarations */
	public Declarations globals(){ return globals; }
	/** @return Body of program */
	public Functions functions(){ return functions; }

	/**
	 * Prints out this program's declarations and body
	 */
	public void display() {
		//System.out.println("--- Abstract Syntax ---");
		System.out.println("Declarations:");
		for(Declaration d : globals.values())
			System.out.println("\t" + d.variable() + " :: " + d.type());
		
		System.out.println("\nFunctions:");
		Iterator<Function> members = functions.values().iterator();
		while(members.hasNext())
			members.next().display(1);
	}
}
