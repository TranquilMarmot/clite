
/**
 * Recursive descent parser that inputs a C++Lite program and
 * generates its abstract syntax. Each method corresponds to
 * a concrete syntax grammar rule, which appears as a comment
 * at the beginning of the method.
 */
public class Parser {
	/**  Current token from the input stream */
	private Token currentToken;
	private Lexer lexer;
	
	public Parser(String file){
		this(new Lexer(file));
	}

	/**
	 * Create a new parser. Call program() to get program from Lexer.
	 * @param lexer Lexer to use to generate program
	 */
	public Parser(Lexer lexer) {
		this.lexer = lexer;
		// retrieve first token
		currentToken = lexer.next();
	}

	/**
	 * Check if the current token is the given token type,
	 * and moves on to the next token if it is,
	 * errors if it isn't.
	 * @param t Token type to check for
	 * @return Value of current token
	 */
	private String match(TokenType t) {
		String value = currentToken.value();
		if (currentToken.type().equals(t))
			currentToken = lexer.next();
		else
			error(t);
		return value;
	}

	private void error(TokenType tok) { error(tok.toString()); }
	private void error(String tok) {
		System.err.println("Syntax error: expecting: " + tok + "; saw: " + currentToken);
		System.exit(1);
	}

	/**
	 * Program --> void main ( ) '{' Declarations Statements '}'
	 * @return Program generated from Lexer given to this Parser's constructor
	 */
	public Program program() {
		// bypass "int main ( )"
		TokenType[] header = { TokenType.Int, TokenType.Main, TokenType.LeftParen, TokenType.RightParen };
		for (int i = 0; i < header.length; i++)
			match(header[i]);
		match(TokenType.LeftBrace);

		// TODO student exercise
		Program prog = new Program(declarations(), statements());

		match(TokenType.RightBrace);
		return prog; // TODO student exercise
	}

	/**
	 * Declarations --> { Declaration }
	 * @return
	 */
	private Declarations declarations() {
		Declarations ds = new Declarations();
		
		while(isType())
			declaration(ds);
		
		return ds; // TODO student exercise
	}

	/**
	 * Declaration --> Type Identifier { , Identifier } ;
	 * @param ds
	 */
	private void declaration(Declarations ds) {
		Type type = type();
		
		do{
			currentToken = lexer.next();
			Variable v = new Variable(currentToken.value());
			ds.add(new Declaration(v, type));
			currentToken = lexer.next();
		} while(currentToken.type() == TokenType.Comma);

		// student exercise (done)
	}

	/**
	 * Type --> int | bool | float | char
	 * @return
	 */
	private Type type() {
		if(currentToken.type() == TokenType.Int)
			return Type.INT;
		else if(currentToken.type() == TokenType.Bool)
			return Type.BOOL;
		else if(currentToken.type() == TokenType.Float)
			return Type.FLOAT;
		else if(currentToken.type() == TokenType.Char)
			return Type.CHAR;
		else
			error("Current token not type (current token: " + currentToken + ")");
		
		// student exercise (done)
		return null;
	}

	/**
	 * Statement --> ; | Block | Assignment | IfStatement | WhileStatement
	 * @return
	 */
	private Statement statement() {
		Statement s = new Skip();
		// TODO student exercise
		return s;
	}

	/**
	 * Block --> '{' Statements '}'
	 * @return
	 */
	private Block statements() {
		Block b = new Block();
		
		// TODO student exercise
		return b;
	}

	/**
	 * Assignment --> Identifier = Expression ;
	 * @return
	 */
	private Assignment assignment() {
		// find identifier for what's being assigned
		Variable target = new Variable(match(TokenType.Identifier));
		match(TokenType.Assign); // match assignment token (=)
		Expression source = expression(); // find out what exactly we're
											// assigning
		match(TokenType.Semicolon); // match semicolon to complete statement
		return new Assignment(target, source); // TODO student exercise
	}

	/**
	 * IfStatement --> if ( Expression ) Statement [ else Statement ]
	 * @return
	 */
	private Conditional ifStatement() {
		match(TokenType.If);
		
		match(TokenType.LeftParen);
		Expression expression = expression();
		match(TokenType.RightParen);
		
		Statement ifstatement = statement();
		Statement elsestatement = (currentToken.type() == TokenType.Else) ? elsestatement = statement() : new Skip();
		
		return new Conditional(expression, ifstatement, elsestatement); // student exercise (done)
	}

	/**
	 * WhileStatement --> while ( Expression ) Statement
	 * @return
	 */
	private Loop whileStatement() {
		match(TokenType.While);
		
		match(TokenType.LeftParen);
		Expression expression = expression();
		match(TokenType.RightParen);
		
		Statement statement = statement();
		
		return new Loop(expression, statement); // student exercise (done)
	}

	/**
	 * Expression --> Conjunction { || Conjunction }
	 * @return
	 */
	private Expression expression() {
		/*
		 * Expression e; Term t1 = term(); Operator op (???) while(isAddOp()){
		 * Term ter = term(); e = new Expression(op, e, ter); <- passing e as a
		 * parameter is resursion (down the line) } return e; SIMILAR TO
		 * ADDITION()
		 */
		return conjunction(); // TODO student exercise
	}

	/**
	 * Conjunction --> Equality { && Equality }
	 * @return
	 */
	private Expression conjunction() {
		return equality(); // TODO student exercise
	}

	/**
	 * Equality --> Relation [ EquOp Relation ]
	 * @return
	 */
	private Expression equality() {
		return null; // TODO student exercise
	}

	/**
	 * Relation --> Addition [RelOp Addition]
	 * @return
	 */
	private Expression relation() {
		return null; // TODO student exercise
	}

	/**
	 * Addition --> Term { AddOp Term }
	 * @return
	 */
	private Expression addition() {
		Expression e = term();
		while (isAddOp()) {
			Operator op = new Operator(match(currentToken.type()));
			Expression term2 = term();
			e = new Binary(op, e, term2);
		}
		return e;
	}

	/**
	 * Term --> Factor { MultiplyOp Factor }
	 * @return
	 */
	private Expression term() {
		Expression e = factor();
		while (isMultiplyOp()) {
			Operator op = new Operator(match(currentToken.type()));
			Expression term2 = factor();
			e = new Binary(op, e, term2);
		}
		return e;
	}

	/**
	 * Factor --> [ UnaryOp ] Primary
	 * @return
	 */
	private Expression factor() {
		if (isUnaryOp()) {
			Operator op = new Operator(match(currentToken.type()));
			Expression term = primary();
			return new Unary(op, term);
		} else
			return primary();
	}

	/**
	 * Primary --> Identifier | Literal | ( Expression ) | Type ( Expression )
	 * @return
	 */
	private Expression primary() {
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
			System.out.println("Got unknown token type for literal! Token value: " + currentToken.value());
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
			|| currentToken.type().equals(TokenType.FloatLiteral)
			|| currentToken.type().equals(TokenType.CharLiteral)
			|| isBooleanLiteral();
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
