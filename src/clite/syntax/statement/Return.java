package clite.syntax.statement;

/**
 * A statement that returns a value from a function
 */
public class Return extends Statement {

	@Override
	public void display(int indent) {
		for(int i = 0; i < indent; i++)
			System.out.println('\t');
		System.out.println("(Return)");
	}

}
