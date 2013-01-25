package play.ide.checker.type.typeAtoms;

/**
 * 
 * @author Shiwei Han
 * 
 */
public class NumberAtom extends StringAtom {
	private static NumberAtom instance = new NumberAtom();
	
	public static synchronized NumberAtom getInstance(){
		return instance;
	}
	
	@Override
	public boolean canonicalOver(TypeAtom t) {
		// TODO Auto-generated method stub
		if(t.equals(NumberAtom.getInstance())){
			return true;
		}else
			return false;		
	}
	
	@Override
	public String toString(){
		return "Number";
	}
}
