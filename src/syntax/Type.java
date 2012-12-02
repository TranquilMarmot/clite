package syntax;

/**
 * Type = int | bool | char | float
 */
public enum Type {
	INT("int"),
	BOOL("bool"),
	CHAR("char"),
	FLOAT("float");

	private String id;

	private Type(String id) { this.id = id; }

	@Override
	public String toString() { return id; }
}
