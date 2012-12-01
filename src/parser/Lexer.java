package parser;
import java.io.*;


/**
 * Turns a given file into a token stream
 */
public class Lexer {
	/** Used for reading through input */
	private BufferedReader input;
	
	/** Current character */
	private char currentChar = ' ';
	
	/** Current line */
	private String currentLine = "";
	
	/** Current line number in file */
	private int lineno = 0;
	
	/** Current column number in file */
	private int column = 1;
	
	/** All possible letters and digits*/
	private final String
	LETTERS = "abcdefghijklmnopqrstuvwxyz"
			+ "ABCDEFGHIJKLMNOPQRSTeVWXYZ",
	DIGITS  = "0123456789";
	
	/** End-of-line and end-of-file chars */
	private final char EOL = '\n', EOF = '\004';

	/**
	 * @param fileName source filename
	 */
	public Lexer(String fileName) {
		try {
			input = new BufferedReader(new FileReader(fileName));
		} catch (FileNotFoundException e) {
			System.out.println("File not found: " + fileName);
			System.exit(1);
		}
	}

	/**
	 * @return next char
	 */
	private char nextChar() {
		if (currentChar == EOF)
			error("Attempt to read past end of file");
		column++;
		if (column >= currentLine.length()) {
			try {
				currentLine = input.readLine();
			} catch (IOException e) {
				System.err.println(e);
				System.exit(1);
			} // try
			if (currentLine == null) // at end of file
				currentLine = "" + EOF;
			else {
				// System.out.println(lineno + ":\t" + line);
				lineno++;
				currentLine += EOL;
			} // if line
			column = 0;
		} // if col
		return currentLine.charAt(column);
	}

	/**
	 * @return next token
	 */
	public Token next() {
		do {
			/*
			 *  if the current character is a letter, keep going
			 *  until hitting something not in
			 *  LETTERS or DIGITS and return it as a keyword
			 */
			if (isLetter(currentChar)) { // ident or keyword
				String spelling = concat(LETTERS + DIGITS);
				return Token.keyword(spelling);
			/*
			 * else if the current character is a digit, keep going
			 * until hitting something not in DIGITS, then check for a .
			 * and gobble up another number to make a float if there is one
			 */
			} else if (isDigit(currentChar)) { // int or float literal
				String number = concat(DIGITS);
				if (currentChar != '.') // int Literal
					return Token.mkIntLiteral(number);
				else{
					number += concat("." + DIGITS);
					return Token.mkFloatLiteral(number);
				}
			/*
			 * else we just return the token type 
			 */
			} else
				switch (currentChar) {
				// skip spaces, tabs, carriage returns, and end-of-line characters
				case ' ':
				case '\t':
				case '\r':
				case EOL:
					currentChar = nextChar();
					break;

				// divide or comment
				case '/':
					// skip /
					currentChar = nextChar();
					// if there's no second /, it's a divide
					if (currentChar != '/')
						return Token.divideTok;
					// else it's a comment, skip second /
					currentChar = nextChar();
					// skip the rest of the line
					while (currentChar != EOL) 
						currentChar = nextChar();
					currentChar = nextChar();
					break;

				// char literal, grab char and skip second '
				case '\'':
					char ch1 = nextChar();
					nextChar(); // get '
					currentChar = nextChar();
					return Token.mkCharLiteral("" + ch1);

				// end of file
				case EOF:
					return Token.eofTok;

				// - * ( ) { } ; , student exercise (done)
				case '+':
					currentChar = nextChar();
					return Token.plusTok;

				case '-':
					currentChar = nextChar();
					return Token.minusTok;

				case '*':
					currentChar = nextChar();
					return Token.multiplyTok;

				case '(':
					currentChar = nextChar();
					return Token.leftParenTok;

				case ')':
					currentChar = nextChar();
					return Token.rightParenTok;

				case '{':
					currentChar = nextChar();
					return Token.leftBraceTok;

				case '}':
					currentChar = nextChar();
					return Token.rightBraceTok;

				case ';':
					currentChar = nextChar();
					return Token.semicolonTok;

				case ',':
					currentChar = nextChar();
					return Token.commaTok;

				// FIXME does skips && and ||? do we even handle & and |?
				case '&':
					check('&');
					return Token.andTok;
				case '|':
					check('|');
					return Token.orTok;

				case '=':
					return chkOpt('=', Token.assignTok, Token.eqeqTok);

				// < > ! student exercise (done)
				case '<':
					return chkOpt('=', Token.ltTok, Token.lteqTok);

				case '>':
					return chkOpt('=', Token.gtTok, Token.gteqTok);

				case '!':
					return chkOpt('=', Token.notTok, Token.noteqTok);

				default:
					error("Illegal character " + currentChar);
				} // switch
		} while (true);
	} // next

	/**
	 * @param c Character to check
	 * @return Whether or not the given character is a letter
	 */
	private boolean isLetter(char c) {
		return LETTERS.contains("" + c);
	}

	/**
	 * @param c Character to check
	 * @return Whether or not the given character is a digit
	 */
	private boolean isDigit(char c) {
		return DIGITS.contains("" + c);
	}

	/**
	 * Checks if the current character is the given character
	 * and errors if it isn't
	 * @param c Character to check for
	 */
	private void check(char c) {
		currentChar = nextChar();
		if (currentChar != c)
			error("Illegal character, expecting " + c);
		currentChar = nextChar();
	}

	/**
	 * Checks if `c` is `two`'s value and, if it is, skips the current character and returns `two`.
	 * If it isn't the value of `two`, `one` is returned.
	 * @param c Character to check for
	 * @param one Return if c isn't two's value
	 * @param two Value to check for character
	 * @return two if c is two's value, one otherwise
	 */
	private Token chkOpt(char c, Token one, Token two) {
		// student exercise (done)
		char nextChar = nextChar();
		
		if (nextChar == c) 
			return two;
		else{
			currentChar = nextChar;
			return one;
		}
	}

	/**
	 * Adds characters until it hits one not in the given set
	 * @param set Set of characters that can be added
	 * @return String containing word
	 */
	private String concat(String set) {
		String r = "";
		while (set.contains("" + currentChar)) {
			r += currentChar;
			currentChar = nextChar();
		}
		return r;
	}

	/**
	 * Print an error
	 * @param msg Message to print
	 */
	public void error(String msg) {
		System.err.println("Error in Lexer!");
		System.err.println("Line: " + lineno + " Col: " + column);
		System.err.print(currentLine);
		System.err.println("Error: column " + column + " " + msg);
		System.exit(1);
	}
	
	public int lineNumber(){
		return lineno;
	}
	
	public int columnNumber(){
		return column;
	}

	static public void main(String[] argv) {
		Lexer lexer = new Lexer(argv[0]);
		Token tok = lexer.next();
		while (tok != Token.eofTok) {
			System.out.println(tok.type() + " | " + tok.toString());
			tok = lexer.next();
		}
	} // main

}
