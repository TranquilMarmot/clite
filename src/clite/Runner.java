package clite;
import java.util.Random;

import clite.interpreter.Interpreter;
import clite.interpreter.State;
import clite.parser.Lexer;
import clite.parser.Parser;
import clite.syntax.Program;
import clite.typing.StaticTypeCheck;
import clite.typing.TypeTransformer;

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
		
		System.out.println("\nAfter static type check and type transform:");
		StaticTypeCheck.validate(prog);
		Program transformed = TypeTransformer.transform(prog);
		transformed.display();
		
		System.out.println("\n-------------------------------");
		
		System.out.println("\nFinal State:");
		State state = Interpreter.interpret(transformed);
		state.display( );
		
		System.out.println("\n-------------------------------");
		System.out.println(goodbye());
	}
	
	/**
	 * This is a secret method that does secret things
	 * @return None of your business
	 */
	private static String goodbye(){
		String[] shutdown = { "Goodbye, world!...", "Goodbye, cruel world!...", "See ya!...", "Later!...", "Buh-bye!...", "Thank you, come again!...",
				"Until Next Time!...", "¡Adios, Amigo!...", "Game Over, Man! Game Over!!!...", "And So, I Bid You Adieu!...", "So Long, And Thanks For All The Fish!...",
				"¡Ciao!...", "Y'all Come Back Now, Ya Hear?...", "Catch You Later!...", "Mahalo And Aloha!...", "Sayonara!...", "Thanks For Playing!...",
				"Auf Wiedersehen!...", "Yo Holmes, Smell Ya Later!... (Looked Up At My Kingdom, I Was Finally There, To Sit On My Throne As The Prince Of Bel-air)",
				"Shop Smart, Shop S-Mart!...", "Good Night, And Good Luck!...", "Remember, I'm Pulling For You. We're All In This Together!...", "Keep Your Stick On The Ice!...",
				"Omnia Extares!...", "C'est la vie!...", "See you on the flip side!...", "Toodle-oo!...", "Ta ta (For Now)!...", "¡Hasta La Vista, Baby!...",
				"Komapsumnida!...", "...!olleH", "Live Long And Prosper!...", "Cheerio!...", "Shalom!...", "Peace Out!...", "Arrivederci!..."};
		
		return shutdown[new Random().nextInt(shutdown.length)];
	}
}
