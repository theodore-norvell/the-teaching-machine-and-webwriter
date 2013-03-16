package play.ide.checker.type.typeAtoms;

/**
 * 
 * @author Shiwei Han
 * 
 */
public class UnknownAtom extends TypeAtom{
	private static UnknownAtom instance = new UnknownAtom();
	
	public static synchronized UnknownAtom getInstance(){
		return instance;
	}

	@Override
	public boolean isSuperAtomOf(TypeAtom ta) {
		// TODO Auto-generated method stub
		if( ta.equals(UnknownAtom.getInstance())
				||ta.equals(AnyAtom.getInstance())||AnyAtom.getInstance().isSuperAtomOf(ta)
				){
				return true;
			}else
				return false;
	}
	
	@Override
	public String toString(){
		return "Unknown";
	}
}