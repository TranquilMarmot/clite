package clite.parser;

import org.pmw.tinylog.Logger;

import clite.function.Function;
import clite.function.FunctionMap;
import clite.syntax.Operator;
import clite.syntax.Program;
import clite.syntax.Type;
import clite.syntax.declaration.Declaration;
import clite.syntax.declaration.Declarations;
import clite.syntax.expression.Binary;
import clite.syntax.expression.Expression;
import clite.syntax.expression.Unary;
import clite.syntax.expression.Variable;
import clite.syntax.statement.Assignment;
import clite.syntax.statement.Block;
import clite.syntax.statement.Conditional;
import clite.syntax.statement.Loop;
import clite.syntax.statement.Skip;
import clite.syntax.statement.Statement;
import clite.syntax.value.CharValue;
import clite.syntax.value.FloatValue;
import clite.syntax.value.IntValue;
import clite.syntax.value.Value;


/**
 * Recursive descent parser that inputs a C++Lite program and
 * generates its abstract syntax.
 * 
 * Each method corresponds to a concrete syntax grammar rule.
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
	 * Check if the current token is the given token type;
	 * moves on to the next token if it is,
	 * errors if it isn't.
	 * @param t Token type to check for
	 * @return Value of current token
	 */
	private String match(Token.Type t) {
		if (currentToken.type() == t)
			currentToken = lexer.next();
		else
			error(t.toString());
		return currentToken.value();
	}

	/**
	 * Print an error message describing what the lexer expected
	 * @param message What was expected
	 */
	private void error(String message) {
		System.err.println("Error in parser!");
		System.err.println("Line: " + lexer.lineNumber() + " Col: " + lexer.columnNumber());
		System.err.println("Syntax error: expecting: " + message + "; saw: " + currentToken.value());
		System.exit(1);
	}

	/**
	 * Program --> int main ( ) '{' Declarations Statements '}'
	 * @return Program generated from Lexer given to this Parser's constructor
	 */
	public Program program() {
		// bypass "int main ( )"
		/*
		Token.Type[] header = { Token.Type.Int, Token.Type.Main, Token.Type.LeftParen, Token.Type.RightParen };
		for (int i = 0; i < header.length; i++){
			match(header[i]);
		}
		*/
		
		Declarations globals = new Declarations();
		FunctionMap funcs = new FunctionMap();
		
		//while(currentToken.type() != Token.Type.Main){
			//functionOrGlobal(globals, funcs);
		//}
			
			globals.putAll(declarations(funcs));
		
		// grab main
		//Function main = mainFunction();
		
		
		// parse program
		//match(Token.Type.LeftBrace);
		//Program prog = new Program(declarations(funcs), statements());
		//match(Token.Type.RightBrace);
		return null;
	}
	/**
	 * Declarations --> { Declaration }
	 * @return List of declarations
	 */
	private Declarations declarations(FunctionMap functions) {
		// create new list of declarations (each function should have one)
		Declarations ds = new Declarations();
		
		// declarations go until there's no more types
		while(isType())
			declaration(ds, functions);
		
		return ds;
	}

	/**
	 * Declaration --> Type Identifier { , Identifier } ;
	 * @param ds Declarations list to add declaration to
	 */
	private void declaration(Declarations ds, FunctionMap functions) {
		// grab the Type
		Type type = type();
		
		while(currentToken.type() != Token.Type.Main){
			// grab variable name from current token
			Variable v = new Variable(currentToken.value());
			// skip identifier
			match(Token.Type.Identifier);
			
			if(currentToken.type() == Token.Type.LeftParen){
				function(functions, type, v);
				if(isType())
					type = type();
				else
					break;
			} else if(currentToken.type() == Token.Type.Main)
				break;
			else{
				ds.put(v.toString(), new Declaration(v, type));
				// currentToken will be a comma if there's more declarations of this type, skip comma
				if(currentToken.type() == Token.Type.Comma){
					match(Token.Type.Comma);
				}else if(currentToken.type() == Token.Type.Semicolon){
					match(Token.Type.Semicolon);
					if(isType())
						type = type();
					else
						break;
				}
			}
		}
	}
	
	private Function function(FunctionMap functions, Type t, Variable v){
		//Function f = new Function();
		
		// match '('
		match(Token.Type.LeftParen);
		Declarations params = parameters();
		match(Token.Type.RightParen);
		match(Token.Type.LeftBrace);
		Declarations locals = declarations(functions);
		Block body = statements();
		match(Token.Type.RightBrace);
		
		
		return new Function(t, v.toString(), params, locals, body);
	}
	
	private Declarations parameters(){
		Declarations decs = new Declarations();
		
		while(currentToken.type() != Token.Type.RightParen){
			Type t = type();
			Variable v = new Variable(currentToken.value());
			match(Token.Type.Identifier);
			
			decs.put(v.toString(), new Declaration(v, t));
			
			// skip comma if it's there and move on to next parameter
			if(currentToken.type() == Token.Type.Comma)
				match(Token.Type.Comma);
		}
		
		return decs;
	}

	/**
	 * Type --> int | bool | float | char
	 * Also moves on to next token
	 * @return Type of current token
	 */
	private Type type() {
		Type t = null;
		
		if(currentToken.type() == Token.Type.Int)
			t = Type.INT;
		else if(currentToken.type() == Token.Type.Bool)
			t = Type.BOOL;
		else if(currentToken.type() == Token.Type.Float)
			t = Type.FLOAT;
		else if(currentToken.type() == Token.Type.Char)
			t = Type.CHAR;
		else if(currentToken.type() == Token.Type.Void)
			t = Type.VOID;
		else
			error("Current token not type (current token: " + currentToken + ")");
		
		match(currentToken.type());
		return t;
	}

	/**
	 * Statement --> ; | Block | Assignment | IfStatement | WhileStatement
	 */
	private Statement statement() {
		// skip any semicolons
		if(currentToken.type() == Token.Type.Semicolon)
			return new Skip();
		
		// if statement
		else if(currentToken.type() == Token.Type.If)
			return ifStatement();
		
		// while loop
		else if(currentToken.type() == Token.Type.While)
			return whileStatement();
		
		// identifier means assignment
		else if(currentToken.type() == Token.Type.Identifier)
			return assignment();
		
		// left brace indicates start of block
		else if(currentToken.type() == Token.Type.LeftBrace){
			match(Token.Type.LeftBrace);
			Block bl = statements();
			match(Token.Type.RightBrace);
			return bl;
			
		// unkown statement type
		} else
			error("Unknown statement type: " + currentToken.value());
		
		return null;
	}

	/**
	 * Block --> '{' Statements '}'
	 * @return A block of statements
	 */
	private Block statements() {
		Block b = new Block();
		
		// statement goes until a right brace is hit (or end of file)
		while(currentToken.type() != Token.Type.RightBrace && currentToken.type() != Token.Type.Eof)
			b.addMember(statement());
		
		return b;
	}

	/**
	 * Assignment --> Identifier = Expression ;
	 */
	private Assignment assignment() {
		// get identifier for what's being assigned
		Variable target = new Variable(currentToken.value());
		
		match(currentToken.type());
		
		// match =
		match(Token.Type.Assign);
		
		// find out what we're assigning the identifier to
		Expression source = expression();
		
		// match semicolon to complete statement
		match(Token.Type.Semicolon);
		return new Assignment(target, source);
	}

	/**
	 * IfStatement --> if ( Expression ) Statement [ else Statement ]
	 */
	private Conditional ifStatement() {
		// gobble up if
		match(Token.Type.If);
		
		// grab expression
		match(Token.Type.LeftParen);
		Expression expression = expression();
		match(Token.Type.RightParen);
		
		// grab if statement
		Statement ifstatement = statement();
		// grab else statement if it's there, skip otherwise
		Statement elsestatement = (currentToken.type() == Token.Type.Else) ? elsestatement = statement() : new Skip();
		
		return new Conditional(expression, ifstatement, elsestatement);
	}

	/**
	 * WhileStatement --> while ( Expression ) Statement
	 */
	private Loop whileStatement() {
		// gobble up while
		match(Token.Type.While);
		
		// grab expression
		match(Token.Type.LeftParen);
		Expression expression = expression();
		match(Token.Type.RightParen);
		
		// grab statement
		Statement statement = statement();
		
		return new Loop(expression, statement);
	}

	/**
	 * Expression --> Conjunction { || Conjunction }
	 */
	private Expression expression() {
		Expression e = conjunction();
		
		// expression goes while there's no more  ||s
		while(currentToken.type() == Token.Type.Or){
			Operator op = new Operator(currentToken.value());
			match(Token.Type.Or);
			Expression term2 = conjunction();
			e = new Binary(op, e, term2);
		}
		
		return e;
	}

	/**
	 * Conjunction --> Equality { && Equality }
	 */
	private Expression conjunction() {
		Expression e = equality();
		
		// conjunction goes until there's no more &&s
		while(currentToken.type() == Token.Type.And){
			Operator op = new Operator(currentToken.value());
			match(Token.Type.And);
			Expression term2 = equality();
			e = new Binary(op, e, term2);
		}
		
		return e;
	}

	/**
	 * Equality --> Relation [ EquOp Relation ]
	 */
	private Expression equality() {
		Expression e = relation();
		
		// equality goes while there's an equality operator
		while(isEqualityOp()){
			Operator op = new Operator(currentToken.value());
			match(currentToken.type());
			Expression term2 = relation();
			e = new Binary(op, e, term2);
		}
		
		return e;
	}

	/**
	 * Relation --> Addition [RelOp Addition]
	 */
	private Expression relation() {
		Expression e = addition();
		
		// relation goes until there's no more relational ops
		while(isRelationalOp()){
			Operator op = new Operator(currentToken.value());
			match(currentToken.type());
			Expression term2 = addition();
			e = new Binary(op, e, term2);
		}
		
		return e;
	}

	/**
	 * Addition --> Term { AddOp Term }
	 */
	private Expression addition() {
		Expression e = term();
		
		// addition goes until there's no more addition ops
		while (isAddOp()) {
			Operator op = new Operator(currentToken.value());
			match(currentToken.type());
			Expression term2 = term();
			e = new Binary(op, e, term2);
		}
		return e;
	}

	/**
	 * Term --> Factor { MultiplyOp Factor }
	 */
	private Expression term() {
		Expression e = factor();
		
		// term goes until there's no more multiply ops
		while (isMultiplyOp()) {
			Operator op = new Operator(currentToken.value());
			match(currentToken.type());
			Expression term2 = factor();
			e = new Binary(op, e, term2);
		}
		return e;
	}

	/**
	 * Factor --> [ UnaryOp ] Primary
	 */
	private Expression factor() {
		if (isUnaryOp()) {
			// grab operator
			Operator op = new Operator(currentToken.value());
			match(currentToken.type());
			
			// grab term
			Expression term = primary();
			
			return new Unary(op, term);
		} else
			// if there's no unary op, just return a primary
			return primary();
	}

	/**
	 * Primary --> Identifier | Literal | ( Expression ) | Type ( Expression )
	 */
	private Expression primary() {
		Expression e = null;
		
		// variable
		if (currentToken.type() == Token.Type.Identifier) {
			e = new Variable(currentToken.value());
			match(Token.Type.Identifier);
			
		// literal
		} else if (isLiteral()) {
			e = literal();
			
		// expression
		} else if (currentToken.type().equals(Token.Type.LeftParen)) {
			currentToken = lexer.next();
			e = expression();
			match(Token.Type.RightParen);
			
		// type
		} else if (isType()) {
			Operator op = new Operator(currentToken.value());
			match(currentToken.type());
			match(Token.Type.LeftParen);
			Expression term = expression();
			match(Token.Type.RightParen);
			e = new Unary(op, term);
		} else
			error("Identifier | Literal | ( | Type");
		return e;
	}

	/**
	 * An int, float or char literal
	 */
	private Value literal() {
		try{
			// int literal
			if (currentToken.type() == Token.Type.IntLiteral){
				Value v = new IntValue(Integer.parseInt(currentToken.value()));
				match(Token.Type.IntLiteral);
				return v;
				
			// float literal
			}else if (currentToken.type() == Token.Type.FloatLiteral){
				Value v = new FloatValue(Float.parseFloat(currentToken.value()));
				match(Token.Type.FloatLiteral);
				return v;
			}
			
			// char literal
			else if (currentToken.type() == Token.Type.CharLiteral){
				Value v = new CharValue(currentToken.value().charAt(0));
				match(Token.Type.CharLiteral);
				return v;
			}
			else
				error("unknown token type for literal! Token value: " + currentToken.value());
		} catch(NumberFormatException e){
			error("Inavlid number format " + e.getLocalizedMessage());
		}
		return null;
	}

	/**
	 * @return Whether or not the current token is an add op
	 */
	private boolean isAddOp() {
		return currentToken.type() == Token.Type.Plus
			|| currentToken.type() == Token.Type.Minus;
	}

	/**
	 * @return Whether or not the current token is a multiply op
	 */
	private boolean isMultiplyOp() {
		return currentToken.type() == Token.Type.Multiply
			|| currentToken.type() == Token.Type.Divide;
	}

	
	/**
	 * @return Whether or not the current token is a unary op
	 */
	private boolean isUnaryOp() {
		return currentToken.type() == Token.Type.Not
			|| currentToken.type() == Token.Type.Minus;
	}

	/**
	 * @return Whether or not the current token is an equality op
	 */
	private boolean isEqualityOp() {
		return currentToken.type() == Token.Type.Equals
			|| currentToken.type() == Token.Type.NotEqual;
	}

	/**
	 * @return Whether or not the current token is a relational op
	 */
	private boolean isRelationalOp() {
		return currentToken.type() == Token.Type.Less
			|| currentToken.type() == Token.Type.LessEqual
			|| currentToken.type() == Token.Type.Greater
			|| currentToken.type() == Token.Type.GreaterEqual;
	}

	/**
	 * @return Whether or not the current token is a type declaration
	 */
	private boolean isType() {
		return currentToken.type() == Token.Type.Int
			|| currentToken.type() == Token.Type.Bool
			|| currentToken.type() == Token.Type.Float
			|| currentToken.type() == Token.Type.Char
			|| currentToken.type() == Token.Type.Void;
	}

	/**
	 * @return Whether or not the current token is a literal int, char or float
	 */
	private boolean isLiteral() {
		return currentToken.type() == Token.Type.IntLiteral
			|| currentToken.type() == Token.Type.FloatLiteral
			|| currentToken.type() == Token.Type.CharLiteral
			|| isBooleanLiteral();
	}

	/**
	 * @return Whether or not the current token is a boolean liter
	 */
	private boolean isBooleanLiteral() {
		return currentToken.type() == Token.Type.True
			|| currentToken.type() == Token.Type.False;
	}
}