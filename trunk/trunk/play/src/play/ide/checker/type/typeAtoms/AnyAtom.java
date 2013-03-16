package play.ide.checker.type.typeAtoms;

/**
 * 
 * @author Shiwei Han
 * 
 */
public class AnyAtom extends UnknownAtom{
	private static AnyAtom instance = new AnyAtom();
	
	public static synchronized AnyAtom getInstance(){
		return instance;
	}
	
	@Override
	public boolean isSuperAtomOf(TypeAtom ta) {
		// TODO Auto-generated method stub
		if( ta.equals(UnknownAtom.getInstance())){
			return false;
		}else
			return true;
	}
	
	@Override
	public String toString(){
		return "Any";
	}
}
