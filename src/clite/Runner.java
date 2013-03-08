package clite;
import clite.parser.Lexer;
import clite.parser.Parser;
import clite.syntax.Program;
import clite.typing.StaticTypeCheck;

/**
 * Parses and interprets a given file
 */
public class Runner {
	/**
	 * Main method
	 * @param args Name of file to interpret
	 */
	public static void main(String args[]) {
		if(args.length < 1){
			System.out.println("No args given, need file");
			return;
		}
		
		System.out.println("-------------------------------");
		
		Parser parser = new Parser(new Lexer(args[0]));
		Program prog = parser.program();
		System.out.println("Initial abstract syntax tree:");
		prog.display();
		
		System.out.println("\n-------------------------------");
		
		System.out.println("\nType map after static type check:");
		StaticTypeCheck.validate(prog);
		//Program out = TypeTransformer.transform(prog);
		
		System.out.println("\n-------------------------------");
		
		System.out.println("\nAbstract syntax tree after type transformer:");
		//out.display(); 
		
		//State state = Interpreter.interpret(out);
		
		System.out.println("\n-------------------------------");
		
		System.out.println("\nFinal State:");
		//state.display( );
	}
}
