package syntax.statement;

import java.util.Iterator;
import java.util.Stack;


/**
 * Block = Statement* (a Vector of members)
 */
public class Block extends Statement {
	/** List of all members */
	private Stack<Statement> members;
	
	/**
	 * Create a new block
	 */
	public Block(){
		members = new Stack<Statement>();
	}
	
	/**
	 * Add a new member to this block
	 * @param newMember Member to add
	 */
	public void addMember(Statement newMember){
		members.push(newMember);
	}
	
	/**
	 * @return Iterator of all members in this block
	 */
	public Iterator<Statement> getMembers(){
		return members.iterator();
	}
	
	@Override
	public void display(int indent){
		for(Statement s : members)
			s.display(indent + 1);
	}
}
