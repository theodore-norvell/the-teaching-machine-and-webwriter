package play.ide.checker.symbolTable;

/**
 * 
 * @author Shiwei Han
 * 
 */
public class FieldKey{
	String i;
	Kind k;
	
	FieldKey(String i, Kind k){
		this.i=i;
		this.k=k;
	}
	
	@Override
	public int hashCode(){
		return i.hashCode()+k.hashCode();
	}
	
	@Override
	public boolean equals(Object o){
		FieldKey fk = (FieldKey)o;
		if(i.equals(fk.i)&&k.equals(fk.k))
			return true;
		else 
			return false;

		/*if(this.hashCode()==fk.hashCode())
			return true;
		else 
			return false;*/
	}
	
}
