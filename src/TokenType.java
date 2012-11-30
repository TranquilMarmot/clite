public enum TokenType {
	Main, Eof,
	
	Int, Float, Char,
	Bool, True, False, 
	
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
