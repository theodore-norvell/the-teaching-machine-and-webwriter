package play.ide.checker.type.typeAtoms;

/**
 * 
 * @author Shiwei Han
 * 
 */
public class StringAtom extends AnyAtom{
	private static StringAtom instance = new StringAtom();
	
	public static synchronized StringAtom getInstance(){
		return instance;
	}
	
	@Override
	public boolean canonicalOver(TypeAtom t) {
		// TODO Auto-generated method stub
		if(t.equals(NumberAtom.getInstance())
			||t.equals(BooleanAtom.getInstance())
			){
			return true;
		}else
			return false;		
	}
	
	@Override
	public String toString(){
		return "String";
	}
}
