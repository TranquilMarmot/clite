package parser;
import abstractsyntax.Assignment;
import abstractsyntax.Declaration;
import abstractsyntax.Declarations;
import abstractsyntax.Operator;
import abstractsyntax.Program;
import abstractsyntax.Skip;
import abstractsyntax.Type;
import abstractsyntax.expression.Binary;
import abstractsyntax.expression.Expression;
import abstractsyntax.expression.Unary;
import abstractsyntax.expression.Variable;
import abstractsyntax.statement.Block;
import abstractsyntax.statement.Conditional;
import abstractsyntax.statement.Loop;
import abstractsyntax.statement.Statement;
import abstractsyntax.value.CharValue;
import abstractsyntax.value.FloatValue;
import abstractsyntax.value.IntValue;
import abstractsyntax.value.Value;


/**
 * Recursive descent parser that inputs a C++Lite program and
 * generates its abstract syntax. Each method corresponds to
 * a concrete syntax grammar rule, which appears as a comment
 * at the beginning of the method.
 */
public class Parser {
	/**  Current token from the input stream */
	private Token currentToken;
	
	/** Lexer that provides token */
	private Lexer lexer;
	
	/** @param file Location of file to read */
	public Parser(String file){ this(new Lexer(file)); }

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
		if (currentToken.type() == t)
			currentToken = lexer.next();
		else
			error(t);
		return currentToken.value();
	}

	private void error(TokenType tok) { error(tok.toString()); }
	private void error(String expected) {
		System.err.println("Error in parser!");
		System.err.println("Line: " + lexer.lineNumber() + " Col: " + lexer.columnNumber());
		System.err.println("Syntax error: expecting: " + expected + "; saw: " + currentToken.value());
		System.exit(1);
	}

	/**
	 * Program --> void main ( ) '{' Declarations Statements '}'
	 * @return Program generated from Lexer given to this Parser's constructor
	 */
	public Program program() {
		// bypass "int main ( )"
		TokenType[] header = { TokenType.Int, TokenType.Main, TokenType.LeftParen, TokenType.RightParen };
		for (int i = 0; i < header.length; i++){
			match(header[i]);
		}
		match(TokenType.LeftBrace);

		// student exercise (done)
		Program prog = new Program(declarations(), statements());

		match(TokenType.RightBrace);
		return prog; // student exercise (done?)
	}

	/**
	 * Declarations --> { Declaration }
	 * @return List of declarations
	 */
	private Declarations declarations() {
		Declarations ds = new Declarations();
		
		while(isType())
			declaration(ds);
		
		return ds; // student exercise (done?)
	}

	/**
	 * Declaration --> Type Identifier { , Identifier } ;
	 * @param ds Declarations list to add declaration to
	 */
	private void declaration(Declarations ds) {
		// grab the Type
		Type type = type();
		
		do{
			Variable v = new Variable(currentToken.value());
			ds.add(new Declaration(v, type));
			
			currentToken = lexer.next();
			
			// if we're at the end of the line, we're done
			if(currentToken .type() == TokenType.Semicolon)
				break;
			// currentToken will be a comma if there's more declarations of this type, skip comma
			else
				currentToken = lexer.next();
		// keep going until there's no more commas
		} while(currentToken.type() != TokenType.Semicolon);
		
		// skip ;
		currentToken = lexer.next();

		// student exercise (done)
	}

	/**
	 * Type --> int | bool | float | char
	 * Also moves on to next token
	 * @return Type of current token
	 */
	private Type type() {
		Type t = null;
		
		if(currentToken.type() == TokenType.Int)
			t = Type.INT;
		else if(currentToken.type() == TokenType.Bool)
			t = Type.BOOL;
		else if(currentToken.type() == TokenType.Float)
			t = Type.FLOAT;
		else if(currentToken.type() == TokenType.Char)
			t = Type.CHAR;
		else
			error("Current token not type (current token: " + currentToken + ")");
		
		currentToken = lexer.next();
		
		// student exercise (done)
		return t;
	}

	/**
	 * Statement --> ; | Block | Assignment | IfStatement | WhileStatement
	 * @return
	 */
	private Statement statement() {
		Statement s = null;
		
		if(currentToken.type() == TokenType.Semicolon)
			return new Skip(); // FIXME not sure what to do here
		else if(currentToken.type() == TokenType.If)
			return ifStatement();
		else if(currentToken.type() == TokenType.While)
			return whileStatement();
		else if(currentToken.type() == TokenType.Identifier)
			return assignment();
		else if(currentToken.type() == TokenType.LeftBrace){
			match(TokenType.LeftBrace);
			Block bl = statements();
			match(TokenType.RightBrace);
			return bl;
		} else
			error("Unknown statement type: " + currentToken.value());
		
		// student exercise (done)
		return s;
	}

	/**
	 * Block --> '{' Statements '}'
	 * @return
	 */
	private Block statements() {
		Block b = new Block();
		
		while(currentToken.type() != TokenType.Eof && currentToken.type() != TokenType.RightBrace)
			b.members.add(statement());
		
		// student exercise (done?)
		return b;
	}

	/**
	 * Assignment --> Identifier = Expression ;
	 * @return
	 */
	private Assignment assignment() {
		// get identifier for what's being assigned
		Variable target = new Variable(match(currentToken.type()));
		
		// match =
		match(TokenType.Assign);
		
		// find out what we're assigning the identifier to
		Expression source = expression();
		
		// match semicolon to complete statement
		match(TokenType.Semicolon);
		return new Assignment(target, source); // student exercise (done)
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
		Expression e = conjunction();
		while(currentToken.type() == TokenType.Or){
			Operator op = new Operator(match(TokenType.Or));
			Expression term2 = conjunction();
			e = new Binary(op, e, term2);
		}
		
		return e; // student exercise (done)
	}

	/**
	 * Conjunction --> Equality { && Equality }
	 * @return
	 */
	private Expression conjunction() {
		Expression e = equality();
		while(currentToken.type() == TokenType.And){ // FIXME is And &&?
			Operator op = new Operator(match(TokenType.And));
			Expression term2 = equality();
			e = new Binary(op, e, term2);
		}
		
		return e; // student exercise (done)
	}

	/**
	 * Equality --> Relation [ EquOp Relation ]
	 * @return
	 */
	private Expression equality() {
		Expression e = relation();
		while(isEqualityOp()){
			Operator op = new Operator(match(currentToken.type()));
			Expression term2 = relation();
			e = new Binary(op, e, term2);
		}
		
		return e; // student exercise (done)
	}

	/**
	 * Relation --> Addition [RelOp Addition]
	 * @return
	 */
	private Expression relation() {
		Expression e = addition();
		while(isRelationalOp()){
			Operator op = new Operator(match(currentToken.type()));
			Expression term2 = addition();
			e = new Binary(op, e, term2);
		}
		
		return e; // student exercise (done)
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
		try{
			if (currentToken.type() == TokenType.IntLiteral){
				Value v = new IntValue(Integer.parseInt(currentToken.value()));
				match(TokenType.IntLiteral);
				return v;
			}else if (currentToken.type() == TokenType.FloatLiteral){
				Value v = new FloatValue(Float.parseFloat(currentToken.value()));
				match(TokenType.FloatLiteral);
				return v;
			}
			else if (currentToken.type() == TokenType.CharLiteral){
				Value v = new CharValue(currentToken.value().charAt(0));
				match(TokenType.CharLiteral);
				return v;
			}
			else
				error("unknown token type for literal! Token value: " + currentToken.value());
		} catch(NumberFormatException e){
			error("Inavlid number format " + e.getLocalizedMessage());
		}
		return null; // student exercise (done)
	}

	private boolean isAddOp() {
		return currentToken.type() == TokenType.Plus
			|| currentToken.type() == TokenType.Minus;
	}

	private boolean isMultiplyOp() {
		return currentToken.type() == TokenType.Multiply
			|| currentToken.type() == TokenType.Divide;
	}

	private boolean isUnaryOp() {
		return currentToken.type() == TokenType.Not
			|| currentToken.type() == TokenType.Minus;
	}

	private boolean isEqualityOp() {
		return currentToken.type() == TokenType.Equals
			|| currentToken.type() == TokenType.NotEqual;
	}

	private boolean isRelationalOp() {
		return currentToken.type() == TokenType.Less
			|| currentToken.type() == TokenType.LessEqual
			|| currentToken.type() == TokenType.Greater
			|| currentToken.type() == TokenType.GreaterEqual;
	}

	private boolean isType() {
		return currentToken.type() == TokenType.Int
			|| currentToken.type() == TokenType.Bool
			|| currentToken.type() == TokenType.Float
			|| currentToken.type() == TokenType.Char;
	}

	private boolean isLiteral() {
		return currentToken.type() == TokenType.IntLiteral
			|| currentToken.type() == TokenType.FloatLiteral
			|| currentToken.type() == TokenType.CharLiteral
			|| isBooleanLiteral();
	}

	private boolean isBooleanLiteral() {
		return currentToken.type() == TokenType.True
			|| currentToken.type() == TokenType.False;
	}

	public static void main(String args[]) {
		Parser parser = new Parser(new Lexer(args[0]));
		Program prog = parser.program();
		prog.display(); // display abstract syntax tree
	} // main

} // Parser
