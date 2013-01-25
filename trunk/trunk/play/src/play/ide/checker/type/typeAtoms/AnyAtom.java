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
	public boolean canonicalOver(TypeAtom ta) {
		// TODO Auto-generated method stub
		if(ta.equals(StringAtom.getInstance())||StringAtom.getInstance().canonicalOver(ta)
			||ta.equals(NullAtom.getInstance())||NullAtom.getInstance().canonicalOver(ta)
			||ta.equals(ClassAtom.getInstance())||ClassAtom.getInstance().canonicalOver(ta)
			){
			return true;
		}else
			return false;
	}
	
	@Override
	public String toString(){
		return "Any";
	}
}
