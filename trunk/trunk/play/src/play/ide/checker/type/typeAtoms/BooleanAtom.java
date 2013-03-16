package play.ide.checker.type.typeAtoms;

/**
 * 
 * @author Shiwei Han
 * 
 */
public class BooleanAtom extends StringAtom {
	private static BooleanAtom instance = new BooleanAtom();
	
	public static synchronized BooleanAtom getInstance(){
		return instance;
	}
	
	@Override
	public boolean isSuperAtomOf(TypeAtom ta) {
		// TODO Auto-generated method stub
		if(ta.equals(BooleanAtom.getInstance()))
			return true;
		else
			return false;		
	}
	
	@Override
	public String toString(){
		return "Boolean";
	}
}
