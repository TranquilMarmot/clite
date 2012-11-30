
public class Parser {
	// Recursive descent parser that inputs a C++Lite program and
	// generates its abstract syntax. Each method corresponds to
	// a concrete syntax grammar rule, which appears as a comment
	// at the beginning of the method.

	private Token currentToken; // current currentToken from the input stream
	Lexer lexer;

	public Parser(Lexer ts) { // Open the C++Lite source program
		lexer = ts; // as a currentToken stream, and
		currentToken = lexer.next(); // retrieve its first Token
	}

	private String match(TokenType t) {
		String value = currentToken.value();
		if (currentToken.type().equals(t))
			currentToken = lexer.next();
		else
			error(t);
		return value;
	}

	private void error(TokenType tok) {
		System.err.println("Syntax error: expecting: " + tok + "; saw: "
				+ currentToken);
		System.exit(1);
	}

	private void error(String tok) {
		System.err.println("Syntax error: expecting: " + tok + "; saw: "
				+ currentToken);
		System.exit(1);
	}

	public Program program() {
		// Program --> void main ( ) '{' Declarations Statements '}'
		TokenType[] header = { TokenType.Int, TokenType.Main,
				TokenType.LeftParen, TokenType.RightParen };
		for (int i = 0; i < header.length; i++)
			// bypass "int main ( )"
			match(header[i]);
		match(TokenType.LeftBrace);

		// TODO student exercise
		Program prog = new Program(declarations(), statements());

		match(TokenType.RightBrace);
		return prog; // TODO student exercise
	}

	private Declarations declarations() {
		// Declarations --> { Declaration }
		return null; // TODO student exercise
	}

	private void declaration(Declarations ds) {
		// Declaration --> Type Identifier { , Identifier } ;
		// TODO student exercise
	}

	private Type type() {
		// Type --> int | bool | float | char
		Type t = null;
		// TODO student exercise
		return t;
	}

	private Statement statement() {
		// Statement --> ; | Block | Assignment | IfStatement | WhileStatement
		Statement s = new Skip();
		// TODO student exercise
		return s;
	}

	private Block statements() {
		// Block --> '{' Statements '}'
		Block b = new Block();
		// TODO student exercise
		return b;
	}

	private Assignment assignment() {
		// Assignment --> Identifier = Expression ;
		// find identifier for what's being assigned
		Variable target = new Variable(match(TokenType.Identifier));
		match(TokenType.Assign); // match assignment token (=)
		Expression source = expression(); // find out what exactly we're
											// assigning
		match(TokenType.Semicolon); // match semicolon to complete statement
		return new Assignment(target, source); // TODO student exercise
	}

	private Conditional ifStatement() {
		// IfStatement --> if ( Expression ) Statement [ else Statement ]
		return null; // TODO student exercise
	}

	private Loop whileStatement() {
		// WhileStatement --> while ( Expression ) Statement
		return null; // TODO student exercise
	}

	private Expression expression() {
		// Expression --> Conjunction { || Conjunction }
		/*
		 * Expression e; Term t1 = term(); Operator op (???) while(isAddOp()){
		 * Term ter = term(); e = new Expression(op, e, ter); <- passing e as a
		 * parameter is resursion (down the line) } return e; SIMILAR TO
		 * ADDITION()
		 */
		return conjunction(); // TODO student exercise
	}

	private Expression conjunction() {
		// Conjunction --> Equality { && Equality }
		return equality(); // TODO student exercise
	}

	private Expression equality() {
		// Equality --> Relation [ EquOp Relation ]
		return null; // TODO student exercise
	}

	private Expression relation() {
		// Relation --> Addition [RelOp Addition]
		return null; // TODO student exercise
	}

	private Expression addition() {
		// Addition --> Term { AddOp Term }
		Expression e = term();
		while (isAddOp()) {
			Operator op = new Operator(match(currentToken.type()));
			Expression term2 = term();
			e = new Binary(op, e, term2);
		}
		return e;
	}

	private Expression term() {
		// Term --> Factor { MultiplyOp Factor }
		Expression e = factor();
		while (isMultiplyOp()) {
			Operator op = new Operator(match(currentToken.type()));
			Expression term2 = factor();
			e = new Binary(op, e, term2);
		}
		return e;
	}

	private Expression factor() {
		// Factor --> [ UnaryOp ] Primary
		if (isUnaryOp()) {
			Operator op = new Operator(match(currentToken.type()));
			Expression term = primary();
			return new Unary(op, term);
		} else
			return primary();
	}

	private Expression primary() {
		// Primary --> Identifier | Literal | ( Expression )
		// | Type ( Expression )
		Expression e = null;
		if (currentToken.type().equals(TokenType.Identifier)) {
			e = new Variable(match(TokenType.Identifier));
		} else if (isLiteral()) {
			e = literal();
		} else if (currentToken.type().equals(TokenType.LeftParen)) {
			currentToken = lexer.next();
			e = expression();
			match(TokenType.RightParen);
		} else if (isType()) {
			Operator op = new Operator(match(currentToken.type()));
			match(TokenType.LeftParen);
			Expression term = expression();
			match(TokenType.RightParen);
			e = new Unary(op, term);
		} else
			error("Identifier | Literal | ( | Type");
		return e;
	}

	private Value literal() {
		if (currentToken.type() == TokenType.Int)
			return new IntValue(Integer.parseInt(currentToken.value()));
		else if (currentToken.type() == TokenType.Float)
			return new FloatValue(Float.parseFloat(currentToken.value()));
		else if (currentToken.type() == TokenType.Char)
			return new CharValue(currentToken.value().charAt(0));
		else
			System.out
					.println("Got unknown token type for literal! Token value: "
							+ currentToken.value());
		return null; // TODO student exercise
	}

	private boolean isAddOp() {
		return currentToken.type().equals(TokenType.Plus)
				|| currentToken.type().equals(TokenType.Minus);
	}

	private boolean isMultiplyOp() {
		return currentToken.type().equals(TokenType.Multiply)
				|| currentToken.type().equals(TokenType.Divide);
	}

	private boolean isUnaryOp() {
		return currentToken.type().equals(TokenType.Not)
				|| currentToken.type().equals(TokenType.Minus);
	}

	private boolean isEqualityOp() {
		return currentToken.type().equals(TokenType.Equals)
				|| currentToken.type().equals(TokenType.NotEqual);
	}

	private boolean isRelationalOp() {
		return currentToken.type().equals(TokenType.Less)
				|| currentToken.type().equals(TokenType.LessEqual)
				|| currentToken.type().equals(TokenType.Greater)
				|| currentToken.type().equals(TokenType.GreaterEqual);
	}

	private boolean isType() {
		return currentToken.type().equals(TokenType.Int)
				|| currentToken.type().equals(TokenType.Bool)
				|| currentToken.type().equals(TokenType.Float)
				|| currentToken.type().equals(TokenType.Char);
	}

	private boolean isLiteral() {
		return currentToken.type().equals(TokenType.IntLiteral)
				|| isBooleanLiteral()
				|| currentToken.type().equals(TokenType.FloatLiteral)
				|| currentToken.type().equals(TokenType.CharLiteral);
	}

	private boolean isBooleanLiteral() {
		return currentToken.type().equals(TokenType.True)
				|| currentToken.type().equals(TokenType.False);
	}

	public static void main(String args[]) {
		Parser parser = new Parser(new Lexer(args[0]));
		Program prog = parser.program();
		prog.display(); // display abstract syntax tree
	} // main

} // Parser
