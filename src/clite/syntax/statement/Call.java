package clite.syntax.statement;

/**
 * A statement to call a method
 */
public class Call extends Statement {

	@Override
	public void display(int indent) {
		for(int i = 0; i < indent; i++)
			System.out.println('\t');
		System.out.println("(Call)");
	}

}
