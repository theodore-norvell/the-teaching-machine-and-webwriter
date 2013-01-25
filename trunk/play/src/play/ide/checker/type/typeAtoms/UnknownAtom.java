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
	public boolean canonicalOver(TypeAtom ta) {
		// TODO Auto-generated method stub
		return true;
	}
	
	@Override
	public String toString(){
		return "Unknown";
	}
}