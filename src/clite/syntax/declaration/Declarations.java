package clite.syntax.declaration;

import java.util.LinkedHashMap;

/**
 * Declarations = Declaration*
 * (a list of declarations d1, d2, ..., dn)
 * 
 * Basically, a renamed ArrayList<Declaration>
 */
@SuppressWarnings("serial")
public class Declarations extends LinkedHashMap<String, Declaration> {

	public void display(int indent) {
		for(Declaration d : this.values()){
			for(int i = 0; i < indent; i++)
				System.out.print("   ");
			System.out.println(d.variable() + " :: " + d.type());
		}
	}}
