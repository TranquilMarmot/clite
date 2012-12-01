package syntax.statement;

import java.util.ArrayList;


/**
 * Block = Statement* (a Vector of members)
 */
public class Block extends Statement {
	public ArrayList<Statement> members;
	
	public Block(){
		members = new ArrayList<Statement>();
	}
	
	public void display(int indent){
		for(Statement s : members)
			s.display(indent + 1);
	}
}
