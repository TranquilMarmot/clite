package clite.syntax.statement;



/**
 * A statement that skips
 */
public class Skip extends Statement {
	@Override
	public void display(int indent){
		for(int i = 0; i < indent; i++)
			System.out.println('\t');
		System.out.println("(Skip)");
	}
}
