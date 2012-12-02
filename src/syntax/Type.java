package syntax;

/**
 * Type = int | bool | char | float
 */
public enum Type {
	INT("int"),
	BOOL("bool"),
	CHAR("char"),
	FLOAT("float");

	/** ID for type */
	private String id;

	/** @param id ID to use for type */
	private Type(String id) { this.id = id; }

	@Override
	public String toString() { return id; }
}
