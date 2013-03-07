package clite.parser;
import java.util.HashMap;


/**
 * Possible tokens that come from the Parser
 */
public class Token {
	/**
	 * Possible token types
	 */
	public enum Type {
		Eof,
		Return,
		
		Int, Float, Char,
		Bool, True, False,
		Void,
		
		Plus, Minus,
		Multiply, Divide,
		
		If, Else, While,
		
		And, Or,
		Less, LessEqual,
		Greater, GreaterEqual,
		Not, NotEqual,
		
		LeftBrace, RightBrace,
		LeftBracket, RightBracket,
		LeftParen, RightParen,
		
		Semicolon, Comma,
		Assign, Equals,
		
		Identifier,
		IntLiteral,
		FloatLiteral,
		CharLiteral
	}
	
	/** List of all possible tokens */
	public static final Token
		eofTok          = new Token(Type.Eof, "<<EOF>>"),
		returnTok       = new Token(Type.Return, "return"),
		
		intTok          = new Token(Type.Int, "int"),
		floatTok        = new Token(Type.Float, "float"),
		charTok         = new Token(Type.Char, "char"),
		voidTok         = new Token(Type.Void, "void"),
		
		boolTok         = new Token(Type.Bool, "bool"),
		ifTok           = new Token(Type.If, "if"),
		elseTok         = new Token(Type.Else, "else"),
		trueTok         = new Token(Type.True, "true"),
		falseTok        = new Token(Type.False, "false"),
		whileTok        = new Token(Type.While, "while"),
		andTok          = new Token(Type.And, "&&"),
		orTok           = new Token(Type.Or, "||"),
		
		leftBraceTok    = new Token(Type.LeftBrace, "{"),
		rightBraceTok   = new Token(Type.RightBrace, "}"),
		leftBracketTok  = new Token(Type.LeftBracket, "["),
		rightBracketTok = new Token(Type.RightBracket, "]"),
		leftParenTok    = new Token(Type.LeftParen, "("),
		rightParenTok   = new Token(Type.RightParen, ")"),
		
		semicolonTok    = new Token(Type.Semicolon, ";"),
		commaTok        = new Token(Type.Comma, ","),
		assignTok       = new Token(Type.Assign, "="),
		
		eqeqTok         = new Token(Type.Equals, "=="),
		ltTok           = new Token(Type.Less, "<"),
		lteqTok         = new Token(Type.LessEqual, "<="),
		gtTok           = new Token(Type.Greater, ">"),
		gteqTok         = new Token(Type.GreaterEqual, ">="),
		notTok          = new Token(Type.Not, "!"),
		noteqTok        = new Token(Type.NotEqual, "!="),
		
		plusTok         = new Token(Type.Plus, "+"),
		minusTok        = new Token(Type.Minus, "-"),
		multiplyTok     = new Token(Type.Multiply, "*"),
		divideTok       = new Token(Type.Divide, "/");

	/** Type of token */
	private Type type;
	
	/** Value of token */
	private String value = "";
	
	/** This maps certain keywords to specific tokens */
	private static HashMap<String, Token> keywords = getKeywordsMap();
	
	/** @return HashMap with keys as keywords and values as the token for that keyword */
	private static HashMap<String, Token> getKeywordsMap(){
		HashMap<String, Token> kw = new HashMap<String, Token>();
		
		kw.put("bool", boolTok);
		kw.put("boolean", boolTok);
		kw.put("char", charTok);
		kw.put("int", intTok);
		kw.put("float", floatTok);
		kw.put("void", voidTok);
		kw.put("if", ifTok);
		kw.put("else", elseTok);
		kw.put("true", trueTok);
		kw.put("false", falseTok);
		kw.put("while", whileTok);
		
		return kw;
	}
	
	/**
	 * Checks if a given string is a keyword and returns its corresponding
	 * token if it is, else returns an identifier token wih the given string
	 * as a parameter.
	 * 
	 * @param name Name to check for
	 * @return Keyword token if found, identifier token otherwise
	 */
	public static Token keyword(String name) {
		Token keyword = keywords.get(name);
		return keyword == null ? mkIdentTok(name) : keyword;
	}

	/**
	 * Create a new token
	 * @param t Type of token
	 * @param v Value of token
	 */
	private Token(Type t, String v) {
		type = t;
		value = v;
	}

	/**
	 * @return Type of token
	 */
	public Type type() {
		return type;
	}

	/**
	 * @return Value of token
	 */
	public String value() {
		return value;
	}

	/**
	 * Make an identifier token from a string
	 * @param name Name of identifier
	 * @return Token representing given name
	 */
	public static Token mkIdentTok(String name) {
		return new Token(Type.Identifier, name);
	}

	/**
	 * Make an int literal token from a string
	 * @param name Int value, in string form
	 * @return Token representing int value
	 */
	public static Token mkIntLiteral(String name) {
		return new Token(Type.IntLiteral, name);
	}

	/**
	 * Make a float literal token from a string
	 * @param name Float value, in string form
	 * @return Token representing float value
	 */
	public static Token mkFloatLiteral(String name) {
		return new Token(Type.FloatLiteral, name);
	}

	/**
	 * Make an char literal token from a string
	 * @param name char value, in string form
	 * @return Token representing char value
	 */
	public static Token mkCharLiteral(String name) {
		return new Token(Type.CharLiteral, name);
	}

	@Override
	public String toString() {
		if (type.compareTo(Type.Identifier) < 0)
			return value;
		return type + "\t" + value;
	}
}
