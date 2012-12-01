package syntax;

/**
 * Type = int | bool | char | float
 */
public enum Type {
	INT("int"),
	BOOL("bool"),
	CHAR("char"),
	FLOAT("float");
	/*
	final static Type INT = new Type("int");
	final static Type BOOL = new Type("bool");
	final static Type CHAR = new Type("char");
	final static Type FLOAT = new Type("float");*/
	// final static Type UNDEFINED = new Type("undef");

	private String id;

	private Type(String id) { this.id = id; }

	@Override
	public String toString() { return id; }
}
