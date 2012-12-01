package abstractsyntax.statement;

import java.util.ArrayList;


/**
 * Block = Statement* (a Vector of members)
 */
public class Block extends Statement {
	public ArrayList<Statement> members;
	
	public Block(){
		members = new ArrayList<Statement>();
	}
}
