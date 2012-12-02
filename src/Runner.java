import parser.Lexer;
import parser.Parser;
import syntax.Program;
import typing.Interpreter;
import typing.State;
import typing.StaticTypeCheck;
import typing.TypeMap;
import typing.TypeTransformer;

public class Runner {
	public static void main(String args[]) {
		if(args.length < 1){
			System.out.println("No args given, need file");
			return;
		}
		Parser parser = new Parser(new Lexer(args[0]));
		Program prog = parser.program();
		prog.display(); // student exercise (done)
		
		System.out.println("\nBegin type checking...");
		System.out.println("Type map:");
		TypeMap map = StaticTypeCheck.typing(prog.declarations);
		map.display(); // student exercise (done)
		
		StaticTypeCheck.verify(prog);
		Program out = TypeTransformer.T(prog, map);
		
		System.out.println("\nOutput AST");
		out.display(); // student exercise (done)
		
		State state = Interpreter.interpret(out);
		
		System.out.println("\nFinal State");
		state.display( ); // student exercise (done)
	}
}
