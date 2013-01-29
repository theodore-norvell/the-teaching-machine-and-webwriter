package play.ide.checker.symbolTable;

import play.ide.checker.type.Type;

/**
 * 
 * @author Shiwei Han
 * 
 */
public class FieldValue {

	Constness c;
	Type t;
	
	public FieldValue(Constness c, Type t){
		this.c=c;
		this.t=t;
	}
	
	public Type getType(){
		return t;
	}
	
	public Constness getConstness(){
		return c;
	}
}