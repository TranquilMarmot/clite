package parser;
import java.util.HashMap;


public class Token {
	public static final Token
		mainTok         = new Token(TokenType.Main, "main"),
		eofTok          = new Token(TokenType.Eof, "<<EOF>>"),
		intTok          = new Token(TokenType.Int, "int"),
		floatTok        = new Token(TokenType.Float, "float"),
		charTok         = new Token(TokenType.Char, "char"),
		boolTok         = new Token(TokenType.Bool, "bool"),
		ifTok           = new Token(TokenType.If, "if"),
		elseTok         = new Token(TokenType.Else, "else"),
		trueTok         = new Token(TokenType.True, "true"),
		falseTok        = new Token(TokenType.False, "false"),
		whileTok        = new Token(TokenType.While, "while"),
		leftBraceTok    = new Token(TokenType.LeftBrace, "{"),
		rightBraceTok   = new Token(TokenType.RightBrace, "}"),
		leftBracketTok  = new Token(TokenType.LeftBracket, "["),
		rightBracketTok = new Token(TokenType.RightBracket, "]"),
		leftParenTok    = new Token(TokenType.LeftParen, "("),
		rightParenTok   = new Token(TokenType.RightParen, ")"),
		semicolonTok    = new Token(TokenType.Semicolon, ";"),
		commaTok        = new Token(TokenType.Comma, ","),
		assignTok       = new Token(TokenType.Assign, "="),
		eqeqTok         = new Token(TokenType.Equals, "=="),
		ltTok           = new Token(TokenType.Less, "<"),
		lteqTok         = new Token(TokenType.LessEqual, "<="),
		gtTok           = new Token(TokenType.Greater, ">"),
		gteqTok         = new Token(TokenType.GreaterEqual, ">="),
		notTok          = new Token(TokenType.Not, "!"),
		noteqTok        = new Token(TokenType.NotEqual, "!="),
		plusTok         = new Token(TokenType.Plus, "+"),
		minusTok        = new Token(TokenType.Minus, "-"),
		multiplyTok     = new Token(TokenType.Multiply, "*"),
		divideTok       = new Token(TokenType.Divide, "/"),
		andTok          = new Token(TokenType.And, "&&"),
		orTok           = new Token(TokenType.Or, "||");

	private TokenType type;
	private String value = "";
	
	private static HashMap<String, Token> keywords = getKeywordsMap();
	
	private static HashMap<String, Token> getKeywordsMap(){
		HashMap<String, Token> kw = new HashMap<String, Token>();
		
		kw.put("main", mainTok);
		kw.put("bool", boolTok);
		kw.put("boolean", boolTok);
		kw.put("char", charTok);
		kw.put("int", intTok);
		kw.put("float", floatTok);
		kw.put("if", ifTok);
		kw.put("else", elseTok);
		kw.put("true", trueTok);
		kw.put("false", falseTok);
		kw.put("while", whileTok);
		
		return kw;
	}

	private Token(TokenType t, String v) {
		type = t;
		value = v;
	}

	public TokenType type() {
		return type;
	}

	public String value() {
		return value;
	}

	public static Token keyword(String name) {
		// if the first character is uppercase, it's an identifier
		if (Character.isUpperCase(name.charAt(0)))
			return mkIdentTok(name);
		else{
			Token keyword = keywords.get(name);
			return keyword == null ? mkIdentTok(name) : keyword;
		}
	}

	public static Token mkIdentTok(String name) {
		return new Token(TokenType.Identifier, name);
	}

	public static Token mkIntLiteral(String name) {
		return new Token(TokenType.IntLiteral, name);
	}

	public static Token mkFloatLiteral(String name) {
		return new Token(TokenType.FloatLiteral, name);
	}

	public static Token mkCharLiteral(String name) {
		return new Token(TokenType.CharLiteral, name);
	}

	public String toString() {
		if (type.compareTo(TokenType.Identifier) < 0)
			return value;
		return type + "\t" + value;
	}
}
