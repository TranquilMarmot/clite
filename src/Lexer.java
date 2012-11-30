import java.io.*;

public class Lexer {
	/** Whether or not we're at the end of the file */
	private boolean isEof = false;
	/** Current character */
	private char ch = ' ';
	/** Used for reading through input */
	private BufferedReader input;
	/** The current line */
	private String line = "";
	/** Current line number in file */
	private int lineno = 0;
	/** Current column number in file */
	private int col = 1;
	/** All possible letters */
	private final String LETTERS = "abcdefghijklmnopqrstuvwxyz"
			+ "ABCDEFGHIJKLMNOPQRSTeVWXYZ";
	/** All possible numbers */
	private final String DIGITS = "0123456789";
	/** End-of-line char */
	private final char EOL = '\n';
	/** End-of-file char */
	private final char EOF = '\004';

	public Lexer(String fileName) { // source filename
		try {
			input = new BufferedReader(new FileReader(fileName));
		} catch (FileNotFoundException e) {
			System.out.println("File not found: " + fileName);
			System.exit(1);
		}
	}

	private char nextChar() { // Return next char
		if (ch == EOF)
			error("Attempt to read past end of file");
		col++;
		if (col >= line.length()) {
			try {
				line = input.readLine();
			} catch (IOException e) {
				System.err.println(e);
				System.exit(1);
			} // try
			if (line == null) // at end of file
				line = "" + EOF;
			else {
				// System.out.println(lineno + ":\t" + line);
				lineno++;
				line += EOL;
			} // if line
			col = 0;
		} // if col
		return line.charAt(col);
	}

	public Token next() { // Return next token
		do {
			if (isLetter(ch)) { // ident or keyword
				String spelling = concat(LETTERS + DIGITS);
				return Token.keyword(spelling);
			} else if (isDigit(ch)) { // int or float literal
				String number = concat(DIGITS);
				if (ch != '.') // int Literal
					return Token.mkIntLiteral(number);
				number += concat(DIGITS);
				return Token.mkFloatLiteral(number);
			} else
				switch (ch) {
				case ' ':
				case '\t':
				case '\r':
				case EOL:
					ch = nextChar();
					break;

				case '/': // divide or comment
					ch = nextChar();
					if (ch != '/')
						return Token.divideTok;
					// comment
					do {
						ch = nextChar();
					} while (ch != EOL);
					ch = nextChar();
					break;

				case '\'': // char literal
					char ch1 = nextChar();
					nextChar(); // get '
					ch = nextChar();
					return Token.mkCharLiteral("" + ch1);

				case EOF:
					return Token.eofTok;

					// - * ( ) { } ; , student exercise
				case '+':
					ch = nextChar();
					return Token.plusTok;

				case '-':
					ch = nextChar();
					return Token.minusTok;

				case '*':
					ch = nextChar();
					return Token.multiplyTok;

				case '(':
					ch = nextChar();
					return Token.leftParenTok;

				case ')':
					ch = nextChar();
					return Token.rightParenTok;

				case '{':
					ch = nextChar();
					return Token.leftBraceTok;

				case '}':
					ch = nextChar();
					return Token.rightBraceTok;

				case ';':
					ch = nextChar();
					return Token.semicolonTok;

				case ',':
					ch = nextChar();
					return Token.commaTok;

				case '&':
					check('&');
					return Token.andTok;
				case '|':
					check('|');
					return Token.orTok;

				case '=':
					return chkOpt('=', Token.assignTok, Token.eqeqTok);

					// < > ! student exercise
				case '<':
					return chkOpt('=', Token.ltTok, Token.lteqTok);

				case '>':
					return chkOpt('=', Token.gtTok, Token.gteqTok);

				case '!':
					return chkOpt('=', Token.notTok, Token.noteqTok);

				default:
					error("Illegal character " + ch);
				} // switch
		} while (true);
	} // next

	private boolean isLetter(char c) {
		return (c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z');
	}

	private boolean isDigit(char c) {
		// TODO student exercise
		for (char dig : DIGITS.toCharArray())
			if (c == dig)
				return true;
		return false;
	}

	private void check(char c) {
		ch = nextChar();
		if (ch != c)
			error("Illegal character, expecting " + c);
		ch = nextChar();
	}

	private Token chkOpt(char c, Token one, Token two) {
		// TODO student exercise
		if (two.value().equals(("" + ch) + c)) {
			ch = nextChar(); // skip 'c'
			return two;
		} else
			return one;
	}

	private String concat(String set) {
		String r = "";
		while (set.indexOf(ch) >= 0) {
			r += ch;
			ch = nextChar();
		}
		return r;
	}

	public void error(String msg) {
		System.err.print(line);
		System.err.println("Error: column " + col + " " + msg);
		System.exit(1);
	}

	static public void main(String[] argv) {
		Lexer lexer = new Lexer(argv[0]);
		Token tok = lexer.next();
		while (tok != Token.eofTok) {
			System.out.println(tok.toString());
			tok = lexer.next();
		}
	} // main

}
