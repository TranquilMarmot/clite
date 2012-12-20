import interpreter.Interpreter;
import interpreter.State;
import parser.Lexer;
import parser.Parser;
import syntax.Program;
import typing.StaticTypeCheck;
import typing.TypeMap;
import typing.TypeTransformer;

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
		TypeMap map = StaticTypeCheck.typing(prog.declarations());
		map.display();
		
		StaticTypeCheck.validate(prog);
		Program out = TypeTransformer.transform(prog, map);
		
		System.out.println("\n-------------------------------");
		
		System.out.println("\nAbstract syntax tree after type transformer:");
		out.display(); 
		
		State state = Interpreter.interpret(out);
		
		System.out.println("\n-------------------------------");
		
		System.out.println("\nFinal State:");
		state.display( );
	}
}
